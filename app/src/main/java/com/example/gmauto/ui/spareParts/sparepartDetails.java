package com.example.gmauto.ui.spareParts;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmauto.R;
import com.example.gmauto.viewHolders.ReviewsViewHolder;
import com.example.gmauto.models.reviews;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class sparepartDetails extends Fragment {
    NavController navController;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView title, disc, price, rateAvgValue, emptyText;
    ImageView mainImg;
    RatingBar Avgrate;
    RecyclerView reviewRecyclerView;
    Button addreview, order;
    String Id, name, userid;
    ProgressDialog progress;
    BottomSheetDialog bottomSheetDialog;
    String gotimg, tit;
    Double itemPrice;
    private FirestoreRecyclerAdapter<reviews, ReviewsViewHolder> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_sparepart_details, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.title);
        disc = view.findViewById(R.id.disc);
        price = view.findViewById(R.id.price);
        emptyText = view.findViewById(R.id.emptyText);
        mainImg = view.findViewById(R.id.mainImg);
        Avgrate = view.findViewById(R.id.avgRating);
        addreview = view.findViewById(R.id.addreview);
        rateAvgValue = view.findViewById(R.id.rateAvgValue);
        reviewRecyclerView = view.findViewById(R.id.reviewRecyclerView);
        order = view.findViewById(R.id.order);
        //progress dilogue
        progress = new ProgressDialog(getContext());
        progress.setContentView(R.layout.loading_dialog);
        progress.setCancelable(false);

        //set layoutmanger into recyclerview
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        reviewRecyclerView.setLayoutManager(layoutManager);
        Id = sparepartDetailsArgs.fromBundle(requireArguments()).getFirebaseID().toString();
        userid = FirebaseAuth.getInstance().getUid().toString();

        db.collection("Users").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                name = snapshot.getString("FullName");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast toast = Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT);
            }
        });
        userid = FirebaseAuth.getInstance().getUid();

        Log.d("id", Id);
        getDetails(Id);
        CalAvgRating(Id);
        getReviews(Id);

        addreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(getContext());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
                TextInputEditText review = bottomSheetDialog.findViewById(R.id.review);
                TextInputLayout reviewTextLayout = bottomSheetDialog.findViewById(R.id.reviewTextLayout);
                RatingBar rate = bottomSheetDialog.findViewById(R.id.ratingInput);
                Button postBtn = bottomSheetDialog.findViewById(R.id.post);
                TextView ratingCount = bottomSheetDialog.findViewById(R.id.ratingCount);
                String reviewMessage = review.getText().toString();
                //keybord hide when unfocus
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
                postBtn.setOnClickListener(new View.OnClickListener() {
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
                            map.put("sparepartid", Id);
                            map.put("userName", name);
                            map.put("userid", userid);

                            db.collection("Reviews").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progress.dismiss();
                                    bottomSheetDialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast toast = Toast.makeText(getContext(), "Error Occured Try Again", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                        }
                    }
                });
                bottomSheetDialog.show();
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("FirebaseID", Id);
                args.putString("Title", tit);
                args.putString("img", gotimg);
                args.putDouble("price", itemPrice);
                Navigation.findNavController(view).navigate(R.id.action_sparepartDetails_to_order2, args);
            }
        });
    }

    private void getDetails(String ID) {
        db.collection("SpareParts").document(ID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    gotimg = documentSnapshot.getString("img");
                    tit = documentSnapshot.getString("productName");
                    String discription = documentSnapshot.getString("productDiscription");
                    itemPrice = documentSnapshot.getDouble("productPrice");
                    String amount = getString(R.string.Price, documentSnapshot.getDouble("productPrice"));
                    title.setText(tit);
                    disc.setText(discription);
                    price.setText(amount);
                    Picasso.get().load(gotimg).placeholder(R.drawable.clearicon).into(mainImg);


                } else {
                    Toast.makeText(getContext(), "Doument Not Exist", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error occur please try agian later", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getReviews(String ID) {
        Query query = FirebaseFirestore.getInstance().collection("Reviews").whereEqualTo("sparepartid", ID).limit(5);
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
                emptyText.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            protected void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position, @NonNull reviews model) {
                holder.username.setText(model.getUserName());
                holder.ratingBar.setRating(model.getRate().floatValue());
                holder.message.setText(model.getReview().toString());
                Date date = new Date(String.valueOf(model.getTimestamp().toDate()));
                SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");
                String value = sfd.format(date);
                holder.timestamp.setText(value);

            }
        };

        reviewRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }

    }

    private void CalAvgRating(String ID) {
//        Query query = FirebaseFirestore.getInstance().collection("Reviews").whereEqualTo("sparepartid",ID).
        db.collection("Reviews").whereEqualTo("sparepartid", ID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                double length = snapshotList.size();
                if (length != 0) {
                    double total = 0;
                    for (DocumentSnapshot snapshot : snapshotList) {
                        total += snapshot.getDouble("rate");
                    }
                    double avg = total / length;
                    Avgrate.setRating((float) avg);
                    String ratingavgstring = getString(R.string.RatingAvgvalue, avg);
                    rateAvgValue.setText(ratingavgstring);
                    Log.d("rate", String.valueOf(avg));
                    updateSparePartavg(ID, avg);
                } else {
                    Avgrate.setRating(0);
                    rateAvgValue.setText("0" + "/5");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
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

    private void updateSparePartavg(String ID, double avg) {
        db.collection("SpareParts").document(ID).update("rateavg", avg).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (progress != null && bottomSheetDialog != null) {

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}