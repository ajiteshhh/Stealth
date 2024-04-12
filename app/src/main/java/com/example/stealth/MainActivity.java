package com.example.stealth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnSignOut;
    BottomNavigationView bottomNavigationView;
//    ArrayList<PostModel> arrPosts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        btnSignOut = findViewById(R.id.btnSignOut);
//        btnSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent var = new Intent(MainActivity.this, SignIn.class);
//                startActivity(var);
//                finish();
//            }
//        });
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        arrPosts.add(new PostModel("The benefits of outdoor activities for mental health.", "50", "10"));
//        arrPosts.add(new PostModel("The impact of technology on modern relationships.", "30", "5"));
//        arrPosts.add(new PostModel("Exploring the wonders of space exploration.", "70", "20"));
//        arrPosts.add(new PostModel("The importance of renewable energy for a sustainable future.", "40", "15"));
//        arrPosts.add(new PostModel("The role of education in reducing poverty.", "55", "8"));
//        arrPosts.add(new PostModel("The future of artificial intelligence in healthcare.", "25", "3"));
//        arrPosts.add(new PostModel("Climate change: challenges and solutions.", "65", "12"));
//        arrPosts.add(new PostModel("The psychology behind decision-making.", "45", "7"));
//        arrPosts.add(new PostModel("The impact of social media on mental health.", "60", "9"));
//        arrPosts.add(new PostModel("The art of effective communication in leadership.", "35", "6"));
//        arrPosts.add(new PostModel("The benefits of meditation for stress relief.", "75", "18"));
//        arrPosts.add(new PostModel("The role of women in STEM fields.", "20", "4"));
//        arrPosts.add(new PostModel("The future of remote work in a post-pandemic world.", "50", "11"));
//        arrPosts.add(new PostModel("Exploring cultural diversity through travel.", "40", "7"));
//        arrPosts.add(new PostModel("The science behind happiness and well-being.", "65", "14"));


//        PostModel post = new PostModel();
//        RecyclerPostAdapter adapter = new RecyclerPostAdapter(this,arrPosts);
//        recyclerView.setAdapter(adapter);
////        adapter;
////        arrPosts.remove(0);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                arrPosts.add(new PostModel(String.valueOf(newState), "65", "14"));
//                adapter.notifyDataSetChanged();
//            }
//        });
////        adapter.;
//


        //Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.menuHome) {
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.menuPoll) {
                    selectedFragment = new PollsFragment();
                } else if (item.getItemId() == R.id.menuPost) {
                    selectedFragment = new AddPostPollFragment();
                } else if (item.getItemId() == R.id.menuMyActivity) {
                    selectedFragment = new MyActivityFragment();
                } else if (item.getItemId() == R.id.menuAbout) {
                    selectedFragment = new AppInfoFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });





    }
}