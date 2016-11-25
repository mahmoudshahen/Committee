package android.support.supportsystem.genaric;

import android.support.supportsystem.Adapter.List_Member_Adapter;
import android.support.supportsystem.Adapter.PickedMemberAdapter;
import android.support.supportsystem.model.Member;
import android.support.supportsystem.model.User;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahmoud shahen on 11/12/2016.
 */

public class GenaricData {
    public static String  get_Id (String  committee  , boolean member ){
        // if member true  , return id for member
        // else   return  id for head

        if(committee.length()>3)
            committee = committee.substring(0,3);

        int number = (int) (Math.random() * 100000);
        if(member)
            committee  = committee + "-mem" + String.valueOf(number);
        else
            committee  = committee + "-head" + String.valueOf(number);

        return committee ;
    }
    public  static  String  Id ;
    public static int A_S_M = 0; // 0 member 1 super 2 Admin
    public static List<Member> getMembers(final FirebaseDatabase database, final String committeeName,
                                          final List<Member> members, final List_Member_Adapter listMemberAdapter){

        DatabaseReference ref = database.getReference("/"+committeeName+"/myMembers");

        ChildEventListener mylistener;
        mylistener=new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final List<String> tasksId = new ArrayList<>();
                DatabaseReference databaseReference = database.getReference("/"+committeeName+"/myMembers")
                        .child(dataSnapshot.getKey()).child("tasksId");
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        tasksId.add(dataSnapshot.getKey());
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

                Member member = new Member();
                User user = dataSnapshot.child("user").getValue(User.class);
                member.setCommittee(dataSnapshot.child("committee").getValue(String.class));
                member.setUser(user);
                member.setTasksId(tasksId);
                members.add(member);
                listMemberAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for(int i=0 ; i<members.size() ; i++)
                {
                    if(dataSnapshot.getKey().equals(members.get(i).user.getId()))
                    {
                        members.remove(i);
                        listMemberAdapter.notifyDataSetChanged();
                    }
                }
                final List<String> tasksId = new ArrayList<>();
                DatabaseReference databaseReference = database.getReference("/"+committeeName+"/myMembers")
                        .child(dataSnapshot.getKey()).child("tasksId");
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        tasksId.add(dataSnapshot.getKey());
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

                Member member = new Member();
                User user = dataSnapshot.child("user").getValue(User.class);
                member.setCommittee(dataSnapshot.child("committee").getValue(String.class));
                member.setUser(user);
                member.setTasksId(tasksId);
                members.add(member);
                listMemberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for(int i=0 ; i<members.size() ; i++)
                {
                    if(dataSnapshot.getKey().equals(members.get(i).user.getId()))
                    {
                        members.remove(i);
                        listMemberAdapter.notifyDataSetChanged();
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

        return members ;
    }// get all tasks of a committee

    public static List<Member> getPickMembers(final FirebaseDatabase database, final String committeeName,
                                              final List<Member> members, final PickedMemberAdapter listMemberAdapter){

        DatabaseReference ref = database.getReference("/"+committeeName+"/myMembers");

        ChildEventListener mylistener;
        mylistener=new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Member member = new Member();
                User user = dataSnapshot.child("user").getValue(User.class);
                member.setCommittee(dataSnapshot.child("committee").getValue(String.class));
                member.setUser(user);
                members.add(member);
                listMemberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for(int i=0 ; i<members.size() ; i++)
                {
                    if(dataSnapshot.getKey().equals(members.get(i).user.getId()))
                    {
                        members.remove(i);
                        listMemberAdapter.notifyDataSetChanged();
                    }
                }
                final List<String> tasksId = new ArrayList<>();
                DatabaseReference databaseReference = database.getReference("/"+committeeName+"/myMembers")
                        .child(dataSnapshot.getKey()).child("tasksId");
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        tasksId.add(dataSnapshot.getKey());
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

                Member member = new Member();
                User user = dataSnapshot.child("user").getValue(User.class);
                member.setCommittee(dataSnapshot.child("committee").getValue(String.class));
                member.setUser(user);
                member.setTasksId(tasksId);
                members.add(member);
                listMemberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for(int i=0 ; i<members.size() ; i++)
                {
                    if(dataSnapshot.getKey().equals(members.get(i).user.getId()))
                    {
                        members.remove(i);
                        listMemberAdapter.notifyDataSetChanged();
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

        return members ;
    }// get all tasks of a committee



}

