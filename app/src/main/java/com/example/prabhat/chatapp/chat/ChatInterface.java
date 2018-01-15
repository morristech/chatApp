package com.example.prabhat.chatapp.chat;

import android.content.Context;

/**
 * Created by prabhat on 15/1/18.
 */

public class ChatInterface {
    public interface View {
        void getMessageSuccess(ChatModel chat);

        void getMessageError();
    }

    public interface Presenter {
        void sendMessageToFirebaseUser(Context context, ChatModel chatModel, String string);

        void getMessageFromFirebaseUser(String senderId, String recId);
    }
}
