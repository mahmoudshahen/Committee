package android.support.supportsystem.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.supportsystem.Profile;
import android.support.supportsystem.R;
import android.support.supportsystem.genaric.GenaricData;
import android.support.supportsystem.genaric.SendNotification;
import android.support.supportsystem.model.Member;
import android.support.supportsystem.model.User;
import android.support.supportsystem.navigationitems.AnnouncementFragment;
import android.support.supportsystem.navigationitems.Charter;
import android.support.supportsystem.navigationitems.Chat;
import android.support.supportsystem.navigationitems.Committee;
import android.support.supportsystem.navigationitems.Ideas;
import android.support.supportsystem.navigationitems.Logout;
import android.support.supportsystem.navigationitems.Vote;
import android.support.supportsystem.navigationitems.home;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment = null;
    private String title="";
    ImageView imageView;
    TextView name_txt;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference mStorage;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = "Tasks";
        getSupportActionBar().setTitle(title);
        firebaseDatabase= FirebaseDatabase.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();
        mStorage = firebaseStorage.getReference();

        Log.v("navega","vvvvvvvv");

        final home h = new home();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, h);
        fragmentTransaction.commit();




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_navigation, null);
        navigationView.addHeaderView(header);
        CardView cardView = (CardView) header.findViewById(R.id.cardView_header);
        imageView = (ImageView) header.findViewById(R.id.imgProfilePicture);
        name_txt = (TextView) header.findViewById(R.id.name_header);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(navigation.this, Profile.class);
                startActivity(intent);
            }
        });
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference DB = firebaseDatabase.getReference("/Android/super");
        DB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user.getUid()).exists()) {
                    GenaricData.A_S_M = 1; // super
                    Log.v("child", "yes");
                }
                else
                    GenaricData.A_S_M = 0; // member
                h.showFab();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
         DB = firebaseDatabase.getReference("/Android"+"/myMembers/"+user.getUid());
        DB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                member = new Member();
                String name = dataSnapshot.child("user").child("name").getValue(String.class);
                String img = dataSnapshot.child("user").child("image").getValue(String.class);
                String id = dataSnapshot.child("user").child("id").getValue(String.class);
                Log.v("img", img);
                User user1 = new User();
                user1.setName(name);
                user1.setImage(img);
                user1.setId(id);
                member.setUser(user1);
                //Log.v("hello",dataSnapshot.getKey());
                mStorage.child(member.user.getId()+"/"+member.user.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.v("link0", String.valueOf(uri));
                        Picasso.with(navigation.this).load(uri).fit().centerCrop().into(imageView);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
                name_txt.setText(member.user.getName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        startService(new Intent(this, SendNotification.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.v("fffff","hey");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home){
            title="Tasks";
            fragment=new home();

        }else if (id == R.id.charter) {
            // Handle the camera action
            title = "Charter";
            fragment = new Charter();
        }
        else if (id == R.id.ideas) {
            title = "Ideas";
            fragment = new Ideas();
        }

        else if (id == R.id.committee) {
            title = "Committee";

            fragment = new Committee();
        }
        else if (id == R.id.announcement) {
            title = "Announcement";
            fragment = new AnnouncementFragment();
            Bundle bundle = new Bundle();
            bundle.putString("name", member.user.getName());
            fragment.setArguments(bundle);
        }
        else if (id == R.id.chat) {
            title = "Chatting";
            fragment = new Chat();
        }
        else if (id == R.id.vote) {
            title = "Vote";
            fragment = new Vote();
        }
        else if (id == R.id.logoutid) {
            title = "Logout";

            fragment=new Logout();
        }

        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        getSupportActionBar().setTitle(title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}