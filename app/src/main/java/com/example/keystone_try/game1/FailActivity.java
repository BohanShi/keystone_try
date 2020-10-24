package com.example.keystone_try.game1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.keystone_try.Util.MetionString;
import com.example.keystone_try.ui.MainActivity;
import com.example.keystone_try.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * fail page
 */
public class FailActivity extends AppCompatActivity {

    private TextView scoreTv;
    private Button backBtn;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail);



        Toast toast =  Toast.makeText(FailActivity.this, "ABCDEFGH", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);	// location of appear
        TextView text = new TextView(FailActivity.this);
        MetionString ms = new MetionString();
        text.setText(ms.returnValue());	// context
        text.setTextColor(getResources().getColor(R.color.white));	// text color
        text.setTextSize(30);	// text size
        text.setWidth(900);		// toast size
        text.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);	// alignment
        text.setBackgroundColor(Color.rgb(64,158,255));	// bg color
        toast.setView(text); // set text into toast
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

        int score = getIntent().getIntExtra("Score", 0);
        scoreTv = findViewById(R.id.textView_lose_score);
        scoreTv.setText("Your Score: " + score);

        backBtn = findViewById(R.id.fail_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FailActivity.this, MainActivity.class);
                intent.putExtra("game","game");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }



}
