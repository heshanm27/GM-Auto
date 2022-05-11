package com.example.gmauto.Tabs;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmauto.R;
import com.example.gmauto.models.orders;
import com.example.gmauto.viewHolders.OrderViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class OrdersTab extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<orders, OrderViewHolder> adapter;
    RecyclerView orderrecyclerview;




    public OrdersTab() {
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
        return inflater.inflate(R.layout.fragment_orders_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderrecyclerview= view.findViewById(R.id.orderrecyclerview);

        //set layoutmanger into recyclerview
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        orderrecyclerview.setLayoutManager(layoutManager);
        //recyler view decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        orderrecyclerview.addItemDecoration(dividerItemDecoration);
        getOrders();
    }


    private  void getOrders(){
        Query query = FirebaseFirestore.getInstance().collection("Orders").orderBy("TimeStamp", Query.Direction.DESCENDING).whereEqualTo("UserId", FirebaseAuth.getInstance().getUid());
        FirestoreRecyclerOptions<orders> options = new FirestoreRecyclerOptions.Builder<orders>()
                .setQuery(query, orders.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<orders, OrderViewHolder>(options){


            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_recyclerview_item, parent, false);
                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull orders model) {

                Log.d("h","hellow");
                holder.customeremail.setText(model.getEmail());
                holder.name.setText(model.getCustomerName());
                holder.item.setText(model.getItemName());
                holder.quantity.setText(model.getQuantity().toString());
                holder.value.setText(model.getTotal().toString());
                holder.statuslayout.setVisibility(View.VISIBLE);
                selectColor(holder.Status,holder.Delete, holder.Update,model.getStatus(),holder.acceptedmsg);

                holder.Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentSnapshot snap = getSnapshots().getSnapshot(position);
                        DialogFragment dialog = OrderFullScreenDialog.newInstance();
                        Bundle args = new Bundle();
                        args.putString("FirebaseID",snap.getId());
                        args.putParcelable("model",model);
                        args.putDouble("Total",model.getTotal());

                        dialog.setArguments(args);
                        dialog.show(getActivity().getSupportFragmentManager(), "Update");
                    }
                });
                holder.Delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentSnapshot doc=getSnapshots().getSnapshot(position);

                        deleteOrder(doc);
                    }
                });
            }
        };

        orderrecyclerview.setAdapter(adapter);

    }

    private   void deleteOrder(DocumentSnapshot snapshot){
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

    public void selectColor(Button btn, Button delete,Button Update, String staus, TextView textView){

        switch(staus){
            case "Pending":
                btn.setBackgroundColor(getResources().getColor(R.color.Pending));
                btn.setText(staus);
                delete.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                Update.setVisibility(View.VISIBLE);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        System.out.println("hello");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter != null){
            adapter.onDataChanged();
        }
    }
}