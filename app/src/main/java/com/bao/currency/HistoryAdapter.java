package com.bao.currency;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bao.currency.Model.History;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryItemViewHolder> {

    Context c;
    public static List<History> histories;

    public HistoryAdapter(Context c, List<History> lists) {
        this.c = c;
        this.histories = lists;
    }

    @Override
    public HistoryAdapter.HistoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new HistoryItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.HistoryItemViewHolder holder, int position) {
        History history=histories.get(position);
        holder.tvDate.setText(history.get_date());
        holder.tvResult.setText(history.get_result());

    }

    @Override
    public int getItemCount() {
        return histories.size();
    }




    public static class HistoryItemViewHolder extends RecyclerView.ViewHolder{
        public TextView tvDate, tvResult ;
        public ImageView flag;



        public HistoryItemViewHolder(View itemView) {
            super(itemView);
            tvDate=itemView.findViewById(R.id.date);
            flag=itemView.findViewById(R.id.flag_img);
            tvResult=itemView.findViewById(R.id.result);
        }
    }
}

