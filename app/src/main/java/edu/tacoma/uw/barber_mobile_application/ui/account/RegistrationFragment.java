package edu.tacoma.uw.barber_mobile_application.ui.account;

import static android.content.ContentValues.TAG;

import android.content.Intent;
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

import edu.tacoma.uw.barber_mobile_application.R;
import edu.tacoma.uw.barber_mobile_application.SplashActivity;
import edu.tacoma.uw.barber_mobile_application.databinding.FragmentRegistrationBinding;

/**
 * This fragment is responsible for the "Registration" section, which is the fragment
 * a user sees when they don't have an account to login with. They will redirect to this
 * page from the login fragment and will allow a user to enter their information that
 * will allow them to sign up and sign into the application.
 * This fragment includes functionality for user registration, validation of user inputs,
 * and handling responses from the registration process.
 *
 * @author Hassan Bassam Farhat
 * @version Spring 2024
 */
public class RegistrationFragment extends Fragment {

    // Class variable(s) attributes
    private FragmentRegistrationBinding mBinding;
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
        mBinding = FragmentRegistrationBinding.inflate(inflater, container, false);

        // Hides Navigation bar from appearing when user is not logged in
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        return mBinding.getRoot();
    }

    /**
     * Handles the initialization of UI components, setting up click listeners,
     * and adding observers for response handling.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState A Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mUserViewModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);
        // Will try to register a user so long as all information provided is validated
        mBinding.registerButton.setOnClickListener(button -> register());

        // Hides Navigation bar from appearing when user is not logged in
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    /**
     * Releases the binding when the fragment's view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }


    // Helper Functions

    /**
     * Takes all the given information that a user provides to create an account, and if that
     * information is sufficient, then the user's data will be registered within our DB. Will
     * also yield custom Error Messages to the user if something is incorrect.
     */
    private void register() {
        String email = String.valueOf(mBinding.emailEditRegister.getText());
        String firstName = String.valueOf(mBinding.firstNameEditRegister.getText());
        String lastName = String.valueOf(mBinding.lastNameEditRegister.getText());
        String password = String.valueOf(mBinding.pwdEditRegister.getText());
        String password2 = String.valueOf(mBinding.pwdCheckEditRegister.getText());
        int phoneNumber = 0;
        Account account;

        // Checks to see if both passwords entered are the same for DB submission
        if (!password.equals(password2)) {
            String err = "The Password Entered Doesn't Match.";
            mBinding.textErrorRegistration.setText(err);
            return;
        }

        try {
            phoneNumber = Integer.parseInt(mBinding.phoneNumEditRegister.getText().toString());
            account = new Account(email, firstName, lastName, password, phoneNumber);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            if (firstName.isEmpty()) {
                mBinding.textErrorRegistration.setText(
                        getString(R.string.enter_first_name_register));
            } else if (lastName.isEmpty()) {
                mBinding.textErrorRegistration.setText(
                        getString(R.string.enter_last_name_register));
            } else if (email.isEmpty()) {
                mBinding.textErrorRegistration.setText(
                        getString(R.string.enter_valid_email_register));
            } else if (password.isEmpty()) {
                mBinding.textErrorRegistration.setText(
                        getString(R.string.enter_password_register));
            } else if (phoneNumber == 0) {
                mBinding.textErrorRegistration.setText(
                        getString(R.string.enter_phone_number_register));
            } else {
                mBinding.textErrorRegistration.setText(e.getMessage());
            }
            return;
        }
        mUserViewModel.addClient(account);
    }

    /**
     *  Responsible for displaying an error if addition of a user in the DB cannot occur
     *  due to some Error. Otherwise, if successful we want to redirect a user to the
     *  login page of the application.
     *
     *  @param response The JSON response received from the server.
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("error")) {
                try {
                    String err = "Error Adding User: " + response.get("error");
                    mBinding.textErrorRegistration.setText(err);
                } catch (JSONException e) {
                    String err = "Error Adding User: " + e.getMessage();
                    Log.e(TAG, err);
                    mBinding.textErrorRegistration.setText(err);
                }
            } else {
                // Starts Custom Splash Screen before redirect back to login page
                Intent splashIntent = new Intent(requireContext(), SplashActivity.class);
                splashIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(splashIntent);

                requireActivity().finish();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

}
