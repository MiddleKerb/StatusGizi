package com.fbasegizi.statusgizi.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by bonie on 29-Jun-18.
 */
@IgnoreExtraProperties
public class User {

    private String Nama;
    private String Username;
    private String Email;

    public User() {

    }

    public User(String nama, String username, String email) {
        this.Nama = nama;
        this.Username = username;
        this.Email = email;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        this.Nama = nama;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }
}
