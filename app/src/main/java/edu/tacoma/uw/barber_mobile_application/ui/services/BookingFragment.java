package edu.tacoma.uw.barber_mobile_application.ui.services;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.barber_mobile_application.R;
import edu.tacoma.uw.barber_mobile_application.util.DateTime;

/**
 * Fragment to show the current bookings. Grabs all bookings from the server and
 * filters for the only ones that are relevant to this user.
 */
public class BookingFragment extends Fragment {

    private TextView mBookingsTextView;

    // Constant(s) definitions for SharedPreference accessibility
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String USER_EMAIL_KEY = "user_email";

    private String curEmail;

    public BookingFragment() {
        // Required empty public constructor
    }

    public static BookingFragment newInstance(String param1, String param2) {
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences =
                requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String curEmail = sharedPreferences.getString(USER_EMAIL_KEY, "");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);
        mBookingsTextView = view.findViewById(R.id.displayBookings);

        fetchBookings();

        return view;
    }

    /**
     * Grab the bookings from the backend and then parse them and add them to the textview.
     */
    private void fetchBookings() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection;
                urlConnection = null;
                try {
                    URL url = new URL("https://students.washington.edu/tratsko/get_bookings.php");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");

                    int responseCode = urlConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        parseAndLogBookings(response.toString());
                    } else {
                        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Failed to retrieve bookings", Toast.LENGTH_SHORT).show());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * Grabs all of the bookings and displays them. Also logs them.
     * @param jsonResponse The json containing all of the bookings.
     * @throws JSONException Throws if passed in a bad formatted json.
     */
    private void parseAndLogBookings(String jsonResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray bookingsArray = jsonObject.getJSONArray("bookings");

        final StringBuilder bookingsDisplay = new StringBuilder();

        for (int i = 0; i < bookingsArray.length(); i++) {
            JSONObject booking = bookingsArray.getJSONObject(i);
//            bookingsDisplay.append("Booking ID: " + booking.getInt("id"));
//            bookingsDisplay.append("User Email: " + booking.getString("user_email"));
            String email = booking.getString("user_email");

            SharedPreferences sharedPreferences =
                    requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            String curEmail = sharedPreferences.getString(USER_EMAIL_KEY, "");

            Log.d("Booking Email", "the email is " + email);
            Log.d("Cur Email", "The current email is " + curEmail);

            if (!email.equals(curEmail)) { continue; }

            String bookingDay = booking.getString("booking_date");
            String bookingTime = booking.getString("booking_time");
            String bookingType = booking.getString("booking_type");

            Log.d(TAG, "Booking ID: " + booking.getInt("id"));
            Log.d(TAG, "User Email: " + booking.getString("user_email"));
            Log.d(TAG, "Date: " + bookingDay);
            Log.d(TAG, "Time: " + bookingTime);
            Log.d(TAG, "Type: " + bookingType);
            Log.d(TAG, "Beard: " + booking.getString("beard"));
            Log.d(TAG, "Hot Towel: " + booking.getString("hot_towel"));

            bookingDay = DateTime.formatDate(bookingDay);
            bookingTime = DateTime.formatTime(bookingTime);

            String message = "On " + bookingDay + " at "
                    + bookingTime + ". There is a " + bookingType + " scheduled.\n";
            bookingsDisplay.append(message);
        }
        Log.d("Full bookings", bookingsDisplay.toString());


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBookingsTextView.setText(bookingsDisplay.toString());
            }
        });
    }
}