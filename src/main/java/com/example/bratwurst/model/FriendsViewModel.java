package com.example.bratwurst.model;

public class FriendsViewModel {

    private int id;
    private String username;
    private String profile_picture;
    private int user1;
    private int user2;
    private boolean accepted;
    private String first_name;
    private String last_name;
    private String country;

    public FriendsViewModel(int id, String username, String profile_picture, int user1, int user2, boolean accepted, String first_name, String last_name, String country, String email) {
        this.id = id;
        this.username = username;
        this.profile_picture = profile_picture;
        this.user1 = user1;
        this.user2 = user2;
        this.accepted = accepted;
        this.first_name = first_name;
        this.last_name = last_name;
        this.country = country;
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

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
