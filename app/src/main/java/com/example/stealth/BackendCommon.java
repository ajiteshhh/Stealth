package com.example.stealth;

import android.util.Pair;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class BackendCommon extends Fragment {
//    static RecyclerPostAdapter adapter = new RecyclerPostAdapter();
    static String UserId="-NvAj0pUDgJKKv4AmcXa";
//    PostsManager postsManager = new PostsManager("-NvAj0pUDgJKKv4AmcXa");
     static PostsManager postsManager = new PostsManager(UserId);
     static MyPosts myPosts = new MyPosts(UserId);
     static CommentManager commentManager;
     static PollManager pollManager = new PollManager(UserId);
     static ArrayList<String> AddOption;
//     static
}
