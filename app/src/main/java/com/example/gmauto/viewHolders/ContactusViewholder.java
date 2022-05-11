package com.example.gmauto.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gmauto.R;

public class ContactusViewholder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView Email;
    public TextView contactno;
    public TextView message;
    public TextView time;
    public ContactusViewholder(@NonNull View itemView) {
        super(itemView);

        name =itemView.findViewById(R.id.name);
        Email =itemView.findViewById(R.id.Email);
        contactno =itemView.findViewById(R.id.contactno);
        message =itemView.findViewById(R.id.message);
        time =itemView.findViewById(R.id.time);
    }
}
