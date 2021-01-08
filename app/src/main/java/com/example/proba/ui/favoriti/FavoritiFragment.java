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

import com.example.proba.R;

public class FavoritiFragment extends Fragment {

    private FavoritiViewModel favoritiViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        favoritiViewModel =
                new ViewModelProvider(this).get(FavoritiViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favoriti, container, false);
        final TextView textView = root.findViewById(R.id.text_favoriti);
        favoritiViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}