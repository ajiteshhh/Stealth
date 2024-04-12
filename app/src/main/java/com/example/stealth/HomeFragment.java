package com.example.stealth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomeFragment extends BackendCommon {
    Button btnSignOut;
    TextView txtNoPost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        txtNoPost = rootView.findViewById(R.id.txtNoPost);
        btnSignOut = rootView.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent var = new Intent(getActivity(), SignIn.class);
                startActivity(var);
                getActivity().finish();
            }
        });


        RecyclerView recyclerView;
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerPostAdapter adapter = new RecyclerPostAdapter(getActivity(), BackendCommon.postsManager.Posts);
        recyclerView.setAdapter(adapter);
//        recyclerView.
        postsManager.adapter = adapter;
        if (adapter.getItemCount() == 0) {
            txtNoPost.setVisibility(View.VISIBLE);
        } else {
            txtNoPost.setVisibility(View.GONE);
        }
        return rootView;
    }
}