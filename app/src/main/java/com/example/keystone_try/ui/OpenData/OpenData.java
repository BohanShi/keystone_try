package com.example.keystone_try.ui.OpenData;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.keystone_try.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.text.method.LinkMovementMethod.getInstance;

public class OpenData extends Fragment {

    //insert codes for data extraction

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Census");
    List<String> census = new ArrayList<String>();
//    List<Object> age_group = new
//    class abc {
//        private String ageGroup;
//        public abc(String ageGroup){
//            this.ageGroup = ageGroup;
//        }
//    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupHyperlink(view);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_open_data, container, false);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //List<String> keys = new ArrayList<>();
//                for (DataSnapshot keyNode : snapshot.getChildren()) {
//                    //keys.add(keyNode.getKey());
//                    //Log.d(String.valueOf(keyNode.getValue()),"None");
//                    String census_new = String.valueOf(keyNode.getValue());
//                    census.add(census_new);
//                }
                //Log.d(String.valueOf(census), "Output_test");
                for (DataSnapshot keyNode : snapshot.getChildren())
                {
                    String ageGroup = (String) keyNode.child("Age_Group").getValue();


                   Log.i("Value of element ",ageGroup);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //ref= null;
        return root;
    }

    private void setupHyperlink(View v)  {

        TextView link = v.findViewById(R.id.OD_Content5);
       //link.setText(Html.fromHtml(getString(R.string.OD_link01)));
        link.setMovementMethod(LinkMovementMethod.getInstance());


    }


}
