package android.support.supportsystem.navigationitems;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.supportsystem.AuthenticationSystem.LoginActivity;
import android.support.supportsystem.AuthenticationSystem.SaveSharedPreference;
import android.support.supportsystem.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class Logout extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearUserName(getContext());
        Firebase.setAndroidContext(getContext());
        FirebaseAuth.getInstance().signOut();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        startActivity(new Intent(getActivity(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        getActivity().finish();

    }
    public  void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = SaveSharedPreference.getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout, container, false);

    }

}