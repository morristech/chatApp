package com.example.prabhat.chatapp.chat;

import android.content.Context;
import android.widget.Toast;

import com.example.prabhat.chatapp.extra.Constants;
import com.example.prabhat.chatapp.extra.SharedPrefUtil;
import com.example.prabhat.chatapp.model.ChatModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by prabhat on 15/1/18.
 */

public class PresenterImpl implements ChatInterface.Presenter {
    private ChatInterface.View view;

    public PresenterImpl(ChatInterface.View view) {
        this.view = view;
    }

    @Override
    public void sendMessageToFirebaseUser(String nameString, final Context context, final ChatModel chatModel, String string) {
        FcmNotificationBuilder.initialize().firebaseToken(SharedPrefUtil.getInstance(context).getString(Constants.FIREBASE_REC_TOKEN)).recToken(SharedPrefUtil.getInstance(context).getString(Constants.FIREBASE_REC_TOKEN))
                .message(chatModel.getMessage()).title("New Message From " + " " + nameString).send();
        final String room_type_1 = chatModel.getSenderUid() + "_" + chatModel.getReceiverUid();
        final String room_type_2 = chatModel.getReceiverUid() + "_" + chatModel.getSenderUid();
        // view.getMessageSuccess(chatModel);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference();

        databaseReference.child(Constants.MESS_TABLE)
                .getRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(room_type_1)) {
                            // Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_1 + " exists");
                            databaseReference.child(Constants.MESS_TABLE)
                                    .child(room_type_1)
                                    .child(chatModel.getTimestamp())
                                    .setValue(chatModel);
                        } else if (dataSnapshot.hasChild(room_type_2)) {
                            // Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_2 + " exists");
                            databaseReference.child(Constants.MESS_TABLE)
                                    .child(room_type_2)
                                    .child(String.valueOf(chatModel.getTimestamp()))
                                    .setValue(chatModel);
                        } else {
                            // Log.e(TAG, "sendMessageToFirebaseUser: success");
                            databaseReference.child(Constants.MESS_TABLE)
                                    .child(room_type_1)
                                    .child(String.valueOf(chatModel.getTimestamp()))
                                    .setValue(chatModel);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Unable to send message", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void getMessageFromFirebaseUser(String senderId, String receiverUid) {
        final String room_type_1 = senderId + "_" + receiverUid;
        final String room_type_2 = receiverUid + "_" + senderId;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference();

        databaseReference.child(Constants.MESS_TABLE)
                .getRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(room_type_1)) {
                            //   Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_1 + " exists");
                            FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child(Constants.MESS_TABLE)
                                    .child(room_type_1)
                                    .addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            // Chat message is retreived.
                                            ChatModel chat = dataSnapshot.getValue(ChatModel.class);
                                            view.getMessageSuccess(chat);
                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Unable to get message.
                                        }
                                    });
                        } else if (dataSnapshot.hasChild(room_type_2)) {
                            // Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_2 + " exists");
                            FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child(Constants.MESS_TABLE)
                                    .child(room_type_2)
                                    .addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            // Chat message is retreived.
                                            ChatModel chat = dataSnapshot.getValue(ChatModel.class);
                                            view.getMessageSuccess(chat);
                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Unable to get message.
                                        }
                                    });
                        } else {
                            //  Log.e(TAG, "getMessageFromFirebaseUser: no such room available");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Unable to get message
                    }
                });
    }
}
