package com.example.proba.datamodels;

import java.io.Serializable;

public class Title implements Serializable {
    public String image;
    public String name;
    public String synopsis;
    public int year;
    public String key;
    public boolean isAMovie;

    public Title()
    {
        image="akvnsdk";
        name="Serijica";
        synopsis="Ovo je serija";
        year=1984;
        isAMovie=true;
    }
    public Title(String t)
    {
        image="shs";
        name="add";
        synopsis="Ovo je film";
        year=1980;
        isAMovie=false;
    }
}
