package com.example.prabhat.chatapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.prabhat.chatapp.extra.Constants;
import com.example.prabhat.chatapp.extra.SharedPrefUtil;
import com.example.prabhat.chatapp.model.UserNameModel;
import com.example.prabhat.chatapp.model.UserStatusModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.prabhat.chatapp.SplashActivty.NAME;
import static com.example.prabhat.chatapp.extra.Constants.UID;
import static com.example.prabhat.chatapp.extra.Constants.USER_STATUS_TABLE;
import static com.example.prabhat.chatapp.extra.FirebaseIDService.FIREBASE_TOKEN;

public class MainActivity extends AppCompatActivity {
    TextView userName;
    RecyclerView recyclerView;
    Button logout;
    private GoogleSignInClient mGoogleSignInClient;
    private String userId = "";
    ArrayList<UserNameModel> itemList = new ArrayList<>();
    private UserStatusModel userStatusModel;
    Adapter itemArrayAdapter;
    public static final String RECEIVERID = "reciverId";
    public static final String SENDERID = "SENDER_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = findViewById(R.id.userName);
        recyclerView = findViewById(R.id.recView);
        logout = findViewById(R.id.logout);
        userStatusModel = new UserStatusModel();
        userName.setText(getIntent().getStringExtra(NAME));
        userId = SharedPrefUtil.getInstance(this).getString(UID);
        updateUserData();
        updateuserStatus(userStatusModel);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                mGoogleSignInClient.signOut().addOnCompleteListener(MainActivity.this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                            }
                        });

            }
        });

        getUsers();


        itemArrayAdapter = new Adapter(itemList, this, userId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemArrayAdapter);


    }

    private void updateuserStatus(final UserStatusModel userStatusModel) {
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

    private void updateUserData() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserNameModel userNameModel = new UserNameModel();
                userNameModel.setEmail(SharedPrefUtil.getInstance(getApplicationContext()).getString("email"));
                userNameModel.setFirebaseToken(SharedPrefUtil.getInstance(getApplicationContext()).getString(FIREBASE_TOKEN));
                userNameModel.setName(SharedPrefUtil.getInstance(getApplicationContext()).getString("name"));
                userNameModel.setUid(SharedPrefUtil.getInstance(getApplicationContext()).getString(UID));
                HashMap<String, Object> result = new HashMap<>();
                result.put("User Data", userNameModel);
                ref.child("users").child(userId).updateChildren(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUsers() {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.USER_TABLE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                            UserNameModel user = dataSnapshotChild.getChildren().iterator().next().getValue(UserNameModel.class);
                            if (!TextUtils.equals(user.getUid(),
                                    FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                itemList.add(user);
                            }
                            itemArrayAdapter.notifyDataSetChanged();
                        }
                        // All users are retrieved except the one who is currently logged
                        // in device.
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Unable to retrieve the users.
                    }
                });
    }
    @Override
    protected void onPause() {
        super.onPause();
        userStatusModel.setIsOnline(false);
        userStatusModel.setLastSeen(Utils.getDummyDateAndTime());
        updateuserStatus(userStatusModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userStatusModel.setIsOnline(true);
        userStatusModel.setLastSeen(Utils.getDummyDateAndTime());
        updateuserStatus(userStatusModel);
    }

}
