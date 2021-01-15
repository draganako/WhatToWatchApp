package com.example.proba.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proba.CommentsActivity;
import com.example.proba.R;
import com.example.proba.TitleAboutActivity;
import com.example.proba.datamodels.Title;
import com.example.proba.datamodels.User;
import com.example.proba.datamodels.UserData;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder>
{
    private List<Title> titleList;
    private List<Title> fullTitleList;
    ItemFilter itemFilter;
    private Context context;
    private SharedPreferences sharedPreferences;

    public TitleAdapter(List<Title> tl, Context c)
    {
        titleList=tl;
        fullTitleList=tl;
        context=c;
        itemFilter=new ItemFilter();
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
        holder.viewTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TitleAboutActivity.class);
                intent.putExtra("titleName",title.name);
                sharedPreferences = context.getSharedPreferences( "Titledata", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("titleName", title.name);
                editor.commit();
                context.startActivity(intent);
            }
        });

        if (title.image != null && !title.image.equals("")) {
            Glide.with(context).load(title.image).into(holder.titleImage);
        } else {
            holder.titleImage.setImageResource(R.drawable.ic_user_24);
        }

    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView titleImage;
        public TextView titleName;
        private ItemFilter itemFilter;
        public Button viewTitleButton;
        private Context context;

        private String currentId;//ours
        private FirebaseFirestore mFirestore;
        UserData userData;
        User current;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleImage=(ImageView)itemView.findViewById(R.id.imageViewTitlePhoto);
            titleName= (TextView)itemView.findViewById(R.id.titleViewName);
            viewTitleButton=(Button)itemView.findViewById(R.id.buttonViewViewTitle);
        }
    }

    public void filterTitles(CharSequence s, int noneMovieSeries)
    {
            titleList=fullTitleList;
            notifyDataSetChanged();

            switch (noneMovieSeries) {
                case(0):
                    itemFilter.publishResults(s, itemFilter.performFiltering(s));
                    break;
                case(1):
                    itemFilter.publishResults(s, itemFilter.performFilteringSeriesOrMovies(s,true));
                    break;
                default:
                    itemFilter.publishResults(s, itemFilter.performFilteringSeriesOrMovies(s,false));
                    break;
            }
    }

    private class ItemFilter extends Filter {

        public ItemFilter()
        {}

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            int count = titleList.size();
            final List<Title> list = titleList;
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString ;
            boolean isAMovie;

            for (int i = 0; i < count; i++)
            {
                filterableString = list.get(i).name;
                isAMovie=list.get(i).isAMovie;

                if (filterableString.toLowerCase().contains(filterString)) {
                   nlist.add(filterableString);

                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        protected FilterResults performFilteringSeriesOrMovies(CharSequence constraint, boolean movie) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            int count = titleList.size();
            final List<Title> list = titleList;
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString;
            boolean isAMovie;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).name;
                isAMovie = list.get(i).isAMovie;

                if (filterableString.toLowerCase().contains(filterString)) {
                    if (movie) {
                        if (isAMovie)
                            nlist.add(filterableString);
                    } else {
                        if (!isAMovie)
                            nlist.add(filterableString);
                    }

                }

            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<String> filteredData = (List<String>) results.values;
            List<Title> temp = new ArrayList<Title>(results.count);
            for (String titlee:filteredData
            ) {
                for (Title u:titleList
                ) {
                    if(titlee.equals(u.name))
                        temp.add(u);
                }

            }
            titleList=temp;
            notifyDataSetChanged();
        }

    }
}
