package com.example.instagram.Models;

public class Discover {
    String profileName;
    int profilePhoto;

    public Discover(String profileName, int profilePhoto) {
        this.profileName = profileName;
        this.profilePhoto = profilePhoto;
    }

    public String getProfileName() {
        return profileName;
    }

    public Discover(int profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public int getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(int profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
