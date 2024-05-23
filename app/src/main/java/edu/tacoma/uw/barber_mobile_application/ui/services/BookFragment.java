package edu.tacoma.uw.barber_mobile_application.ui.services;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.barber_mobile_application.R;
import edu.tacoma.uw.barber_mobile_application.util.DateTime;
import edu.tacoma.uw.barber_mobile_application.util.NotificationHelper;

/**
 * create an instance of this fragment.
 */
public class BookFragment extends Fragment {

    private String serviceType;

    public BookFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book, container, false);

        assert getArguments() != null;
        serviceType = getArguments().getString("service_type");

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        // Create the notification channel
        NotificationHelper.createNotificationChannel(getContext());

        // Find the button in your layout
        Button bookButton = view.findViewById(R.id.bookButton);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        final EditText[] editTextTime = {view.findViewById(R.id.editTextTime)};

        final String[] selectedDate = {""};


        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // Note: month is 0-based, so add 1
            selectedDate[0] = year + "-" + (month + 1) + "-" + dayOfMonth;
        });

        // Set click listener for the button
        bookButton.setOnClickListener(v -> {
            String email = "test@test.com";
            String date = selectedDate[0];
            //String time = "16:00:00";
            String time = editTextTime[0].getText().toString();
//                String type = "Haircut";
            String type = serviceType;
            boolean beard = false;
            boolean towel = false;

            if (date.isEmpty() || time.isEmpty()) {
                Toast.makeText(getContext(), "Please select a date and time.", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);

            if (hour < 9 && hour > 5 || hour > 12) {
                Toast.makeText(getContext(), "Please select a valid time.", Toast.LENGTH_SHORT).show();
                return;
            }

            submitBookingToServer(email, date, time, type, beard, towel);

            String output = String.format("Your booking on %s, at %s been confirmed.", date, DateTime.formatTime(hour));
            NotificationHelper.displayNotification(getContext(), "Booking Confirmed", output);

            // Display Snack bar notification
            Snackbar.make(view, output, Snackbar.LENGTH_LONG).show();
        });

        // Inflate the layout for this fragment
        return view;
    }


    private void submitBookingToServer(String email, String date, String time, String type, boolean beard, boolean towel) {
        // Create a JSON object with the booking details
        JSONObject jsonObject = new JSONObject();
        try {
            // Populate the JSON object with booking details
            jsonObject.put("email", email);
            jsonObject.put("date", date);
            jsonObject.put("time", time);
            jsonObject.put("booking_type", type);
            jsonObject.put("beard", beard);
            jsonObject.put("hot_towel", towel);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }

        // Create a new thread to perform network operations
        new Thread(() -> {
            HttpURLConnection urlConnection = null;
            try {
                // Create URL object with the server URL
                URL url = new URL("https://students.washington.edu/tratsko/add_a_booking.php");

                // Open HttpURLConnection
                urlConnection = (HttpURLConnection) url.openConnection();

                // Set request method to POST
                urlConnection.setRequestMethod("POST");

                // Set content type for the request
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                // Enable output for the request
                urlConnection.setDoOutput(true);

                // Write JSON data to the output stream
                DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
                os.writeBytes(jsonObject.toString());
                os.flush();
                os.close();

                // Get response code from the server
                int responseCode = urlConnection.getResponseCode();

                // Check if request was successful (HTTP 200)
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Display success message in UI thread
                    getActivity().runOnUiThread(() -> {
//                        Toast.makeText(getContext(), "Booking successful!", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Display error message in UI thread
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Failed to book appointment", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Disconnect HttpURLConnection
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }).start(); // Start the thread
    }
}