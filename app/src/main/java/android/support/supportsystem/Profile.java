package android.support.supportsystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.supportsystem.Adapter.TaskAdapter;
import android.support.supportsystem.genaric.GenaricData;
import android.support.supportsystem.model.Member;
import android.support.supportsystem.model.Task;
import android.support.supportsystem.model.User;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    Member member = null;
    TextView mPosition, mCommitee, mFaculty, mMobile, mEmail, mBirthDate;
    ImageView imageView;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference mStorage;
    FirebaseUser user;
    List<Task> tasks;
    TaskAdapter taskAdapter;
    RecyclerView recyclerView;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseDatabase= FirebaseDatabase.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();
        mStorage = firebaseStorage.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();

        mPosition = (TextView) findViewById(R.id.position);
        mCommitee = (TextView) findViewById(R.id.txtCommittee);
        mFaculty = (TextView) findViewById(R.id.faculty);
        mMobile = (TextView) findViewById(R.id.mobil);
        mEmail = (TextView) findViewById(R.id.mail);
        mBirthDate = (TextView) findViewById(R.id.birthday);
        imageView = (ImageView)findViewById(R.id.cardview_imgmember);
        imageButton = (ImageButton)findViewById(R.id.uploadimage);
        recyclerView = (RecyclerView)findViewById(R.id.profileRecycleTasks);

        member = (Member) getIntent().getSerializableExtra("Member");

        if(member != null) {
            organize();

            if(!user.getUid().equals(member.user.getId())) {
                imageButton.setVisibility(View.GONE);
            }
            if(user.getUid().equals(member.user.getId()) || GenaricData.A_S_M != 0)
                setTasks();
        }
        if(member == null)
        {
            final List<String> tasksId = new ArrayList<>();
            DatabaseReference databaseReference = firebaseDatabase.getReference("/Android/myMembers")
                    .child(user.getUid()).child("tasksId");
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

            DatabaseReference DB = firebaseDatabase.getReference("/Android"+"/myMembers/"+user.getUid());
            DB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    member = new Member();
                    User user1 = dataSnapshot.child("user").getValue(User.class);
                    member.setCommittee(dataSnapshot.child("committee").getValue(String.class));
                    member.setUser(user1);
                    member.setTasksId(tasksId);
                    Log.v("hello", String.valueOf(tasksId.size()));

                    if(!user.getUid().equals(member.user.getId())) {
                        imageButton.setVisibility(View.GONE);
                    }
                    organize();
                    if(user.getUid().equals(member.user.getId()) || GenaricData.A_S_M != 0)
                        setTasks();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getUid().equals(member.user.getId())) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                }
            }
        });
    }

    public void setTasks()
    {
        tasks = new ArrayList<>();
        taskAdapter=new TaskAdapter(tasks, firebaseDatabase, this);
        taskAdapter.setMemberId(member.user.getId());
        //Log.v("Sizeoo", String.valueOf(tasks.size()));
        getTasks();
        recyclerView.setAdapter(taskAdapter);
        //recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        defaultItemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(defaultItemAnimator);
    }
    public void getTasks()
    {
        //Log.v("taska",member.user.getId());

        List<String> data = new ArrayList<>();
        data.add("content");
        data.add("title");
        data.add("deadLine");
        data.add("id");
        data.add("timeStamp");

        DatabaseReference databaseReference = firebaseDatabase.getReference("/Android/myTasks");

        Log.v("sizaa", String.valueOf(member.getTasksId().size()));
        for(int i=0 ; i<member.getTasksId().size() ; i++) {
            Log.v("taska", member.getTasksId().get(i));
            final Task task = new Task();
            for (int j = 0; j < 5; j++) {
                final int tmp = j;

                databaseReference.child(member.getTasksId().get(i)).child(data.get(j)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(tmp==0)
                            task.setContent(dataSnapshot.getValue(String.class));
                        if(tmp==1)
                            task.setTitle(dataSnapshot.getValue(String.class));
                        if(tmp==2) {
                            task.setDeadLine(dataSnapshot.getValue(String.class));
                        }
                        if(tmp==3)
                            task.setId(dataSnapshot.getValue(String.class));

                        if(tmp==4)
                        {
                            task.setTimeStamp(dataSnapshot.getValue(String.class));
                            tasks.add(task);
                        }
//                        Log.v("taska", task.getContent());
                        taskAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }
    public void organize()
    {
        setDataMember();
        // child name can't be null or empty
        mStorage.child(member.user.getId()+"/"+member.user.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.v("link0", String.valueOf(uri));
                Picasso.with(getApplicationContext()).load(uri).fit().centerCrop().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout   = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(member.user.getName());
        Log.v("mooza", member.user.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null)
        {
            final Uri uri = data.getData();

            StorageReference filePath = mStorage.child(user.getUid()).child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    mStorage.child(member.user.getId()+"/"+member.user.getImage()).delete();
                    Log.v("link", String.valueOf(uri.getLastPathSegment()));
                    member.user.setImage(String.valueOf(uri.getLastPathSegment()));
                    DatabaseReference DB = firebaseDatabase.getReference("/"+member.getCommittee()+"/myMembers/"+member.user
                    .getId());
                    DB.child("user").child("image").setValue(member.user.getImage());
                    Picasso.with(getApplicationContext()).load(downloadUri).fit().centerCrop().into(imageView);
                }
            });
        }
    }

    public void setDataMember()
    {
        mPosition.setText(member.user.getPosition());
        mCommitee.setText(member.getCommittee());
        mFaculty.setText(member.user.getFaculty());
        mMobile.setText(member.user.getPhone());
        mEmail.setText(member.user.getEmail());
        mBirthDate.setText(member.user.getBirthDate());
    }

}