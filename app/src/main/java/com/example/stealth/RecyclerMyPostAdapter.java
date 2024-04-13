
package com.example.stealth;

        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.cardview.widget.CardView;
        import androidx.recyclerview.widget.RecyclerView;

        import java.util.ArrayList;
        import android.view.MenuItem;
        import android.widget.PopupMenu;
        import android.widget.Toast;
public class RecyclerMyPostAdapter extends RecyclerView.Adapter<RecyclerMyPostAdapter.ViewHolder> {
    Context context;
    ArrayList<PostInfo> arrPosts;

    RecyclerMyPostAdapter(Context context, ArrayList<PostInfo> arrPosts) {
        this.context = context;
        this.arrPosts = arrPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtPost.setText(arrPosts.get(position).Info);
        holder.txtUp.setText(arrPosts.get(position).UpVote+"");
        holder.txtDown.setText(arrPosts.get(position).DownVote+"");
        if(arrPosts.get(position).VoteType == 1) {
            holder.txtUp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_upward_filled, 0, 0, 0);
            holder.txtDown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_downward, 0, 0, 0);

        }        else if (arrPosts.get(position).VoteType == -1) {
            holder.txtDown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_downward_filled, 0, 0, 0);
            holder.txtUp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_upward, 0, 0, 0);

        }
        else {
            holder.txtDown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_downward, 0, 0, 0);
            holder.txtUp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_upward, 0, 0, 0);
        }

        holder.txtUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(BackendCommon.UserId.equals(arrPosts.get(position).UserId));
//                {
//                    return;
//                }
                if(arrPosts.get(position).VoteType == 1)
                {
                    holder.txtUp.setText(String.valueOf(--arrPosts.get(position).UpVote));
                    arrPosts.get(position).VoteType = 0;
                    holder.txtUp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_upward, 0, 0, 0);
                }
                else if(arrPosts.get(position).VoteType == -1)
                {
                    holder.txtUp.setText(String.valueOf(++arrPosts.get(position).UpVote));
                    holder.txtDown.setText(String.valueOf(--arrPosts.get(position).DownVote));
                    arrPosts.get(position).VoteType = 1;
                    holder.txtUp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_upward_filled, 0, 0, 0);
                    holder.txtDown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_downward, 0, 0, 0);
                }
                else
                {
                    holder.txtUp.setText(String.valueOf(++arrPosts.get(position).UpVote));
                    arrPosts.get(position).VoteType=1;
                    holder.txtUp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_upward_filled, 0, 0, 0);
                }

                BackendCommon.postsManager.UpVote(arrPosts.get(position));
            }
        });
        holder.txtDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {

                if(arrPosts.get(position).VoteType == -1)
                {
                    holder.txtDown.setText(String.valueOf(--arrPosts.get(position).DownVote));
                    arrPosts.get(position).VoteType=0;
                    holder.txtDown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_downward, 0, 0, 0);
                }
                else if(arrPosts.get(position).VoteType == 1)
                {
                    holder.txtDown.setText(String.valueOf(++arrPosts.get(position).DownVote));
                    holder.txtUp.setText(String.valueOf(--arrPosts.get(position).UpVote));
                    arrPosts.get(position).VoteType=-1;
                    holder.txtUp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_upward, 0, 0, 0);
                    holder.txtDown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_downward_filled, 0, 0, 0);
                }
                else
                {
                    holder.txtDown.setText(String.valueOf(++arrPosts.get(position).DownVote));
                    arrPosts.get(position).VoteType = -1;
                    holder.txtDown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_downward_filled, 0, 0, 0);
                }

                BackendCommon.postsManager.DownVote(arrPosts.get(position));
            }
        });
        if (position == getItemCount() - 3) {
            if(BackendCommon.myPosts.PostKey==null)
                Toast.makeText(context, "Reach End", Toast.LENGTH_SHORT).show();
            BackendCommon.myPosts.RetrieveNPost(5);
        }
        holder.imgReport.setVisibility(View.GONE);
        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostInfo clickedPost = arrPosts.get(position);

                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("postText", clickedPost.Info);
                intent.putExtra("upVote", clickedPost.UpVote+"");
                intent.putExtra("downVote", clickedPost.DownVote+"");
                intent.putExtra("Key",clickedPost.Key);
                intent.putExtra("VoteType", clickedPost.VoteType+"");
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPost, txtUp, txtDown, txtComment;
        ImageView imgReport;
        CardView post;
        public ViewHolder(@NonNull View item) {
            super(item);
            post = item.findViewById(R.id.postLayout);
            txtPost = item.findViewById(R.id.txtPost);
            txtUp = item.findViewById(R.id.txtUp);
            txtDown = item.findViewById(R.id.txtDown);
            txtComment = item.findViewById(R.id.txtComment);
            imgReport = item.findViewById(R.id.imgReport);        }
    }
}