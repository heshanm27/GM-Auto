package com.example.gmauto;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gmauto.models.sparepart;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment implements FirebaseAuth.AuthStateListener {


    RecyclerView sparepartrecyclerView, vehile, foryou;
    NavController navController;
    ShimmerFrameLayout shimmerLayout, shimmerLayout2, shimmerLayout3;
    FirestoreRecyclerAdapter<sparepart, SparePartHomeViewHolder> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        sparepartrecyclerView = root.findViewById(R.id.sparepartrecyclerView);
        vehile = root.findViewById(R.id.vehiclerecyclerView);
        foryou = root.findViewById(R.id.foryourecylerview);
        shimmerLayout = root.findViewById(R.id.shimmerLayout);
        shimmerLayout2 = root.findViewById(R.id.shimmerLayout2);
        shimmerLayout3 = root.findViewById(R.id.shimmerLayout3);
        shimmerLayout.startShimmer();
        shimmerLayout2.startShimmer();
        shimmerLayout3.startShimmer();

        //add divider between each item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        sparepartrecyclerView.addItemDecoration(dividerItemDecoration);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);

    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            navController.navigate(R.id.action_dashFragment_to_login2);
        }
        initRecyclerView();
    }


    private void initRecyclerView() {
        Query query = FirebaseFirestore.getInstance().collection("SpareParts");
        FirestoreRecyclerOptions<sparepart> options = new FirestoreRecyclerOptions.Builder<sparepart>()
                .setQuery(query, sparepart.class)
                .build();




        adapter = new FirestoreRecyclerAdapter<sparepart, SparePartHomeViewHolder>(options) {


            @NonNull
            @Override
            public SparePartHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutinflater = LayoutInflater.from(parent.getContext());
                View view = layoutinflater.inflate(R.layout.cardview_sparepart, parent, false);
                return new SparePartHomeViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                sparepartrecyclerView.setVisibility(View.VISIBLE);
                shimmerLayout2.stopShimmer();
                shimmerLayout2.setVisibility(View.GONE);
                vehile.setVisibility(View.VISIBLE);
                shimmerLayout3.stopShimmer();
                shimmerLayout3.setVisibility(View.GONE);
                foryou.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Log.d("err","error occuer" +e);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.getMessage().toString(),Toast.LENGTH_LONG);
                toast.show();
                shimmerLayout.startShimmer();
                shimmerLayout.setVisibility(View.VISIBLE);
                sparepartrecyclerView.setVisibility(View.GONE);
            }

            @Override
            protected void onBindViewHolder(@NonNull SparePartHomeViewHolder holder, int position, @NonNull sparepart model) {
                holder.title.setText(model.getProductName());
                holder.price.setText("Rs" + Double.toString(model.getProductPrice()));
                holder.ratevalue.setText(model.getRateavg().toString() + "/5");
                holder.ratingBar.setRating((float) model.getRateavg().doubleValue());
                Picasso.get().load(model.getImg()).placeholder(R.drawable.clearicon).into(holder.cardimg);
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    navController.navigate(R.id.action_dashFragment_to_sparepartDetails);
                    }
                });
            }


        };

        sparepartrecyclerView.setAdapter(adapter);
        vehile.setAdapter(adapter);
        foryou.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}