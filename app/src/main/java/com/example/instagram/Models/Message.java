package com.example.instagram.Models;

public class Message {
    String profileName, shortMessage;
    int profileImage;

    public Message(String profileName, String shortMessage, int profileImage) {
        this.profileName = profileName;
        this.shortMessage = shortMessage;
        this.profileImage = profileImage;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }
}
