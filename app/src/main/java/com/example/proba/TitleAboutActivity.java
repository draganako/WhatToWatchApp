package com.example.proba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proba.datamodels.Comment;
import com.example.proba.datamodels.CommentData;
import com.example.proba.datamodels.FavoriteTitle;
import com.example.proba.datamodels.FavoriteTitleData;
import com.example.proba.datamodels.Title;
import com.example.proba.datamodels.TitleData;

public class TitleAboutActivity extends AppCompatActivity {

    private Button newComment;
    private Button viewComment;
    private Button buttonAddToFavs;
    private ImageView titleImageView;
    private TextView nameAndYeartextView;
    private TextView synopsisTextView;
    private TextView actorstextView;

    Title currentTitle;
    String username;
    String email;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
        setContentView(R.layout.activity_title_about);

        sharedPreferences=getApplicationContext().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
        username = sharedPreferences.getString(getString(R.string.loggedUser_username), "EMPTY");
        email=sharedPreferences.getString(getString(R.string.loggedUser_email), "EMPTY");
        currentTitle=TitleData.getInstance().getTitle(getIntent().getStringExtra("titleName"));


        buttonAddToFavs=findViewById(R.id.buttonAddToFavs);
        buttonAddToFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteTitleData.getInstance().AddFavoriteTitle(new FavoriteTitle(email,currentTitle));
                Toast.makeText(getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();

            }
        });

        titleImageView=findViewById(R.id.imageViewTitleImage);
        if (currentTitle.image != null && !currentTitle.image.equals("")) {
            Glide.with(this).load(currentTitle.image).into(titleImageView);
        } else {
            titleImageView.setImageResource(R.drawable.ic_user_24);
        }

        nameAndYeartextView=findViewById(R.id.textViewTitleNameAndYear);
        nameAndYeartextView.setText(currentTitle.name+" ("+currentTitle.year+")");

        synopsisTextView=findViewById(R.id.textViewTitleSynopsis);
        synopsisTextView.setText("Synopsis: "+currentTitle.synopsis);

        actorstextView=findViewById(R.id.textViewTitleActors);
        actorstextView.setText("Actors: "+currentTitle.actors);

        newComment=findViewById(R.id.buttonLeave);
        newComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(TitleAboutActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(TitleAboutActivity.this);
                builder.setTitle("Add a comment")
                        .setView(input)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==-1)//ADD
                                {
                                    String commText = String.valueOf(input.getText());
                                    CommentData.getInstance().AddComment(new Comment(username,commText,getIntent().getStringExtra("titleName")));
                                    Toast.makeText(getApplicationContext(), "Comment added", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();

                        builder.show();

            }
        });

        viewComment=findViewById(R.id.buttonView);
        viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), CommentsActivity.class);
                intent.putExtra("titleName",currentTitle.name);
                startActivity(intent);
               // finish();
            }
        });

    }
}