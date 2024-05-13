package edu.tacoma.uw.barber_mobile_application.ui.account;

import static android.content.ContentValues.TAG;

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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import edu.tacoma.uw.barber_mobile_application.R;
import edu.tacoma.uw.barber_mobile_application.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textAccount;
//        accountViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button editBtn = view.findViewById(R.id.edit_btn);
        Button cardBtn = view.findViewById(R.id.card_details_btn);
        Button logoutBtn = view.findViewById(R.id.logout_btn);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Will take user to edit account information", Toast.LENGTH_SHORT).show();
            }
        });
        cardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Will take user Square page to add cards safely", Toast.LENGTH_SHORT).show();
            }
        });
        logoutBtn.setOnClickListener(button -> {

            LoginFragment loginFragment = (LoginFragment) getParentFragmentManager().findFragmentById(R.id.mobile_navigation);
            if (loginFragment != null) {
                loginFragment.clearLoginState();
            } else {
                Log.e(TAG, "Y IT NO WORK");
            }

            NavDirections action = AccountFragmentDirections.actionNavigationAccountToLoginFragment();
            Navigation.findNavController(requireView()).navigate(action);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}