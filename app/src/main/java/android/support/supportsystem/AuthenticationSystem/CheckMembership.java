package android.support.supportsystem.AuthenticationSystem;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.supportsystem.R;
import android.support.supportsystem.genaric.GenaricData;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckMembership extends AppCompatActivity {


    FirebaseDatabase  firebaseDatabase ;
    Button btn_check   , btn_back ;
    EditText id_txt  ;
     private ProgressBar mProgressBar  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_membership);
        btn_back = (Button) findViewById(R.id.back_btn);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn_check = (Button) findViewById(R.id.checked_btn);
        id_txt = (EditText) findViewById(R.id.checked_Id);
        firebaseDatabase = FirebaseDatabase.getInstance();
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                 finish();
            }
        });

        btn_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn_back.setElevation(5);
                }
                return false;
            }
        });
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GenaricData.Id  = id_txt.getText().toString() ;
                DatabaseReference reference = firebaseDatabase.getReference("/Android/myMembers");

                if(id_txt.getText().toString().isEmpty())
                    Toast.makeText(CheckMembership.this, "Please Enter Key", Toast.LENGTH_LONG).show();

                else {
                    mProgressBar.setVisibility(View.VISIBLE);

                    reference.child(GenaricData.Id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // TODO: handle the case where the data already exists
                                Log.v("Checked Id ", " exists ");
                                startActivity(new Intent(CheckMembership.this, SignupActivity.class));
                                finish();
                            } else {
                                // TODO: handle the case where the data does not yet exist
                                Log.v("Checked Id ", "Nooooot  exists ");
                                mProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Invaild ID", Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }
}
