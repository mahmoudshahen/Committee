package android.support.supportsystem.navigationitems;


import android.content.Context;
import android.os.Bundle;
import android.support.supportsystem.Adapter.CommentAdapter;
import android.support.supportsystem.R;
import android.support.supportsystem.model.Comment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
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

/**
 * Created by mahmoud shahen on 11/22/2016.
 */

public class Chat extends Fragment {
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    CommentAdapter commentAdapter;
    List<Comment> comments;
    RecyclerView recyclerView;
    ImageButton send;
    EditText contentMessage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.charRecycle);
        send = (ImageButton) root.findViewById(R.id.sendmessage);
        contentMessage = (EditText) root.findViewById(R.id.contentmessage);
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String[] name = new String[1];
        DatabaseReference databaseReference = firebaseDatabase.getReference("/Android/myMembers/"+user.getUid()+"/user");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name[0] = dataSnapshot.child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(comments);
        getComments();

        recyclerView.setAdapter(commentAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        defaultItemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(defaultItemAnimator);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!contentMessage.getText().toString().isEmpty()) {
                    Comment comment = new Comment();
                    comment.setComment(contentMessage.getText().toString());
                    comment.setOwner(name[0]);
                    setComment(comment);
                    contentMessage.setText("");
                }
                else
                    Toast.makeText(getContext(), "Message is Empty", Toast.LENGTH_LONG).show();

                View v = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        return root;

    }

    public void setComment(Comment comment)
    {
            DatabaseReference databaseReference = firebaseDatabase.getReference("/Android/chat");
            String pushid = databaseReference.push().getKey();
            databaseReference.child(pushid).setValue(comment);
    }

    public void getComments()
    {

            DatabaseReference databaseReference = firebaseDatabase.getReference("/Android/chat");
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
