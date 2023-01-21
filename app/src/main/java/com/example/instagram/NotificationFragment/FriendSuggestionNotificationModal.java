package com.example.instagram.NotificationFragment;

public class FriendSuggestionNotificationModal {
    String userName, profileName;
    int profileImage;

    public FriendSuggestionNotificationModal(String userName, String profileName, int profileImage) {
        this.userName = userName;
        this.profileName = profileName;
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
