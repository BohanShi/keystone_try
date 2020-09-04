package com.example.keystone_try.ui.dashboard;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.keystone_try.CalTitle;
import com.example.keystone_try.R;
import com.example.keystone_try.step.utils.SPHelper;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;


    Button game1;
    Button num;
    Button cal;
    Button game2;
    Button game3;
    int highScore;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_games, container, false);
        highScore = SPHelper.getInt(getContext(), "HighScore");


        game1 = root.findViewById(R.id.count_game_btn);
        game1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirstTime("Calculation Game Instruction","Welcome to Count Game! In this game, you will need to calculate the equation that appeared on screen. You will get one point if you can answer it correctly, and you can see your current score on top of the screen. The game will continue until you get one wrong answer. You can exit the game in the middle of the game and your current score at the point when you leave will be your final score of the game.", CalTitle.class);

            }
        });

        num = root.findViewById(R.id.number);
        cal= root.findViewById(R.id.calculation);
        num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirstTime("Calculation Game Instruction","Welcome to Count Game! In this game, you will need to calculate the equation that appeared on screen. You will get one point if you can answer it correctly, and you can see your current score on top of the screen. The game will continue until you get one wrong answer. You can exit the game in the middle of the game and your current score at the point when you leave will be your final score of the game.", CalTitle.class);

            }
        });

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirstTime("Calculation Game Instruction","Welcome to Count Game! In this game, you will need to calculate the equation that appeared on screen. You will get one point if you can answer it correctly, and you can see your current score on top of the screen. The game will continue until you get one wrong answer. You can exit the game in the middle of the game and your current score at the point when you leave will be your final score of the game.", CalTitle.class);

            }
        });

        game2 = root.findViewById(R.id.second_game_btn);
        game3 = root.findViewById(R.id.third_game_btn);
        game2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game2.setError("Not available now");
                Toast.makeText(getContext(), "Not available now" , Toast.LENGTH_LONG).show();
            }
        });

        game3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game3.setError("Not available now");
                Toast.makeText(getContext(), "Not available now" , Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }


    public void isFirstTime(final String instructionTitle, String instruction, final Class c){
        if (!(highScore > 1)){
            final TextView tv = new TextView(getContext());
            tv.setText(instruction);
            tv.setTextSize(26);
            tv.setPadding(25,0,15,0);
            new AlertDialog.Builder(getContext()).setTitle(instructionTitle)
                    .setView(tv)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            new AlertDialog.Builder(getContext()).setTitle(instructionTitle)
//                                    .setPositiveButton("Confirm", )

                            Intent intent = new Intent(getActivity(), c);
                            startActivity(intent);
                        }
                    })
                    .show();
        }else {
            Intent intent = new Intent(getActivity(), c);
            startActivity(intent);
        }
    }
}
