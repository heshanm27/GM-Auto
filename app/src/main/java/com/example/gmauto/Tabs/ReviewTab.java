package com.example.gmauto.Tabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gmauto.R;
import com.example.gmauto.models.reviews;
import com.example.gmauto.viewHolders.ReviewsViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ReviewTab extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<reviews, ReviewsViewHolder> adapter;
    RecyclerView reviewRecyclerView;



    public ReviewTab() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reviewRecyclerView = view.findViewById(R.id.reviewRecyclerView);
        getReviews(FirebaseAuth.getInstance().getUid());


        //set layoutmanger into recyclerview
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        reviewRecyclerView.setLayoutManager(layoutManager);
        //recyler view decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        reviewRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private  void getReviews(String ID){
        Query query = FirebaseFirestore.getInstance().collection("Reviews").whereEqualTo("userid",ID);
        FirestoreRecyclerOptions<reviews> options = new FirestoreRecyclerOptions.Builder<reviews>()
                .setQuery(query, reviews.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<reviews, ReviewsViewHolder>(options){
            @NonNull
            @Override
            public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_recycler_layout, parent, false);
                return new ReviewsViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();

            }
            @Override
            protected void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position, @NonNull reviews model) {
                holder.username.setText(model.getUserName());
                holder.ratingBar.setRating(model.getRate().floatValue());
                holder.message.setText(model.getReview().toString());
                Log.d("timestamp",model.getTimestamp().toDate().toString());

            }
        };

        reviewRecyclerView.setAdapter(adapter);

    }
    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null){
            adapter.startListening();
        }
    }
}