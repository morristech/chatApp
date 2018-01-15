package com.example.prabhat.chatapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by prabhat on 15/1/18.
 */

public class Utils {
    public static String getDummyDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("KK:mm a", Locale.ENGLISH);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM ", Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        return (sdf1.format(new Date()) + "at" + " " + sdf.format(cal.getTime()).replace("AM", "am").replace("PM", "pm"));
    }
}
