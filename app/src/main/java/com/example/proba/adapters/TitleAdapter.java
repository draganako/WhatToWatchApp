package com.example.proba.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proba.R;
import com.example.proba.datamodels.Title;

import java.util.List;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder>
{
    private List<Title> titleList;
    private Context context;

    public TitleAdapter(List<Title> tl, Context c)
    {
        titleList=tl;
        context=c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_member_title,parent,false);
       return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Title title=titleList.get(position);
        holder.titleName.setText(title.name);
        //holder.titleImage...(title.image);
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView titleImage;
        public TextView titleName;
        public Button viewTitleButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleImage=(ImageView)itemView.findViewById(R.id.imageViewTitlePhoto);
            titleName= (TextView)itemView.findViewById(R.id.titleViewName);
        }
    }
}
