package android.support.supportsystem.activities;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.supportsystem.Adapter.PickedMemberAdapter;
import android.support.supportsystem.R;
import android.support.supportsystem.genaric.GenaricData;
import android.support.supportsystem.model.Head;
import android.support.supportsystem.model.Member;
import android.support.supportsystem.model.PickedMembers;
import android.support.supportsystem.model.Task;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahmoud shahen on 11/16/2016.
 */

public class PickedMemberDialog extends DialogFragment {
    FirebaseStorage firebaseStorage;
    StorageReference mStorage;
    public FirebaseDatabase mFirebase  ;
    DatabaseReference databaseReference;
    public List<Member> members ;
    RecyclerView recList;
    PickedMemberAdapter pickedMemberAdapter;
    Task task;
    Head head;
    boolean checkAll;
    static List<String> pickedID;
    Button sendButton, cancelButton;
    CheckBox selectAll;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dialog, container, false);

        checkAll = false;
        sendButton = (Button)root.findViewById(R.id.sendTask);
        cancelButton = (Button)root.findViewById(R.id.cancel);
        selectAll = (CheckBox) root.findViewById(R.id.selectAll_checkBox);

        mFirebase = FirebaseDatabase.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();
        mStorage = firebaseStorage.getReference();
        databaseReference = mFirebase.getReference("/Android/myMembers");
        head=new Head();
        pickedID = new ArrayList<>();
        members =  new ArrayList<Member>();

        pickedMemberAdapter = new PickedMemberAdapter(members, mFirebase, firebaseStorage, mStorage, getContext());

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<PickedMembers> assignedMembers = new ArrayList<PickedMembers>();
                for(int i=0 ; i<pickedID.size() ; i++)
                {
                    PickedMembers pickedMembers = new PickedMembers();
                    pickedMembers.setMemberId(pickedID.get(i));
                    pickedMembers.setDelivered(false);
                    pickedMembers.setDeliveryTime("--");
                    pickedMembers.setContent("");
                    assignedMembers.add(pickedMembers);
                }

                task.setAssignedMembers(assignedMembers);
                head.sendTask(mFirebase, task, "/Android/myTasks", pickedID);
                getDialog().dismiss();
            }
        });
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(selectAll.isChecked())
                   checkAll = true;
                else
                   checkAll = false;

                pickedMemberAdapter = new PickedMemberAdapter(members, mFirebase, firebaseStorage, mStorage, getContext());
                pickedMemberAdapter.setCheckeAll(checkAll);
                pickedMemberAdapter.notifyDataSetChanged();
                recList.setAdapter(pickedMemberAdapter);
                DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
                defaultItemAnimator.setAddDuration(1000);
                defaultItemAnimator.setRemoveDuration(1000);
                recList.setItemAnimator(defaultItemAnimator);
                Log.v("check", String.valueOf(checkAll));
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        members = GenaricData.getPickMembers(mFirebase, "Android", members, pickedMemberAdapter);

        recList=(RecyclerView) root.findViewById(R.id.recyclerView_pickMember);
        recList.setAdapter(pickedMemberAdapter);
        //recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        defaultItemAnimator.setRemoveDuration(1000);
        recList.setItemAnimator(defaultItemAnimator);
        return root;
    }
    public void setTask(Task task)
    {
        this.task = task;
    }

    public static void setPickedID(String ID)
    {
        pickedID.add(ID);
    }
    public static void delPickID(String ID)
    {
        if(pickedID.contains(ID))
            pickedID.remove(ID);
    }
}
