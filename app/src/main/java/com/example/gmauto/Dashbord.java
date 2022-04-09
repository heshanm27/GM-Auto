package com.example.gmauto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.gmauto.databinding.ActivityDashbordBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashbord extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityDashbordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashbordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbar);
        binding.navView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerlayout, binding.toolbar, R.string.nav_drawer_open, R.string.nav_darwer_close);
        binding.drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
    }

    //avoid when press back app close
    @Override
    public void onBackPressed() {
        if (binding.drawerlayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerlayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}