package android.support.supportsystem.activities;

import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.supportsystem.R;
import android.support.supportsystem.model.Task;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class sendtask extends AppCompatActivity {

    private String title;
    private  String content;
    private Task task;
    EditText taskContent, taskDeadLine, taskTitle;
    private FirebaseDatabase firebaseDatabase;
    Button PickMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendtask);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mywelcomebar_three);
        toolbar.setTitle("Send Task");

        taskContent=(EditText)findViewById(R.id.edittextcontentid);
        taskTitle=(EditText)findViewById(R.id.edittexttitleid);
        taskDeadLine=(EditText)findViewById(R.id.edittextdeadLineid);

        PickMember = (Button)findViewById(R.id.PickID);
        firebaseDatabase=FirebaseDatabase.getInstance();

        PickMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.FragmentManager manager = getFragmentManager();
                SendTaskMethod();
                PickedMemberDialog pickedMemberDialog = new PickedMemberDialog();
                pickedMemberDialog.setTask(task);
                pickedMemberDialog.show(manager, null);
            }
        });
    }
    private void SendTaskMethod(){
            task=new Task();
            task.setContent(taskContent.getText().toString());
            task.setDeadLine(taskDeadLine.getText().toString());
            task.setId("tmp");
            task.setTitle(taskTitle.getText().toString());
            task.setTimeStamp(DateFormat.getDateTimeInstance().format(new Date()));

            Toast.makeText(sendtask.this,"from send task method",Toast.LENGTH_SHORT).show();

    }
}