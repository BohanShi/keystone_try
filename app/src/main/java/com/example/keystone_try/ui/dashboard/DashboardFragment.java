package com.example.keystone_try.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.keystone_try.CalTitle;
import com.example.keystone_try.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;


    Button game1;
    Button num;
    Button cal;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_games, container, false);


        game1 = root.findViewById(R.id.count_game_btn);
        game1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CalTitle.class);
                startActivity(intent);
            }
        });

        num = root.findViewById(R.id.number);
        cal= root.findViewById(R.id.calculation);
        num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CalTitle.class);
                startActivity(intent);
            }
        });

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CalTitle.class);
                startActivity(intent);
            }
        });

        return root;
    }
}
