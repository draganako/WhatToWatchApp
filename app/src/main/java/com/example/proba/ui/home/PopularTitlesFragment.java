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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PopularTitlesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopularTitlesFragment extends Fragment {
    View view;
    RecyclerView recyclerView;//
    public TitleAdapter adapter;//
    private List<Title> popularTitles;//

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PopularTitlesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PopularTitlesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PopularTitlesFragment newInstance(String param1, String param2) {
        PopularTitlesFragment fragment = new PopularTitlesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_popular_titles, container, false);
        recyclerView= (RecyclerView) view.findViewById(R.id.rv_popular_titles);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        popularTitles=new ArrayList<>();
        popularTitles.add(new Title());
        popularTitles.add(new Title(""));

        adapter=new TitleAdapter(popularTitles,view.getContext());
        recyclerView.setAdapter(adapter);
        return view;
    }
}