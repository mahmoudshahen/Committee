package android.support.supportsystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.supportsystem.Profile;
import android.support.supportsystem.R;
import android.support.supportsystem.genaric.anime;
import android.support.supportsystem.model.Member;
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
 * Created by Piso on 11/11/2016.
 */
public class List_Member_Adapter extends RecyclerView.Adapter<List_Member_Adapter.MemberHolder>{

    List<Member> memberList;
    FirebaseDatabase firebase;
    Context context;
    FirebaseStorage firebaseStorage;
    StorageReference mStorage;
    int prevoisPosition = 0;
    public List_Member_Adapter(List<Member> taskList, FirebaseDatabase firebase,
            FirebaseStorage firebaseStorage, StorageReference storageReference, Context context)
    {
        this.memberList=taskList;
        this.firebase = firebase;
        this.firebaseStorage = firebaseStorage;
        this.mStorage = storageReference;
        this.context = context;

    }
    @Override
    public MemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_members_item, parent,false);
        return new MemberHolder (itemView);
     }

    @Override
    public void onBindViewHolder(final MemberHolder holder, final int position) {
        final Member member= memberList.get(position);
        holder.mName.setText(member.user.getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("Member", memberList.get(position));
                context.startActivity(intent);
            }
        });

        // child name can't be null or empty
        mStorage.child(member.user.getId()+"/"+member.user.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
        if(position>prevoisPosition)
            anime.animate(holder,true);
        else
            anime.animate(holder,false);

        prevoisPosition = position;
    }
    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public static class MemberHolder extends RecyclerView.ViewHolder {

        protected TextView  mName;
         ImageView  mPhoto ;
        LinearLayout cardView;
        public MemberHolder(View itemView) {
            super(itemView);
            mName=(TextView) itemView.findViewById(R.id.cardview_name);
            mPhoto = (ImageView)  itemView.findViewById(R.id.cardview_imgmember);
             cardView = (LinearLayout) itemView.findViewById(R.id.card_view_member);
        }
    }
}
