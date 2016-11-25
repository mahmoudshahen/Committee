package android.support.supportsystem.AuthenticationSystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.supportsystem.R;
import android.support.supportsystem.activities.navigation;
import android.support.supportsystem.genaric.GenaricData;
import android.support.supportsystem.model.Member;
import android.support.supportsystem.model.User;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {


    EditText mfirst_name,mLast_name ,mphone  , mFaculty , mId;
    Spinner day, month, year, gender;
    private String marray_gender[] , mArray_days[] ,mArray_months[] , mArray_years[];
    // android, game, web, pr, fr, lr, hr, social media, market, video
    private List<String> Commitiees = new ArrayList<String>();
    Button mDone_btn ;
     Member mNew_Member ;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Firebase.setAndroidContext(this);
        Commitiees.add("Android");
        Commitiees.add("Game");
        Commitiees.add("Web");
        Commitiees.add("PR");
        Commitiees.add("FR");
        Commitiees.add("LR");
        Commitiees.add("HR");
        Commitiees.add("Social Media");
        Commitiees.add("Marketing");
        Commitiees.add("Video Editor");

        set_Date_Data();
        mfirst_name = (EditText) findViewById(R.id.first_name);
        mLast_name  = (EditText) findViewById(R.id.last_name);
        mphone  = (EditText) findViewById(R.id.phone);
        mFaculty  = (EditText) findViewById(R.id.faculty);
        day = (Spinner) findViewById(R.id.days);
        month = (Spinner)findViewById(R.id.months);
        year = (Spinner)findViewById(R.id.years);
        gender = (Spinner)findViewById(R.id.gender);
        mDone_btn  = (Button) findViewById(R.id.done);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SaveSharedPreference.setUserName(InfoActivity.this, mfirst_name.getText().toString().toLowerCase());

                startActivity(new Intent(InfoActivity.this, navigation.class));

                if (user != null) {
                    String uid = user.getUid();
                    Log.v("UIiiiiD", uid);

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("/Android/myMembers/" + GenaricData.Id);
                    databaseReference.removeValue();
                    databaseReference = firebaseDatabase.getReference("/Android/myMembers/" + user.getUid());
                    Instantiate_member();
                    databaseReference.setValue(mNew_Member);
                    if(mNew_Member.user.getPosition().equals("Head"))
                    {
                        databaseReference = firebaseDatabase.getReference("/Android/super");
                        databaseReference.child(mNew_Member.user.getId()).setValue(mNew_Member.user.getId());
                    }
//                    Log.v("sizeoflist", String.valueOf(mNew_Member.getTasksId().size()));
                    finish();
                }

            }
        });

    }
    private  void  Instantiate_member (){
        mNew_Member = new Member();
        mNew_Member.user =  new User();
        //List<String> TasksId = new ArrayList<>();
        // list was null in firebase.
       // TasksId.add("Dummy Data");
        //mNew_Member.setTasksId(TasksId);

        mNew_Member.user.setId(user.getUid());
        mNew_Member.user.setEmail(user.getEmail());
        mNew_Member.user.setName(mfirst_name.getText().toString()+" "+mLast_name.getText().toString());
        mNew_Member.user.setBirthDate(day.getSelectedItem().toString()+"/"+month.getSelectedItem().toString()+"/"+
                                        year.getSelectedItem().toString());
        mNew_Member.user.setImage("-");
        mNew_Member.user.setFaculty(mFaculty.getText().toString());
        mNew_Member.user.setPhone(mphone.getText().toString());
        mNew_Member.user.setGender(gender.getSelectedItem().toString());
        // android, game, web, pr, fr, lr, hr, social media, market, video
        for (int i=0 ; i<Commitiees.size() ; i++)
        {
            if(GenaricData.Id.substring(0,1).equals(Commitiees.get(i).substring(0,1))){
                mNew_Member.setCommittee(Commitiees.get(i));

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(Commitiees.get(i))
                        .build();
                break;
            }
        }
            if(GenaricData.Id.contains("head"))
            {
                mNew_Member.user.setPosition("Head");
            }
            else if(GenaricData.Id.contains("mem"))
            {
                mNew_Member.user.setPosition("Member");
            }

    }

    void set_Date_Data(){
        marray_gender = new String[2];
        marray_gender[0]="Male";
        marray_gender[1]="Female";
        Spinner s = (Spinner) findViewById(R.id.gender);
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,marray_gender);
        s.setAdapter(adapter);



        mArray_days = new String[31];
        mArray_days[0]="Day";
        for(int i=1 ; i<31 ; i++)
            mArray_days[i] = String.valueOf(i);


        Spinner mDays_spinner = (Spinner) findViewById(R.id.days);
        final ArrayAdapter mDays_adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,mArray_days);
        mDays_spinner.setAdapter(mDays_adapter);



        mArray_months = new String[13];
        mArray_months[0] = "Month";mArray_months[1] = "Jan";mArray_months[2] = "Fab";mArray_months[3] = "Mar";
        mArray_months[4] = "Apr";mArray_months[5] = "May";mArray_months[6] = "Jun";mArray_months[7] = "Jul";
        mArray_months[8] = "Aug";mArray_months[9] = "Sept";mArray_months[10] = "Oct";mArray_months[11] = "Nov";
        mArray_months[12] = "Dec";
        Spinner mMonths_spinner = (Spinner) findViewById(R.id.months);
        final ArrayAdapter mMonths_adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,mArray_months);
        mMonths_spinner.setAdapter(mMonths_adapter);
        mArray_years = new String[50];
        mArray_years[0] = "Year";
        for(int i=1 ; i<50 ; i++)
        {
           mArray_years[i]= String.valueOf((i+1970));
        }
        Spinner mYears_spinner = (Spinner) findViewById(R.id.years);
        final ArrayAdapter mYears_adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,mArray_years);
        mYears_spinner.setAdapter(mYears_adapter);





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
