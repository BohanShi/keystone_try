package com.example.keystone_try.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.keystone_try.game1.CalQuestion;
import com.example.keystone_try.R;
import com.example.keystone_try.game2.game_2048;
import com.example.keystone_try.step.utils.SPHelper;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private static final String HIGH_SCORE = "high score";


    RoundedImageView game1;
    RoundedImageView game2;
    RoundedImageView game3;
    int highScore;
    int highScore2;
    ArrayList<Integer> scores;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_games, container, false);
        scores = new ArrayList<>();
        highScore = SPHelper.getInt(getContext(), "OneTimes");
        highScore2 = SPHelper.getInt(getContext(), "TwoTimes");
        scores.add(highScore);
        scores.add(highScore2);
        game1 = root.findViewById(R.id.game1);
        game1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirstTime(R.string.intro, R.string.introCal, CalQuestion.class, 0);
            }
        });


        game2 = root.findViewById(R.id.game2);
        game3 = root.findViewById(R.id.game3);
        game2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isFirstTime(R.string.intro, R.string.intro2048, game_2048.class, 1);
            }
        });

        game3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //game3.setError("Not available now");
                Toast.makeText(getContext(), "Not available now" , Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }


    public void isFirstTime(final int instructionTitle, int instruction, final Class c, int position){
        if (!(scores.get(position) > 1)){
            new AlertDialog.Builder(getContext()).setTitle(instructionTitle)
                    .setIcon(android.R.drawable.ic_menu_help)
                    .setMessage(instruction)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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

    private long get2HighScore() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        return settings.getLong(HIGH_SCORE, -1);
    }
}
