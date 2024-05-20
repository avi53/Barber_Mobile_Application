package edu.tacoma.uw.barber_mobile_application.ui.services;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.barber_mobile_application.R;
import edu.tacoma.uw.barber_mobile_application.util.NotificationHelper;

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
                String email = "test@test.com";
                String date = "2024-5-21";
                String time = "16:00:00";
                String type = "Haircut";
                boolean beard = false;
                boolean towel = false;

                String[] parts = time.split(":");
                int hour = Integer.parseInt(parts[0]);

                // Subtract 9 to get the hour starting from 0 at 9 AM
                String noon = "PM";
                int singleDigitHour = hour - 12;
                if (singleDigitHour < 1) {
                    singleDigitHour += 12;
                    noon = "AM";
                }
                String formatTime = singleDigitHour + " " + noon;

                submitBookingToServer(email, date, time, type, beard, towel);

                Toast.makeText(getContext(), "Date Booked Successfully!", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Notifcation");
                String output = String.format("Your booking on %s, at %s been confirmed.", date, formatTime);
                NotificationHelper.displayNotification(getContext(), "Booking Confirmed", output);
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
                                Toast.makeText(getContext(), "Booking successful!", Toast.LENGTH_SHORT).show();
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
}