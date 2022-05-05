package com.example.gmauto.viewHolders;

import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gmauto.R;

public class ReviewsViewHolder extends RecyclerView.ViewHolder {

    public TextView username;
    public TextView timestamp;
    public TextView message;
    public RatingBar ratingBar;

    public ReviewsViewHolder(@NonNull View itemView) {
        super(itemView);
        username=itemView.findViewById(R.id.username);
        timestamp = itemView.findViewById(R.id.timestamp);
        message = itemView.findViewById(R.id.reviewMessage);
        ratingBar = itemView.findViewById(R.id.rating);
    }
}
