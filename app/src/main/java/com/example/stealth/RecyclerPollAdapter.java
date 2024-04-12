package com.example.stealth;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerPollAdapter extends RecyclerView.Adapter<RecyclerPollAdapter.ViewHolder> {
    private ArrayList<PollInfo> polls;
    Context context;
    public RecyclerPollAdapter(Context context, ArrayList<PollInfo> polls) {
        this.polls = polls;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerPollAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_row, parent, false);
        return new RecyclerPollAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PollInfo poll = polls.get(position);

        holder.txtTitle.setText(poll.Title);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        RecyclerPollOptionsAdapter optionAdapter = new RecyclerPollOptionsAdapter(context, poll.Options);
        holder.recyclerView.setAdapter(optionAdapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));

        holder.pollLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PollDetailsActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return polls.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        CardView pollLayout;
        TextView txtTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.options_recycler_view);
            pollLayout = itemView.findViewById(R.id.pollLayout);
            txtTitle = itemView.findViewById(R.id.txtTitle);
        }
    }
}

