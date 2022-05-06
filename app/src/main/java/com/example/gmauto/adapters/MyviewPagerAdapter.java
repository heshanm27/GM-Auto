package com.example.gmauto.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gmauto.Tabs.ProfileTab;
import com.example.gmauto.Tabs.ReservationTab;
import com.example.gmauto.Tabs.ReviewTab;

public class MyviewPagerAdapter extends FragmentStateAdapter {
    public MyviewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new ReviewTab();
            case 2:
                return new ReservationTab();
        }
        return new ProfileTab();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
