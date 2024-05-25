package edu.tacoma.uw.barber_mobile_application.util;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTime {

    public static String formatDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d", Locale.getDefault());
        try {
            Date date = inputFormat.parse(dateString);
            assert date != null;
            return outputFormat.format(date);
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
            //e.printStackTrace();
            return dateString; // Return original string if parsing fails
        }
    }

    public static void main(String[] args) {
        String inputDate = "2024-5-21";
        String formattedDate = formatDate(inputDate);
        System.out.println(formattedDate); // Output: May 21st, 2024
    }

    public static String formatTime(String bookingTime) {
        String[] parts = bookingTime.split(":");
        int hour = Integer.parseInt(parts[2]);
        String noon;
        int singleDigitHour = hour;
        if (hour < 6 || hour == 12) { noon = "PM"; } else { noon = "AM"; }
        String formatTime = singleDigitHour + " " + noon;
        return formatTime;
    }

    public static String formatTime(int hour) {
        String noon;
        int singleDigitHour = hour;
        if (hour < 6 || hour == 12) { noon = "PM"; } else { noon = "AM"; }

        String formatTime = singleDigitHour + " " + noon;
        return formatTime;
    }
}
