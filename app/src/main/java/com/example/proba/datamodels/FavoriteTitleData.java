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

public class FavoriteTitleData
{
    private ArrayList<FavoriteTitle> favTitlePairs;
    private HashMap<String, Integer> favTitlePairsMapping;
    public DatabaseReference db;
    private static final String FIREBASE_CHILD= "FavoriteTitle";

    private FavoriteTitleData()
    {
        favTitlePairs = new ArrayList<>();
        favTitlePairsMapping  = new HashMap<String, Integer>();

        db = FirebaseDatabase.getInstance().getReference();
        db.child(FIREBASE_CHILD).addChildEventListener(childEventListener);
        db.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);
    }

    private static class SingletonHolder {
        public static final FavoriteTitleData instance = new FavoriteTitleData();
    }

    public static FavoriteTitleData getInstance() {
        return FavoriteTitleData.SingletonHolder.instance;
    }

    public ArrayList<FavoriteTitle> getfavTitlePairs() {
        return favTitlePairs;
    }

    FavoriteTitleData.ListUpdatedEventListener updateListener;
    public void setEventListener(FavoriteTitleData.ListUpdatedEventListener listener) {
        updateListener = listener;
    }

    public interface ListUpdatedEventListener {
        void onListUpdatedFavs(FavoriteTitle f);
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
           /*if (updateListener != null)
                updateListener.onListUpdatedFavs();*/
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                favTitlePairs.add(postSnapshot.getValue(FavoriteTitle.class));
            }

            if(probaList != null)
            {
                probaList.onReady();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myFavoriteTitleKey = dataSnapshot.getKey();

            if (!favTitlePairsMapping.containsKey(myFavoriteTitleKey)) {
                FavoriteTitle myFavoriteTitle = dataSnapshot.getValue(FavoriteTitle.class);
                myFavoriteTitle.key = myFavoriteTitleKey;
                favTitlePairs.add(myFavoriteTitle);
                favTitlePairsMapping.put(myFavoriteTitleKey, favTitlePairs.size() - 1);
                if (updateListener != null)
                    updateListener.onListUpdatedFavs(myFavoriteTitle);

            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myFavoriteTitleKey = dataSnapshot.getKey();
            FavoriteTitle myFavoriteTitle = dataSnapshot.getValue(FavoriteTitle.class);
            myFavoriteTitle.key = myFavoriteTitleKey;
            if (favTitlePairsMapping.containsKey(myFavoriteTitleKey)) {
                int index = favTitlePairsMapping.get(myFavoriteTitleKey);
                favTitlePairs.set(index, myFavoriteTitle);
            } else {
                favTitlePairs.add(myFavoriteTitle);
                favTitlePairsMapping.put(myFavoriteTitleKey, favTitlePairs.size() - 1);
            }
            if (updateListener != null)
                updateListener.onListUpdatedFavs(myFavoriteTitle);

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            String myFavoriteTitleKey = dataSnapshot.getKey();
            FavoriteTitle myFavoriteTitle=null;
            if (favTitlePairsMapping.containsKey(myFavoriteTitleKey)) {
                int index = favTitlePairsMapping.get(myFavoriteTitleKey);
                myFavoriteTitle=favTitlePairs.remove(index);
                recreateKeyIndexMapping();
            }
            if (updateListener != null) {
                updateListener.onListUpdatedFavs(myFavoriteTitle);
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public void AddFavoriteTitle(FavoriteTitle p)
    {
        String key = db.push().getKey();
        favTitlePairs.add(p);
        favTitlePairsMapping.put(key, favTitlePairs.size() - 1);
        db.child(FIREBASE_CHILD).child(key).setValue(p);
        p.key = key;
    }

    public ArrayList<Title> GetUserFavorites(String userEmail)
    {
        ArrayList<FavoriteTitle> probepos;
        probepos =  this.favTitlePairs;

        ArrayList<Title> favorites = new ArrayList<>();


        for (int i = 0; i < probepos.size(); i++)
        {
            String un = probepos.get(i).email;

            if (un.compareTo(userEmail) == 0 )
            {
                favorites.add(probepos.get(i).userFavTitle);//!!!!!!!!!!!1
            }

        }

        return favorites;
    }

    public FavoriteTitle getFavoriteTitlePair(int index) {
        return favTitlePairs.get(index);
    }

    public void deleteFavoriteTitlePair(int index) {

        db.child(FIREBASE_CHILD).child(favTitlePairs.get(index).key).removeValue();
        favTitlePairs.remove(index);
        recreateKeyIndexMapping();
    }

    public void deleteFavoriteTitlePair(String email, String titlename) {

        int indexx = -1;
        for(int i =0; i< favTitlePairs.size(); i++)
        {

            if (email.compareTo(favTitlePairs.get(i).email) == 0
                    && titlename.compareTo(favTitlePairs.get(i).userFavTitle.name) == 0)
                indexx = i;

        }

        if(indexx != -1) {
            db.child(FIREBASE_CHILD).child(favTitlePairs.get(indexx).key).removeValue();
            favTitlePairs.remove(indexx);
            recreateKeyIndexMapping();
        }
    }


    private void recreateKeyIndexMapping()
    {
        favTitlePairsMapping.clear();
        for (int i=0;i<favTitlePairs.size();i++)
            favTitlePairsMapping.put(favTitlePairs.get(i).key,i);
    }
}
