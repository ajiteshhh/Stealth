package com.example.stealth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AppInfoFragment extends Fragment {
    Button btnLogout, btnForgotPass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app_info, container, false);
        btnLogout = rootView.findViewById(R.id.btnLogout);
        btnForgotPass = rootView.findViewById(R.id.btnForgotPass);
        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent var = new Intent(getActivity(), ForgotPassword.class);
                startActivity(var);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent var = new Intent(getActivity(), SignIn.class);
                startActivity(var);
                getActivity().finish();
            }
        });
        return rootView;
    }
}