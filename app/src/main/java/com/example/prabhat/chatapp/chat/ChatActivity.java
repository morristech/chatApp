package com.example.prabhat.chatapp.chat;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.prabhat.chatapp.R;
import com.example.prabhat.chatapp.Utils;
import com.example.prabhat.chatapp.extra.SharedPrefUtil;
import com.example.prabhat.chatapp.model.ChatModel;
import com.example.prabhat.chatapp.model.UserNameModel;
import com.example.prabhat.chatapp.model.UserStatusModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.prabhat.chatapp.MainActivity.RECEIVERID;
import static com.example.prabhat.chatapp.extra.Constants.REC_USER_NAME;
import static com.example.prabhat.chatapp.extra.Constants.UID;
import static com.example.prabhat.chatapp.extra.Constants.USER_TABLE;
import static com.example.prabhat.chatapp.extra.FirebaseIDService.FIREBASE_TOKEN;

public class ChatActivity extends AppCompatActivity implements ChatInterface.View {
    private RecyclerView chatRecycle;
    private AppCompatEditText editText;
    private Button send;
    private PresenterImpl presenter;
    private Context context;
    private String nameString;
    private TextView name,status,lastSeen;


    private String recId, senderId;
    private List<ChatModel> chatModels = new ArrayList<>();
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        nameString = getIntent().getStringExtra(REC_USER_NAME);
        recId = getIntent().getStringExtra(RECEIVERID);
        senderId = SharedPrefUtil.getInstance(this).getString(UID);
        bindViews();

        name.setText(nameString);
        setStatus();
       // getRecFirebaseId();
    }

    private void setStatus() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child(USER_TABLE).child(recId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               UserStatusModel userStatusModel = dataSnapshot.getValue(UserStatusModel.class);
               if (userStatusModel.getIsOnline())
               {
                   status.setText("Online");
               }else {
                   status.setText("offline");
               }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void bindViews() {
        presenter = new PresenterImpl(this);
        context = this;

        name = findViewById(R.id.name);
        status = findViewById(R.id.status);
        lastSeen = findViewById(R.id.lastSeen);

        chatRecycle = findViewById(R.id.chatRecycle);
        editText = findViewById(R.id.editText);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
        chatAdapter = new ChatAdapter(this, chatModels, senderId);
        chatRecycle.setLayoutManager(new LinearLayoutManager(this));
        chatRecycle.setItemAnimator(new DefaultItemAnimator());
        chatRecycle.setAdapter(chatAdapter);

        presenter.getMessageFromFirebaseUser(senderId, recId);
    }

//    private void getData() {
//        FirebaseDatabase.getInstance().getReference("message").getRef().addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    ChatModel chatModel = postSnapshot.getChildren().iterator().next().getValue(ChatModel.class);
//                    chatModels.add(chatModel);
//
//                }
//
//                chatAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.e("error", "Failed to read app title value.", error.toException());
//            }
//        });
//    }

    private void sendData() {
        ChatModel chatModel = new ChatModel();
        chatModel.setMessage(editText.getText().toString().trim());
        chatModel.setSenderUid(senderId);
        chatModel.setReceiverUid(recId);
        chatModel.setTimestamp(Utils.getDummyDateAndTime());
        presenter.sendMessageToFirebaseUser(context, chatModel, SharedPrefUtil.getInstance(context).getString(FIREBASE_TOKEN));
    }

    @Override
    public void getMessageSuccess(ChatModel chat) {
        chatModels.add(chat);
        chatAdapter.notifyMe();
    }

    @Override
    public void getMessageError() {

    }


}
