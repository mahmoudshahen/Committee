package android.support.supportsystem.genaric;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.supportsystem.Profile;
import android.support.supportsystem.model.Task;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mahmoud shahen on 11/15/2016.
 */

public class SendNotification extends Service{
    int i=000;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(getApplicationContext(), "service started", Toast.LENGTH_LONG).show();
        Log.v("fffff","hooo");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("/Android/myTasks");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    String content = dataSnapshot.child("content").getValue(String.class); // cast child to task
                    String deadline = dataSnapshot.child("deadLine").getValue(String.class);
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String id = dataSnapshot.child("id").getValue(String.class);
                    String timeStamp = dataSnapshot.child("timeStamp").getValue(String.class);
                    Task task = new Task();

                    task.setContent(content);
                    task.setId(id);
                    task.setDeadLine(deadline);
                    task.setTitle(title);
                    task.setTimeStamp(timeStamp);

                    NotificationCompat.Builder mBuilder =
                            (NotificationCompat.Builder) new NotificationCompat.Builder(SendNotification.this)
                                    .setSmallIcon(android.R.drawable.ic_menu_my_calendar)
                                    .setContentTitle(task.getTitle())
                                    .setContentText(task.getContent());

                    Intent resultIntent = new Intent(SendNotification.this, Profile.class);
                    PendingIntent resultPendingIntent =
                            PendingIntent.getActivity(
                                    SendNotification.this,
                                    0,
                                    resultIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );

                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotifyMgr =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    int mNotificationId = ++i;

                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
                    //        Toast.makeText(notify.this, "done", Toast.LENGTH_LONG).show();
                    Log.v("fffff", "heeee");
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
        return super.onStartCommand(intent, flags, startId);
    }
}
