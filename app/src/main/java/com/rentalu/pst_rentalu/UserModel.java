package com.rentalu.pst_rentalu;

public class UserModel {
    int user_id;
    String fullname, username, password, email, phnum;

    public UserModel(int user_id, String fullname, String username, String password, String email, String phnum) {
        this.user_id = user_id;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phnum = phnum;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhnum() {
        return phnum;
    }

    public void setPhnum(String phnum) {
        this.phnum = phnum;
    }
}
