package com.example.stealth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerPollOptionsAdapter extends RecyclerView.Adapter<RecyclerPollOptionsAdapter.ViewHolder> {
    private ArrayList<Pair<String,Integer>> pollOptions;
    private int selectedPosition = -1;  // No selection by default
    private Context context;
    private int totalCount = 0;
    private int pollindex;
    public RecyclerPollOptionsAdapter(Context context, ArrayList<Pair<String,Integer>> pollOptions,int position) {
        this.pollOptions = pollOptions;
        this.context = context;
        this.pollindex=position;
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

        holder.txtOption.setText(pollOptions.get(position).first);
        Pair<String, Integer> pollOption = pollOptions.get(position);

        holder.txtOption.setOnClickListener(v -> {
            int time = 300;//time in milliseconds
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,"Option Clicked: "+position +"\nselected: "+BackendCommon.pollManager.Polls.get(pollindex).Selected,Toast.LENGTH_SHORT).show();

                    BackendCommon.pollManager.SelectPoll(BackendCommon.pollManager.Polls.get(pollindex),pollOption.first);
                    totalCount=0;
                    for (Pair<String, Integer> option : pollOptions) {
                        totalCount += option.second;
                    }
                    notifyDataSetChanged();
                }
            }, time);
        });
        float percentage =totalCount!=0? Float.max(0, (pollOption.second / (float) totalCount) * 100):Float.max(0, 0);
        if(BackendCommon.pollManager.Polls.get(pollindex).Selected!=null) {
            holder.txtPercent.setText(String.format("%.0f%%", percentage));
            if(pollOption.first.equals(BackendCommon.pollManager.Polls.get(pollindex).Selected))
            {
//                holder.itemView.findViewById().setBackgroundColor(Color.GREEN);
//                holder.seekBar
                // Retrieve the progress drawable from the XML file
                Drawable progressDrawable = ContextCompat.getDrawable(context, R.drawable.progress_track_selected);
                holder.seekBar.setProgressDrawable(progressDrawable);
            }
            else
            {
                Drawable progressDrawable = ContextCompat.getDrawable(context, R.drawable.progress_track);
                holder.seekBar.setProgressDrawable(progressDrawable);
            }
            holder.seekBar.setProgress((int) percentage + 1);
        }
        else
        {
            Drawable progressDrawable = ContextCompat.getDrawable(context, R.drawable.progress_track);
            holder.seekBar.setProgressDrawable(progressDrawable);
            holder.txtPercent.setText(null);
        }
//        else
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
