package android.support.supportsystem.Adapter;

import android.support.supportsystem.R;
import android.support.supportsystem.model.Comment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mahmoud shahen on 11/22/2016.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    List<Comment> comments;

    public CommentAdapter(List<Comment> comments)
    {
        this.comments = comments;
    }
    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.comment_card, parent,false);
        return new CommentAdapter.CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.name.setText(comments.get(position).getOwner());
        holder.content.setText(comments.get(position).getComment());

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        protected TextView name, content;

        public CommentViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.ownerName);
            content = (TextView) itemView.findViewById(R.id.comment);

        }
    }
}
