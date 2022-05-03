package com.example.gmauto.viewHolders;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gmauto.R;

public class VehicleViewHolder extends RecyclerView.ViewHolder {

    public TextView title,price;
    public ImageView cardimg;
    public View view;
    public ProgressBar progressLoad;
    public ImageButton editBtn,deleteBtn;
    public VehicleViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
        price = itemView.findViewById(R.id.price);
        cardimg =itemView.findViewById(R.id.cardimg);
        editBtn = itemView.findViewById(R.id.editBtn);
        deleteBtn =itemView.findViewById(R.id.deleteBtn);
        progressLoad = itemView.findViewById(R.id.progressLoad);
        view = itemView;
    }

}
