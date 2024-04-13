package com.example.stealth;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerPollOptionsAdapter extends RecyclerView.Adapter<RecyclerPollOptionsAdapter.ViewHolder> {
    private ArrayList<Pair<String,Integer>> pollOptions;
    private int selectedPosition = -1;  // No selection by default
    private Context context;
    private int totalCount = 0;

    public RecyclerPollOptionsAdapter(Context context, ArrayList<Pair<String,Integer>> pollOptions) {
        this.pollOptions = pollOptions;
        this.context = context;
        if (pollOptions != null) {
            for (Pair<String, Integer> option : pollOptions) {
                totalCount += option.second;
            }
        }
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

        holder.txtOption.setOnClickListener(v -> {
            pollOptions.set(position,new Pair<>(pollOption.first,pollOption.second+20));
//            BackendCommon.pollManager.SelectPoll(BackendCommon.pollManager.Polls.get(position),pollOption.first);
//            totalCount+=1;
            Toast.makeText(context,"Option Clicked: ggg "+position +"\nporgress: ",Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        });

        float percentage =totalCount!=0? Float.max(0, (pollOption.second / (float) totalCount) * 100):Float.max(0, 0);
        holder.txtPercent.setText(String.format("%.0f%%", percentage));
        holder.seekBar.setProgress((int)percentage+1);
    }


@Override
    public int getItemCount() {
        return pollOptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        TextView txtOption, txtPercent;
        SeekBar seekBar;
        ConstraintLayout optionLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            txtOption = itemView.findViewById(R.id.txtOption);
            txtPercent = itemView.findViewById(R.id.txtPercent);
            seekBar = itemView.findViewById(R.id.seekBar);
            optionLayout = itemView.findViewById(R.id.optionLayout);
        }
    }
}
