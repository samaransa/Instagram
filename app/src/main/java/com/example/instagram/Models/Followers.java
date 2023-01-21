package com.example.instagram.Models;

public class Followers {
    private String followedById;
    private long followedAt;

    public Followers() {
    }

    public String getFollowedById() {
        return followedById;
    }

    public void setFollowedById(String followedById) {
        this.followedById = followedById;
    }

    public long getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(long followedAt) {
        this.followedAt = followedAt;
    }
}
