package com.example.gmauto.viewHolders;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gmauto.R;

public class SparePartHomeViewHolder  extends RecyclerView.ViewHolder {

    public ImageView cardimg;
    public TextView title, ratevalue, price,itemdescription;
    public RatingBar ratingBar;
    public ImageButton editBtn,deleteBtn;
    public View V;
    public ProgressBar progressLoad;
    public SparePartHomeViewHolder(@NonNull View itemView) {
        super(itemView);
        cardimg = itemView.findViewById(R.id.cardimg);
        title = itemView.findViewById(R.id.title);
        price = itemView.findViewById(R.id.price);
        ratevalue = itemView.findViewById(R.id.ratevalue);
        ratingBar = itemView.findViewById(R.id.ratingBar);
        editBtn = itemView.findViewById(R.id.editBtn);
        deleteBtn =itemView.findViewById(R.id.deleteBtn);
        progressLoad =itemView.findViewById(R.id.progressLoad);
        V=itemView;

    }
}
