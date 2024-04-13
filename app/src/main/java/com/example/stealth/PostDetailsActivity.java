package com.example.stealth;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


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
        String upVotes = getIntent().getStringExtra("upVote");
        String downVotes = getIntent().getStringExtra("downVote");
        String key = getIntent().getStringExtra("Key");
        Toast.makeText(this,key,Toast.LENGTH_SHORT).show();
        txtPost = findViewById(R.id.txtPost);
        txtUp = findViewById(R.id.txtUp);
        txtDown = findViewById(R.id.txtDown);

        txtPost.setText(postText);
        txtUp.setText(upVotes);
        txtDown.setText(downVotes);
        String VoteType = getIntent().getStringExtra("VoteType");

        if(VoteType==null)
        {

        }
        else if(VoteType.equals("1")) {
            txtUp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_upward_filled, 0, 0, 0);
            txtDown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_downward, 0, 0, 0);
        }else if (VoteType.equals("-1")) {
            txtDown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_downward_filled, 0, 0, 0);
            txtUp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_upward, 0, 0, 0);
        }

//        txtUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(BackendCommon.myPosts.Posts.get(position).VoteType == 1)
//                {
//                    txtUp.setText(String.valueOf(--arrPosts.get(position).UpVote));
//                    arrPosts.get(position).VoteType = 0;
//                    txtUp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_upward, 0, 0, 0);
//                }
//                else if(arrPosts.get(position).VoteType == -1)
//                {
//                    txtUp.setText(String.valueOf(++arrPosts.get(position).UpVote));
//                    txtDown.setText(String.valueOf(--arrPosts.get(position).DownVote));
//                    arrPosts.get(position).VoteType = 1;
//                    txtUp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_upward_filled, 0, 0, 0);
//                    txtDown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_downward, 0, 0, 0);
//                }
//                else
//                {
//                    txtUp.setText(String.valueOf(++arrPosts.get(position).UpVote));
//                    arrPosts.get(position).VoteType=1;
//                    txtUp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_upward_filled, 0, 0, 0);
//                }
//
//                BackendCommon.postsManager.UpVote(arrPosts.get(position));
//            }
//        });
//        txtDown.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)  {
//
//                if(VoteType == -1)
//                {
//                    txtDown.setText(String.valueOf(--arrPosts.get(position).DownVote));
//                    arrPosts.get(position).VoteType=0;
//                    txtDown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_downward, 0, 0, 0);
//                }
//                else if(arrPosts.get(position).VoteType == 1)
//                {
//                    txtDown.setText(String.valueOf(++arrPosts.get(position).DownVote));
//                    txtUp.setText(String.valueOf(--arrPosts.get(position).UpVote));
//                    arrPosts.get(position).VoteType=-1;
//                    txtUp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_upward, 0, 0, 0);
//                    txtDown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_downward_filled, 0, 0, 0);
//                }
//                else
//                {
//                    txtDown.setText(String.valueOf(++arrPosts.get(position).DownVote));
//                    arrPosts.get(position).VoteType = -1;
//                    txtDown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_downward_filled, 0, 0, 0);
//                }
//
//                BackendCommon.postsManager.DownVote(arrPosts.get(position));
//            }
//        });
//
        MaterialToolbar topBar = findViewById(R.id.top_bar);
        topBar.setNavigationIcon(R.drawable.arrow_back);
        topBar.setTitle("Post");
        Menu menu = topBar.getMenu();
        topBar.inflateMenu(R.menu.menu_popup);
        MenuItem menuItem = menu.findItem(R.id.menuReport);
        menuItem.setVisible(false);
        topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menuCopy) {
                    // Copy text to clipboard
                    CharSequence text = ("Shared via Stealth\n").concat(postText);
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text", text);
                    clipboard.setPrimaryClip(clip);
                    return true;
                }
                return false;
            }
        });


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