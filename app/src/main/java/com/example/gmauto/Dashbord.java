package com.example.gmauto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gmauto.databinding.ActivityDashbordBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashbord extends AppCompatActivity {

    ActivityDashbordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashbordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);



//        String userName = fAuth.getCurrentUser().getDisplayName();
//        binding.textView.setText(userName);
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });
    }
}