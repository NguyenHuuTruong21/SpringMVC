package com.abc.entities;

import java.time.LocalDate;

public class User {
    private int id;
    private String username;
    private String passWord;
    private String email; // Thêm trường email
    private String createdAt;
    private LocalDate dateOfBirth;
    private int provinceId;
    private String avatar;

    // Constructor đầy đủ
    public User(int id, String username, String passWord, String email, String createdAt, LocalDate dateOfBirth, int provinceId, String avatar) {
        this.id = id;
        this.username = username;
        this.passWord = passWord;
        this.email = email;
        this.createdAt = createdAt;
        this.dateOfBirth = dateOfBirth;
        this.provinceId = provinceId;
        this.avatar = avatar;
    }

    // Constructor cho đăng ký
    public User(String username, String passWord, String email) {
        this.username = username;
        this.passWord = passWord;
        this.email = email;
    }

    // Getters và Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}