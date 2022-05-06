package com.example.gmauto.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gmauto.R;

public class ReservationViewHolder extends RecyclerView.ViewHolder {

    public Button decline,Accept;
    public TextView times,dates,ServiceType,name;

    public ReservationViewHolder(@NonNull View itemView) {
        super(itemView);
        decline = itemView.findViewById(R.id.decline);
        Accept = itemView.findViewById(R.id.Accept);
        times = itemView.findViewById(R.id.times);
        dates = itemView.findViewById(R.id.dates);
        ServiceType = itemView.findViewById(R.id.ServiceType);
        name = itemView.findViewById(R.id.name);
    }
}
