package com.example.gmauto.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gmauto.R;

public class ReservationViewHolder extends RecyclerView.ViewHolder {

    public Button decline,Accept,Status,Delete,Update;
    public TextView times,dates,ServiceType,name,acceptedmsg;
    public LinearLayout statuslayout,adminlayout;
    public ReservationViewHolder(@NonNull View itemView) {
        super(itemView);
        decline = itemView.findViewById(R.id.decline);
        Accept = itemView.findViewById(R.id.Accept);
        Status = itemView.findViewById(R.id.Status);
        Delete = itemView.findViewById(R.id.Delete);
        times = itemView.findViewById(R.id.times);
        dates = itemView.findViewById(R.id.dates);
        statuslayout = itemView.findViewById(R.id.statuslayout);
        adminlayout = itemView.findViewById(R.id.adminlayout);
        ServiceType = itemView.findViewById(R.id.ServiceType);
        name = itemView.findViewById(R.id.name);
        acceptedmsg = itemView.findViewById(R.id.acceptedmsg);
        Update = itemView.findViewById(R.id.Update);
    }
}
