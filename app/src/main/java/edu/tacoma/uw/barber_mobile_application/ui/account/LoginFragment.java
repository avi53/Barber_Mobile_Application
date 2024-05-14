package edu.tacoma.uw.barber_mobile_application.ui.account;

import android.content.Context;
import android.content.Intent;
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
import org.mindrot.jbcrypt.BCrypt;

import edu.tacoma.uw.barber_mobile_application.Account;
import edu.tacoma.uw.barber_mobile_application.R;
import edu.tacoma.uw.barber_mobile_application.UserViewModel;
import edu.tacoma.uw.barber_mobile_application.databinding.FragmentLoginBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    // Define a constant for your SharedPreferences key
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String USER_EMAIL_KEY = "user_email";

    private FragmentLoginBinding mBinding;
    private UserViewModel mUserViewModel;

    private static final String TAG = "LoginFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUserViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        mBinding = FragmentLoginBinding.inflate(inflater, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        // Check if the user is already logged in
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN, false);

        if (isLoggedIn) {
            Navigation.findNavController(requireView()).navigate(R.id.navigation_services);
            return;
        }

        mBinding.textErrorLogin.setText("");

        mUserViewModel.addResponseObserver(getViewLifecycleOwner(), response -> {
            observeResponse(response);

        });
        mBinding.registerButton.setOnClickListener(button -> navigateToRegister());
        mBinding.loginButton.setOnClickListener(button -> login());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }


    // Helper Methods
    private void navigateToRegister() {
        Navigation.findNavController(requireView()).navigate(R.id.registrationFragment);
    }

    private void login() {
        String email = String.valueOf(mBinding.emailEditLogin.getText());
        String password = String.valueOf(mBinding.pwdEditLogin.getText());
        Account account = null;

        try {
            account = new Account(email, password);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
            if (email.isEmpty()) {
                mBinding.textErrorLogin.setText("Please Enter a Valid Email to Login");
            } else if (password.isEmpty()) {
                mBinding.textErrorLogin.setText("Please Enter a Correct and Valid Password for the Account");
            } else {
                mBinding.textErrorLogin.setText(e.getMessage());
            }
            return;
        }
        Log.i(TAG, email);
        mUserViewModel.authenticateClient(account);

        // After successful login, save the login state using SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(USER_EMAIL_KEY, email);
        editor.apply();

    }

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
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

}