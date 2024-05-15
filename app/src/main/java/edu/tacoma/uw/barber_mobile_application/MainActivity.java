package edu.tacoma.uw.barber_mobile_application;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.tacoma.uw.barber_mobile_application.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edu.tacoma.uw.barber_mobile_application.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_services, R.id.navigation_portfolio, R.id.navigation_details, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Observe the current destination of the NavController
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Check if the current destination is the LoginFragment
            if (destination.getId() == R.id.loginFragment || destination.getId() == R.id.registrationFragment) {
                // Hide the BottomNavigationView
                navView.setVisibility(View.GONE);
            } else {
                // Show the BottomNavigationView for other destinations
                navView.setVisibility(View.VISIBLE);
            }
        });


    }


}