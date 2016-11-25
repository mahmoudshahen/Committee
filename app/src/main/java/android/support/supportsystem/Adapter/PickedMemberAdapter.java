package android.support.supportsystem.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.supportsystem.R;
import android.support.supportsystem.activities.PickedMemberDialog;
import android.support.supportsystem.model.Member;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
 * Created by mahmoud shahen on 11/16/2016.
 */

public class PickedMemberAdapter extends RecyclerView.Adapter<PickedMemberAdapter.MemberHolder>{

    List<Member> memberList;
    FirebaseDatabase firebase;
    Context context;
    FirebaseStorage firebaseStorage;
    StorageReference mStorage;
    boolean checkAll;
    public PickedMemberAdapter(List<Member> memberList, FirebaseDatabase firebase,
                               FirebaseStorage firebaseStorage, StorageReference storageReference, Context context)
    {
        this.memberList=memberList;
        this.firebase = firebase;
        this.firebaseStorage = firebaseStorage;
        this.mStorage = storageReference;
        this.context = context;

    }
    @Override
    public PickedMemberAdapter.MemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.one_item_picked_member, parent,false);
        return new PickedMemberAdapter.MemberHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PickedMemberAdapter.MemberHolder holder, final int position) {
        final Member member = memberList.get(position);
        holder.mName.setText(member.user.getName());
        final String id = member.user.getId();
//        Log.v("tasksidsize", String.valueOf(member.getTasksId().size()));
        if (checkAll) {
            holder.checkBox.setChecked(true);
            PickedMemberDialog.setPickedID(id);
        } else {
            holder.checkBox.setChecked(false);
            PickedMemberDialog.delPickID(id);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("adapter", "checkbox");
                if ((holder.checkBox.isChecked())) {
                    Log.v("adapter", "checktrue");
                    PickedMemberDialog.setPickedID(id);

                } else {
                    Log.v("adapter", "checkfalse");
                    PickedMemberDialog.delPickID(id);
                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("adapter", "adapter");

                if (holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(false);
                    PickedMemberDialog.delPickID(id);
                } else {
                    holder.checkBox.setChecked(true);
                    PickedMemberDialog.setPickedID(id);
                }
            }
        });
        // child name can't be null or empty
        mStorage.child(member.user.getId() + "/" + member.user.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).fit().centerCrop().into(holder.mPhoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public  class MemberHolder extends RecyclerView.ViewHolder {

        protected TextView  mName;
        ImageView mPhoto ;
        LinearLayout cardView;
        CheckBox checkBox;
        public MemberHolder(View itemView) {
            super(itemView);
            mName=(TextView) itemView.findViewById(R.id.cardview_name_pickedMember);
            mPhoto = (ImageView)  itemView.findViewById(R.id.imgProfilePicture_pickedMember);
            cardView = (LinearLayout) itemView.findViewById(R.id.card_view_member);
            checkBox = (CheckBox)itemView.findViewById(R.id.checkBox_pickedMember);

        }

    }
    public void setCheckeAll(boolean checkeAll)
    {
        this.checkAll = checkeAll;
    }

}
