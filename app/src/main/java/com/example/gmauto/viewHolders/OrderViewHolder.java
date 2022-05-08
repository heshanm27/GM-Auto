package com.example.gmauto.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gmauto.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {
    public Button decline,Accept,Status,Delete,Update;
    public TextView value,quantity,item,name,customeremail,acceptedmsg;
    public LinearLayout statuslayout,adminlayout;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        decline = itemView.findViewById(R.id.decline);
        Accept = itemView.findViewById(R.id.Accept);
        item = itemView.findViewById(R.id.item);
        quantity = itemView.findViewById(R.id.quantity);
        value = itemView.findViewById(R.id.value);
        name = itemView.findViewById(R.id.name);
        customeremail = itemView.findViewById(R.id.customeremail);
        statuslayout = itemView.findViewById(R.id.statuslayout);
        adminlayout = itemView.findViewById(R.id.adminlayout);
        Status = itemView.findViewById(R.id.Status);
        Delete = itemView.findViewById(R.id.Delete);
        Update = itemView.findViewById(R.id.Update);
        acceptedmsg = itemView.findViewById(R.id.acceptedmsg);
    }
}
