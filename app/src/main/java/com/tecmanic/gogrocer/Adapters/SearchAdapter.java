package com.tecmanic.gogrocer.Adapters;

import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tecmanic.gogrocer.ModelClass.SearchModel;
import com.tecmanic.gogrocer.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

private List<SearchModel> searchList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title;

    public MyViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.pNAme);

    }
}


    public SearchAdapter(List<SearchModel> searchList) {
        this.searchList = searchList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_searchlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SearchModel ss = searchList.get(position);
        holder.title.setText(ss.getpNAme());

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }
}