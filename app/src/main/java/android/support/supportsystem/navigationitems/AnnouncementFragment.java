package android.support.supportsystem.navigationitems;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.supportsystem.Adapter.AnnouncementAdapter;
import android.support.supportsystem.R;
import android.support.supportsystem.activities.sendannouncement;
import android.support.supportsystem.genaric.GenaricData;
import android.support.supportsystem.model.Announcement;
import android.support.supportsystem.model.Head;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahmoud shahen on 10/23/2016.
 */

public class AnnouncementFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    View view;
    Head head=new Head();
    AnnouncementAdapter announcementAdapter;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recList;
    List<Announcement> announcements;
    FloatingActionButton fab;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    String name ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_announcement, container, false);
        firebaseDatabase= FirebaseDatabase.getInstance();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        if(getArguments() != null)
            name = getArguments().getString("name");

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if(GenaricData.A_S_M==0)
            fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"from Announcement fragment", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(),sendannouncement.class);
                intent.putExtra("name", name);
                startActivity(intent);

            }
        });
        ShowAnnouncements();

        return view;
    }

    private void ShowAnnouncements() {
        announcements = new ArrayList<>();
        announcementAdapter=new AnnouncementAdapter(announcements, firebaseDatabase, getContext());
        announcements = head.getAnnouncements(firebaseDatabase, "Android", announcements, announcementAdapter);
        Log.v("Sizeoo", String.valueOf(announcements.size()));
        recList=(RecyclerView) view.findViewById(R.id.announcement_rec_view);
        recList.setAdapter(announcementAdapter);
        //recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        defaultItemAnimator.setRemoveDuration(1000);
        recList.setItemAnimator(defaultItemAnimator);
        recList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if(GenaricData.A_S_M != 0) {
                    if (dy > 0)
                        fab.hide();
                    else if (dy < 0)
                        fab.show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                ShowAnnouncements();
            }
        }, 2000);
    }
}
