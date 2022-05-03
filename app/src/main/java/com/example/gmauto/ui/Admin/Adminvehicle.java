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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmauto.Dashbord;
import com.example.gmauto.R;
import com.example.gmauto.models.sparepart;
import com.example.gmauto.models.vehicle;
import com.example.gmauto.viewHolders.SparePartHomeViewHolder;
import com.example.gmauto.viewHolders.VehicleViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Map;


public class Adminvehicle extends Fragment {


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter<vehicle, VehicleViewHolder> adapter;
    RecyclerView adminRecylerView;
    FloatingActionButton fab;
    TextView year,year2,year3,year4;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adminvehicle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        adminRecylerView= view.findViewById(R.id.adminRecylerView);
        //set layoutmanger into recyclerview
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        adminRecylerView.setLayoutManager(layoutManager);
        //recyler view decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        adminRecylerView.addItemDecoration(dividerItemDecoration);

        //get Floating button from mainActivity
        fab = ((Dashbord) getActivity()).getFloatingActionButton();

        if(fab != null){
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment dialog   = vehicleFullScrrenDialog.newInstance();

                    dialog.show(getActivity().getSupportFragmentManager(), "tag");

                }
            });
        }
        getFirebase();
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
        Query query = FirebaseFirestore.getInstance().collection("Vehicles").orderBy("Timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<vehicle> options = new FirestoreRecyclerOptions.Builder<vehicle>()
                .setQuery(query, vehicle.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<vehicle, VehicleViewHolder>(options) {
            DocumentSnapshot doc;
            String id;

            @NonNull
            @Override
            public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutinflater = LayoutInflater.from(parent.getContext());
                View view = layoutinflater.inflate(R.layout.admin_sparepart_recycler, parent, false);
                return new VehicleViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VehicleViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull vehicle model) {

                holder.title.setText(model.getTitle());

                holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doc=getSnapshots().getSnapshot(position);
                        id = doc.getId();
                        delete(id);
                    }
                });
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

        adminRecylerView.setAdapter(adapter);
        adapter.startListening();
    }


    public void delete(String ID){
        Log.d("btn",ID);

        db.collection("SpareParts").document(ID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast taost = Toast.makeText(getContext(),"suessFully Deleted",Toast.LENGTH_LONG);
                taost.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public void  update(String ID,vehicle model){
        Log.d("btn",ID);
        DialogFragment dialog   = vehicleFullScrrenDialog.newInstance();
        Bundle args = new Bundle();
        args.putString("FirebaseID",ID);
        args.putParcelable("model",  model);
//        args.putString("productDiscription",model.getProductDiscription());
//        args.putString("productName",model.getProductName());
//        args.putDouble("productPrice",model.getProductPrice());
//        args.putDouble("rateavg",model.getRateavg());
//        args.putString("imgUrl",model.getImg());

        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), "Update");

    }





    @Override
    public void onStart() {
        super.onStart();
        Log.d("steps","start");
        fab.setVisibility(View.VISIBLE);
    }

    public void getFirebase(){

        db.collection("Vehicles").document("oNYPZq3jN6wPf103b6Tk").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {

        if(snapshot != null) {
            String years23 = snapshot.getString("Title");
//////            Map<String, Object> map = (Map<String, Object>) snapshot.get("discription");
////            String years = (String) map.get("Mileage");
////            String years2 = (String) map.get("year");
////            String years24 = (String) map.get("a");
//
//            Log.d("maps", years+years2 + years23+years24);

        }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("maps","error"+e);
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("steps","Stop");
        adapter.stopListening();
        fab.setVisibility(View.GONE);
    }
}