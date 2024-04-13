package com.example.stealth;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostsManager extends DatabaseManager{
    String PostKey;
    int PostToRead;
    String UserId;
    static final long MaxReport=10;
    static final long NoOfPost=10;
    ArrayList<PostInfo> Posts;
    RecyclerPostAdapter adapter;
    public void addListenerforHead()
    {
        Post.child("Head").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key=snapshot.getValue(String.class);
                if(key==null||(!Posts.isEmpty() && (Posts.get(0).Key).equals(key)))return;
                if(Posts.isEmpty())
                {
                    PostKey=key;
                    RetrieveNPost(5);
                    return;
                }
                Post.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PostInfo pinfo =new PostInfo();
                        if(!snapshot.exists())return ;
                        pinfo.Info=snapshot.child("Info").getValue(String.class);
                        pinfo.Key=key;
//                        pinfo.commentManager=new CommentManager(key);
                        pinfo.UserId=snapshot.child("User").getValue(String.class);
                        pinfo.DownVote=snapshot.child("DownVote").getValue(long.class);
                        pinfo.UpVote=snapshot.child("UpVote").getValue(long.class);
                        pinfo.VoteType=0;
                        Posts.add(0,pinfo);
                        Vote.child(key).child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())pinfo.VoteType=snapshot.getValue(long.class);
                                OnCompletePostRead();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}});
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}});
    }
    public void addListenerForRemoved()
    {
        Post.child("Removed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key=snapshot.getValue(String.class);
                if(Posts.isEmpty() || key==null)return;
                else
                {
                    for(int i=0;i<Posts.size();i++)
                    {
                        if(Posts.get(i).Key.equals(key))
                        {
                            Posts.remove(i);
                            OnCompletePostRead();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    PostsManager(String User) {
        UserId=User;
        PostToRead=0;
        Posts=new ArrayList<>();
        addListenerforHead();
        addListenerForRemoved();
    }
    public void OnCompletePostRead()
    {

        if(adapter!=null) {
            Log.i("Function","Called");
            adapter.notifyDataSetChanged();
        }
//        this.UpVote(Posts.get(0));
    }
    public boolean RetrieveNPost(int n)
    {
        if(PostKey == null)
                return false;
        PostToRead=n;
        GetNextPost();
        return true;
    }
    public void ResetPostKey()
    {
        Post.child("Head").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Out("head:"+snapshot.getValue(String.class));
                PostKey=snapshot.getValue(String.class);
            }
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    protected boolean OnPostRead(PostInfo pinfo)
    {
        if(pinfo==null)
        {
            OnCompletePostRead();
            return false;
        }
        Posts.add(pinfo);
//        adapter
//        adapter.notifyItemChanged(Posts.size()-1);
        if(--PostToRead>0)
        {
            GetNextPost();
        }
        else OnCompletePostRead();
        return true;
    }
    public boolean GetNextPost()
    {
        if(PostKey==null)
        {
            OnPostRead(null);
//            Toast.makeText(context, "Reach End"+BackendCommon.postsManager.PostKey, Toast.LENGTH_SHORT).show();
            return false; ///there is no further post to read
        }
        Post.child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostInfo pinfo =new PostInfo();
                if(!snapshot.exists())OnPostRead(null);
                pinfo.Info=snapshot.child("Info").getValue(String.class);
                pinfo.Key=PostKey;
//                pinfo.commentManager=new CommentManager(PostKey);
                PostKey=snapshot.child("Next").getValue(String.class);
                pinfo.UserId=snapshot.child("User").getValue(String.class);
                pinfo.DownVote=snapshot.child("DownVote").getValue(long.class);
                pinfo.UpVote=snapshot.child("UpVote").getValue(long.class);
                pinfo.VoteType=0;
                Vote.child(pinfo.Key).child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())pinfo.VoteType=snapshot.getValue(long.class);
                                OnPostRead(pinfo);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return true; //means atleast one post is there to read
    }

    public void AddMyActivityPostRef(String key) {
        MyActivity.child(UserId).child("Post").child("Head").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String HeadKey=snapshot.getValue(String.class);
                if(HeadKey!=null)
                {
                    MyActivity.child(UserId).child("Post").child(key).child("Next").setValue(HeadKey);
                    MyActivity.child(UserId).child("Post").child(HeadKey).child("Prev").setValue(key);
                }
                MyActivity.child(UserId).child("Post").child("Head").setValue(key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void MakePost(String info)
    {
        if(UserId==null)return;
        String key=Post.push().getKey();
        // Don't remove this below code , users of other application will also change this
        Post.child("Head").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String HeadKey=snapshot.getValue(String.class);
                Post.child(key).child("User").setValue(UserId);
                Post.child(key).child("Info").setValue(info);
                Post.child(key).child("UpVote").setValue((long)(0));
                Post.child(key).child("DownVote").setValue((long)(0));
                Post.child(key).child("Report").setValue((long)(0));
                if(HeadKey!=null)
                {
                    Post.child(key).child("Next").setValue(HeadKey);
                    Post.child(HeadKey).child("Prev").setValue(key);
                }
                Post.child("Head").setValue(key);
                AddMyActivityPostRef(key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void DeletePost(String PostId)
    {
        Post.child(PostId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())return ; //if already deleted
                String Prev=snapshot.child("Prev").getValue(String.class);
                String Next=snapshot.child("Next").getValue(String.class);
                String User=snapshot.child("User").getValue(String.class);
                Post.child("Removed").setValue(PostId);
                if(Prev!=null)
                    Post.child(Prev).child("Next").setValue(Next);
                else Post.child("Head").setValue(Next);
                if(Next!=null)
                    Post.child(Next).child("Prev").setValue(Prev);
                DeleteActivityPostRef(PostId);
                Post.child(PostId).removeValue();
                Vote.child(PostId).removeValue();
                Comment.child(PostId).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void ReportPost(String PostId)
    {
        Post.child(PostId).child("Report").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long report=snapshot.getValue(long.class);
                if(report >= MaxReport)
                {
                    DeletePost(PostId);
                }
                else Post.child(PostId).child("Report").setValue(report + 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private String vote1,vote2;
    private void OnVoteTypeRead(Object voteId,String PostId,long VoteType)
    {
        //reading Whole
        Post.child(PostId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long vote1count = snapshot.child(vote1).getValue(long.class);
                long vote2count = snapshot.child(vote2).getValue(long.class);
                if(voteId==null) {
                    Post.child(PostId).child(vote1).setValue(vote1count+1);
                    Vote.child(PostId).child(UserId).setValue(VoteType);
                }
                else if((long)voteId==VoteType)
                {
                    Post.child(PostId).child(vote1).setValue(vote1count-1);
                    Vote.child(PostId).child(UserId).removeValue();
                }
                else if((long)voteId==-VoteType)
                {
                    Post.child(PostId).child(vote1).setValue(vote1count+1);
                    Vote.child(PostId).child(UserId).setValue(VoteType);
                    Post.child(PostId).child(vote2).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Post.child(PostId).child(vote2).setValue(vote2count-1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void Vote(PostInfo pinfo,long VoteType)       //VoteType 1 for Upvote -1 for Downvote
    {
        if(UserId==null)return ;
        if(VoteType==1) {
            vote1 = "UpVote";
            vote2 = "DownVote";
        }
        else if(VoteType==-1)
        {
            vote1="DownVote";
            vote2="UpVote";
        }
        Vote. child(pinfo.Key).child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot){
                Object voteId=snapshot.getValue();
                OnVoteTypeRead(voteId,pinfo.Key,VoteType);
//                if(voteId==null || VoteType!=(long)voteId)pinfo.VoteType=VoteType;
//                else pinfo.VoteType=0;
//                OnCompletePostRead();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    public void DeleteActivityPostRef(String PostId)
    {
        MyActivity.child(UserId).child(PostId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists() )return ; //if already deleted
                String Prev=snapshot.child("Prev").getValue(String.class);
                String Next=snapshot.child("Next").getValue(String.class);
                if(Prev!=null)
                    MyActivity.child(UserId).child(Prev).child("Next").setValue(Next);
                else MyActivity.child(UserId).child("Head").setValue(Next);
                if(Next!=null)
                    MyActivity.child(UserId).child(Next).child("Prev").setValue(Prev);
                MyActivity.child(UserId).child(PostId).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void DownVote(PostInfo pinfo)
    {
        Vote(pinfo,-1);
    }

    public void UpVote(PostInfo pinfo) {Vote(pinfo,1);}
}
