package com.example.gmauto;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gmauto.R;

public class SparePartHomeViewHolder  extends RecyclerView.ViewHolder {

    ImageView cardimg;
    TextView title, ratevalue, price;
    RatingBar ratingBar;
    View V;
    public SparePartHomeViewHolder(@NonNull View itemView) {
        super(itemView);
        cardimg = itemView.findViewById(R.id.cardimg);
        title = itemView.findViewById(R.id.title);
        price = itemView.findViewById(R.id.price);
        ratevalue = itemView.findViewById(R.id.ratevalue);
        ratingBar = itemView.findViewById(R.id.ratingBar);
        V=itemView;

    }
}
