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
        toast.setGravity(Gravity.CENTER, 0, 0);	// location of appear
        TextView text = new TextView(SuccessActivity.this);
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
