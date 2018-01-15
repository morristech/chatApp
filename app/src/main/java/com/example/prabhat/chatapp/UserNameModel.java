package com.example.prabhat.chatapp;

/**
 * Created by prabhat on 13/1/18.
 */

public class UserNameModel {
    private String name;
    private String email;
    private String Uid;
    private String firebaseToken;

    public UserNameModel() {
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
