package com.example.prabhat.chatapp.chat;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.prabhat.chatapp.R;
import com.example.prabhat.chatapp.UserNameModel;
import com.example.prabhat.chatapp.Utils;
import com.example.prabhat.chatapp.extra.Constants;
import com.example.prabhat.chatapp.extra.SharedPrefUtil;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.prabhat.chatapp.MainActivity.RECEIVERID;
import static com.example.prabhat.chatapp.extra.Constants.FIREBASE_REC_TOKEN;
import static com.example.prabhat.chatapp.extra.Constants.UID;
import static com.example.prabhat.chatapp.extra.FirebaseIDService.FIREBASE_TOKEN;

public class ChatActivity extends AppCompatActivity implements ChatInterface.View {
    private RecyclerView chatRecycle;
    private AppCompatEditText editText;
    private Button send;
    private PresenterImpl presenter;
    private Context context;
    private String recId, senderId;
    private List<ChatModel> chatModels = new ArrayList<>();
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        bindViews();
       // getRecFirebaseId();
    }

//    private void getRecFirebaseId() {
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference().child(Constants.USER_TABLE).child(recId);
//
//// Attach a listener to read the data at our posts reference
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                UserNameModel post = dataSnapshot.getValue(UserNameModel.class);
//                SharedPrefUtil.getInstance(context).put(FIREBASE_REC_TOKEN,post.getFirebaseToken());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//            }
//        });
//    }

    private void bindViews() {
        presenter = new PresenterImpl(this);
        context = this;
        recId = getIntent().getStringExtra(RECEIVERID);
        senderId = SharedPrefUtil.getInstance(this).getString(UID);
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
//        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        ChatModel chatModel = new ChatModel();
//        chatModel.setMessage(editText.getText().toString().trim());
//        chatModel.setType(senderId);
//        chatModels.add(chatModel);
//        chatAdapter.notifyMe();
//        ref.child("message").child(senderId + "->" + recId).push().setValue(chatModel);
        ChatModel chatModel = new ChatModel();
        chatModel.setMessage(editText.getText().toString().trim());
        chatModel.setSenderUid(senderId);
        chatModel.setReceiverUid(recId);
        chatModel.setTimestamp(String.valueOf(System.currentTimeMillis()));
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
