package com.example.gmauto.Tabs;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmauto.R;
import com.example.gmauto.models.reservation;
import com.example.gmauto.models.reviews;
import com.example.gmauto.models.sparepart;
import com.example.gmauto.viewHolders.ReservationViewHolder;
import com.example.gmauto.viewHolders.ReviewsViewHolder;
import com.example.gmauto.viewHolders.SparePartHomeViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ReservationTab extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<reservation, ReservationViewHolder> adapter;
    RecyclerView reservationRecyclerView;




    public ReservationTab() {
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
        return inflater.inflate(R.layout.fragment_reservation_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reservationRecyclerView = view.findViewById(R.id.reservationRecyclerView);

        getReservations();

        //set layoutmanger into recyclerview
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        reservationRecyclerView.setLayoutManager(layoutManager);
        //recyler view decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        reservationRecyclerView.addItemDecoration(dividerItemDecoration);

    }



    private  void getReservations(){
        Query query = FirebaseFirestore.getInstance().collection("OnlineReservation").orderBy("Timestamp", Query.Direction.DESCENDING).whereEqualTo("userID",FirebaseAuth.getInstance().getUid());
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
                holder.statuslayout.setVisibility(View.VISIBLE);
                selectColor(holder.Status,holder.Delete, holder.Update, model.getStatus(),holder.acceptedmsg);
                holder.Delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentSnapshot doc=getSnapshots().getSnapshot(position);
                        delete(doc);
                    }
                });
                holder.Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentSnapshot snap = getSnapshots().getSnapshot(position);
                        DialogFragment dialog = ReservationFullScreenDialog.newInstance();
                        Bundle args = new Bundle();
                        args.putString("FirebaseID",snap.getId());
                        args.putParcelable("model",model);
                        dialog.setArguments(args);
                        dialog.show(getActivity().getSupportFragmentManager(), "Update");
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


    public void selectColor(Button btn, Button delete,Button Update, String staus, TextView textView){

        switch(staus){
            case "Pending":
                btn.setBackgroundColor(getResources().getColor(R.color.Pending));
                btn.setText(staus);
                delete.setVisibility(View.VISIBLE);
                Update.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                break;
            case "Declined":
                btn.setBackgroundColor(getResources().getColor(R.color.Declined));
                btn.setText(staus);
                delete.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                Update.setVisibility(View.GONE);
                break;
            case "Accept":
                btn.setBackgroundColor(getResources().getColor(R.color.Accept));
                btn.setText("Accepted");
                delete.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                Update.setVisibility(View.GONE);
                break;

        }

    }

    private   void delete(DocumentSnapshot snapshot){
        DocumentReference documentReference = snapshot.getReference();
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error occured", Toast.LENGTH_SHORT).show();
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