package com.example.bratwurst.model;

public class User {

   private int id;
   private String username;
   private String  password;
   private String salt;
   private String first_name;
   private String last_name;
   private String country;
   private String city;
   private int age;
   private String email;
   private boolean gender;
   private String profile_picture;

    public User(int id, String username, String password, String salt, String first_name, String last_name, String country, String city, int age, String email, boolean gender, String profile_picture) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.first_name = first_name;
        this.last_name = last_name;
        this.country = country;
        this.city = city;
        this.age = age;
        this.email = email;
        this.gender = gender;
        this.profile_picture = profile_picture;
    }

    //Get Users at homepage
    public User(String username, String first_name, String last_name, String country, String email) {
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.country = country;
        this.email = email;
    }

    public User() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", profile_picture='" + profile_picture + '\'' +
                '}';
    }
}
