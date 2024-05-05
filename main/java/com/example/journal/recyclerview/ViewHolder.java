package com.example.journal.recyclerview;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journal.R;

public class ViewHolder extends RecyclerView.ViewHolder{
    TextView dateTextView;
    TextView weatherTextView;
    TextView titleTextView;
    TextView locationTextView;
    LinearLayout linearLayout;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        dateTextView = itemView.findViewById(R.id.date_textView);
        weatherTextView = itemView.findViewById(R.id.weather_textView);
        titleTextView = itemView.findViewById(R.id.title_textView);
        locationTextView = itemView.findViewById(R.id.location_textView);
        linearLayout = itemView.findViewById(R.id.linearLayout);

    }
}
