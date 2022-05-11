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
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmauto.R;
import com.example.gmauto.models.contcatus;
import com.example.gmauto.models.orders;
import com.example.gmauto.models.reservation;
import com.example.gmauto.viewHolders.ContactusViewholder;
import com.example.gmauto.viewHolders.OrderViewHolder;
import com.example.gmauto.viewHolders.ReservationViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AdminContactus extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<contcatus, ContactusViewholder> adapter;
    RecyclerView contactRecyclerview;

    public AdminContactus() {
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
        return inflater.inflate(R.layout.fragment_admin_contactus, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactRecyclerview= view.findViewById(R.id.contactRecyclerview);

        //set layoutmanger into recyclerview
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        contactRecyclerview.setLayoutManager(layoutManager);
        //recyler view decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        contactRecyclerview.addItemDecoration(dividerItemDecoration);
        getOrders();

    }

    private  void getOrders(){
        Query query = FirebaseFirestore.getInstance().collection("ContactUs").orderBy("TimeStamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<contcatus> options = new FirestoreRecyclerOptions.Builder<contcatus>()
                .setQuery(query, contcatus.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<contcatus, ContactusViewholder>(options){


            @Override
            protected void onBindViewHolder(@NonNull ContactusViewholder holder, int position, @NonNull contcatus model) {
                holder.contactno.setText(model.getPhoneno());
                holder.Email.setText(model.getEmail());
                holder.message.setText(model.getExtraDetails());
                holder.name.setText(model.getCustomerName());

                model.getTimestamp();
//                Date date = new Date(String.valueOf(model.getTimestamp().toDate()));
//                SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");
//                String value = sfd.format(date);
//                holder.time.setText(value);
            }

            @NonNull
            @Override
            public ContactusViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contcatus_reyclerview, parent, false);
                return new ContactusViewholder(view);
            }

        };

        contactRecyclerview.setAdapter(adapter);

    }




    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }

    }
}