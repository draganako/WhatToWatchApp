package com.example.proba.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proba.R;
import com.example.proba.adapters.TitleAdapter;
import com.example.proba.datamodels.Title;
import com.example.proba.datamodels.TitleData;

import java.util.ArrayList;
import java.util.List;

public class PopularTitlesFragment extends Fragment {
    View view;
    RecyclerView recyclerView;//
    public TitleAdapter adapter;//
    private List<Title> popularTitles;//

    public PopularTitlesFragment() {
        // Required empty public constructor
    }

    public static PopularTitlesFragment newInstance(String param1, String param2) {
        PopularTitlesFragment fragment = new PopularTitlesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_popular_titles, container, false);
        recyclerView= (RecyclerView) view.findViewById(R.id.rv_popular_titles);
        recyclerView.setHasFixedSize(true);

        //fillDatabase();//////////////////////////////////////////////////////

        popularTitles= TitleData.getInstance().getTitles();
        adapter=new TitleAdapter(popularTitles,view.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void fillDatabase()
    {
        Title newTitle = new Title();
        TitleData.getInstance().AddTitle(newTitle);

        newTitle=new Title();
        TitleData.getInstance().AddTitle(newTitle);

    }
}