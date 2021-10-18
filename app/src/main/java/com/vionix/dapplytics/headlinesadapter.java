package com.vionix.dapplytics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class headlinesadapter extends RecyclerView.Adapter<headlinesadapter.ViewHolder> {
    List<headingmodel> headingmodelList;
    Context context;

    public headlinesadapter(List<headingmodel> headingmodelList, Context context) {
        this.headingmodelList = headingmodelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.newsheadlines,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tittle.setText(headingmodelList.get(position).getTittle());
        holder.des.setText(headingmodelList.get(position).getDesp());
        holder.source.setText(headingmodelList.get(position).getSource());

    }

    @Override
    public int getItemCount() {
        return headingmodelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tittle,des,source;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tittle= itemView.findViewById(R.id.headline);
            des= itemView.findViewById(R.id.description);
            source= itemView.findViewById(R.id.source);
        }
    }
}
