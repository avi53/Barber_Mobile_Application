package edu.tacoma.uw.barber_mobile_application.util;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Utility class to help with sending and getting data from database
 */
public class BookingHelper {

    public static void submitBookingToServer(String email, String date, String time, String type, boolean beard, boolean towel, Fragment f) {
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
                    f.getActivity().runOnUiThread(() -> {
//                        Toast.makeText(getContext(), "Booking successful!", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Display error message in UI thread
                    f.getActivity().runOnUiThread(() -> Toast.makeText(f.getContext(), "Failed to book appointment", Toast.LENGTH_SHORT).show());
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
