package com.example.proba.datamodels;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Title implements Serializable {
    public String image;
    public String name;
    public String synopsis;
    public String actors;
    public int year;
    public boolean isAMovie;
    @Exclude
    public String key;
    public Title()
    {
        image="akvnsdk";
        name="Serijica";
        synopsis="Ovo je serija";
        actors="Clod, Phil, Mark, Diana";
        year=1984;
        isAMovie=true;
    }
    public Title(String t)
    {
        image="shs";
        name="add";
        synopsis="Ovo je film";
        actors="Clod, Anna, Mark, Diana";
        year=1980;
        isAMovie=false;
    }
}
