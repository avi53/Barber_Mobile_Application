package edu.tacoma.uw.barber_mobile_application.ui.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import edu.tacoma.uw.barber_mobile_application.R;
import edu.tacoma.uw.barber_mobile_application.databinding.FragmentServicesBinding;

public class ServicesFragment extends Fragment {

    private FragmentServicesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ServicesViewModel dashboardViewModel =
                new ViewModelProvider(this).get(ServicesViewModel.class);

        binding = FragmentServicesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Set onClickListeners for buttons
        binding.haircut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToOptionsFragment();
            }
        });
        binding.beard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToOptionsFragment();
            }
        });
        binding.taper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToOptionsFragment();
            }
        });
        binding.lineup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToOptionsFragment();
            }
        });
        binding.child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToOptionsFragment();
            }
        });
        binding.bookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToBookingFragment();
            }
        });


        return root;
    }

    private void navigateToOptionsFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_navigation_services_to_optionsFragment);
    }

    private void navigateToBookingFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_navigation_services_to_bookingFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}