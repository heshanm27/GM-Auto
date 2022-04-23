package com.example.gmauto.ui.spareParts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmauto.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class sparepartDetails extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView title,disc,price;
    ImageView mainImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root= inflater.inflate(R.layout.fragment_sparepart_details, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.title);
        disc = view.findViewById(R.id.disc);
        price = view.findViewById(R.id.price);
        mainImg=view.findViewById(R.id.mainImg);


        String Id = sparepartDetailsArgs.fromBundle(requireArguments()).getFirebaseID().toString();
        Log.d("id",Id);
        db.collection("SpareParts").document(Id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                        String img = documentSnapshot.getString("img");
                        String tit = documentSnapshot.getString("productName");
                        String discription = documentSnapshot.getString("productDiscription");
                        String prie = documentSnapshot.getDouble("productPrice").toString();
                        title.setText(tit);

                }else{
                    Toast.makeText(getContext(),"Doument Not Exist",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}