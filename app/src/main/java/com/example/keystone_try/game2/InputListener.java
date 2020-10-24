package com.example.keystone_try.game2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.keystone_try.R;
import com.example.keystone_try.Util.MetionString;
import com.example.keystone_try.game1.CalQuestion;
import com.example.keystone_try.game1.SuccessActivity;

/**
 * leaned from Youtuber AtoTalKs
 */
class InputListener extends AppCompatActivity implements View.OnTouchListener {

    private static final int SWIPE_MIN_DISTANCE = 0;
    private static final int SWIPE_THRESHOLD_VELOCITY = 25;
    private static final int MOVE_THRESHOLD = 250;
    private static final int RESET_STARTING = 10;
    private final MainView mView;
    private float x;
    private float y;
    private float lastDx;
    private float lastDy;
    private float previousX;
    private float previousY;
    private float startingX;
    private float startingY;
    private int previousDirection = 1;
    private int veryLastDirection = 1;
    // Whether or not we have made a move, i.e. the blocks shifted or tried to shift.
    private boolean hasMoved = false;
    // Whether or not we began the press on an icon. This is to disable swipes if the user began
    // the press on an icon.
    private boolean beganOnIcon = false;

    public InputListener(MainView view) {
        super();
        this.mView = view;
    }

    /**
     * set on touch event listener
     * @param view
     * @param event what kind of operation
     * @return
     */
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                startingX = x;
                startingY = y;
                previousX = x;
                previousY = y;
                lastDx = 0;
                lastDy = 0;
                hasMoved = false;
                beganOnIcon = iconPressed(mView.sXNewGame, mView.sYIcons)
                        || iconPressed(mView.sXUndo, mView.sYIcons);
                return true;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                if (mView.game.isActive() && !beganOnIcon) {
                    float dx = x - previousX;
                    if (Math.abs(lastDx + dx) < Math.abs(lastDx) + Math.abs(dx) && Math.abs(dx) > RESET_STARTING
                            && Math.abs(x - startingX) > SWIPE_MIN_DISTANCE) {
                        startingX = x;
                        startingY = y;
                        lastDx = dx;
                        previousDirection = veryLastDirection;
                    }
                    if (lastDx == 0) {
                        lastDx = dx;
                    }
                    float dy = y - previousY;
                    if (Math.abs(lastDy + dy) < Math.abs(lastDy) + Math.abs(dy) && Math.abs(dy) > RESET_STARTING
                            && Math.abs(y - startingY) > SWIPE_MIN_DISTANCE) {
                        startingX = x;
                        startingY = y;
                        lastDy = dy;
                        previousDirection = veryLastDirection;
                    }
                    if (lastDy == 0) {
                        lastDy = dy;
                    }
                    if (pathMoved() > SWIPE_MIN_DISTANCE * SWIPE_MIN_DISTANCE && !hasMoved) {
                        boolean moved = false;
                                                                                                        //Vertical
                        if (((dy >= SWIPE_THRESHOLD_VELOCITY && Math.abs(dy) >= Math.abs(dx)) || y - startingY >= MOVE_THRESHOLD) && previousDirection % 2 != 0) {
                            moved = true;
                            previousDirection = previousDirection * 2;
                            veryLastDirection = 2;
                            mView.game.move(2);
                        } else if (((dy <= -SWIPE_THRESHOLD_VELOCITY && Math.abs(dy) >= Math.abs(dx)) || y - startingY <= -MOVE_THRESHOLD) && previousDirection % 3 != 0) {
                            moved = true;
                            previousDirection = previousDirection * 3;
                            veryLastDirection = 3;
                            mView.game.move(0);
                        }
                                                                                                        //Horizontal
                        if (((dx >= SWIPE_THRESHOLD_VELOCITY && Math.abs(dx) >= Math.abs(dy)) || x - startingX >= MOVE_THRESHOLD) && previousDirection % 5 != 0) {
                            moved = true;
                            previousDirection = previousDirection * 5;
                            veryLastDirection = 5;
                            mView.game.move(1);
                        } else if (((dx <= -SWIPE_THRESHOLD_VELOCITY && Math.abs(dx) >= Math.abs(dy)) || x - startingX <= -MOVE_THRESHOLD) && previousDirection % 7 != 0) {
                            moved = true;
                            previousDirection = previousDirection * 7;
                            veryLastDirection = 7;
                            mView.game.move(3);
                        }
                        if (moved) {
                            hasMoved = true;
                            startingX = x;
                            startingY = y;
                        }
                    }
                }
                previousX = x;
                previousY = y;
                return true;
            case MotionEvent.ACTION_UP:                                                             // click
                x = event.getX();
                y = event.getY();
                previousDirection = 1;
                veryLastDirection = 1;
                //"Menu" inputs
                if (!hasMoved) {
                    if (iconPressed(mView.sXNewGame, mView.sYIcons)) {
                        if (!mView.game.gameLost()) {
                            new AlertDialog.Builder(mView.getContext())
                                    .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.game.newGame();
                                        }
                                    })
                                    .setNegativeButton(R.string.continue_game, null)
                                    .setTitle(R.string.reset_dialog_title)
                                    .setMessage(R.string.reset_dialog_message)
                                    .show();
                        } else {
                            mView.game.newGame();
                        }

                    } else if (iconPressed(mView.sXUndo, mView.sYIcons)) {
                        mView.game.revertUndoState();
                    } else if (iconPressed(mView.sXIntro, mView.sYIcons)){

                        AlertDialog.Builder builder = new AlertDialog.Builder(mView.getContext())
                                .setTitle(R.string.intro)
                                //.setMessage(R.string.intro2048)
                                .setNegativeButton("Sure", null);
                        TextView tv = new TextView(mView.getContext());
                        tv.setText(R.string.main_content);
                        tv.setTextSize(24);
                        tv.setPaddingRelative(40,60,40,0);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        builder.setView(tv);
                       // builder.setView(tv1);

                        builder.show();
//                        new AlertDialog.Builder(mView.getContext())
//                                .setPositiveButton("Go to video", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Uri uri = Uri.parse("http://www.baidu.com");
//                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                                        startActivity(intent);
//                                    }
//
//                                })
//                                .setNegativeButton("Sure", null)
//                                .setTitle(R.string.intro)
//                                .setMessage(R.string.intro2048)
//                                .show();
                    }
                    else if (isTap(2) && inRange(mView.startingX, x, mView.endingX)
                            && inRange(mView.startingY, x, mView.endingY) && mView.continueButtonEnabled) {
                        mView.game.setEndlessMode();
                    }
                }
        }
        return true;
    }

    private float pathMoved() {
        return (x - startingX) * (x - startingX) + (y - startingY) * (y - startingY);
    }

    private boolean iconPressed(int sx, int sy) {
        return isTap(1) && inRange(sx, x, sx + mView.iconSize)
                && inRange(sy, y, sy + mView.iconSize);
    }

    private boolean inRange(float starting, float check, float ending) {
        return (starting <= check && check <= ending);
    }

    private boolean isTap(int factor) {
        return pathMoved() <= mView.iconSize * mView.iconSize * factor;
    }

    @Override
    public void onBackPressed() {
        Toast toast =  Toast.makeText(InputListener.this, "ABCDEFGH", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);	// location of appear
        TextView text = new TextView(InputListener.this);
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
    }
}