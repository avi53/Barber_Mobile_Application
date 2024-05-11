package edu.tacoma.uw.barber_mobile_application.ui.account;

import static android.content.ContentValues.TAG;

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
import edu.tacoma.uw.barber_mobile_application.databinding.FragmentRegistrationBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding mBinding;
    private UserViewModel mUserViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mUserViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        mBinding = FragmentRegistrationBinding.inflate(inflater, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUserViewModel.addResponseObserver(getViewLifecycleOwner(), response -> {
            observeResponse(response);
        });

        mBinding.registerButton.setOnClickListener(button -> register());

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    // Helper Functions
    private void register() {
        String email = String.valueOf(mBinding.emailEditRegister.getText());
        String firstName = String.valueOf(mBinding.firstNameEditRegister.getText());
        String lastName = String.valueOf(mBinding.lastNameEditRegister.getText());
        String password = String.valueOf(mBinding.pwdEditRegister.getText());
        String password2 = String.valueOf(mBinding.pwdCheckEditRegister.getText());
        int phoneNumber = 0;
        Account account = null;

        if (!password.equals(password2)) {
            String err = "The Password Entered Doesn't Match.";
            mBinding.textErrorRegistration.setText(err);
            return;
        }

        try {
            phoneNumber = Integer.parseInt(mBinding.phoneNumEditRegister.getText().toString());
            account = new Account(email, firstName, lastName, password, phoneNumber);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
            if (firstName.isEmpty()) {
                mBinding.textErrorRegistration.setText("Please Enter Your First Name to Register");
            } else if (lastName.isEmpty()) {
                mBinding.textErrorRegistration.setText("Please Enter Your Last Name to Register");
            } else if (email.isEmpty()) {
                mBinding.textErrorRegistration.setText("Please Enter a Valid Email to Register");
            } else if (password.isEmpty()) {
                mBinding.textErrorRegistration.setText("Please Enter a Password to Register");
            } else if (password2.isEmpty()) {
                mBinding.textErrorRegistration.setText("Please Re-Enter the Password to Register");
            } else if (phoneNumber == 0) {
                mBinding.textErrorRegistration.setText("Please Enter a Phone Number to Register");
            } else {
                mBinding.textErrorRegistration.setText(e.getMessage());
            }
            return;
        }
        Log.i(TAG, email);
        mUserViewModel.addClient(account);
    }

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
                Navigation.findNavController(requireView()).navigate(R.id.loginFragment);
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }


}