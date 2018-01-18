package com.example.prabhat.chatapp;

import com.example.prabhat.chatapp.model.UserStatusModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.example.prabhat.chatapp.extra.Constants.USER_STATUS_TABLE;

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

    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("KK:mm a", Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        return (sdf.format(cal.getTime()).replace("AM", "am").replace("PM", "pm"));

    }

    public static void updateuserStatus(final UserStatusModel userStatusModel, final String userId) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> result = new HashMap<>();
                result.put(USER_STATUS_TABLE, userStatusModel);
                ref.child("users").child(userId).updateChildren(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
