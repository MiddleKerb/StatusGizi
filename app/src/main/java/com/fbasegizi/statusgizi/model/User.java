package com.fbasegizi.statusgizi.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by bonie on 29-Jun-18.
 */

@IgnoreExtraProperties
public class User {

    public String Username;
    public String email;
    public String Nama;

    public User() {

    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        this.Nama = nama;
    }

    public User(String username, String email, String nama) {
        this.Username = username;
        this.email = email;
        this.Nama = nama;
    }
}
