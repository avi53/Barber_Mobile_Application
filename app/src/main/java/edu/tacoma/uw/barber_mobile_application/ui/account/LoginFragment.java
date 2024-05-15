package edu.tacoma.uw.barber_mobile_application.ui.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import edu.tacoma.uw.barber_mobile_application.Account;
import edu.tacoma.uw.barber_mobile_application.R;
import edu.tacoma.uw.barber_mobile_application.UserViewModel;
import edu.tacoma.uw.barber_mobile_application.databinding.FragmentLoginBinding;

/**
 * This fragment is responsible for the "Login" section, which is the first fragment
 * a user sees when they open the application for the first time. It allows a user
 * to enter their valid login information.
 * This fragment includes functionality for navigating to the registration page,
 * authenticating user credentials, and handling responses from the authentication process.
 *
 * @author Hassan Bassam Farhat
 * @version Spring 2024
 */
public class LoginFragment extends Fragment {

    // Constant(s) definitions for SharedPreference accessibility
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String USER_EMAIL_KEY = "user_email";
    private static final String TAG = "LoginFragment";

    // Class variable(s) attributes
    private FragmentLoginBinding mBinding;
    private UserViewModel mUserViewModel;


    // Public Class Methods

    /**
     * Inflates the layout for this fragment, initializes ViewModel, and returns the root view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle object containing the activity's previously saved state.
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mBinding = FragmentLoginBinding.inflate(inflater, container, false);

        // Hides Navigation bar from appearing when user is not logged in
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        return mBinding.getRoot();
    }


    /**
     * Handles the initialization of UI components, setting up click listeners,
     * and checking if the user is already logged in.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState A Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Hides Navigation bar from appearing when user is not logged in
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        // Check to see if the user is already logged in
        SharedPreferences sharedPreferences =
                requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN, false);

        // If someone is already logged in, just take them to the main page
        if (isLoggedIn) {
            Navigation.findNavController(requireView()).navigate(R.id.navigation_services);
            return;
        }

        mBinding.textErrorLogin.setText("");
        mUserViewModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);

        // Will redirect a user to the Registration page of the application
        mBinding.registerButton.setOnClickListener(button -> navigateToRegister());
        // Will try to login a user given their inputted email,password
        mBinding.loginButton.setOnClickListener(button -> login());
    }

    /**
     * Releases the binding when the fragment's view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }


    // Helper Methods

    /** Navigates user from login fragment to registration fragment */
    private void navigateToRegister() {
        Navigation.findNavController(requireView()).navigate(R.id.registrationFragment);
    }

    /**
     *  Takes the given email/password from the user and sends to UserViewModel to authenticate
     *  the credentials based on what/who is registered within our DB. Will also yield custom
     *  Error Messages to the user if something is incorrect.
     */
    private void login() {
        String email = String.valueOf(mBinding.emailEditLogin.getText());
        String password = String.valueOf(mBinding.pwdEditLogin.getText());
        Account account;

        try {
            account = new Account(email, password);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            if (email.isEmpty()) {
                mBinding.textErrorLogin.setText(getString(R.string.enter_valid_email_login));
            } else if (password.isEmpty()) {
                mBinding.textErrorLogin.setText(getString(R.string.enter_valid_password_login));
            } else {
                mBinding.textErrorLogin.setText(e.getMessage());
            }
            return;
        }

        mUserViewModel.authenticateClient(account);

        // After successful login, save the login state using SharedPreferences
        SharedPreferences sharedPreferences =
                requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(USER_EMAIL_KEY, email);
        editor.apply();
    }

    /**
     *  Responsible for displaying an error if authentication of user entered email/password
     *  is incorrect. Otherwise, if successful we want to redirect a user to the main page of
     *  the application.
     *
     *  @param response The JSON response received from the server.
     */
    private void observeResponse(final JSONObject response) {
        String err;
        if (response.length() > 0) {
            if (response.has("error")) {
                try {
                    err = "Error Authenticating User: " + response.get("error");
                    mBinding.textErrorLogin.setText(err);

                } catch (JSONException e) {
                    err = "JSON Parse Error" + e.getMessage();
                    Log.e("JSON Parse Error", err);
                    mBinding.textErrorLogin.setText(err);

                }
            } else if (response.has("result")) {
                try {
                    String result = (String) response.get("result");
                    if (result.equals("success")) {
                        Navigation.findNavController(requireView()).navigate(R.id.navigation_services);
                        this.onDestroyView();
                    } else {
                        Log.e(TAG, response.toString());
                        mBinding.textErrorLogin.setText(response.getString("result"));
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", Objects.requireNonNull(e.getMessage()));
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

}
