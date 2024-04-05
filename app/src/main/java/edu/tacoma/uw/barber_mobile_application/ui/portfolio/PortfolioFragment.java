package edu.tacoma.uw.barber_mobile_application.ui.portfolio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.tacoma.uw.barber_mobile_application.databinding.FragmentPortfolioBinding;

public class PortfolioFragment extends Fragment {

    private FragmentPortfolioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PortfolioViewModel portfolioViewModel =
                new ViewModelProvider(this).get(PortfolioViewModel.class);

        binding = FragmentPortfolioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPortfolio;
        portfolioViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}