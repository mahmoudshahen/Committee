package android.support.supportsystem.activities;

import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.supportsystem.R;
import android.support.supportsystem.model.Announcement;
import android.support.supportsystem.model.Head;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class sendannouncement extends AppCompatActivity {
    Button sendAnnouncement;
    EditText editTextContent, editTextTitle;
    Head head = new Head();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendannouncement);
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        sendAnnouncement = (Button)findViewById(R.id.broadcastid);
        editTextContent = (EditText)findViewById(R.id.edittextcontentannouncementid);
        editTextTitle = (EditText)findViewById(R.id.edittexttitleannouncementid);

        final String name = getIntent().getStringExtra("name");
        sendAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!editTextContent.getText().toString().isEmpty())
                {
                    Announcement announcement = new Announcement();
                    announcement.setContent(editTextContent.getText().toString());
                    announcement.setTitle(editTextTitle.getText().toString());
                        announcement.setTimeStamp(String.valueOf(DateFormat.getDateTimeInstance().format(new Date())));

                    announcement.setOwner(name);
                    head.sendAnnouncement(firebaseDatabase, announcement,"/Android/myAnnouncements");
                    Toast.makeText(sendannouncement.this, "announcement broadcasted", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                    Toast.makeText(sendannouncement.this, "Content is Empty", Toast.LENGTH_LONG).show();

            }
        });
    }
}
