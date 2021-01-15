package com.example.proba.datamodels;

import com.google.firebase.database.Exclude;

import java.util.List;

public class User {

    public String username;
    public String email;
    public String picture;

    @Exclude
    public String key;

}
