package com.example.stealth;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class PostDetailsActivity extends AppCompatActivity {
    private BottomSheetBehavior bottomSheetBehavior;
    TextView txtPost, txtUp, txtDown;
    ImageView imgSend;
    EditText edtComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        final LinearLayout postLayout = findViewById(R.id.postLayout);

        postLayout.post(new Runnable() {
            @Override
            public void run() {
                int postHeight = postLayout.getHeight();

                View bottomSheet = findViewById(R.id.bottom_sheet);

                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int screenHeight = displayMetrics.heightPixels;

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheetBehavior.setPeekHeight(screenHeight - postHeight);
            }
        });
        String postText = getIntent().getStringExtra("postText");
        String upVotes = getIntent().getStringExtra("upVotes");
        String downVotes = getIntent().getStringExtra("downVotes");
        String key = getIntent().getStringExtra("Key");
        txtPost = findViewById(R.id.txtPost);
        txtUp = findViewById(R.id.txtUp);
        txtDown = findViewById(R.id.txtDown);

        txtPost.setText(postText);
        txtUp.setText(upVotes);
        txtDown.setText(downVotes);


        MaterialToolbar topBar = findViewById(R.id.top_bar);
        topBar.setNavigationIcon(R.drawable.arrow_back);
        topBar.setTitle("Post");
        topBar.inflateMenu(R.menu.menu_popup); // Create a menu resource for this
        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        BackendCommon.commentManager = new CommentManager(key,BackendCommon.UserId);
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerCommentAdapter adapter = new RecyclerCommentAdapter(this,BackendCommon.commentManager.Comments );
        recyclerView.setAdapter(adapter);

        BackendCommon.commentManager.adapter = adapter;


        edtComment = findViewById(R.id.edtComment);
        imgSend = findViewById(R.id.imgSend);
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = edtComment.getText().toString();
                if(comment.isEmpty())
                    return;
                BackendCommon.commentManager.MakeComment(comment);
                edtComment.setText(null);
            }
        });
    }
}