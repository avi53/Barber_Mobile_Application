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

import com.android.volley.BuildConfig;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import edu.tacoma.uw.barber_mobile_application.MainActivity;
import edu.tacoma.uw.barber_mobile_application.R;
import edu.tacoma.uw.barber_mobile_application.SplashActivity;
import edu.tacoma.uw.barber_mobile_application.databinding.FragmentAccountBinding;

/**
 * This fragment is responsible for the "Account" section of the application. It will
 * create its view based on the current user that is logged into the application.
 * This fragment includes functionality for viewing and editing user account details,
 * as well as logging out from the application.
 *
 * @author Hassan Bassam Farhat
 * @version Spring 2024
 */
public class AccountFragment extends Fragment {

    // Constant(s) definitions for SharedPreference accessibility
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String USER_EMAIL_KEY = "user_email";

    // Class variable(s) attributes
    private FragmentAccountBinding mBinding;
    private UserViewModel mModel;


    // Public Class Methods

    /**
     * Inflates the layout for this fragment, initializes ViewModel, and returns the root view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle object containing the activity's previously saved state.
     * @return The root view of the fragment.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mModel = new ViewModelProvider(this).get(UserViewModel.class);
        mBinding = FragmentAccountBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    /**
     * Handles the initialization of UI components and setting up click listeners.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState A Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Instantiating all accessible buttons within the Account Fragment via their IDs
        Button editBtn = view.findViewById(R.id.edit_btn);
        Button logoutBtn = view.findViewById(R.id.logout_btn);
        PaymentButtonContainer paymentBtn = view.findViewById(R.id.payment_container_btn);

        // Obtaining Email from SharedPreferences to populate User Account Information
        SharedPreferences sharedPreferences =
                requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(USER_EMAIL_KEY, "");
        mModel.getAccountByEmail(email);
        mModel.observeAccountDetails(getViewLifecycleOwner(), this::updateUI);






        paymentBtn.setup(
                createOrderActions -> {
                    ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                    purchaseUnits.add(
                            new PurchaseUnit.Builder()
                                    .amount(
                                            new Amount.Builder()
                                                    .currencyCode(CurrencyCode.USD)
                                                    .value("10.00")
                                                    .build()
                                    )
                                    .build()
                    );
                    OrderRequest order = new OrderRequest(
                            OrderIntent.CAPTURE,
                            new AppContext.Builder()
                                    .userAction(UserAction.PAY_NOW)
                                    .build(),
                            purchaseUnits
                    );
                    createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                },
                approval -> approval.getOrderActions().capture(new OnCaptureComplete() {
                    @Override
                    public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                        Log.d("AccountFragment", "CaptureOrderResult: " + result);
                        Toast.makeText(getContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
                    }
                })
        );







        // TODO: Will allow a user to edit their information within the application
        editBtn.setOnClickListener(button -> {
            Toast.makeText(getContext(),
                    "Take user to edit account information", Toast.LENGTH_SHORT).show();
        });

        // When a user logs out, SharedPreference is cleared, and application is restarted
        logoutBtn.setOnClickListener(button -> {
            clearLoginState();

            // Starts Custom Splash Screen before redirect back to login page
            Intent splashIntent = new Intent(requireContext(), SplashActivity.class);
            splashIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(splashIntent);

            requireActivity().finish();
        });
    }

    /**
     * Releases the binding when the fragment's view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }


    // Private Helper Methods

    /** Clears current user login state from the SharedPreference Data */
    private void clearLoginState() {
        SharedPreferences sharedPreferences =
                requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.putBoolean(IS_LOGGED_IN, false);
        editor.putString(USER_EMAIL_KEY, "");
        editor.apply();
    }

    /**
     * Updates the UI with the account information obtained from the database.
     *
     * @param account The account object containing user details.
     */
    private void updateUI(Account account) {
        TextView firstNameTextView = requireView().findViewById(R.id.first_name_text_view);
        TextView lastNameTextView = requireView().findViewById(R.id.last_name_text_view);
        TextView emailTextView = requireView().findViewById(R.id.email_text_view);
        TextView phoneNumberTextView = requireView().findViewById(R.id.phone_number_text_view);

        firstNameTextView.setText(account.getFirstName());
        lastNameTextView.setText(account.getLastName());
        emailTextView.setText(account.getEmail());
        phoneNumberTextView.setText(String.valueOf(account.getPhoneNumber()));
    }

}
