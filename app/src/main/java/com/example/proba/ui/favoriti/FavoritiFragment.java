package com.example.proba.ui.favoriti;

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
import com.example.proba.datamodels.Title;

import java.util.ArrayList;

public class FavoritiFragment extends Fragment {

    private FavoritiViewModel favoritiViewModel;
    private RecyclerView recyclerView;//
    private ArrayList<Title> popularTitles;//
    private TitleAdapter adapter;//

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       /* favoritiViewModel =
                new ViewModelProvider(this).get(FavoritiViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favoriti, container, false);
        final TextView textView = root.findViewById(R.id.text_favoriti);
        favoritiViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;*/
        View view = inflater.inflate(R.layout.fragment_favoriti, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_favorite_titles);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        popularTitles=new ArrayList<>();
        popularTitles.add(new Title());
        popularTitles.add(new Title());

        adapter=new TitleAdapter(popularTitles,view.getContext());
        recyclerView.setAdapter(adapter);
        return view;
    }
}