package android.support.supportsystem.model;

import android.support.supportsystem.Adapter.AnnouncementAdapter;
import android.support.supportsystem.Adapter.TaskAdapter;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahmoud shahen on 10/21/2016.
 */

public class Head {

    public Member member;

    public Head() {}

    public void sendTask(FirebaseDatabase database, Task task, String URL, List<String> pickedID) { //  /Android/myTasks
        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(URL);// URL to enter tasks part

        List<PickedMembers> pickedMembers = task.getAssignedMembers();
        task.setAssignedMembers(new ArrayList<PickedMembers>());


        task.setId(databaseReference.push().getKey());
        databaseReference.child(task.getId()).setValue(task);
        databaseReference = database.getReference("/Android/myTasks/"+task.getId()+"/assignedMembers");
        for(int i=0 ; i<pickedID.size() ; i++)
        {
            databaseReference.child(pickedID.get(i)).setValue(pickedMembers.get(i));
        }

        databaseReference = database.getReference("/Android/myMembers");
        for (int i=0 ; i<pickedID.size() ; i++)
        {
           // Log.v("listIDs", pickedID.get(i));
            databaseReference.child(pickedID.get(i)).child("tasksId")
                    .child(task.getId()).setValue(task.getId());
        }

       // Log.v("ccccc", );
    }// send task

    public void sendAnnouncement(final FirebaseDatabase database, Announcement announcement, String URL){ //  /Android/myAnnouncements
        DatabaseReference databaseReference = database.getReference(URL);// URL to enter announcement part
        announcement.setId(databaseReference.push().getKey());
        databaseReference.child(announcement.getId()).setValue(announcement); // add task in the end of announcement
    }// send announcemen


    public List<Task> getTasks(final FirebaseDatabase database, final String committeeName,
                               final List<Task> tasks, final TaskAdapter taskAdapter){

        DatabaseReference ref = database.getReference("/"+committeeName+"/myTasks"); // stand on Tasks on firebase

        ChildEventListener mylistener;
        mylistener=new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String content = dataSnapshot.child("content").getValue(String.class); // cast child to task
                String deadline = dataSnapshot.child("deadLine").getValue(String.class);
                String title = dataSnapshot.child("title").getValue(String.class);
                String id = dataSnapshot.child("id").getValue(String.class);
                String timeStamp = dataSnapshot.child("timeStamp").getValue(String.class);
                Task task = new Task();
               // task.setAssignedMembers(pickedMembersList);
                task.setContent(content);
                task.setId(id);
                task.setDeadLine(deadline);
                task.setTitle(title);
                task.setTimeStamp(timeStamp);
                tasks.add(task);
                Log.v("Size", String.valueOf(tasks.size()));
                taskAdapter.notifyItemChanged(tasks.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(final DataSnapshot dataSnapshot) {
              Log.v("qqqqq", String.valueOf(dataSnapshot.getKey()));

                for(int i=0 ; i<tasks.size() ; i++)
                {
                    if(dataSnapshot.getKey().equals(tasks.get(i).getId()))
                    {
                        tasks.remove(i);
                        taskAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref.addChildEventListener(mylistener);

        return tasks;
    }// get all tasks of a committee

    public List<Announcement> getAnnouncements(final FirebaseDatabase database, String committeeName,
                                               final List<Announcement> announcements, final AnnouncementAdapter announcementAdapter){

        DatabaseReference ref = database.getReference("/"+committeeName+"/myAnnouncements"); // stand on Announcement on firebase

        ChildEventListener mylistener=new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Announcement announcement = new Announcement();// cast child to task
                announcement.setId(dataSnapshot.child("id").getValue(String.class));
                announcement.setOwner(dataSnapshot.child("owner").getValue(String.class));
                announcement.setTitle(dataSnapshot.child("title").getValue(String.class));
                announcement.setTimeStamp(dataSnapshot.child("timeStamp").getValue(String.class));
                announcement.setContent(dataSnapshot.child("content").getValue(String.class));

                announcements.add(announcement);
                Log.v("Size", String.valueOf(announcements.size()));
                announcementAdapter.notifyItemChanged(announcements.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Log.v("qqqqq",  dataSnapshot.getKey());
                for(int i=0 ; i<announcements.size() ; i++)
                {
                    if(dataSnapshot.getKey().equals(announcements.get(i).getId()))
                    {
                        announcements.remove(i);
                        announcementAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref.addChildEventListener(mylistener);

        return announcements;
    }//get all announcements
    public void Add_Member (final FirebaseDatabase database, final Member member, final String URL)
    {// /Android/myMembers/UID
        DatabaseReference databaseReference = database.getReference(URL);// URL to enter tasks part
        databaseReference.setValue(member);
    }


}