package com.example.prabhat.chatapp.extra;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by prabh on 25-10-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String NOTIFICATION_CLICKED = "clicked";
    private static final String TAG = "FCM Service";
    private Bitmap bitmap;
    private Context context = this;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

    }

}
