package android.support.supportsystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.supportsystem.R;
import android.support.supportsystem.activities.SubmittedTask;
import android.support.supportsystem.genaric.GenaricData;
import android.support.supportsystem.genaric.anime;
import android.support.supportsystem.model.Announcement;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by mahmoud shahen on 11/9/2016.
 */

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>{
    List<Announcement> announcementList;
    FirebaseDatabase firebase;
    Context context;
    int prevoisPosition = 0;
    public AnnouncementAdapter(List<Announcement> announcementList, FirebaseDatabase firebase, Context context)
    {
        this.announcementList=announcementList;
        this.firebase = firebase;
        this.context = context;
    }
    @Override
    public AnnouncementAdapter.AnnouncementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.announcement_card_layout, parent,false);


        return new AnnouncementAdapter.AnnouncementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AnnouncementAdapter.AnnouncementViewHolder holder, int position) {

        final Announcement announcement= announcementList.get(position);
        holder.vTitle.setText(announcement.getTitle());
        holder.vTimeStamp.setText(announcement.getTimeStamp());
        holder.vContent.setText(announcement.getContent());
        holder.owner.setText(announcement.getOwner());
        if(GenaricData.A_S_M == 0)
            holder.close.setVisibility(View.GONE);
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GenaricData.A_S_M != 0) {
                    DatabaseReference databaseReference = firebase.getReference("/Android/myAnnouncements/" + announcement.getId());// URL to enter tasks part
                    databaseReference.removeValue();
                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SubmittedTask.class);
                intent.putExtra("vis", true);
                intent.putExtra("id", announcement.getId());
                context.startActivity(intent);
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
        return announcementList.size();
    }

    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {

        protected TextView vTitle, vTimeStamp, vContent, owner;
        ImageButton close;
        CardView cardView;
        public AnnouncementViewHolder(View itemView) {
            super(itemView);
            vTimeStamp=(TextView) itemView.findViewById(R.id.card_view_timeStampannouncement);
            vContent = (TextView)  itemView.findViewById(R.id.card_view_contentannouncement);
            vTitle = (TextView) itemView.findViewById(R.id.card_view_titleannouncement);
            owner = (TextView)itemView.findViewById(R.id.card_view_owner_announcement);
            cardView = (CardView)itemView.findViewById(R.id.card_view_announcement);
            close = (ImageButton) itemView.findViewById(R.id.deleteAnnounc);
        }
    }
}
