package com.example.bratwurst.model;

import java.sql.Timestamp;

public class File {
    private int id;
    private String filename;
    private Timestamp upload_time;
    private int user;

    public File() {
    }

    public File(String filename, int user) {
        this.filename = filename;
        this.user = user;
    }

    public File(int id, String filename, Timestamp upload_time, int user) {
        this.id = id;
        this.filename = filename;
        this.upload_time = upload_time;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Timestamp getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(Timestamp upload_time) {
        this.upload_time = upload_time;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", upload_time=" + upload_time +
                ", user=" + user +
                '}';
    }
}
