package android.support.supportsystem.activities;

import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.supportsystem.R;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class Reply extends AppCompatActivity {

    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    EditText editText;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        submit = (Button) findViewById(R.id.submit);
        editText = (EditText)findViewById(R.id.reply);

        String id = getIntent().getStringExtra("id");

        final DatabaseReference databaseReference = firebaseDatabase.getReference("/Android/myTasks")
                .child(id).child("assignedMembers").child(user.getUid());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("content").setValue(editText.getText().toString());
                databaseReference.child("delivered").setValue(true);
                databaseReference.child("deliveryTime").setValue(java.text.DateFormat.getDateTimeInstance()
                        .format(Calendar.getInstance().getTime()));
                finish();
            }
        });


    }
}
