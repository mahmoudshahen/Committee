package android.support.supportsystem.navigationitems;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.supportsystem.Adapter.TaskAdapter;
import android.support.supportsystem.R;
import android.support.supportsystem.activities.sendtask;
import android.support.supportsystem.genaric.GenaricData;
import android.support.supportsystem.model.Head;
import android.support.supportsystem.model.Task;
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

 */
public class home extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    Head head=new Head();
    TaskAdapter taskAdapter;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recList;
    View view;

    List<Task> tasks ;
    FloatingActionButton fab;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        if(GenaricData.A_S_M == 0)
            fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"from Home fragment",Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(getContext(),sendtask.class);
                startActivity(intent);

            }
        });

        ShowTasks();

        return view;
    }

    public void ShowTasks()
    {
        tasks = new ArrayList<>();
        taskAdapter=new TaskAdapter(tasks, firebaseDatabase, getContext());
        tasks = head.getTasks(firebaseDatabase, "Android", tasks, taskAdapter);
        Log.v("Sizeoo", String.valueOf(tasks.size()));
        recList=(RecyclerView) view.findViewById(R.id.task_rec_view);
        recList.setAdapter(taskAdapter);
        //recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
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
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        defaultItemAnimator.setRemoveDuration(1000);
        recList.setItemAnimator(defaultItemAnimator);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                ShowTasks();
            }
        }, 2000);
    }
    public void showFab()
    {
        if(GenaricData.A_S_M == 0)
            fab.setVisibility(View.GONE);
    }
}