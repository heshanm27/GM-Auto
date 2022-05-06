package com.example.gmauto.ui.Profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gmauto.R;
import com.example.gmauto.Tabs.ProfileTab;
import com.example.gmauto.Tabs.ReservationTab;
import com.example.gmauto.Tabs.ReviewTab;
import com.example.gmauto.adapters.MyviewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class Profile extends Fragment {


TabLayout tabLayout;
ViewPager2 viewPager;
ProfileTab profileTab;
ReservationTab reservationTab;
ReviewTab reviewTab;

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

       profileTab =new ProfileTab();
       reservationTab = new ReservationTab();
         reviewTab = new ReviewTab();

        FragmentManager manager = getActivity().getSupportFragmentManager();
        MyviewPagerAdapter adapter = new MyviewPagerAdapter(manager,getLifecycle());
        viewPager.setAdapter(adapter);


        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Reviews"));
        tabLayout.addTab(tabLayout.newTab().setText("Reservations"));

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
    }
}