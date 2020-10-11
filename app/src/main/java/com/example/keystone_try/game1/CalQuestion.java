package com.example.keystone_try.game1;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.keystone_try.R;
import com.example.keystone_try.bean.GameOneScore;
import com.example.keystone_try.step.utils.DbUtils;
import com.example.keystone_try.step.utils.SPHelper;
import com.example.keystone_try.views.BaseActivity;

import java.util.Random;

public class CalQuestion extends AppCompatActivity implements View.OnClickListener {

    //Create the Buttons for Game 1 - Calculation Game interface
    private TextView answerTv;
    private TextView scoreTv;
    private TextView operator;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button btn0;
    private Button btnClear;
    private Button btnOK;
    private Button help;
    private int num1;
    private int num2;
    private Random random;
    private TextView num1Tv;
    private TextView num2Tv;
    private int score = 0;
    private TextView highScoreTV;
    private TextView timeCounterTV;
    private boolean gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {                                            // set listener and initial

        super.onCreate(savedInstanceState);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_cal_question);
        answerTv = findViewById(R.id.textViewResult);
        operator = findViewById(R.id.operator);
        btn0 = findViewById(R.id.btn_0);
        btn0.setOnClickListener(this);
        btn1 = findViewById(R.id.btn_1);
        btn1.setOnClickListener(this);
        btn2 = findViewById(R.id.btn_2);
        btn2.setOnClickListener(this);
        btn3 = findViewById(R.id.btn_3);
        btn3.setOnClickListener(this);
        btn4 = findViewById(R.id.btn_4);
        btn4.setOnClickListener(this);
        btn5 = findViewById(R.id.btn_5);
        btn5.setOnClickListener(this);
        btn6 = findViewById(R.id.btn_6);
        btn6.setOnClickListener(this);
        btn7 = findViewById(R.id.btn_7);
        btn7.setOnClickListener(this);
        btn8 = findViewById(R.id.btn_8);
        btn8.setOnClickListener(this);
        btn9 = findViewById(R.id.btn_9);
        btn9.setOnClickListener(this);
        btnClear = findViewById(R.id.btn_clean);
        btnClear.setOnClickListener(this);
        btnOK = findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(this);
        num1Tv = findViewById(R.id.num1);
        num2Tv = findViewById(R.id.num2);
        scoreTv = findViewById(R.id.textView_score);
        timeCounterTV = findViewById(R.id.timeCounter);
        help = findViewById(R.id.game1_help_btn);
        help.setOnClickListener(this);
        highScoreTV = findViewById(R.id.cal_ques_hg_scor);
        gameOver = false;

        Intent it = getIntent();

        String highScore = it.getStringExtra("highScore");
        //highScoreTV.setText(SPHelper.getInt(this, "HighScore"+""));
        initData();
        startCounter();
    }

    /**
     * get all data
     */
    private void initData() {
        getRandomNumber();

        score = SPHelper.getInt(this, "currentScore");
        //highScoreTV.setText(SPHelper.getInt(this, "HighScore"+""));
        int highScore = SPHelper.getInt(this, "HighScore");
        scoreTv.setText(score + "");
        highScoreTV.setText(highScore+"");
    }

    /**
     *  start counter
     */
    private void startCounter() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeCounterTV.setText(millisUntilFinished/1000 + "");
            }

            @Override
            public void onFinish() {
                timeCounterTV.setText("0");
                gameOver = true;

                int oneTimes = SPHelper.getInt(getApplicationContext(), "OneTimes");
                oneTimes++;
                SPHelper.putInt(getApplicationContext(), "OneTimes", oneTimes);

                int highScore = SPHelper.getInt(getApplicationContext(), "HighScore");
                //highScoreTV.setText(SPHelper.getInt(this, "HighScore"));
                GameOneScore gameOneScore = new GameOneScore();
                gameOneScore.setScore(score);
                DbUtils.insert(gameOneScore);
                /**
                 * save the score
                 */
                if (score > highScore) {
                    SPHelper.putInt(getApplicationContext(), "HighScore", score);
                    Intent it = new Intent(CalQuestion.this, SuccessActivity.class);

                    it.putExtra("Score", score);
                    score = 0;
                    startActivity(it);
                } else {
                    Intent it = new Intent(CalQuestion.this, FailActivity.class);

                    it.putExtra("Score", score);
                    score = 0;
                    startActivity(it);
                }
            }
        }.start();
    }

    /**
     * get random numbers and set operator
     */
    private void getRandomNumber() {
        random = new Random();
        num1 = random.nextInt(20);
        num2 = random.nextInt(20);
        if (num1 > num2){
            operator.setText("-");
        }
        else {
            operator.setText("+");
        }
        num1Tv.setText(num1+"");
        num2Tv.setText(num2+"");
        answerTv.setText("0");
    }

    /**
    set on click event
     */
    @Override
    public void onClick(View v) {
        String input = answerTv.getText().toString();
        switch (v.getId()) {
            case R.id.btn_0:
            {
                if (input != "0") {
                    input = input+"0";
                }
                answerTv.setText(input);
            }
            break;
            case R.id.btn_1:
            {
                if (input == "0") {
                    input = "1";
                }
                else {
                    input = input + "1";
                }
                answerTv.setText(input);
            }
            break;
            case R.id.btn_2:
            {
                if (input == "0") {
                    input = "2";
                }
                else {
                    input = input + "2";
                }
                answerTv.setText(input);
            }
            break;
            case R.id.btn_3:
            {
                if (input == "0") {
                    input = "3";
                }
                else {
                    input = input + "3";
                }
                answerTv.setText(input);
            }
            break;
            case R.id.btn_4:
            {
                if (input == "0") {
                    input = "4";
                }
                else {
                    input = input + "4";
                }
                answerTv.setText(input);
            }
            break;
            case R.id.btn_5:
            {
                if (input == "0") {
                    input = "5";
                }
                else {
                    input = input + "5";
                }
                answerTv.setText(input);
            }
            break;
            case R.id.btn_6:
            {
                if (input == "0") {
                    input = "6";
                }
                else {
                    input = input + "6";
                }
                answerTv.setText(input);
            }
            break;
            case R.id.btn_7:
            {
                if (input == "0") {
                    input = "7";
                }
                else {
                    input = input + "7";
                }
                answerTv.setText(input);
            }
            break;
            case R.id.btn_8:
            {
                if (input == "0") {
                    input = "8";
                }
                else {
                    input = input + "8";
                }
                answerTv.setText(input);
            }
            break;
            case R.id.btn_9:
            {
                if (input == "0") {
                    input = "9";
                }
                else {
                    input = input + "9";
                }
                answerTv.setText(input);
            }
            break;
            case R.id.btn_clean:
            {
                input = "0";
                answerTv.setText(input);
            }
            break;
            /**
             * pressed ok and check validation
             */
            case R.id.btn_ok:
            {
                int inputNum = Integer.parseInt(input);

                /**
                 * if correct
                 */
                if ((num1+num2) == inputNum || (num1-num2) == inputNum) {
                    score++;
                    scoreTv.setText(score+"");
                    try {

                    }catch (Exception e){}

                    getRandomNumber();
                } else {
                    /**
                     * if not
                     */
                    int oneTimes = SPHelper.getInt(this, "OneTimes");
                    oneTimes++;
                    SPHelper.putInt(this, "OneTimes", oneTimes);

                    int highScore = SPHelper.getInt(this, "HighScore");
                    //highScoreTV.setText(SPHelper.getInt(this, "HighScore"));
                    GameOneScore gameOneScore = new GameOneScore();
                    gameOneScore.setScore(score);
                    DbUtils.insert(gameOneScore);
                    /**
                     * save the score
                     */
                    if (score > highScore) {
                        SPHelper.putInt(this, "HighScore", score);
                        Intent it = new Intent(CalQuestion.this, SuccessActivity.class);

                        it.putExtra("Score", score);
                        score = 0;
                        startActivity(it);
                    } else {
                        Intent it = new Intent(CalQuestion.this, FailActivity.class);

                        it.putExtra("Score", score);
                        score = 0;
                        startActivity(it);
                    }
                }
            }
            SPHelper.putInt(this, "currentScore", score);
            break;
            case R.id.game1_help_btn:{

                new AlertDialog.Builder(this).setTitle("Game Introduction")
                        .setIcon(android.R.drawable.ic_menu_help)
                        .setMessage(R.string.introCal)
                        .setPositiveButton("Sure", null)
                        .show();
            }
            break;
        }
    }


    /**
     * when quit, set current status
     */
    public void onBackPressed() {
        SPHelper.putInt(this, "currentScore", score);
        CalQuestion.this.finish();
    }
}
