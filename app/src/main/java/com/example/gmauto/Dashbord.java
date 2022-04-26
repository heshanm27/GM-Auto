package com.example.gmauto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.gmauto.databinding.ActivityDashbordBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashbord extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

        DrawerLayout drawerLayout;
        NavigationView navigationView;
        Toolbar toolbar;
        AppBarConfiguration mAppBarconfig;
    ActionBarDrawerToggle toggle;
    NavController navController;
    FloatingActionButton fab;
    boolean mToolBarNavigationRegister = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);

        //layout reference
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.floating_action_button);
        //set app bar
        setSupportActionBar(toolbar);


        navigationView.bringToFront();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_darwer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mAppBarconfig = new AppBarConfiguration.Builder(R.id.nav_home,R.id.adminSparePart,R.id.testing).setDrawerLayout(drawerLayout).build();
        navigationView.setNavigationItemSelectedListener(this);
        navController= Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this,navController,mAppBarconfig);
        NavigationUI.setupWithNavController(navigationView,navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if(mAppBarconfig.getTopLevelDestinations().contains(navDestination.getId())){
                    toggle.setDrawerIndicatorEnabled(true);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                }else{
                    toggle.setDrawerIndicatorEnabled(false);
                    toggle.setHomeAsUpIndicator(R.drawable.appbarbackbtn);

                }
            }
        });
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }



    //avoid when press back app close
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.topappbarr,menu);
         return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    //return fab
    public FloatingActionButton getFloatingActionButton (){
        return fab;
    }
}