package com.example.proba.datamodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentData {
    private ArrayList<Comment> comments;
    private HashMap<String, Integer> commentsMapping;
    public DatabaseReference db;
    private static final String FIREBASE_CHILD= "Comments";

    private CommentData()
    {
        comments = new ArrayList<>();
        commentsMapping  = new HashMap<String, Integer>();

        db = FirebaseDatabase.getInstance().getReference();
        db.child(FIREBASE_CHILD).addChildEventListener(childEventListener);
        db.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);

    }

    private static class SingletonHolder {
        public static final CommentData instance = new CommentData();
    }

    public static CommentData getInstance() {
        return CommentData.SingletonHolder.instance;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    CommentData.ListUpdatedEventListener updateListener;
    public void setEventListener(CommentData.ListUpdatedEventListener listener) {
        updateListener = listener;
    }
    public interface ListUpdatedEventListener {
        void onCommentUpdated();
    }

    ReadyEventListener probaList;
    public void setReadyList(ReadyEventListener listener) {
        probaList = listener;
    }
    public interface ReadyEventListener {
        void onReady();
    }

    ValueEventListener parentEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (updateListener != null)
             updateListener.onCommentUpdated();

            if(probaList != null)
                probaList.onReady();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myCommentKey = dataSnapshot.getKey();

            if (!commentsMapping.containsKey(myCommentKey)) {
                Comment myComment = dataSnapshot.getValue(Comment.class);
                myComment.key = myCommentKey;
                comments.add(myComment);
                commentsMapping.put(myCommentKey, comments.size() - 1);
                if (updateListener != null)
                  updateListener.onCommentUpdated();
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myCommentKey = dataSnapshot.getKey();
            Comment myComment = dataSnapshot.getValue(Comment.class);
            myComment.key = myCommentKey;
            if (commentsMapping.containsKey(myCommentKey)) {
                int index = commentsMapping.get(myCommentKey);
                comments.set(index, myComment);
            } else {
                comments.add(myComment);
                commentsMapping.put(myCommentKey, comments.size() - 1);
            }
            if (updateListener != null)
                updateListener.onCommentUpdated();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            String myCommentKey = dataSnapshot.getKey();
            if (commentsMapping.containsKey(myCommentKey)) {
                int index = commentsMapping.get(myCommentKey);
                comments.remove(index);
                recreateKeyIndexMapping();
            }
             if (updateListener != null)
             updateListener.onCommentUpdated();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void AddComment(Comment p)
    {
        String key = db.push().getKey();
        comments.add(p);
        commentsMapping.put(key, comments.size() - 1);
        db.child(FIREBASE_CHILD).child(key).setValue(p);
        p.key = key;
    }

    public Comment getComment(int index) {
        return comments.get(index);
    }

    public List<Comment> getComments(String titleName) {

        List<Comment> comms=new ArrayList<>();
        for(int i =0; i< this.comments.size(); i++)
        {
            if(this.comments.get(i).titleName.compareTo(titleName) ==0)
            {
                comms.add(this.comments.get(i));
            }
        }

        return comms;


    }

    private void recreateKeyIndexMapping()
    {
        commentsMapping.clear();
        for (int i=0;i<comments.size();i++)
            commentsMapping.put(comments.get(i).key,i);
    }
}
