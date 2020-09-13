package com.example.keystone_try.ui.OpenData;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.keystone_try.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static android.content.ContentValues.TAG;


public class OpenData extends AppCompatActivity {

DatabaseReference ref;
TextView a;


    public View onCreate(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_open_data);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_open_data, container, false);

        //insert codes for data extraction
        //a = (TextView)findViewById(R.id.test_text);

        ref = FirebaseDatabase.getInstance().getReference().child("Census").child("1");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String age = Objects.requireNonNull(snapshot.child("Age Group").getValue()).toString();
                a.setText(age);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return root;
    }
}
