package android.support.supportsystem.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.supportsystem.R;
import android.support.supportsystem.activities.TaskDetails;
import android.support.supportsystem.genaric.GenaricData;
import android.support.supportsystem.genaric.anime;
import android.support.supportsystem.model.Task;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Nouran on 11/3/2016.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{


    List<Task> taskList;
    FirebaseDatabase firebase;
    Context context;
    int prevoisPosition = 0;
    String memberId;
    public TaskAdapter(List<Task> taskList, FirebaseDatabase firebase, Context context)
    {
        this.taskList=taskList;
        this.firebase = firebase;
        this.context = context;
    }
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.home_card_layout, parent,false);


        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, final int position) {

        final Task task= taskList.get(position);
        holder.vTitle.setText(task.getTitle());
        holder.vDeadline.setText(task.getDeadLine());
        holder.vContent.setText(task.getContent());
        holder.vTimeStamp.setText(task.getTimeStamp());
        if(GenaricData.A_S_M == 0)
            holder.close.setVisibility(View.GONE);
        holder.close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(GenaricData.A_S_M != 0) {

                    if(memberId != null)
                    {
                        DatabaseReference databaseReference = firebase.getReference("/Android/myMembers/" + memberId+"/tasksId");
                        databaseReference.child(task.getId()).removeValue();
                        databaseReference = firebase.getReference("/Android/myTasks/"+task.getId()+"/assignedMembers");
                        databaseReference.child(memberId).removeValue();
                        taskList.remove(position);
                        TaskAdapter.super.notifyDataSetChanged();
                    }
                    else {
                        DatabaseReference databaseReference = firebase.getReference("/Android/myTasks/" + task.getId() + "/assignedMembers");
                        databaseReference.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Log.v("qqqqq", String.valueOf(dataSnapshot.getKey()));
                                DatabaseReference db = firebase.getReference("/Android/myMembers/" + dataSnapshot.getKey() + "/tasksId");
                                db.child(task.getId()).removeValue();

                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        DatabaseReference db = firebase.getReference("/Android/myTasks/" + task.getId());// URL to enter tasks part
                        db.removeValue();
                    }
                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TaskDetails.class);
                intent.putExtra("Task", task);
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
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        protected TextView vTitle, vDeadline, vContent, vTimeStamp;
        ImageButton close;
        CardView cardView;
        public TaskViewHolder(View itemView) {
            super(itemView);
            vDeadline=(TextView) itemView.findViewById(R.id.card_view_deadline);
            vContent = (TextView)  itemView.findViewById(R.id.card_view_content);
            vTitle = (TextView) itemView.findViewById(R.id.card_view_title);
            vTimeStamp = (TextView) itemView.findViewById(R.id.card_view_TimeStamp);
            cardView = (CardView)itemView.findViewById(R.id.card_view_task);
            close = (ImageButton) itemView.findViewById(R.id.deleteTask);
        }
    }
    public void setMemberId( String id)
    {
        this.memberId = id;
    }
    public String getMemberId()
    {
        return memberId;
    }
}
