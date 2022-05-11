package com.example.gmauto.ui.Profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmauto.R;
import com.example.gmauto.Tabs.OrdersTab;
import com.example.gmauto.Tabs.ProfileTab;
import com.example.gmauto.Tabs.ReservationTab;
import com.example.gmauto.Tabs.ReviewTab;
import com.example.gmauto.adapters.MyviewPagerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class Profile extends Fragment {


TabLayout tabLayout;
ViewPager2 viewPager;
ProfileTab profileTab;
ReservationTab reservationTab;
ReviewTab reviewTab;
OrdersTab ordersTab;
ImageView profile_image;
TextView username;
ProgressBar progressLoad;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        profile_image =view.findViewById(R.id.profile_image);
        username =view.findViewById(R.id.username);
        progressLoad =view.findViewById(R.id.progressLoad);
       profileTab =new ProfileTab();
       reservationTab = new ReservationTab();
         reviewTab = new ReviewTab();
        ordersTab = new OrdersTab();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        MyviewPagerAdapter adapter = new MyviewPagerAdapter(manager,getLifecycle());
        viewPager.setAdapter(adapter);


        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Reviews"));
        tabLayout.addTab(tabLayout.newTab().setText("Reservations"));
        tabLayout.addTab(tabLayout.newTab().setText("Orders"));
        viewPager.setUserInputEnabled(true);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        db.collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                username.setText(snapshot.getString("FullName"));
                Log.d("s",snapshot.getString("FullName"));
                Picasso.get().load(snapshot.getString("Img")).placeholder(R.drawable.placeholder_images).into(profile_image, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error occured", Toast.LENGTH_SHORT).show();
            }
        });
    }
}