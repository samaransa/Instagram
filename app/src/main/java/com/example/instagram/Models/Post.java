package com.example.instagram.Models;

public class Post {
    int profileImage, postPicture;

    public Post(int profileImage, int postPicture) {
        this.profileImage = profileImage;
        this.postPicture = postPicture;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public int getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(int postPicture) {
        this.postPicture = postPicture;
    }
}
