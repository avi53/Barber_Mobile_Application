package edu.tacoma.uw.barber_mobile_application.ui.services;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.barber_mobile_application.R;
import edu.tacoma.uw.barber_mobile_application.util.NotificationHelper;

/**
 * create an instance of this fragment.
 */
public class BookFragment extends Fragment {

    // Constant(s) definitions for SharedPreference accessibility
    private static final String SHARED_PREFS = "sharedPrefs";

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
        Button getBookingsButton = view.findViewById(R.id.getBookingsButton);
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

            // Subtract 9 to get the hour starting from 0 at 9 AM
            String noon = "AM";
            int singleDigitHour = hour - 12;
            if (singleDigitHour < 1) {
                singleDigitHour += 12;
                noon = "PM";
            }
            String formatTime = singleDigitHour + " " + noon;

            submitBookingToServer(email, date, time, type, beard, towel);

            //Toast.makeText(getContext(), "Date Booked Successfully!", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "Notification");
            String output = String.format("Your booking on %s, at %s been confirmed.", date, formatTime);
            NotificationHelper.displayNotification(getContext(), "Booking Confirmed", output);

            // Display Snackbar notification
            Snackbar.make(view, output, Snackbar.LENGTH_LONG).show();
        });

        // Set click listener for the get bookings button
        getBookingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAllBookings();
            }
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
            e.printStackTrace();
        }

        // Create a new thread to perform network operations
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(getContext(), "Booking successful!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Display error message in UI thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Failed to book appointment", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Disconnect HttpURLConnection
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        }).start(); // Start the thread
    }

    private void fetchAllBookings() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL("https://students.washington.edu/tratsko/get_bookings.php");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");

                    int responseCode = urlConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        in.close();

                        Log.d(TAG, response.toString());

                        // Parse JSON and log the bookings
                        parseAndLogBookings(response.toString());
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Failed to retrieve bookings", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void parseAndLogBookings(String jsonResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray bookingsArray = jsonObject.getJSONArray("bookings");

        for (int i = 0; i < bookingsArray.length(); i++) {
            JSONObject booking = bookingsArray.getJSONObject(i);
            Log.d(TAG, "Booking ID: " + booking.getInt("id"));
            Log.d(TAG, "User Email: " + booking.getString("user_email"));
            Log.d(TAG, "Date: " + booking.getString("booking_date"));
            Log.d(TAG, "Time: " + booking.getString("booking_time"));
            Log.d(TAG, "Type: " + booking.getString("booking_type"));
            Log.d(TAG, "Beard: " + booking.getBoolean("beard"));
            Log.d(TAG, "Hot Towel: " + booking.getBoolean("hot_towel"));
        }
    }
}