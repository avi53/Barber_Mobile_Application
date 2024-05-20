package edu.tacoma.uw.barber_mobile_application.ui.services;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import edu.tacoma.uw.barber_mobile_application.R;
import edu.tacoma.uw.barber_mobile_application.helper.NotificationHelper;

/**
 * create an instance of this fragment.
 */
public class BookFragment extends Fragment {
    public BookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment BookFragment.
     */
    public static BookFragment newInstance(String param1, String param2) {
        BookFragment fragment = new BookFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        // Create the notification channel
        NotificationHelper.createNotificationChannel(getContext());

        // Find the button in your layout
        Button bookButton = view.findViewById(R.id.bookButton);

        // Set click listener for the button
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Date Booked Successfully!", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Notifcation");
                NotificationHelper.displayNotification(getContext(), "Booking Confirmed", "Your booking has been confirmed.");
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}