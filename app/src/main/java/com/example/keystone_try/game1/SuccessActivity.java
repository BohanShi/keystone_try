package com.example.keystone_try.game1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
