package com.example.stealth;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerPollOptionsAdapter extends RecyclerView.Adapter<RecyclerPollOptionsAdapter.ViewHolder> {
    private ArrayList<Pair<String,Integer>> pollOptions;
    private int selectedPosition = -1;  // No selection by default
    private Context context;

    public RecyclerPollOptionsAdapter(Context context, ArrayList<Pair<String,Integer>> pollOptions) {
        this.pollOptions = pollOptions;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pair<String, Integer> pollOption = pollOptions.get(position);
        holder.txtOption.setText(pollOptions.get(position).first);

        holder.radioButton.setChecked(position == selectedPosition);
        holder.radioButton.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return pollOptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        TextView txtOption, txtPercent;

        public ViewHolder(View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radio_button);
            txtOption = itemView.findViewById(R.id.txtOption);
            txtPercent = itemView.findViewById(R.id.txtPercent);
        }
    }
}
