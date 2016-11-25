package android.support.supportsystem.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.supportsystem.Adapter.CommentAdapter;
import android.support.supportsystem.R;
import android.support.supportsystem.model.Comment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SubmittedTask extends AppCompatActivity {

    TextView date, delivered, content;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    ImageButton send;
    EditText commentContent;
    String id;
    CommentAdapter commentAdapter;
    List<Comment> comments;
    RecyclerView recyclerView;
    boolean vis;
     String name, userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted_task);
        date = (TextView)findViewById(R.id.date);
        delivered = (TextView)findViewById(R.id.delivered);
        content = (TextView)findViewById(R.id.content);
        send = (ImageButton) findViewById(R.id.sendComment);
        commentContent = (EditText) findViewById(R.id.contentComment);
        recyclerView = (RecyclerView)findViewById(R.id.commentRecycle);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("/Android/myMembers/"+user.getUid()+"/user");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userID = getIntent().getStringExtra("userid");
        id = getIntent().getStringExtra("id");


        vis = getIntent().getBooleanExtra("vis", false);
        Log.v("check", String.valueOf(vis));

    if(!vis) {
         databaseReference = firebaseDatabase.getReference("/Android/myTasks")
                .child(id).child("assignedMembers").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                content.setText(dataSnapshot.child("content").getValue(String.class));
                date.setText(dataSnapshot.child("deliveryTime").getValue(String.class));
                boolean b = dataSnapshot.child("delivered").getValue(boolean.class);
                if (!b) {
                    delivered.setText("Not Delivered");
                    delivered.setTextColor(SubmittedTask.this.getResources().getColor(R.color.red));
                }
                if (b) {
                    delivered.setText("Delivered");
                    delivered.setTextColor(SubmittedTask.this.getResources().getColor(R.color.green));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
        else{
            databaseReference = firebaseDatabase.getReference("/Android/myAnnouncements").child(id);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Log.v("check",dataSnapshot.child("content").getValue(String.class));
                    content.setText(dataSnapshot.child("content").getValue(String.class));
                    date.setText(dataSnapshot.child("title").getValue(String.class));
                    delivered.setText(dataSnapshot.child("owner").getValue(String.class));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!commentContent.getText().toString().isEmpty()) {
                    Comment comment = new Comment();
                    comment.setComment(commentContent.getText().toString());
                    comment.setOwner(name);
                    setComment(id, comment);
                    commentContent.setText("");
                }
                else
                    Toast.makeText(SubmittedTask.this, "Comment is Empty", Toast.LENGTH_LONG).show();

                    View v = SubmittedTask.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

            }
        });
        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(comments);
        getComments(id);

        recyclerView.setAdapter(commentAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(2000);
        defaultItemAnimator.setRemoveDuration(2000);
        recyclerView.setItemAnimator(defaultItemAnimator);
    }
    public void setComment(String id, Comment comment)
    {
        if(!vis) {
            DatabaseReference databaseReference = firebaseDatabase.getReference("/Android/myTasks/" + id + "/assignedMembers/" + userID + "/comments");
            String pushid = databaseReference.push().getKey();
            databaseReference.child(pushid).setValue(comment);
        }
        else
        {
            DatabaseReference databaseReference = firebaseDatabase.getReference("/Android/myAnnouncements/"+id+"/comments");
            String pushid = databaseReference.push().getKey();
            databaseReference.child(pushid).setValue(comment);
        }
    }

    public void getComments(String id)
    {
        if(!vis) {
            DatabaseReference databaseReference = firebaseDatabase.getReference("/Android/myTasks/" + id + "/assignedMembers/" + userID + "/comments");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    comments.add(dataSnapshot.getValue(Comment.class));
                    commentAdapter.notifyDataSetChanged();
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
        }
        else
        {
            DatabaseReference databaseReference = firebaseDatabase.getReference("/Android/myAnnouncements/"+id+"/comments");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    comments.add(dataSnapshot.getValue(Comment.class));
                    commentAdapter.notifyDataSetChanged();
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
        }
    }


}
