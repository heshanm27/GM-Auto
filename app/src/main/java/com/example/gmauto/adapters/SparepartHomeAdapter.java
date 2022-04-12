package com.example.gmauto.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.gmauto.HomeFragment;
import com.example.gmauto.R;
import com.example.gmauto.models.sparepart;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class SparepartHomeAdapter extends FirestoreRecyclerAdapter<sparepart, SparepartHomeAdapter.sparepartViewholder> {

    public SparepartHomeAdapter(@NonNull FirestoreRecyclerOptions<sparepart> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull sparepartViewholder holder, int position, @NonNull sparepart model) {

        holder.title.setText(model.getProductName());
        holder.price.setText("Rs" + Double.toString(model.getProductPrice()));
        holder.ratevalue.setText(model.getRateavg().toString() + "/5");
        holder.ratingBar.setRating((float) model.getRateavg().doubleValue());
        Picasso.get().load(model.getImg()).into(holder.cardimg);
    }

    @NonNull
    @Override
    public sparepartViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutinflater = LayoutInflater.from(parent.getContext());
        View view = layoutinflater.inflate(R.layout.cardview_sparepart, parent, false);
        return new sparepartViewholder(view);
    }

    class sparepartViewholder extends RecyclerView.ViewHolder {

        ImageView cardimg;
        TextView title, ratevalue, price;
        RatingBar ratingBar;

        public sparepartViewholder(@NonNull View itemView) {
            super(itemView);

            cardimg = itemView.findViewById(R.id.cardimg);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            ratevalue = itemView.findViewById(R.id.ratevalue);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }
    }


}
