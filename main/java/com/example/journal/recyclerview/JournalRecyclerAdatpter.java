package com.example.journal.recyclerview;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journal.Journal;
import com.example.journal.R;
import com.example.journal.activity.JournalBrowseActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class JournalRecyclerAdatpter extends RecyclerView.Adapter<ViewHolder> {

    List<Journal> journals;
    Context context;

    private MyItemClick myItemClick;

    public JournalRecyclerAdatpter(List<Journal> journals, Context context) {
        this.journals = journals;
        this.context = context;
    }

    public JournalRecyclerAdatpter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Journal journal = journals.get(position);

        holder.dateTextView.setText(new SimpleDateFormat("yyyy-MM-dd").format(journal.getDate()));
        holder.weatherTextView.setText(journal.getWeather());
        holder.titleTextView.setText(journal.getTitle());
        holder.locationTextView.setText(journal.getAddress());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myItemClick != null) {
                    myItemClick.onItemClick(journal, position);
                }

                // myItemClick.onItemClick(journal, position);
//                Intent intent = new Intent(context, JournalBrowseActivity.class);
//
//                Bundle bundle = new Bundle();
//                bundle.putInt("id", journal.getId());
//                bundle.putString("date", new SimpleDateFormat("yyyy-MM-dd").format(journal.getDate()));
//                bundle.putString("weather", journal.getWeather());
//                bundle.putString("title", journal.getTitle());
//                bundle.putString("content", journal.getContent());
//
//                intent.putExtras(bundle);
    //            context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (journals == null){
            return 0;
        }else{
            return journals.size();
        }
    }


    public interface MyItemClick{
        void onItemClick(Journal journal, int postion);
    }

    public void setOnItemClick(MyItemClick myItemClick){
        this.myItemClick = myItemClick;
    }

    public void setNewData(@Nullable List<Journal> journals) {
        this.journals = journals;
        notifyDataSetChanged();
    }


}
