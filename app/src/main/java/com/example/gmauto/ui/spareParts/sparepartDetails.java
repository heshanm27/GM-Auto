package com.example.gmauto.ui.spareParts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;


public class sparepartDetails extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView title,disc,price,rateAvgValue;
    ImageView mainImg;
    RatingBar Avgrate;
    RecyclerView reviewRecyclerView;

    private FirestoreRecyclerAdapter<reviews, ReviewsViewHolder> adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root= inflater.inflate(R.layout.fragment_sparepart_details, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.title);
        disc = view.findViewById(R.id.disc);
        price = view.findViewById(R.id.price);
        mainImg=view.findViewById(R.id.mainImg);
        Avgrate = view.findViewById(R.id.avgRating);
        rateAvgValue = view.findViewById(R.id.rateAvgValue);
        reviewRecyclerView= view.findViewById(R.id.reviewRecyclerView);
        String Id = sparepartDetailsArgs.fromBundle(requireArguments()).getFirebaseID().toString();
        Log.d("id",Id);
        getDetails(Id);
        CalAvgRating(Id);
        getReviews(Id);

    }

    private void getDetails(String ID){
        db.collection("SpareParts").document(ID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String img = documentSnapshot.getString("img");
                    String tit = documentSnapshot.getString("productName");
                    String discription = documentSnapshot.getString("productDiscription");
                    String amount = documentSnapshot.getDouble("productPrice").toString();
                    title.setText(tit);
                    disc.setText(discription);
                    price.setText("LKR"+amount);
                    Picasso.get().load(img).placeholder(R.drawable.clearicon).into(mainImg);

                }else{
                    Toast.makeText(getContext(),"Doument Not Exist",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error occur please try agian later",Toast.LENGTH_LONG).show();
            }
        });
    }

    private  void getReviews(String ID){
        Query query = FirebaseFirestore.getInstance().collection("Reviews").whereEqualTo("sparepartid",ID).limit(5);
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
            protected void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position, @NonNull reviews model) {
                            holder.username.setText(model.getUserName());
                            holder.ratingBar.setRating(model.getRate().floatValue());
                            holder.message.setText(model.getReview().toString());
                            Log.d("holder",model.getUserid());
            }
        };

        reviewRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private  void CalAvgRating(String ID) {
//        Query query = FirebaseFirestore.getInstance().collection("Reviews").whereEqualTo("sparepartid",ID).
        db.collection("Reviews").whereEqualTo("sparepartid", ID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                double length = snapshotList.size();
                double total = 0;
                for (DocumentSnapshot snapshot : snapshotList) {
                    total += snapshot.getDouble("rate");
                }
                double avg = total / length;
                Avgrate.setRating((float) avg);
                rateAvgValue.setText(avg+"/5");
                Log.d("rate", String.valueOf(avg));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}