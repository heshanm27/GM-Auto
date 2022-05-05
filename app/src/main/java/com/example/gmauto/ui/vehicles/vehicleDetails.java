package com.example.gmauto.ui.vehicles;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gmauto.R;
import com.example.gmauto.ui.spareParts.sparepartDetailsArgs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class vehicleDetails extends Fragment {

    String Id, name, userid;

    TextView title, price, EngineCapacity,TransmissionType,Color,Mileage,Fuel,ManufacturingYear,discription;
    ChipGroup chipGroup;
    ImageView imageView5;
    ProgressBar progressLoad;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Id = vehicleDetailsArgs.fromBundle(requireArguments()).getFirebaseID().toString();
        title = view.findViewById(R.id.title);
        price = view.findViewById(R.id.price);
        ManufacturingYear = view.findViewById(R.id.ManufacturingYear);
        Fuel = view.findViewById(R.id.Fuel);
        Mileage = view.findViewById(R.id.Mileage);
        EngineCapacity = view.findViewById(R.id.EngineCapacity);
        TransmissionType= view.findViewById(R.id.TransmissionType);
        Color= view.findViewById(R.id.Color);
        chipGroup = view.findViewById(R.id.chipGroup);
        imageView5 = view.findViewById(R.id.imageView5);
        progressLoad = view.findViewById(R.id.progressLoad);
        discription = view.findViewById(R.id.discription);
        Log.d("debug", Id);

        getDetails();
    }

    public void getDetails() {
        db.collection("Vehicles").document(Id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                Map<String,Object> map = (Map<String, Object>) snapshot.get("details");
                List<String> group = (List<String>) snapshot.get("Amenities");

                Picasso.get().load(snapshot.getString("ImgUrl")).placeholder(R.drawable.clearicon).into(imageView5, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                for ( String elem : group ) {
                    addChip(elem);
                }
                title.setText((String) map.get("Title"));
                String amount = snapshot.getDouble("Price").toString();
                price.setText(amount);
                ManufacturingYear.setText((String) map.get("ManufacturingYear"));
                Fuel.setText((String) map.get("FuleType"));
                Mileage.setText((String) map.get("Mileage"));
                EngineCapacity.setText((String) map.get("EngineCapacity"));
                TransmissionType.setText((String) map.get("TransmissionType"));
                Color.setText((String) map.get("Color"));
                discription.setText(snapshot.getString("Discription"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void addChip(String text){
        Log.d("chip","added");
        Chip chips = new Chip(getContext());
        chips.setText(text);
        chips.setChipBackgroundColorResource(R.color.blueAccent);
        chips.setCloseIconVisible(false);
        chips.setTextColor(getResources().getColor(R.color.blue));
        chipGroup.addView(chips);


    }
}