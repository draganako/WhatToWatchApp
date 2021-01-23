package com.example.proba.datamodels;

import com.google.firebase.database.Exclude;

public class FavoriteTitle {
    public String email;
    public Title userFavTitle;

    @Exclude
    public String key;
    public FavoriteTitle()
    {
        email="proba@gmail.com";
        userFavTitle=new Title();
    }

    public FavoriteTitle(String email)
    {
        this.email=email;
        userFavTitle=new Title();
    }

    public FavoriteTitle(String email,Title title)
    {
        this.email=email;
        userFavTitle=title;
    }
}
