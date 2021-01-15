package com.example.proba;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proba.adapters.CommentAdapter;
import com.example.proba.adapters.TitleAdapter;
import com.example.proba.datamodels.CommentData;
import com.example.proba.datamodels.Title;
import com.example.proba.datamodels.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentsFragment extends Fragment {


    private RecyclerView recyclerView;
    private List<Comment> commentList;
    private CommentAdapter adapter;
    private SharedPreferences sharedPreferences;

    public CommentsFragment() {
        // Required empty public constructor
    }


    public static CommentsFragment newInstance(String param1, String param2) {
        CommentsFragment fragment = new CommentsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_comments);
        recyclerView.setHasFixedSize(true);

       // CommentData.getInstance().AddComment(new Comment());
        sharedPreferences=getContext().getSharedPreferences( "Titledata", Context.MODE_PRIVATE);
        String titleName=sharedPreferences.getString(getString(R.string.title_name), "EMPTY");
        commentList= CommentData.getInstance().getComments(titleName);

        adapter=new CommentAdapter(commentList,view.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }
}