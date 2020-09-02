package com.example.keystone_try.step.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.keystone_try.MainActivity;
import com.example.keystone_try.R;
import com.example.keystone_try.step.UpdateUiCallBack;
import com.example.keystone_try.step.accelerometer.StepCount;
import com.example.keystone_try.step.accelerometer.StepValuePassListener;
import com.example.keystone_try.step.bean.StepData;
import com.example.keystone_try.step.utils.DbUtils;
import com.example.keystone_try.step.utils.SPHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StepService extends Service implements SensorEventListener {
    private String TAG = "StepService";

    private static int duration = 30 * 1000;

    private static String CURRENT_DATE = "";

    private SensorManager sensorManager;

    private BroadcastReceiver mBatInfoReceiver;

    private TimeCount time;

    private int CURRENT_STEP;

    private static int stepSensorType = -1;

    private boolean hasRecord = false;

    private int hasStepCount = 0;

    private int previousStepCount = 0;

    private NotificationManager mNotificationManager;

    private StepCount mStepCount;

    private StepBinder stepBinder = new StepBinder();

    private NotificationCompat.Builder mBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
        initNotification();
        initTodayData();
        initBroadcastReceiver();
        new Thread(new Runnable() {
            public void run() {
                startStepDetector();
            }
        }).start();
        startTimeCount();

    }

    /**
     *Get today's date
     *
     * @return
     */
    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * Initialize the notification bar
     */
    private void initNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else{
            mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText("Today Step " + CURRENT_STEP + " Steps")
                    .setContentIntent(getDefalutIntent(Notification.FLAG_ONGOING_EVENT))
                    .setWhen(System.currentTimeMillis())//The time when the notification is generated will be displayed in the notification message
                    .setPriority(Notification.PRIORITY_DEFAULT)//Set the priority of this notification
                    .setAutoCancel(false)//Set this flag when the user clicks on the panel to make the notification automatically cancel
                    .setOngoing(true)
                    /**
                     * Set him as an ongoing notification.
                     * They are usually used to indicate a background task where the user is actively participating (such as playing music)
                     * or is waiting in some way,
                     * thus occupying the device (such as a file download, synchronization operation, active network connection)
                     */
                    .setSmallIcon(R.mipmap.keystone_logo);
            Notification notification = mBuilder.build();
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            startForeground(notifyId_Step, notification);
        }

        Log.d(TAG, "initNotification()");
    }

    @TargetApi(26)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.keystone_try";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert mNotificationManager != null;
        mNotificationManager.createNotificationChannel(chan);

        mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = mBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.keystone_logo)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Today Step " + CURRENT_STEP + " steps")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setOngoing(true)
                .build();
        startForeground(notifyId_Step, notification);
    }

    /**
     * Initialize the number of steps on the day
     */
    private void initTodayData() {
        CURRENT_DATE = getTodayDate();
        DbUtils.createDb(this, "DylanStepCount");
        DbUtils.getLiteOrm().setDebugged(false);
        //Get the data of the day for display
        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{CURRENT_DATE});
        if (list.size() == 0 || list.isEmpty()) {
            CURRENT_STEP = 0;
        } else if (list.size() == 1) {
            Log.v(TAG, "StepData=" + list.get(0).toString());
            CURRENT_STEP = Integer.parseInt(list.get(0).getStep());
        } else {
            Log.v(TAG, "something wrong！");
        }
        if (mStepCount != null) {
            mStepCount.setSteps(CURRENT_STEP);
        }
        updateNotification();
    }

    /**
     * Register to broadcast
     */
    private void initBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        // Screen off screen broadcast
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //Shutdown broadcast
        filter.addAction(Intent.ACTION_SHUTDOWN);
        // Bright screen broadcast
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // Screen unlock broadcast
//        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //Monitoring date changes
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);

        mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    Log.d(TAG, "screen on");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    Log.d(TAG, "screen off");
                    //Change to 60 seconds to store
                    duration = 60000;
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    Log.d(TAG, "screen unlock");
//                    save();
                    //Change to 30 seconds to store
                    duration = 30000;
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    Log.i(TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                    //Save once
                    save();
                } else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
                    Log.i(TAG, " receive ACTION_SHUTDOWN");
                    save();
                } else if (Intent.ACTION_DATE_CHANGED.equals(action)) {//Reset the number of steps for date change to 0
//                    Logger.d("Reset steps" + StepDcretor.CURRENT_STEP);
                    save();
                    isNewDay();
                } else if (Intent.ACTION_TIME_CHANGED.equals(action)) {
                    //Reset the number of steps in time change to 0
                    isCall();
                    save();
                    isNewDay();
                } else if (Intent.ACTION_TIME_TICK.equals(action)) {//日期变化步数重置为0
                    isCall();
//                    Logger.d("Reset" + StepDcretor.CURRENT_STEP);
                    save();
                    isNewDay();
                }
            }
        };
        registerReceiver(mBatInfoReceiver, filter);
    }


    /**
     * Monitor 0 o'clock in the evening to initialize the data
     */
    private void isNewDay() {
        String time = "00:00";
        if (time.equals(new SimpleDateFormat("HH:mm").format(new Date())) || !CURRENT_DATE.equals(getTodayDate())) {
            initTodayData();
        }
    }


    /**
     * Monitor time changes to remind users to exercise
     */
    private void isCall() {
        String time = this.getSharedPreferences("share_date", Context.MODE_MULTI_PROCESS).getString("achieveTime", "21:00");
        String plan = this.getSharedPreferences("share_date", Context.MODE_MULTI_PROCESS).getString("planWalk_QTY", SPHelper.getString(getApplicationContext(),  "planWalk_QTY"));
        String remind = this.getSharedPreferences("share_date", Context.MODE_MULTI_PROCESS).getString("remind", "1");

        if (("1".equals(remind)) &&                                       
                (CURRENT_STEP < Integer.parseInt(plan)) &&
                (time.equals(new SimpleDateFormat("HH:mm").format(new Date())))
        ) {
            remindNotify();
        }

    }

    /**
     * Start saving step data
     */
    private void startTimeCount() {
        if (time == null) {
            time = new TimeCount(duration, 1000);
        }
        time.start();
    }

    /**
     * Update step notification
     */
    private void updateNotification() {
        //Set click to jump
        Intent hangIntent = new Intent(this, MainActivity.class);
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = mBuilder.setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Today's step: " + CURRENT_STEP + " steps")
                .setWhen(System.currentTimeMillis())//The time when the notification is generated will be displayed in the notification message
                .setContentIntent(hangPendingIntent)
                .build();
        mNotificationManager.notify(notifyId_Step, notification);
        if (mCallback != null) {
            mCallback.updateUi(CURRENT_STEP);
        }
        Log.d(TAG, "updateNotification()");
    }

    /**
     * UI listener object
     */
    private UpdateUiCallBack mCallback;

    /**
     * Register UI update listener
     *
     * @param paramICallback
     */
    public void registerCallback(UpdateUiCallBack paramICallback) {
        this.mCallback = paramICallback;
    }

    /**
     * ID of the notification
     */
    int notifyId_Step = 100;
    /**
     * The ID of the Notification that reminds you to exercise
     */
    int notify_remind_id = 200;

    /**
     * Reminder exercise notification bar
     */
    private void remindNotify() {

        //设置点击跳转
        Intent hangIntent = new Intent(this, MainActivity.class);
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        String plan = this.getSharedPreferences("share_date", Context.MODE_MULTI_PROCESS).getString("planWalk_QTY", "2000");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Today's step: " + CURRENT_STEP + " steps")
                .setContentText("Still needs" + (Integer.valueOf(plan) - CURRENT_STEP) + " steps to goal，come on！")
                .setContentIntent(hangPendingIntent)
                .setTicker(getResources().getString(R.string.app_name) + "notice you to have some work")//The notification appears in the notification bar for the first time, with a rising animation effect
                .setWhen(System.currentTimeMillis())//The time when the notification is generated will be displayed in the notification message
                .setPriority(Notification.PRIORITY_DEFAULT)//Set the priority of this notification
                .setAutoCancel(true)//Set this flag when the user clicks on the panel to make the notification automatically cancel
                .setOngoing(false)//ture，
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)//Add sound to notification
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.keystone_logo);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(notify_remind_id, mBuilder.build());
    }

    /**
     * @flags Attributes: Permanent at the top:Notification.FLAG_ONGOING_EVENT
     */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return stepBinder;
    }

    /**
     * The link to transfer data to Activity
     */
    public class StepBinder extends Binder {

        /**
         * @return StepService
         */
        public StepService getService() {
            return StepService.this;
        }
    }

    /**
     * @return
     */
    public int getStepCount() {
        return CURRENT_STEP;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * Get sensor instance
     */
    private void startStepDetector() {
        if (sensorManager != null) {
            sensorManager = null;
        }
        // Get an instance of the sensor manager
        sensorManager = (SensorManager) this
                .getSystemService(SENSOR_SERVICE);
        //Pedometer sensor can be used after android4.4
        int VERSION_CODES = Build.VERSION.SDK_INT;
        if (VERSION_CODES >= 19) {
            addCountStepListener();
        } else {
            addBasePedometerListener();
        }
    }

    /**
     * Add sensor monitoring
     */
    private void addCountStepListener() {
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_COUNTER;
            Log.v(TAG, "Sensor.TYPE_STEP_COUNTER");
            boolean success = sensorManager.registerListener(StepService.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.v(TAG, success+"");
        } else if (detectorSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_DETECTOR;
            Log.v(TAG, "Sensor.TYPE_STEP_DETECTOR");
            sensorManager.registerListener(StepService.this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.v(TAG, "Count sensor not available!");
            addBasePedometerListener();
        }
    }

    /**
     * Sensor monitoring callback
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stepSensorType == Sensor.TYPE_STEP_COUNTER) {
            //Get the number of temporary steps returned by the current sensor
            int tempStep = (int) event.values[0];
            //For the first time, if the number of steps already in the mobile phone system is not obtained,
            // then the number of steps that the APP in the system has not yet started to count steps
            if (!hasRecord) {
                hasRecord = true;
                hasStepCount = tempStep;
            } else {
                //Get the total number of steps since the APP was opened
                // = the total number of steps in this system callback
                // -the number of steps that existed before the APP was opened
                int thisStepCount = tempStep - hasStepCount;
                //The number of effective steps this time
                // = (the total number of steps recorded after the APP is opened-the total number of steps recorded
                // since the last time the APP is opened)
                int thisStep = thisStepCount - previousStepCount;
                //The total number of steps = the number of existing steps + the number of effective steps this time
                CURRENT_STEP += (thisStep);
                //Record the total number of steps since the last time the APP was opened
                previousStepCount = thisStepCount;
            }
        } else if (stepSensorType == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0) {
                CURRENT_STEP++;
            }
        }
        updateNotification();
    }

    /**
     * Counting steps by accelerometer
     */
    private void addBasePedometerListener() {
        mStepCount = new StepCount();
        mStepCount.setSteps(CURRENT_STEP);
        // Get the type of sensor, the type obtained here is an acceleration sensor
        // This method is used to register, and it will take effect only after registration.
        // Parameters: instance of SensorEventListener, instance of Sensor, update rate
        Sensor sensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        boolean isAvailable = sensorManager.registerListener(mStepCount.getStepDetector(), sensor,
                SensorManager.SENSOR_DELAY_UI);
        mStepCount.initListener(new StepValuePassListener() {
            @Override
            public void stepChanged(int steps) {
                CURRENT_STEP = steps;
                updateNotification();
            }
        });
        if (isAvailable) {
            Log.v(TAG, "accelerated speed sensor can use");
        } else {
            Log.v(TAG, "accelerated speed sensor cannot use");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    /**
     * Save step data
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // If the timer ends normally, start counting steps
            time.cancel();
            save();
            startTimeCount();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

    }

    /**
     * Save step data
     */
    private void save() {
        int tempStep = CURRENT_STEP;

        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{CURRENT_DATE});
        if (list.size() == 0 || list.isEmpty()) {
            StepData data = new StepData();
            data.setToday(CURRENT_DATE);
            data.setStep(tempStep + "");
            DbUtils.insert(data);
        } else if (list.size() == 1) {
            StepData data = list.get(0);
            data.setStep(tempStep + "");
            DbUtils.update(data);
        } else {
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //Cancel the foreground process
        stopForeground(true);
        DbUtils.closeDb();
        unregisterReceiver(mBatInfoReceiver);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}