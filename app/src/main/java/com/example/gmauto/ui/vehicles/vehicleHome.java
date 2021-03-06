package com.example.gmauto.ui.vehicles;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gmauto.HomeFragmentDirections;
import com.example.gmauto.R;
import com.example.gmauto.models.vehicle;
import com.example.gmauto.viewHolders.VehicleViewHolder;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class vehicleHome extends Fragment implements FirebaseAuth.AuthStateListener {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter<vehicle, VehicleViewHolder> adapter;
    Spinner spinner;
    RecyclerView vehicle;
    NavController navController;
    ShimmerFrameLayout shimmerLayout;
    String orderByText="Timestamp",data="";
    Query.Direction Directions = Query.Direction.DESCENDING;
    TextInputEditText searchEdit;
    ProgressBar progressLoad;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_home, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressLoad = view.findViewById(R.id.progressLoad);

        //reycler view
        vehicle = view.findViewById(R.id.vehicle);
        navController = Navigation.findNavController(view);

        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        shimmerLayout.startShimmer();

        spinner = view.findViewById(R.id.filter);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.filters, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();
                switch (text){
                    case "Newst":
                        orderByText="Timestamp";
                        Directions = Query.Direction.DESCENDING;
                        filterquey(orderByText,Directions);
                        break;
                    case "Old":
                        orderByText="Timestamp";
                        Directions = Query.Direction.ASCENDING;
                        filterquey(orderByText,Directions);
                        break;
                    case "low-priced":
                        orderByText = "Price";
                        Directions = Query.Direction.ASCENDING;
                        filterquey(orderByText,Directions);
                        break;
                    case "high-priced":
                        orderByText = "Price";
                        Directions = Query.Direction.DESCENDING;
                        filterquey(orderByText,Directions);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //edit text
        searchEdit=view.findViewById(R.id.searchEdit);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("search",editable.toString());
                if(editable.toString().length() == 0){
                    orderByText="Timestamp";
                    Directions = Query.Direction.DESCENDING;
                    filterquey(orderByText,Directions);
                    Log.d("search","empty");
                }else{
                    data =editable.toString().toLowerCase();
                    orderByText="Title";
                    Directions = Query.Direction.DESCENDING;
                    FilterSearh(orderByText,data);

                }

            }
        });
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
    }
    @Override
    public void onResume() {
        super.onResume();
        filterquey(orderByText,Directions);
        if(adapter != null){
            adapter.startListening();
        }
    }

    public void filterquey(String orderBy, Query.Direction Direction){
        Query query  = FirebaseFirestore.getInstance().collection("Vehicles").orderBy(orderBy,Direction);
        initRecyclerView(query);
    }
    public void FilterSearh(String orderBy, String data){
        Query query = FirebaseFirestore.getInstance().collection("Vehicles").orderBy(orderBy).startAt(data).endAt(data + "\uf8ff");
        initRecyclerView(query);
    }
    private void initRecyclerView(Query query) {
        FirestoreRecyclerOptions<vehicle> options = new FirestoreRecyclerOptions.Builder<vehicle>()
                .setQuery(query, vehicle.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<vehicle, VehicleViewHolder>(options) {


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
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                vehicle.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Log.d("err","error occuer" +e);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.getMessage().toString(),Toast.LENGTH_LONG);
                toast.show();
                shimmerLayout.startShimmer();
                shimmerLayout.setVisibility(View.VISIBLE);
                vehicle.setVisibility(View.GONE);
            }

            @Override
            protected void onBindViewHolder(@NonNull VehicleViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull vehicle model) {

                holder.title.setText(model.getTitle());
                String price= getString(R.string.vehiclePrice,model.getPrice());
                holder.price.setText(price);
                Picasso.get().load(model.getImg()).placeholder(R.drawable.clearicon).into(holder.cardimg, new Callback() {
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
                        navController.navigate(vehicleHomeDirections.actionVehicleHomeToVehicleDetails(id));
                    }
                });
            }


        };

        vehicle.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null){
            adapter.stopListening();
        }

    }

}