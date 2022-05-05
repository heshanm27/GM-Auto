package com.example.gmauto;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmauto.models.sparepart;
import com.example.gmauto.models.vehicle;
import com.example.gmauto.viewHolders.SparePartHomeViewHolder;
import com.example.gmauto.viewHolders.VehicleViewHolder;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment implements FirebaseAuth.AuthStateListener ,View.OnClickListener {

    //Reference
    RecyclerView sparepartrecyclerView, vehile, foryou;
    NavController navController;
    ShimmerFrameLayout shimmerLayout, shimmerLayout2, shimmerLayout3;
    FirestoreRecyclerAdapter<sparepart, SparePartHomeViewHolder> adapter;
    FirestoreRecyclerAdapter<vehicle, VehicleViewHolder> Vehicleadapter;
    TextView view_all_sparepart;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        view_all_sparepart = root.findViewById(R.id.view_all_sparepart);
        view_all_sparepart.setOnClickListener(this::onClick);
        // spare part recycler View reference

        sparepartrecyclerView = root.findViewById(R.id.sparepartrecyclerView);
        // vehiclerecyclerView fragment
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
        vehcileRecylerView();
    }


    private void initRecyclerView() {
        Query query = FirebaseFirestore.getInstance().collection("SpareParts").orderBy("Timestamp",Query.Direction.DESCENDING).limit(5);
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
            protected void onBindViewHolder(@NonNull SparePartHomeViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull sparepart model) {
                holder.title.setText(model.getProductName());
                holder.price.setText("Rs" + Double.toString(model.getProductPrice()));
                float avg = (float) model.getRateavg().doubleValue();
                String ratingavgstring = getString(R.string.RatingAvgvalue,avg);
                holder.ratevalue.setText(ratingavgstring);
                holder.ratingBar.setRating((float) model.getRateavg().doubleValue());
                Picasso.get().load(model.getImg()).placeholder(R.drawable.clearicon).into(holder.cardimg, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                holder.V.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    DocumentSnapshot doc=getSnapshots().getSnapshot(position);
                        String id = doc.getId().toString();
                        navController.navigate(HomeFragmentDirections.actionDashFragmentToSparepartDetails(id));
                    }
                });
            }


        };

        sparepartrecyclerView.setAdapter(adapter);
        foryou.setAdapter(adapter);
        adapter.startListening();
    }

    private  void vehcileRecylerView(){
        Query query = FirebaseFirestore.getInstance().collection("Vehicles").orderBy("Timestamp",Query.Direction.DESCENDING).limit(5);
        FirestoreRecyclerOptions<vehicle> options = new FirestoreRecyclerOptions.Builder<vehicle>()
                .setQuery(query, vehicle.class)
                .build();
        Vehicleadapter = new FirestoreRecyclerAdapter<vehicle, VehicleViewHolder>(options) {

            @NonNull
            @Override
            public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutinflater = LayoutInflater.from(parent.getContext());
                View view = layoutinflater.inflate(R.layout.vehicle_cardview, parent, false);
                return new VehicleViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                shimmerLayout2.stopShimmer();
                shimmerLayout2.setVisibility(View.GONE);
                vehile.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                shimmerLayout2.startShimmer();
                shimmerLayout2.setVisibility(View.VISIBLE);
                vehile.setVisibility(View.GONE);
            }

            @Override
            protected void onBindViewHolder(@NonNull VehicleViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull vehicle model) {
                holder.title.setText(model.getTitle());
                holder.price.setText(Double.toString(model.getPrice())+"mil");
                Picasso.get().load(model.getImgUrl()).placeholder(R.drawable.clearicon).into(holder.cardimg, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentSnapshot doc=getSnapshots().getSnapshot(position);
                        String id = doc.getId().toString();
                        navController.navigate(HomeFragmentDirections.actionNavHomeToVehicleDetails(id));
                    }
                });
            }
        };
        vehile.setAdapter(Vehicleadapter);
        Vehicleadapter.startListening();
    }





    public void onClick(View v) {

                    navController.navigate(HomeFragmentDirections.actionDashFragmentToSparePartsHome());

    }
    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null && Vehicleadapter != null){
            adapter.stopListening();
            Vehicleadapter.stopListening();
        }

    }
}