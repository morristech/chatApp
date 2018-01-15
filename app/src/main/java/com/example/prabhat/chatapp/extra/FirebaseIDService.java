package com.example.prabhat.chatapp.extra;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by prabh on 25-10-2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    public static final String FIREBASE_TOKEN = "token";


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPrefUtil.getInstance(getApplicationContext()).put(FIREBASE_TOKEN,refreshedToken);

//        SharedPref.getInstance(getApplicationContext()).putValue(FIREBASEID.toString(), refreshedToken);

    }


}
