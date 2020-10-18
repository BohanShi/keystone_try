package com.example.keystone_try.game3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keystone_try.R;
import com.example.keystone_try.Util.MetionString;
import com.example.keystone_try.bean.GameThreeScore;
import com.example.keystone_try.game1.FailActivity;
import com.example.keystone_try.step.utils.DbUtils;
import com.example.keystone_try.step.utils.SPHelper;

import java.util.Random;

public class GameThree extends Activity {

    private int[] item_images = new int[20];// image id
    private String[] item_names = new String[20];// image name
    private int[] card_images;// card image index
    private GameThreeCard[][] cards = new GameThreeCard[4][3];// card
    private int lastcard_x = -1;
    private int lastcard_y = -1;
    private int currentcard_x = -1;
    private int currentcard_y = -1;

    private TextView highScoreTxt;
    private TableLayout tableLayout;
    private TextView timeTxt, quantityTxt;
    private ImageButton[][] buttons = new ImageButton[4][5];

    private boolean isFirstClick = false;
    private int second = 0;
    private int quantity = 0;
    private SoundPool soundPool;
    private int clickSoundID;
    private int removeSoundID;
    private int winSoundID;
    private float volumnCurrent;
    private float volumnRatio;
    private static final String HIGH_SCORE = "three high score";
    public long highScore = 0;
    public long score = 0;
    private CountDownTimer countDownTimer;
    private CountDownTimer startDownTimer;

    private Button helpB;
    private Button start;

    /**
     * Timer, regular update time
     */
    Handler handler = new Handler();
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            if (second < 999) {
//                handler.postDelayed(this, 1000);
//                second++;
//            } else {
//                handler.removeCallbacks(this);
//            }
//            timeTxt.setText(String.format("%03d", second));
//        }
//    };

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            int image_index = cards[currentcard_x][currentcard_y]
                    .getImage_index();
            if (image_index == cards[lastcard_x][lastcard_y].getImage_index()) {
                buttons[currentcard_x][currentcard_y].setImageDrawable(null);
                buttons[lastcard_x][lastcard_y].setImageDrawable(null);
                cards[currentcard_x][currentcard_y].setRemove(true);
                cards[lastcard_x][lastcard_y].setRemove(true);
                quantity = quantity + 2;
                quantityTxt.setText(String.format("%03d", quantity));
                playSound(removeSoundID);
            } else {
                buttons[currentcard_x][currentcard_y]
                        .setImageResource(R.drawable.leaf);
                buttons[lastcard_x][lastcard_y]
                        .setImageResource(R.drawable.leaf);
                cards[currentcard_x][currentcard_y].setShow(false);
                cards[lastcard_x][lastcard_y].setShow(false);
            }
            lastcard_x = -1;
            lastcard_y = -1;
            checkOver();
        }
    };

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game_three);

        timeTxt = (TextView) findViewById(R.id.timeTxt);
        quantityTxt = (TextView) findViewById(R.id.quantityTxt);
        highScoreTxt = (TextView) findViewById(R.id.highScoreText);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        quantityTxt.setText(String.format("%03d", quantity));

        highScore = getHighScore();
        highScoreTxt.setText(String.format("%03d", highScore));

        helpB = findViewById(R.id.helpb);
       // start = findViewById(R.id.startThirdGame);

        helpB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.intro)
                        //.setMessage(R.string.intro2048)
                        .setNegativeButton("Sure", null);
                TextView tv = new TextView(v.getContext());
                tv.setText(R.string.introGameThree);
                tv.setTextSize(24);
                tv.setPaddingRelative(40,60,40,0);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                builder.setView(tv);
                // builder.setView(tv1);
                builder.show();
            }
        });

//        faceImg.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                AlertDialog.Builder builder = new Builder(GameThree.this);
//                builder.setMessage("The goal of this game is to find all identical pictures. Flip a tile to show the picture, and then try to find its match.");
//                builder.setTitle("Help");
//                builder.setNegativeButton("Close",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                builder.create().show();
//            }
//        });

        Resources res = getResources();
        item_names = res.getStringArray(R.array.item_names);
        TypedArray array = res.obtainTypedArray(R.array.item_images);
        for (int i = 0; i < array.length(); i++) {
            item_images[i] = array.getResourceId(i, 0);
        }
        array.recycle();

        initCardPanel();

        AudioManager am = (AudioManager) this
                .getSystemService(Context.AUDIO_SERVICE);
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumnRatio = volumnCurrent / audioMaxVolumn;
        initSound();
        startShowCard();


    }

    /**
     *  start counter
     */
    private void startCounter() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeTxt.setText(String.format("%03d", millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                timeTxt.setText("000");
                showMessage("Time out!", "You have scored " + quantity + "! Keep trying!!");
            }
        };
        countDownTimer.start();
    }

    private void startShowCard() {
        startDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeTxt.setText(String.format("%03d", millisUntilFinished/1000));
                if (millisUntilFinished/1000 == 9) {

                    toastShow("Game will start in " + millisUntilFinished / 1000 + " seconds.", 0, true);

                }
            }

            @Override
            public void onFinish() {
                timeTxt.setText("000");
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 4; x++) {
                        cards[x][y].setShow(false);
                        buttons[x][y].setImageResource(R.drawable.leaf);
                    }
                }

                toastShow("Game Start!", 0, true);

                startCounter();
            }
        };
        startDownTimer.start();
    }

    private void recordHighScore() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(HIGH_SCORE, highScore);
        editor.commit();
    }

    private long getHighScore() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return settings.getLong(HIGH_SCORE, 0);
    }

    /**
     * init card view
     */
    private void initCardPanel() {
        isFirstClick = false;
        // Randomly take 10 picture indexes from 20 pictures
        int[] randomNumber = getRandomNumber(0, 19, 6);
        // Copy 10 pictures, generate double picture index
        card_images = getRandomCardNumber(randomNumber);

        for (int y = 0; y < 3; y++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT, 1));
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT, 1);
            layoutParams.setMargins(0, 0, 0, 0);
            for (int x = 0; x < 4; x++) {
                cards[x][y] = new GameThreeCard(x, y);
                cards[x][y].setImage_index(card_images[y * 4 + x]);
                cards[x][y].setShow(true);

                buttons[x][y] = new ImageButton(this);
                buttons[x][y].setLayoutParams(layoutParams);
                buttons[x][y].setOnClickListener(new CardClickListener());
                buttons[x][y].setBackgroundResource(R.drawable.box);
                buttons[x][y].setImageResource(item_images[card_images[y * 4 + x]]);
//                buttons[x][y].setImageResource(R.drawable.leaf);
                buttons[x][y].setScaleType(ScaleType.FIT_CENTER);
                buttons[x][y].setTag(cards[x][y]);
                tableRow.addView(buttons[x][y]);
            }
            tableLayout.addView(tableRow);
        }
    }

    /**
     * Card click event
     *
     */
    private class CardClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
//            if (!isFirstClick) {
//                handler.postDelayed(runnable, 1000);
//                isFirstClick = true;
//            }
            int showNum = 0;
            boolean isClick = true;
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 4; x++) {
                    if (!cards[x][y].isRemove() && cards[x][y].isShow()) {
                        showNum++;
                    }
                    if (showNum == 2) {
                        isClick = false;
                        break;
                    }
                }
            }
            if (isClick) {
                playSound(clickSoundID);
                ImageButton button = (ImageButton) v;
                GameThreeCard card = (GameThreeCard) button.getTag();
                int x = card.getX(), y = card.getY();
                if (!card.isShow()) {
                    button.setImageResource(item_images[card.getImage_index()]);
                    card.setShow(true);
                    currentcard_x = x;
                    currentcard_y = y;
                    if (lastcard_x == -1) {
                        lastcard_x = currentcard_x;
                        lastcard_y = currentcard_y;
                    } else {
                        handler.postDelayed(runnable1, 1000);
                    }

                }
            }
        }
    }

    private void checkOver() {
        int rightNum = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                if (cards[x][y].isRemove()) {
                    rightNum++;
                }
            }
        }

        if (rightNum == 12) {
//            handler.removeCallbacks(runnable);
            playSound(winSoundID);
            showMessage("Congratulation", "Great Job! You have found the images correctly!!!");
        }
    }

    /**
     * Show info dialog
     *
     * @param title
     * @param message
     */
    private void showMessage(String title, String message) {

        int threeTimes = SPHelper.getInt(getApplicationContext(), "ThreeTimes");
        threeTimes++;
        SPHelper.putInt(getApplicationContext(), "ThreeTimes", threeTimes);

        GameThreeScore gameThreeScore = new GameThreeScore();
        gameThreeScore.setScore(quantity);
        DbUtils.insert(gameThreeScore);

        if (quantity > getHighScore()) {
            highScore = quantity;
            recordHighScore();
        }

        try{
            countDownTimer.cancel();
            startDownTimer.cancel();
        }catch(Exception e){}
        countDownTimer = null;
        startDownTimer = null;
        Builder builder = new Builder(GameThree.this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("Restart",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        resetTime();
                        tableLayout.removeAllViews();
                        initCardPanel();
                        startShowCard();
                    }
                });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                System.exit(0);
            }
        });
        builder.create().show();

    }

    /**
     * Reset timer
     */
    private void resetTime() {
        second = 0;
        timeTxt.setText("000");
        quantityTxt.setText("000");
        quantity = 0;
        quantityTxt.setText(String.format("%03d", quantity));
        highScore = getHighScore();
        highScoreTxt.setText(String.format("%03d", highScore));
    }

    private int[] getRandomCardNumber(int[] arr) {
        int[] arr1 = new int[12];
        for (int j = 0; j < arr1.length; j++) {
            if (j < 6) {
                arr1[j] = arr[j];
            } else {
                arr1[j] = arr[j - 6];
            }
        }
        Random random = new Random();
        for (int m = 0; m < arr1.length; m++) {
            int p = random.nextInt(arr1.length);
            int tmp = arr1[m];
            arr1[m] = arr1[p];
            arr1[p] = tmp;
        }
        random = null;
        return arr1;
    }

    /**
     * Randomly specify N unique numbers in the range
     *
     * @param min
     * @param max
     * @param n
     * @return
     */
    private int[] getRandomNumber(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    /**
     * Initial click sound effect
     */
    private void initSound() {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        //clickSoundID = soundPool.load(this, R.raw.click, 1);
        //removeSoundID = soundPool.load(this, R.raw.remove, 1);
       // winSoundID = soundPool.load(this, R.raw.win, 2);
    }

    /**
     * Play click sound
     */
    private void playSound(int soundID) {
        soundPool.play(soundID, volumnRatio, // Left ear canal volume [0~1]
                volumnRatio, // Right ear canal volume [0~1]
                0, // Play priority [0 means lowest priority]
                1, // Cycle mode [0 means cycle once, -1 means cycle all the time, other numbers +1 means the number of cycles corresponding to the current number]
                1 // Play speed [1 is normal, the range is from 0~2]
        );
    }

    /**
     * Back button
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try{
            countDownTimer.cancel();
            startDownTimer.cancel();
            countDownTimer = null;
            startDownTimer = null;} catch (Exception e){}

        if (keyCode == KeyEvent.KEYCODE_BACK) {


            MetionString ms = new MetionString();
            toastShow("",ms.returnValue(),false);
            Builder builder = new Builder(GameThree.this);
            builder.setMessage("Exit the game?");
            builder.setTitle("Alert");
            builder.setPositiveButton("Restart",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
//                            handler.removeCallbacks(runnable);
                            resetTime();
                            tableLayout.removeAllViews();
                            initCardPanel();
                            startShowCard();
                        }
                    });
            builder.setNegativeButton("Exit",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            System.exit(0);

                        }
                    });
            builder.create().show();
        }



        return true;
    }

    private void toastShow(String texta, int a, boolean b){
        Toast toast =  Toast.makeText(GameThree.this, "ABCDEFGH", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);	// 设置出现位置
        TextView text = new TextView(GameThree.this);
        MetionString ms = new MetionString();
        if (b)
            text.setText(texta);// 设置文本内容
        else
            text.setText(a);
        text.setTextColor(getResources().getColor(R.color.white));	// 文本颜色
        text.setTextSize(30);	// 文本字体大小
        text.setWidth(900);		// 设置toast的大小
        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);	// 设置文本居中
        text.setBackgroundColor(Color.rgb(64,158,255));	// 设置背景颜色
        toast.setView(text); // 将文本插入到toast里
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}