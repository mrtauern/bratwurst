package com.example.bratwurst.model;

public class FriendsViewModel {

    private int id;
    private String username;
    private String profile_picture;
    private int user1;
    private int user2;
    private boolean accepted;

    public FriendsViewModel(int id, String username, String profile_picture, int user1, int user2, boolean accepted) {
        this.id = id;
        this.username = username;
        this.profile_picture = profile_picture;
        this.user1 = user1;
        this.user2 = user2;
        this.accepted = accepted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public int getUser1() {
        return user1;
    }

    public void setUser1(int user1) {
        this.user1 = user1;
    }

    public int getUser2() {
        return user2;
    }

    public void setUser2(int user2) {
        this.user2 = user2;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
