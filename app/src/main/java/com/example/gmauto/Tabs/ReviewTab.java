package com.example.gmauto.Tabs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmauto.R;
import com.example.gmauto.models.reviews;
import com.example.gmauto.viewHolders.ReviewsViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ReviewTab extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<reviews, ReviewsViewHolder> adapter;
    RecyclerView reviewRecyclerView;
    BottomSheetDialog bottomSheetDialog;
    ProgressDialog progress;

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

        //progress dilogue
        progress = new ProgressDialog(getContext());
        progress.setContentView(R.layout.loading_dialog);
        progress.setCancelable(false);
        //set layoutmanger into recyclerview
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        reviewRecyclerView.setLayoutManager(layoutManager);
        //recyler view decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        reviewRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void getReviews(String ID) {
        Query query = FirebaseFirestore.getInstance().collection("Reviews").whereEqualTo("userid", ID);
        FirestoreRecyclerOptions<reviews> options = new FirestoreRecyclerOptions.Builder<reviews>()
                .setQuery(query, reviews.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<reviews, ReviewsViewHolder>(options) {
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
            protected void onBindViewHolder(@NonNull ReviewsViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull reviews model) {
                holder.username.setText(model.getUserName());
                holder.ratingBar.setRating(model.getRate().floatValue());
                holder.message.setText(model.getReview().toString());
                Date date = new Date(String.valueOf(model.getTimestamp().toDate()));
                SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");
                String value = sfd.format(date);
                holder.timestamp.setText(value);
                Log.d("timestamp", value);

                holder.V.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentSnapshot doc = getSnapshots().getSnapshot(position);
                        bottomsheet(model,doc);
                    }
                });

            }
        };
        reviewRecyclerView.setAdapter(adapter);
    }


    public void bottomsheet(reviews model, DocumentSnapshot snapshot) {

        DocumentReference documentReference = snapshot.getReference();
        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        TextInputEditText review = bottomSheetDialog.findViewById(R.id.review);
        TextInputLayout reviewTextLayout = bottomSheetDialog.findViewById(R.id.reviewTextLayout);
        RatingBar rate = bottomSheetDialog.findViewById(R.id.ratingInput);
        Button postBtn = bottomSheetDialog.findViewById(R.id.post);
        Button update = bottomSheetDialog.findViewById(R.id.update);
        Button delete = bottomSheetDialog.findViewById(R.id.delete);
        TextView ratingCount = bottomSheetDialog.findViewById(R.id.ratingCount);

        update.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        review.setText(model.getReview());
       rate.setRating(model.getRate().floatValue());
        postBtn.setVisibility(View.GONE);
        String reviewMessage = review.getText().toString();
//        keybord hide when unfocus
        review.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }
        });
        Float rateing = rate.getRating();
        String ratingCountstring = getString(R.string.RatingWithCount, rateing);
        ratingCount.setText(ratingCountstring);
        //rating bar value changed
        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Float rateing = rate.getRating();
                String ratingCountstring = getString(R.string.RatingWithCount, rateing);
                ratingCount.setText(ratingCountstring);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        if (!validReview(review, reviewTextLayout) && !validateRating(rate)) {
            Float rateValue = rate.getRating();
            String ReviewMsg = review.getText().toString();
            progress.show();
            Map<String, Object> map = new HashMap<>();
            map.put("Timestamp", new Timestamp(new Date()));
            map.put("rate", rateValue);
            map.put("review", ReviewMsg);
            map.put("sparepartid", model.getSparepartid());
            map.put("userName", model.getUserName());
            map.put("userid", model.getUserid());

            documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progress.dismiss();
                    bottomSheetDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }
    });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Your review deleted", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error occured", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        bottomSheetDialog.show();
    }




    private boolean validReview(TextInputEditText edit, TextInputLayout layout) {
        String value = edit.getText().toString();
        Log.d("review", value);
        if (value.isEmpty()) {
            layout.setError("Please Enter Your Review");
            return true;
        } else {
            Log.d("review", "valid");
            layout.setError(null);
            return false;
        }
    }

    private boolean validateRating(RatingBar rate) {
        Float rateValue = rate.getRating();

        if (rateValue == 0) {
            Toast toast = Toast.makeText(getContext(), "Please Enter Rate for Product", Toast.LENGTH_LONG);
            toast.show();
            return true;
        } else {
            return false;
        }


    }


    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }
}
