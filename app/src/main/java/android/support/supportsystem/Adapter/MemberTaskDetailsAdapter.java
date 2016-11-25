package android.support.supportsystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.supportsystem.R;
import android.support.supportsystem.activities.SubmittedTask;
import android.support.supportsystem.genaric.GenaricData;
import android.support.supportsystem.model.Member;
import android.support.supportsystem.model.Task;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mahmoud shahen on 11/20/2016.
 */

public class MemberTaskDetailsAdapter extends RecyclerView.Adapter<MemberTaskDetailsAdapter.MemberViewHolder> {

    List<Member> members;
    FirebaseDatabase firebase;
    Context context;
    FirebaseStorage firebaseStorage;
    StorageReference mStorage;
    Task task;
    boolean draw;
    String ID;
    public MemberTaskDetailsAdapter(List<Member> members, FirebaseDatabase firebase, Task task, String ID,
                                    Context context, FirebaseStorage firebaseStorage, StorageReference mStorage) {
        this.members = members;
        this.firebase = firebase;
        this.context = context;
        this.firebaseStorage = firebaseStorage;
        this.mStorage = mStorage;
        this.task = task;
        this.ID = ID;
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_members_item, parent,false);
        return new MemberTaskDetailsAdapter.MemberViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MemberViewHolder holder, final int position) {
//        Log.v("ccc", members.get(position).user.getName());
        holder.name.setText(members.get(position).user.getName());
        holder.deliver.setVisibility(View.VISIBLE);
        if(!draw )
            holder.deliver.setVisibility(View.GONE);
        if(GenaricData.A_S_M != 0)
            holder.deliver.setVisibility(View.VISIBLE);

        if(task.getAssignedMembers().get(position).getDelivered()) {
            holder.deliver.setText("Delivered");
            holder.deliver.setTextColor(context.getResources().getColor(R.color.green));
        }
        else if(!task.getAssignedMembers().get(position).getDelivered()) {
            holder.deliver.setText("Not Delivered");
            holder.deliver.setTextColor(context.getResources().getColor(R.color.red));
        }

        mStorage.child(members.get(position).user.getId()+"/"+ members.get(position).user.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).fit().centerCrop().into(holder.imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        if(ID.equals(members.get(position).user.getId()) || GenaricData.A_S_M != 0) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SubmittedTask.class);
                    intent.putExtra("id", task.getId());
                    intent.putExtra("userid", members.get(position).user.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {

        protected TextView name, deliver;
        ImageView imageView;
        LinearLayout cardView;

        public MemberViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.cardview_imgmember);
            name = (TextView) itemView.findViewById(R.id.cardview_name);
            deliver = (TextView) itemView.findViewById(R.id.cardview_deliver);
            cardView = (LinearLayout) itemView.findViewById(R.id.card_view_member);
        }
    }
    public void setDraw(boolean draw)
    {
        this.draw = draw;
    }
}
