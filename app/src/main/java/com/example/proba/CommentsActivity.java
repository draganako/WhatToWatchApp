package com.example.proba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class CommentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        TextView title=findViewById(R.id.textViewTitleName);
        title.setText(getIntent().getStringExtra("titleName"));
    }
}