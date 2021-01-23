package com.example.proba.datamodels;

import com.google.firebase.database.Exclude;

import java.sql.Time;

public class Comment
{
    public String titleName;
    public String author;
    public String authorImage;
    public String text;
    //public Time time;

    @Exclude
    public String key;

    public Comment(String user, String textt, String titleNamee)
    {
        author=user;
        text=textt;
        titleName=titleNamee;
    }
}
