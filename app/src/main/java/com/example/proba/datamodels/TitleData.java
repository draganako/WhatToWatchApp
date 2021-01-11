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

public class TitleData {

    private ArrayList<Title> titles;
    private HashMap<String, Integer> titlesMapping;
    public DatabaseReference db;
    private static final String FIREBASE_CHILD= "Titles";

    private TitleData()
    {
        titles = new ArrayList<>();
        titlesMapping  = new HashMap<String, Integer>();

        db = FirebaseDatabase.getInstance().getReference();
        db.child(FIREBASE_CHILD).addChildEventListener(childEventListener);
        db.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);

    }

    private static class SingletonHolder {
        public static final TitleData instance = new TitleData();
    }

    public static TitleData getInstance() {
        return TitleData.SingletonHolder.instance;
    }

    public ArrayList<Title> getTitles() {
        return titles;
    }

    TitleData.ListUpdatedEventListener updateListener;
    public void setEventListener(TitleData.ListUpdatedEventListener listener) {
        updateListener = listener;
    }
    public interface ListUpdatedEventListener {
        void onTitleUpdated(Title ss);
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
            //if (updateListener != null)
            // updateListener.onTitleUpdated();

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
            String myTitleKey = dataSnapshot.getKey();

            if (!titlesMapping.containsKey(myTitleKey)) {
                Title myTitle = dataSnapshot.getValue(Title.class);
                myTitle.key = myTitleKey;
                titles.add(myTitle);
                titlesMapping.put(myTitleKey, titles.size() - 1);
                //if (updateListener != null)
                //  updateListener.onTitleUpdated();
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myTitleKey = dataSnapshot.getKey();
            Title myTitle = dataSnapshot.getValue(Title.class);
            myTitle.key = myTitleKey;
            if (titlesMapping.containsKey(myTitleKey)) {
                int index = titlesMapping.get(myTitleKey);
                titles.set(index, myTitle);
            } else {
                titles.add(myTitle);
                titlesMapping.put(myTitleKey, titles.size() - 1);
            }
            if (updateListener != null)
                updateListener.onTitleUpdated(myTitle);
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            String myTitleKey = dataSnapshot.getKey();
            if (titlesMapping.containsKey(myTitleKey)) {
                int index = titlesMapping.get(myTitleKey);
                titles.remove(index);
                recreateKeyIndexMapping();
            }
            // if (updateListener != null)
            // updateListener.onTitleUpdated();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void AddTitle(Title p)
    {
        String key = db.push().getKey();
        titles.add(p);
        titlesMapping.put(key, titles.size() - 1);
        db.child(FIREBASE_CHILD).child(key).setValue(p);
        p.key = key;
    }

    public Title getTitle(int index) {
        return titles.get(index);
    }

   /* public Title getTitle(String username) {
        Title uu = null;
        for(int i =0; i< this.titles.size(); i++)
        {
            if(this.titles.get(i).useer.compareTo(username) ==0)
            {
                uu = this.titles.get(i);
            }
        }

        return uu;


    }*/

    public void deleteTitle(int index) {

        db.child(FIREBASE_CHILD).child(titles.get(index).key).removeValue();
        titles.remove(index);
        recreateKeyIndexMapping();
    }

    public void updateTitle(int index,  Title u)
    {
        Title uu =titles.get(index);
        uu.image=u.image;
        uu.name=u.name;
        uu.synopsis=u.synopsis;
        uu.year=u.year;

        db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);

    }



    private void recreateKeyIndexMapping()
    {
        titlesMapping.clear();
        for (int i=0;i<titles.size();i++)
            titlesMapping.put(titles.get(i).key,i);
    }
}
