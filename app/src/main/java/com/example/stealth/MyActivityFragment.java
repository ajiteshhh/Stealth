package com.example.stealth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class MyActivityFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_activity, container, false);

        ViewPager2 viewPager = rootView.findViewById(R.id.viewPager);
        VPAdapter adapter = new VPAdapter(this);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
            Fragment selectedFragment = null;
                    if (position == 0) {
                        selectedFragment = new MyPostFragment();
                    } else if (position == 1) {
                        selectedFragment = new MyPollFragment();
                    }
                }).attach();
        return rootView;

    }
}