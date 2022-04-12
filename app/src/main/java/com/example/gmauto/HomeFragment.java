package com.example.gmauto;

import android.app.DownloadManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gmauto.adapters.SparepartHomeAdapter;
import com.example.gmauto.models.sparepart;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class HomeFragment extends Fragment implements FirebaseAuth.AuthStateListener {


    RecyclerView recyclerView,vehile,foryou;
    SparepartHomeAdapter spareRecy;
    NavController navController;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.sparepartrecyclerView);
        vehile = root.findViewById(R.id.vehiclerecyclerView);
        foryou= root.findViewById(R.id.foryourecylerview);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }



    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getCurrentUser() == null){
        navController.navigate(R.id.action_dashFragment_to_login2);
        }
        initRecyclerView();
    }


    private void initRecyclerView(){
        Query query = FirebaseFirestore.getInstance().collection("SpareParts");
        FirestoreRecyclerOptions<sparepart> options = new FirestoreRecyclerOptions.Builder<sparepart>()
                .setQuery(query, sparepart.class)
                .build();

        spareRecy = new SparepartHomeAdapter(options);
        recyclerView.setAdapter(spareRecy);
        vehile.setAdapter(spareRecy);
        foryou.setAdapter(spareRecy);
        spareRecy.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        spareRecy.stopListening();
    }
}