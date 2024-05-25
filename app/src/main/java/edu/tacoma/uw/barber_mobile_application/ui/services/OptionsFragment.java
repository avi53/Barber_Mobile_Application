package edu.tacoma.uw.barber_mobile_application.ui.services;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import edu.tacoma.uw.barber_mobile_application.R;

/**
 * Options fragment to select options for a haircut. Currently nonfunctional.
 */
public class OptionsFragment extends Fragment {

    public OptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        assert getArguments() != null;
        String serviceType = getArguments().getString("service_type");

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        // Find the button in your layout
        Button submitButton = view.findViewById(R.id.submitButton);

        // Set click listener for the button
        submitButton.setOnClickListener(v -> navigateToBookFragment(serviceType));

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Passes on the service type. In the future will pass service options.
     * @param serviceType P
     */
    private void navigateToBookFragment(String serviceType) {
        Bundle bundle = new Bundle();
        bundle.putString("service_type", serviceType);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_optionsFragment_to_bookFragment, bundle);
    }
}