package com.example.prabhat.chatapp.model;

/**
 * Created by Admin on 1/17/2018.
 */

public class UserStatusModel {
    boolean isOnline;
    String lastSeen;

    public String getTyping() {
        return typing;
    }

    public void setTyping(String typing) {
        this.typing = typing;
    }

    String typing;

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }
}
