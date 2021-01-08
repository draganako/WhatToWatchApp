package com.example.proba.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proba.CommentsActivity;
import com.example.proba.R;
import com.example.proba.datamodels.Comment;
import com.example.proba.datamodels.Title;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>
{
    private List<Comment> commentList;
    private Context context;

    public CommentAdapter(List<Comment> cl, Context c)
    {
        commentList=cl;
        context=c;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_member_comment,parent,false);
        return new CommentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        Comment comm=commentList.get(position);
        holder.commentText.setText(comm.author+": "+comm.text);
        //holder.authorImage...(title.image);
        holder.authorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(this, ProfileActivity.class);
                //startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView authorImage;
        public TextView commentText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            authorImage=(ImageView)itemView.findViewById(R.id.imageViewTitlePhotoComms);
            commentText=(TextView)itemView.findViewById(R.id.commText);
        }
    }
}
