package android.support.supportsystem.activities;

import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.supportsystem.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by mahmoud shahen on 11/19/2016.
 */

public class IdDialog extends DialogFragment {
    String ID;
    TextView idtxt;
    Button share;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.id_dialog, container, false);
        idtxt = (TextView)root.findViewById(R.id.idDialogTxt);
        share = (Button)root.findViewById(R.id.idDialogSharebtn);
        idtxt.setText(ID);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Your MemberShip ID is " + ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        return root;
    }

    public void setID(String ID) {
        this.ID = ID;

    }
}
