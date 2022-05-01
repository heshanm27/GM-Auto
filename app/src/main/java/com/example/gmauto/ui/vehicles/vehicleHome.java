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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gmauto.R;
import com.example.gmauto.models.sparepart;
import com.example.gmauto.ui.spareParts.sparePartsHomeDirections;
import com.example.gmauto.viewHolders.SparePartHomeViewHolder;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;


public class vehicleHome extends Fragment implements FirebaseAuth.AuthStateListener {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter<sparepart, SparePartHomeViewHolder> adapter;
    Spinner spinner;
    RecyclerView sparepart;
    NavController navController;
    ShimmerFrameLayout shimmerLayout;
    String orderByText="Timestamp",data="";
    Query.Direction Directions = Query.Direction.DESCENDING;
    TextInputEditText searchEdit;
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
        //reycler view
        sparepart = view.findViewById(R.id.sparepart);
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
                        orderByText = "productPrice";
                        Directions = Query.Direction.ASCENDING;
                        filterquey(orderByText,Directions);
                        break;
                    case "high-priced":
                        orderByText = "productPrice";
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
                    data =editable.toString();
                    orderByText="productName";
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
        Query query = FirebaseFirestore.getInstance().collection("SpareParts").orderBy(orderBy).startAt(data).endAt(data + "\uf8ff");
        initRecyclerView(query);
    }
    private void initRecyclerView(Query query) {
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
                sparepart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Log.d("err","error occuer" +e);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.getMessage().toString(),Toast.LENGTH_LONG);
                toast.show();
                shimmerLayout.startShimmer();
                shimmerLayout.setVisibility(View.VISIBLE);
                sparepart.setVisibility(View.GONE);
            }

            @Override
            protected void onBindViewHolder(@NonNull SparePartHomeViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull sparepart model) {
                holder.title.setText(model.getProductName());
                holder.price.setText("Rs" + Double.toString(model.getProductPrice()));
                float avg = (float) model.getRateavg().doubleValue();
                String ratingavgstring = getString(R.string.RatingAvgvalue,avg);
                holder.ratevalue.setText(ratingavgstring);


                holder.ratingBar.setRating((float) model.getRateavg().doubleValue());
                Picasso.get().load(model.getImg()).placeholder(R.drawable.clearicon).into(holder.cardimg);
                holder.V.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentSnapshot doc=getSnapshots().getSnapshot(position);
                        String id = doc.getId().toString();
                        navController.navigate(sparePartsHomeDirections.actionSparePartsHomeToSparepartDetails(id));
                    }
                });
            }


        };

        sparepart.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}