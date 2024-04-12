package com.example.stealth;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class RecyclerCommentAdapter extends RecyclerView.Adapter<RecyclerCommentAdapter.ViewHolder> {
    private Context context;
    ArrayList<CommentInfo> arrComments= new ArrayList<>();
    RecyclerCommentAdapter(Context context, ArrayList<CommentInfo> arrComments) {
        this.context = context;
        this.arrComments = arrComments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerCommentAdapter.ViewHolder holder, int position) {
        holder.txtComment.setText(arrComments.get(position).Info);
        holder.imgReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menuReport) {
                            LayoutInflater inflater = LayoutInflater.from(context);
                            View customView = inflater.inflate(R.layout.report_dialog, null);
                            Button button = customView.findViewById(R.id.btnReport);
                            EditText editText = customView.findViewById(R.id.edtReport);
                            RadioGroup radioGroup = customView.findViewById(R.id.radio_group);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setView(customView);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String text = editText.getText().toString();
                                    if(text.isEmpty() || radioGroup.getCheckedRadioButtonId() == -1)
                                        return;
                                    BackendCommon.commentManager.ReportComment(arrComments.get(position).Key);
                                    Toast.makeText(context, "Report Sent", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            });
                            return true;
                        }else if (item.getItemId() == R.id.menuCopy) {
                            CharSequence text = ("Shared via Stealth\n").concat(arrComments.get(position).Info);
                            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Copied Text", text);
                            clipboard.setPrimaryClip(clip);
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrComments.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtComment;
        ImageView imgReport;
        public ViewHolder(@NonNull View item) {
            super(item);
            txtComment = item.findViewById(R.id.txtComment);
            imgReport = item.findViewById(R.id.imgReport);
        }
    }
}
