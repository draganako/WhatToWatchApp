package com.example.proba.datamodels;

import android.icu.text.Transliterator;

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

public class    UserData
{
    private ArrayList<User> users;
    public DatabaseReference db;
    private static final String FIREBASE_CHILD= "Users";
    private HashMap<String, Integer> UsersMapping;

    private UserData()
    {
        users = new ArrayList<>();
        UsersMapping  = new HashMap<String, Integer>();
        db = FirebaseDatabase.getInstance().getReference();
        db.child(FIREBASE_CHILD).addChildEventListener(childEventListener);
        db.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);

    }

    private static class SingletonHolder {
        public static final UserData instance = new UserData();
    }

    public static UserData getInstance() {
        return UserData.SingletonHolder.instance;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    UserData.UpdateEventListener updateListener;
    public void setEventListener(UserData.UpdateEventListener listener) {
        updateListener = listener;
    }

    public interface UpdateEventListener {
        void onListUpdated();
        void onSingleUserUpdated(String username);
        void onUserAdded(String username);
    }

    UserData.onUpdateUserListener updateUserListener;
    public void setUserPostListener(UserData.onUpdateUserListener listener) {
        updateUserListener = listener;
    }
    public interface onUpdateUserListener {
        void onUserUpdated();
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
                updateListener.onListUpdated();

            if(probaList != null)
            {
                probaList.onReady();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }

    };

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myUserKey = dataSnapshot.getKey();

            if (!UsersMapping.containsKey(myUserKey)) {
                User myUser = dataSnapshot.getValue(User.class);
                myUser.key = myUserKey;
                users.add(myUser);
                UsersMapping.put(myUserKey, users.size() - 1);
                if (updateListener != null)
                    updateListener.onUserAdded(myUser.username);
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myUserKey = dataSnapshot.getKey();
            User myUser = dataSnapshot.getValue(User.class);
            myUser.key = myUserKey;
            if (UsersMapping.containsKey(myUserKey)) {
                int index = UsersMapping.get(myUserKey);
                users.set(index, myUser);
            } else {
                users.add(myUser);
                UsersMapping.put(myUserKey, users.size() - 1);
            }
            if (updateListener != null) {
                updateListener.onSingleUserUpdated(myUser.username);
            }

            if(updateUserListener != null)
            {
                updateUserListener.onUserUpdated();
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            String myUserKey = dataSnapshot.getKey();
            if (UsersMapping.containsKey(myUserKey)) {
                int index = UsersMapping.get(myUserKey);
                users.remove(index);
                recreateKeyIndexMapping();
            }
            if (updateListener != null)
                updateListener.onListUpdated();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void AddUser(User p)
    {
        String key = db.push().getKey();
        users.add(p);
        UsersMapping.put(key, users.size() - 1);
        db.child(FIREBASE_CHILD).child(key).setValue(p);
        p.key = key;
    }

    public User getUser(int index) {
        return users.get(index);
    }


    public User getUser(String email) {
        User u = null;
        for(int i = 0; i< this.users.size(); i++)
        {
            if(this.users.get(i).email.compareTo(email) == 0)
            {
                u = this.users.get(i);
                break;
            }
        }

        return u;
    }



    public User getUserByUsername(String username) {
        User u = null;
        for(int i = 0; i< this.users.size(); i++)
        {
            if(this.users.get(i).username.compareTo(username) == 0)
            {
                u = this.users.get(i);
                break;
            }
        }

        return u;
    }



    public void deleteUser(int index) {

        db.child(FIREBASE_CHILD).child(users.get(index).key).removeValue();
        users.remove(index);
        recreateKeyIndexMapping();
    }

    public void updateUser(int index,  User u)
    {
        if(index > -1) {
            User uu = users.get(index);
            uu.email = u.email;
            uu.username=u.username;
            uu.picture=u.picture;
            db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);
        }

    }

    public void updateUserProfile(String email, String username, String newImage)
    {
        int indexx = -1;
        for(int i =0; i < users.size(); i++)
        {
            if(users.get(i).email.compareTo(email) == 0) {
                indexx = i;
                break;
            }
        }
        if(indexx == -1)
            return;

        User uu =users.get(indexx);
        uu.username = username;
        uu.picture = newImage;

        db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);

    }

    private void recreateKeyIndexMapping()
    {
        UsersMapping.clear();
        for (int i=0;i<users.size();i++)
            UsersMapping.put(users.get(i).key,i);


    }
}
