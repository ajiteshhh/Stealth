package com.example.stealth;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Optional;

public class PollManager extends DatabaseManager{

    String PollKey;
    int PollToRead;
    String UserId;
    static final long MaxReport = 3;
    ArrayList<PollInfo> Polls;
    RecyclerPollAdapter adapter;
    public void addListenerforHead()
    {
        Poll.child("Head").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key=snapshot.getValue(String.class);
                if(key==null||(!Polls.isEmpty() && (Polls.get(0).Key).equals(key)))return;
                if(Polls.isEmpty())
                {
                    PollKey=key;
                    RetrieveNPoll(10);
                    return;
                }
                Poll.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(!snapshot.exists())return ;
                        PollInfo pinfo =new PollInfo();
                        pinfo.Options=new ArrayList<>();
                        pinfo.Title=snapshot.child("Title").getValue(String.class);
                        pinfo.Key=key;
                        pinfo.UserId=snapshot.child("User").getValue(String.class);
//                        pinfo.Options=snapshot.child("Option").getValue(ArrayList<>.class);
                        for(DataSnapshot child:snapshot.child("Option").getChildren())
                        {
                            pinfo.Options.add(new Pair<>(child.getKey(),child.getValue(Integer.class)));
                        }
                        Polls.add(0,pinfo);
                        OnCompletePollRead();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}});
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}});
    }
    public void addListenerForRemoved()
    {
        Poll.child("Removed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key=snapshot.getValue(String.class);
                if(Polls.isEmpty() || key==null)return;
                else
                {
                    for(int i=0;i<Polls.size();i++)
                    {
                        if(Polls.get(i).Key.equals(key))
                        {
                            Polls.remove(i);
                            OnCompletePollRead();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    PollManager(String User) {
        UserId=User;
        PollToRead=0;
        Polls=new ArrayList<>();
        addListenerforHead();
        addListenerForRemoved();
    }
    public void OnCompletePollRead()
    {
        if(adapter!=null)adapter.notifyDataSetChanged();;
    }
    public void RetrieveNPoll(int n)
    {
        PollToRead=n;
        GetNextPoll();
    }


    protected boolean OnPollRead(PollInfo pinfo)
    {
        if(pinfo==null)
        {
            OnCompletePollRead();
            return false;
        }
        Polls.add(pinfo);
        if(--PollToRead>0)
        {
            GetNextPoll();
        }
        else OnCompletePollRead();
        return true;
    }
    public boolean GetNextPoll()
    {
        if(PollKey==null)
        {
            OnPollRead(null);
            return false; ///there is no further Poll to read
        }
        Poll.child(PollKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())OnPollRead(null);
                PollInfo pinfo =new PollInfo();
                pinfo.Options=new ArrayList<>();
                pinfo.Title=snapshot.child("Title").getValue(String.class);
                pinfo.Key=PollKey;
                PollKey=snapshot.child("Next").getValue(String.class);
                pinfo.UserId=snapshot.child("User").getValue(String.class);
                Log.i("Function", pinfo.UserId+"");
                PollVote.child(pinfo.Key).child(pinfo.UserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pinfo.Selected=snapshot.getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                for(DataSnapshot child:snapshot.child("Option").getChildren())
                {
                    pinfo.Options.add(new Pair<>(child.getKey(),child.getValue(Integer.class)));
                }
                OnPollRead(pinfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return true; //means atleast one Poll is there to read
    }

    public void AddMyActivityPollRef(String key) {
        MyActivity.child(UserId).child("Poll").child("Head").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String HeadKey=snapshot.getValue(String.class);
                if(HeadKey!=null)
                {
                    MyActivity.child(UserId).child("Poll").child(key).child("Next").setValue(HeadKey);
                    MyActivity.child(UserId).child("Poll").child(HeadKey).child("Prev").setValue(key);
                }
                MyActivity.child(UserId).child("Poll").child("Head").setValue(key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void MakePoll(PollInfo pinfo)
    {
        if(UserId==null || pinfo.Options.isEmpty())return;
        String key=Poll.push().getKey();
        // Don't remove this below code , users of other application will also change this
        Poll.child("Head").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String HeadKey=snapshot.getValue(String.class);
                Poll.child(key).child("User").setValue(BackendCommon.UserId);
                Poll.child(key).child("Title").setValue(pinfo.Title);
                for(int i=0;i<pinfo.Options.size();i++)
                {

                    Poll.child(key).child("Option").child(pinfo.Options.get(i).first).setValue(pinfo.Options.get(i).second);
                }
                Poll.child(key).child("Report").setValue((long)(0));
                if(HeadKey!=null)
                {
                    Poll.child(key).child("Next").setValue(HeadKey);
                    Poll.child(HeadKey).child("Prev").setValue(key);
                }
                Poll.child("Head").setValue(key);
                AddMyActivityPollRef(key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void DeletePoll(String PollId)
    {
        Poll.child(PollId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())return ; //if already deleted
                String Prev=snapshot.child("Prev").getValue(String.class);
                String Next=snapshot.child("Next").getValue(String.class);
                String User=snapshot.child("User").getValue(String.class);
                Poll.child("Removed").setValue(PollId);
                if(Prev!=null)
                    Poll.child(Prev).child("Next").setValue(Next);
                else Poll.child("Head").setValue(Next);
                if(Next!=null)
                    Poll.child(Next).child("Prev").setValue(Prev);
                DeleteActivityPollRef(PollId);
                Poll.child(PollId).removeValue();
                Vote.child(PollId).removeValue();
                Comment.child(PollId).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void IncreaseReport(String PollId)
    {
        Poll.child(PollId).child("Report").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long report=snapshot.getValue(long.class);
                if(report>=MaxReport)
                {
                    DeletePoll(PollId);
                }
                else Poll.child(PollId).child("Report").setValue(report+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void ReportPoll(String PollId)
    {
        ReportCount.child("Poll").child(PollId).child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())return;
                else
                {
                    IncreaseReport(PollId);
                    ReportCount.child("Poll").child(PollId).child(UserId).setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void DeleteActivityPollRef(String PollId)
    {
        MyActivity.child(UserId).child(PollId).addListenerForSingleValueEvent(new ValueEventListener() {
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
                MyActivity.child(UserId).child(PollId).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void DecreaseVote(PollInfo pinfo,String ToSelect)
    {
        Poll.child(pinfo.Key).child("Option").child(ToSelect).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) return;
                long vote = snapshot.getValue(long.class);
                Poll.child(pinfo.Key).child("Option").child(ToSelect).setValue(vote - 1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    void IncreaseVote(PollInfo pinfo,String ToSelect)
    {
        Poll.child(pinfo.Key).child("Option").child(ToSelect).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) return;
                long vote = snapshot.getValue(long.class);
                Poll.child(pinfo.Key).child("Option").child(ToSelect).setValue(vote + 1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    void  SelectPoll(PollInfo pinfo,String ToSelect) {
        if (pinfo.Selected != null) {
            DecreaseVote(pinfo, pinfo.Selected);
            for(int i=0;i<pinfo.Options.size();i++)
            {
                if(pinfo.Options.get(i).first.equals(pinfo.Selected))
                {
                    Log.i("Decrease",pinfo.Selected+"Vote:"+(pinfo.Options.get(i).second-1));
                    pinfo.Options.set(i,new Pair<>(pinfo.Options.get(i).first,pinfo.Options.get(i).second-1));
                }
            }
            PollVote.child(pinfo.Key).child(UserId).removeValue();
            if (!ToSelect.equals(pinfo.Selected)) {
                pinfo.Selected=ToSelect;
                IncreaseVote(pinfo, ToSelect);
                for(int i=0;i<pinfo.Options.size();i++)
                {
                    if(pinfo.Options.get(i).first.equals(ToSelect))
                    {
                        Log.i("Increase",ToSelect+"Vote:"+(pinfo.Options.get(i).second+1));
                        pinfo.Options.set(i,new Pair<>(pinfo.Options.get(i).first,pinfo.Options.get(i).second+1));
                    }
                }
                PollVote.child(pinfo.Key).child(UserId).setValue(ToSelect);
            }
            else
                pinfo.Selected=null;
        } else {
            IncreaseVote(pinfo, ToSelect);
            for(int i=0;i<pinfo.Options.size();i++)
            {
                if(pinfo.Options.get(i).first.equals(ToSelect))
                {
                    Log.i("Increase",ToSelect);
                    pinfo.Options.set(i,new Pair<>(pinfo.Options.get(i).first,pinfo.Options.get(i).second+1));
                }
            }
            pinfo.Selected=ToSelect;
            PollVote.child(pinfo.Key).child(UserId).setValue(ToSelect);
        }
    }
}

