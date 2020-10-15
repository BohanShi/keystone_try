package com.example.keystone_try.game1;

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
/**
 * success page
 */

public class SuccessActivity extends AppCompatActivity {

    private TextView scoreTv;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Toast toast =  Toast.makeText(SuccessActivity.this, "ABCDEFGH", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);	// 设置出现位置
        TextView text = new TextView(SuccessActivity.this);
        MetionString ms = new MetionString();
        text.setText(ms.returnValue());
        text.setTextColor(getResources().getColor(R.color.white));	// 文本颜色
        text.setTextSize(30);	// 文本字体大小
        text.setWidth(900);		// 设置toast的大小
        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);	// 设置文本居中
        text.setBackgroundColor(Color.rgb(64,158,255));	// 设置背景颜色
        toast.setView(text); // 将文本插入到toast里
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
        int score = getIntent().getIntExtra("Score", 0);
        scoreTv = findViewById(R.id.textView_win_score);
        scoreTv.setText("Your Score: " + score);

        backBtn = findViewById(R.id.success_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
                intent.putExtra("game","game");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

}
