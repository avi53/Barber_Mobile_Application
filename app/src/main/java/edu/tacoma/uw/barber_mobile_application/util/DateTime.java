package edu.tacoma.uw.barber_mobile_application.util;

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
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString; // Return original string if parsing fails
        }
    }

    public static void main(String[] args) {
        String inputDate = "2024-5-21";
        String formattedDate = formatDate(inputDate);
        System.out.println(formattedDate); // Output: May 21st, 2024
    }
}
