package android.support.supportsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.supportsystem.Adapter.MemberTaskDetailsAdapter;
import android.support.supportsystem.R;
import android.support.supportsystem.model.Member;
import android.support.supportsystem.model.PickedMembers;
import android.support.supportsystem.model.Task;
import android.support.supportsystem.model.User;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class TaskDetails extends AppCompatActivity {

    TextView title, deadLine, content;
    RecyclerView recyclerView;
    MemberTaskDetailsAdapter memberTaskDetailsAdapter;
    List<Member> members;
    FirebaseStorage firebaseStorage;
    StorageReference mStorage;
    private FirebaseDatabase mFirebase;
    FirebaseUser user;
    Task task;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        mFirebase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        mStorage = firebaseStorage.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        task = (Task) getIntent().getSerializableExtra("Task");

        DatabaseReference db = mFirebase.getReference("/Android/myTasks").child(task.getId()).child("assignedMembers");
        final List<PickedMembers> pickedMembersList = new ArrayList<>();
        //Log.v("xxxx", dataSnapshot.getKey());
        final long[] childrencount = new long[1];
        DatabaseReference dbtmp = mFirebase.getReference("/Android/myTasks").child(task.getId()).child("assignedMembers");
        dbtmp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                childrencount[0] = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.TaskMember);
        title = (TextView) findViewById(R.id.TaskTitle);
        deadLine = (TextView) findViewById(R.id.TaskDeadLine);
        content = (TextView) findViewById(R.id.TaskContent);
        fab = (FloatingActionButton) findViewById(R.id.fabSendTask);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskDetails.this, Reply.class);
                intent.putExtra("id", task.getId());
                startActivity(intent);
            }
        });
        title.setText(task.getTitle());
        deadLine.setText(task.getDeadLine());
        content.setText(task.getContent());

        members = new ArrayList<>();

        memberTaskDetailsAdapter = new MemberTaskDetailsAdapter(members, mFirebase,task, user.getUid(), this,firebaseStorage, mStorage );
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PickedMembers pickedMembers = new PickedMembers();

                pickedMembers.setContent(dataSnapshot.child("content").getValue(String.class));
                pickedMembers.setDelivered(dataSnapshot.child("delivered").getValue(Boolean.class));
                pickedMembers.setDeliveryTime(dataSnapshot.child("deliveryTime").getValue(String.class));
                pickedMembers.setMemberId(dataSnapshot.child("memberId").getValue(String.class));

                pickedMembersList.add(pickedMembers);
              //  Log.v("xxxx", dataSnapshot.child("content").getValue(String.class));
                if(dataSnapshot.getKey().equals(user.getUid())) {
                    fab.setVisibility(View.VISIBLE);
                    memberTaskDetailsAdapter.setDraw(true);
                }
                if(pickedMembersList.size() == childrencount[0])
                {
                    task.setAssignedMembers(pickedMembersList);

                    getSpecificDataMember();
                }
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

        recyclerView.setAdapter(memberTaskDetailsAdapter);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        defaultItemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(defaultItemAnimator);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }
    public void getSpecificDataMember()
    {
        final List<String> data = new ArrayList<>();
        data.add("name");
        data.add("id");
        data.add("image");
        DatabaseReference databaseReference = mFirebase.getReference("/Android/myMembers");
        if(task.getAssignedMembers()!= null) {
            for (int i = 0; i < task.getAssignedMembers().size(); i++) {
                Log.v("zzz", String.valueOf(task.getAssignedMembers().size()));
                final Member member = new Member();
                member.user = new User();
                for (int j = 0; j < 3; j++) {
                    final int tmp = j;
                    Log.v("zzz", task.getAssignedMembers().get(i).getMemberId()+"  "+ data.get(j));
                    databaseReference.child(task.getAssignedMembers().get(i).getMemberId()).child("user").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.v("zzz", String.valueOf(dataSnapshot.child(data.get(tmp))));

                            if (tmp == 0)
                                member.user.setName(dataSnapshot.child(data.get(tmp)).getValue(String.class));
                            if (tmp == 1)
                                member.user.setId(dataSnapshot.child(data.get(tmp)).getValue(String.class));
                            if (tmp == 2) {
                                member.user.setImage(dataSnapshot.child(data.get(tmp)).getValue(String.class));

                                members.add(member);

                            }
                            memberTaskDetailsAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        }
    }
}
