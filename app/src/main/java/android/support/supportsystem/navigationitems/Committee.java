package android.support.supportsystem.navigationitems;


import android.os.Bundle;
import android.support.supportsystem.Adapter.List_Member_Adapter;
import android.support.supportsystem.R;
import android.support.supportsystem.activities.IdDialog;
import android.support.supportsystem.genaric.GenaricData;
import android.support.supportsystem.model.Head;
import android.support.supportsystem.model.Member;
import android.support.supportsystem.model.User;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class Committee extends Fragment {

    private Head mhead;
    private List<Member> members;
    public static Member mNew_Member;
    private FirebaseDatabase mFirebase;
    RecyclerView ListMembersrecycleview;
    List_Member_Adapter listMemberAdapter;
    private List<Member> filterList;
    FirebaseStorage firebaseStorage;
    StorageReference mStorage;

    FloatingActionButton createMember, pendingMember, currentMembers, All;
    FloatingActionMenu floatingActionMenu;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        members = new ArrayList<Member>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_committee, container, false);
        floatingActionMenu = (FloatingActionMenu) root.findViewById(R.id.material_design_android_floating_action_menu);
        createMember = (FloatingActionButton) root.findViewById(R.id.material_design_floating_action_menu_item1);
        pendingMember = (FloatingActionButton) root.findViewById(R.id.material_design_floating_action_menu_item2);
        currentMembers = (FloatingActionButton)root.findViewById(R.id.material_design_floating_action_menu_item3);
        All = (FloatingActionButton)root.findViewById(R.id.material_design_floating_action_menu_item4);
        mhead = new Head();
        mNew_Member = new Member();
        mFirebase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        mStorage = firebaseStorage.getReference();
        if(GenaricData.A_S_M==0)
            floatingActionMenu.setVisibility(View.GONE);

        createMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = GenaricData.get_Id("Android", true); // true to create member
                String Url = "/Android/myMembers/" + id;
                FragmentManager manager = getFragmentManager();

                IdDialog idDialog = new IdDialog();
                idDialog.setID(id);
                idDialog.show(manager, null);

                Instantiate_member(id);
                mhead.Add_Member(mFirebase, mNew_Member, Url);
                listMemberAdapter.notifyDataSetChanged();

            }
        });
        pendingMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterList = new ArrayList<Member>();
                for(int i=0 ; i<members.size() ; i++)
                {
                    if(members.get(i).getCommittee().equals("Waiting..."))
                        filterList.add(members.get(i));
                }
                listMemberAdapter = new List_Member_Adapter(filterList, mFirebase, firebaseStorage, mStorage, getActivity());
                ListMembersrecycleview.setAdapter(listMemberAdapter);

            }
        });
        currentMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterList = new ArrayList<Member>();
                for(int i=0 ; i<members.size() ; i++)
                {
                    if(!members.get(i).getCommittee().equals("Waiting..."))
                        filterList.add(members.get(i));
                }
                listMemberAdapter = new List_Member_Adapter(filterList, mFirebase, firebaseStorage, mStorage, getActivity());
                ListMembersrecycleview.setAdapter(listMemberAdapter);
            }
        });
        All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listMemberAdapter = new List_Member_Adapter(members, mFirebase, firebaseStorage, mStorage, getActivity());
                ListMembersrecycleview.setAdapter(listMemberAdapter);
            }
        });

        listMemberAdapter = new List_Member_Adapter(members, mFirebase, firebaseStorage, mStorage, getActivity());

        members = GenaricData.getMembers(mFirebase, "Android", members, listMemberAdapter);

        ListMembersrecycleview = (RecyclerView) root.findViewById(R.id.list_members);
        ListMembersrecycleview.setAdapter(listMemberAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ListMembersrecycleview.setLayoutManager(linearLayoutManager);

        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        defaultItemAnimator.setRemoveDuration(1000);
        ListMembersrecycleview.setItemAnimator(defaultItemAnimator);
        return root;
    }

    private void Instantiate_member(String id) {
        mNew_Member = new Member();
        mNew_Member.user = new User();
        //List<String> tasks= new ArrayList<>();
        //tasks.add("Dummy Data");
        //mNew_Member.setTasksId(tasks);
        mNew_Member.user.setId(id);
        mNew_Member.user.setEmail("Waiting...");
        mNew_Member.user.setImage("-");
        mNew_Member.user.setName(id);
        mNew_Member.user.setBirthDate("Waiting...");
        mNew_Member.user.setFaculty("Waiting...");
        mNew_Member.user.setPhone("Waiting...");
        mNew_Member.user.setGender("Waiting...");
        mNew_Member.setCommittee("Waiting...");
    }
}