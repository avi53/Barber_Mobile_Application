package edu.tacoma.uw.barber_mobile_application.ui.account;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.tacoma.uw.barber_mobile_application.Account;
import edu.tacoma.uw.barber_mobile_application.R;
import edu.tacoma.uw.barber_mobile_application.SplashActivity;
import edu.tacoma.uw.barber_mobile_application.UserViewModel;
import edu.tacoma.uw.barber_mobile_application.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {

    // Define a constant for your SharedPreferences key
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String USER_EMAIL_KEY = "user_email";

    private FragmentAccountBinding mBinding;

    private UserViewModel mModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mModel = new ViewModelProvider(this).get(UserViewModel.class);
        mBinding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = mBinding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(USER_EMAIL_KEY, "");
        mModel.getAccountByEmail(email);

        mModel.observeAccountDetails(getViewLifecycleOwner(), account -> {
            updateUI(account);
        });

        Button editBtn = view.findViewById(R.id.edit_btn);
        Button cardBtn = view.findViewById(R.id.card_details_btn);
        Button logoutBtn = view.findViewById(R.id.logout_btn);

        editBtn.setOnClickListener(button -> {
            Toast.makeText(getContext(), "Will take user to edit account information", Toast.LENGTH_SHORT).show();
        });

        cardBtn.setOnClickListener(button -> {
            Toast.makeText(getContext(), "Will take user to add card information on Square", Toast.LENGTH_SHORT).show();
        });

        logoutBtn.setOnClickListener(button -> {
            clearLoginState();

            Intent splashIntent = new Intent(requireContext(), SplashActivity.class);
            splashIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(splashIntent);

            requireActivity().finish();
        });
    }

    private void clearLoginState() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putBoolean(IS_LOGGED_IN, false);
        editor.putString(USER_EMAIL_KEY, "");
        editor.apply();
    }

    private void updateUI(Account account) {
        TextView firstNameTextView = getView().findViewById(R.id.first_name_text_view);
        TextView lastNameTextView = getView().findViewById(R.id.last_name_text_view);
        TextView emailTextView = getView().findViewById(R.id.email_text_view);
        TextView phoneNumberTextView = getView().findViewById(R.id.phone_number_text_view);

        firstNameTextView.setText(account.getFirstName());
        lastNameTextView.setText(account.getLastName());
        emailTextView.setText(account.getEmail());
        phoneNumberTextView.setText(String.valueOf(account.getPhoneNumber()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}