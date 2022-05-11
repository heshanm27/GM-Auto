package com.example.gmauto.ui.Admin;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gmauto.Dashbord;
import com.example.gmauto.HomeFragmentDirections;
import com.example.gmauto.MainActivity;
import com.example.gmauto.R;
import com.example.gmauto.models.reviews;
import com.example.gmauto.models.sparepart;
import com.example.gmauto.viewHolders.ReviewsViewHolder;
import com.example.gmauto.viewHolders.SparePartHomeViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AdminSparePart extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter<sparepart, SparePartHomeViewHolder> adapter;
    RecyclerView adminSparePartRecylerView;
    FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_spare_part, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adminSparePartRecylerView= view.findViewById(R.id.adminSparePartRecylerView);
        //set layoutmanger into recyclerview
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        adminSparePartRecylerView.setLayoutManager(layoutManager);
        //recyler view decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        adminSparePartRecylerView.addItemDecoration(dividerItemDecoration);

        //get Floating button from mainActivity
       fab = ((Dashbord) getActivity()).getFloatingActionButton();

        if(fab != null){
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment dialog   = FullScreenDialog.newInstance();

                   dialog.show(getActivity().getSupportFragmentManager(), "tag");

                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        initRecyclerView();
        if(adapter != null){
            adapter.startListening();
        }
    }

    private void initRecyclerView() {
        Query query = FirebaseFirestore.getInstance().collection("SpareParts").orderBy("Timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<sparepart> options = new FirestoreRecyclerOptions.Builder<sparepart>()
                .setQuery(query, sparepart.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<sparepart, SparePartHomeViewHolder>(options) {
            DocumentSnapshot doc;
            String id;
            @NonNull
            @Override
            public SparePartHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutinflater = LayoutInflater.from(parent.getContext());
                View view = layoutinflater.inflate(R.layout.admin_sparepart_recycler, parent, false);
                return new SparePartHomeViewHolder(view);
            }
            @Override
            public void onDataChanged() {
                super.onDataChanged();
            }
            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Log.d("err","error occuer" +e);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), e.getMessage().toString(),Toast.LENGTH_LONG);
                toast.show();
            }
            @Override
            protected void onBindViewHolder(@NonNull SparePartHomeViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull sparepart model) {
                holder.title.setText(model.getProductName());
//                holder.itemdescription.setText(model.getProductDiscription());
//                Picasso.get().load(model.getImg()).placeholder(R.drawable.clearicon).into(holder.cardimg);
                    try{
                holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         doc=getSnapshots().getSnapshot(position);
                        id = doc.getId();
                        delete(doc,model);
                            adapter.startListening();
                    }
                });}catch(Exception e) {
                        e.printStackTrace();
                }
                holder.editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doc=getSnapshots().getSnapshot(position);
                        id = doc.getId();
                        update(id,model);
                    }
                });
            }
        };
        adminSparePartRecylerView.setAdapter(adapter);
        adapter.startListening();
    }


    public void delete(DocumentSnapshot snapshot,sparepart model) {
            DocumentReference documentReference = snapshot.getReference();
        Map<String,Object> map = new HashMap<>();
        map.put("img",model.getImg());
        map.put("productDiscription",model.getProductDiscription());
        map.put("productPrice",model.getProductPrice());
        map.put("productName",model.getProductName());
        map.put("rateavg",0.0);
        map.put("Timestamp",model.getTimestamp());
        map.put("SearchKey",model.getProductName());

            documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        Snackbar.make(adminSparePartRecylerView,"Item Deleted",Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.startListening();
                documentReference.set(map);
            }
        }).show();

    }


    public void  update(String ID,sparepart model){
        Log.d("btn",ID);
        DialogFragment dialog   = FullScreenDialog.newInstance();
        Bundle args = new Bundle();
        args.putString("FirebaseID",ID);
        args.putParcelable("model",  model);
        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), "Update");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("steps","start");
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("steps","Stop");
        adapter.stopListening();
        fab.setVisibility(View.GONE);
    }
}