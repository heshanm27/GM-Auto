package com.example.gmauto.ui.Admin;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.example.gmauto.R;
import com.example.gmauto.models.reservation;
import com.example.gmauto.models.reviews;
import com.example.gmauto.viewHolders.ReservationViewHolder;
import com.example.gmauto.viewHolders.ReviewsViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AdminReservation extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<reservation, ReservationViewHolder> adapter;
    RecyclerView reservationRecyclerView;

    public AdminReservation() {
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
        return inflater.inflate(R.layout.fragment_admin_reservation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reservationRecyclerView= view.findViewById(R.id.reservationRecyclerView);

        //set layoutmanger into recyclerview
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        reservationRecyclerView.setLayoutManager(layoutManager);
        //recyler view decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        reservationRecyclerView.addItemDecoration(dividerItemDecoration);


        getReservations();
    }



    private  void getReservations(){
        Query query = FirebaseFirestore.getInstance().collection("OnlineReservation").orderBy("Timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<reservation> options = new FirestoreRecyclerOptions.Builder<reservation>()
                .setQuery(query, reservation.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<reservation, ReservationViewHolder>(options){

            @NonNull
            @Override
            public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminr_reservation_recyclerview_layout, parent, false);
                return new ReservationViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ReservationViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull reservation model) {

                holder.ServiceType.setText(model.getServiceType());
                holder.name.setText(model.getFullName());
                holder.dates.setText(model.getPreferedDate());
                holder.times.setText(model.getPrefferedTime());
                holder.adminlayout.setVisibility(View.VISIBLE);

                holder.Accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DocumentSnapshot doc=getSnapshots().getSnapshot(position);
                        UpdateStatus(doc.getId(),"Accept");
                    }
                });

                holder.decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DocumentSnapshot doc=getSnapshots().getSnapshot(position);
                        UpdateStatus(doc.getId(),"Declined");
                    }
                });

            }
        };

        reservationRecyclerView.setAdapter(adapter);

    }
    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }

    }


    public void UpdateStatus(String Id,String status){

        Map<String,Object> map = new HashMap<>();
        map.put("Status",status);
        map.put("UpdatedTimestamp",new Timestamp(new Date()));
        db.collection("OnlineReservation").document(Id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(),"Sucess", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error occured", Toast.LENGTH_SHORT).show();
            }
        });
    }






    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null){
            adapter.startListening();
        }
    }
}