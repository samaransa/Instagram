package com.example.instagram.Models;

public class Stories {
    String  profileName;
    int profileImage;

    public Stories(String profileName, int profileImage) {
        this.profileName = profileName;
        this.profileImage = profileImage;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }
}
