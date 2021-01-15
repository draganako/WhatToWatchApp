package com.example.proba.ui.favoriti;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proba.R;
import com.example.proba.adapters.TitleAdapter;
import com.example.proba.datamodels.FavoriteTitle;
import com.example.proba.datamodels.FavoriteTitleData;
import com.example.proba.datamodels.Title;
import com.example.proba.datamodels.TitleData;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class FavoritiFragment extends Fragment {

    private FavoritiViewModel favoritiViewModel;
    private RecyclerView recyclerView;
    private TitleAdapter adapter;
    private ArrayList<Title> titleListFavs;
    private SharedPreferences sharedPref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favoriti, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_favorite_titles);
        recyclerView.setHasFixedSize(true);

        sharedPref = getActivity().getSharedPreferences( "Userdata", Context.MODE_PRIVATE);
        String email = sharedPref.getString(getString(R.string.loggedUser_email), "EMPTY");
        FavoriteTitleData.getInstance().AddFavoriteTitle(new FavoriteTitle("proba@gmail.com"));
        //FavoriteTitleData.getInstance().AddFavoriteTitle(new FavoriteTitle());
        titleListFavs=FavoriteTitleData.getInstance().GetUserFavorites(email);
        adapter=new TitleAdapter(titleListFavs,view.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
}