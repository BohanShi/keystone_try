package com.example.keystone_try;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.keystone_try.bean.GameOneScore;
import com.example.keystone_try.step.utils.DbUtils;
import com.example.keystone_try.step.utils.SPHelper;
import com.example.keystone_try.views.BaseActivity;

import java.util.List;

public class CalTitle extends BaseActivity {
    Button enter;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_title);
        enter = findViewById(R.id.cal_enter_question);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(CalTitle.this, CalQuestion.class);
                startActivity(it);
            }
        });

        textView = findViewById(R.id.textView);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(this, "jingzhi");
        }
        List<GameOneScore> scoreList = DbUtils.getQueryAll(GameOneScore.class);
        int highScore = SPHelper.getInt(this, "HighScore");
        for (int i=0; i<scoreList.size(); i++) {
            if (scoreList.get(i).getScore() > highScore) {
                highScore = scoreList.get(i).getScore();
            }
        }
        SPHelper.putInt(this, "HighScore", highScore);
        textView.setText("High Score: "+highScore);
    }
}
