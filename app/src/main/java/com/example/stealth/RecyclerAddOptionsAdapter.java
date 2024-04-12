package com.example.stealth;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAddOptionsAdapter extends RecyclerView.Adapter<RecyclerAddOptionsAdapter.ViewHolder> {

    private Context context;
    static ArrayList<String> addOption;
    public RecyclerAddOptionsAdapter(Context context, ArrayList<String > addOption) {
        this.addOption = addOption;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_option_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String pollOption = addOption.get(position);
        holder.txtOpt.setText(pollOption);
    }

    @Override
    public int getItemCount() {
        return addOption.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOpt;

        public ViewHolder(View itemView) {
            super(itemView);
            txtOpt = itemView.findViewById(R.id.txtOpt);
        }
    }
}
