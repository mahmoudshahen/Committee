package android.support.supportsystem.AuthenticationSystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.supportsystem.R;
import android.support.supportsystem.activities.navigation;
import android.support.supportsystem.genaric.GenaricData;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText mInputEmail, mInputPassword;
    private FirebaseAuth mAuth;
    private ProgressBar mprogressBar;
    private Button mSignup_btn, mLogin_btn, mReset_btn;
    private FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        if(SaveSharedPreference.getUserName(LoginActivity.this).length()> 0)
        {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
            GenaricData.A_S_M = pref.getInt("who", 0);
            startActivity(new Intent(LoginActivity.this, navigation.class));
            finish();
        }


        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();



        if (mAuth.getCurrentUser() != null) {
        //    startActivity(new Intent(LoginActivity.this, MainActivity.class));
          //  finish();
            Toast.makeText(LoginActivity.this, "You are now logged in", Toast.LENGTH_SHORT).show();
        }

        // set the view now
        setContentView(R.layout.activity_login);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        mInputEmail = (EditText) findViewById(R.id.email);
        mInputPassword = (EditText) findViewById(R.id.password);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        mSignup_btn = (Button) findViewById(R.id.btn_sign_up);
        mLogin_btn = (Button) findViewById(R.id.btn_login);
        mReset_btn = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        mSignup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CheckMembership.class));
            }
        });

        mReset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(LoginActivity.this,Forgot_Password.class));
            }
        });

        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mInputEmail.getText().toString();
                final String password = mInputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mprogressBar.setVisibility(View.VISIBLE);

                //authenticate user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        mInputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    SaveSharedPreference.setUserName(LoginActivity.this, email.toLowerCase());
                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    DatabaseReference DB = firebaseDatabase.getReference("/Android/super");
                                    DB.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            mprogressBar.setVisibility(View.GONE);
                                            if(dataSnapshot.child(user.getUid()).exists()) {
                                                GenaricData.A_S_M = 1; // super
                                                Log.v("child", "yes");
                                            }
                                            else
                                                GenaricData.A_S_M = 0; // member
                                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putInt("who", GenaricData.A_S_M);
                                            editor.commit();

                                            Intent intent = new Intent(LoginActivity.this, navigation.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }
                        });
            }
        });
    }
}