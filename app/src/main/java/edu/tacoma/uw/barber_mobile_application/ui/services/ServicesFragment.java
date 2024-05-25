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

/**
 * Base fragment for the services section. Has options for creating bookings as well
 * as an option to view the bookings that were already booked.
 */
public class ServicesFragment extends Fragment {

    private FragmentServicesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ServicesViewModel dashboardViewModel =
                new ViewModelProvider(this).get(ServicesViewModel.class);

        binding = FragmentServicesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set onClickListeners for buttons
        binding.haircut.setOnClickListener(view -> navigateToOptionsFragment("Haircut"));
        binding.beard.setOnClickListener(view -> navigateToBookFragment("Beard Trim"));
        binding.taper.setOnClickListener(view -> navigateToOptionsFragment("Taper"));
        binding.lineup.setOnClickListener(view -> navigateToBookFragment("Lineup"));
        binding.child.setOnClickListener(view -> navigateToBookFragment("Children's Haircut"));
        binding.bookings.setOnClickListener(view -> navigateToBookingFragment());

        return root;
    }

    /**
     * Navigate to the options page with the specific booking desired.
     * @param serviceType The specific service that the client wants is passed along.
     */
    private void navigateToOptionsFragment(String serviceType) {
        Bundle bundle = new Bundle();
        bundle.putString("service_type", serviceType);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_navigation_services_to_optionsFragment, bundle);
    }

    /**
     * Navigate to the book page with the specific booking desired.
     * Bypasses options as it is not necessary for all haircut types.
     * @param serviceType The specific service that the client wants is passed along.
     */
    private void navigateToBookFragment(String serviceType) {
        Bundle bundle = new Bundle();
        bundle.putString("service_type", serviceType);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_navigation_services_to_bookFragment, bundle);
    }

    /**
     * Navigate to the current bookings.
     */
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