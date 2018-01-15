package com.example.prabhat.chatapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.prabhat.chatapp.extra.Constants;
import com.example.prabhat.chatapp.extra.SharedPrefUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.prabhat.chatapp.SplashActivty.NAME;
import static com.example.prabhat.chatapp.SplashActivty.USER_ID;
import static com.example.prabhat.chatapp.extra.Constants.UID;

public class MainActivity extends AppCompatActivity {
    TextView userName;
    RecyclerView recyclerView;
    Button logout;
    private GoogleSignInClient mGoogleSignInClient;
    private String userId = "";
    ArrayList<UserNameModel> itemList = new ArrayList<>();
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
        userName.setText(getIntent().getStringExtra(NAME));
        userId = SharedPrefUtil.getInstance(this).getString(UID);
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
}
