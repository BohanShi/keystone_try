package com.example.keystone_try.ui.home;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.keystone_try.R;
import com.example.keystone_try.views.StepView;
import com.example.keystone_try.step.UpdateUiCallBack;
import com.example.keystone_try.step.bean.StepData;
import com.example.keystone_try.step.service.StepService;
import com.example.keystone_try.step.utils.DbUtils;
import com.example.keystone_try.step.utils.SPHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    StepView stepView;
    LineChart lineChart;
    Button settingBtn;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        lineChart = root.findViewById(R.id.linechart);
        lineChart.animateXY(1000,1000);
        lineChart.getDescription().setEnabled(false);

        TextView tv = new TextView(getContext());
        tv.setPadding(35,0,25,0);
        tv.setText("For the better experience, we Strong Advise you to test this App on real machine");


            int highScore = SPHelper.getInt(getContext(), "HighScore");
        if (highScore <1 ){
            new AlertDialog.Builder(getContext()).setTitle("Friendly notice")
                    .setView(tv)
                    .setPositiveButton("Confirm", null)
                    .show();
        }



        settingBtn = root.findViewById(R.id.goal_btn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText et = new EditText(getContext());
                et.setInputType(InputType.TYPE_CLASS_NUMBER);

                new AlertDialog.Builder(getContext()).setTitle("Set Your Daily Steps Goal")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(et)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et.getText().toString();
                                if (input.equals("")) {
                                    Toast.makeText(getContext(), "Invalid input!" + input, Toast.LENGTH_LONG).show();
                                }else if (Integer.parseInt(input) > 15000){
                                    Toast.makeText(getContext(), "Too many steps!" + input, Toast.LENGTH_LONG).show();
                                }else if (Integer.parseInt(input) < 1000){
                                    Toast.makeText(getContext(), "Please set more!" + input, Toast.LENGTH_LONG).show();
                                }
                                else {
                                    SPHelper.putString(getContext(),"planWalk_QTY", input);
                                    stepView.setGoalStep(Integer.parseInt(input));
                                    Toast.makeText(getContext(), "Your goal " + input + " steps setting success!", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            }
        });

//Pedometer animation
        stepView = root.findViewById(R.id.step_display);

        //====
        try {
            if(SPHelper.getString(getContext(), "planWalk_QTY") != null){
                stepView.setGoalStep(Integer.parseInt(SPHelper.getString(getContext(), "planWalk_QTY")));
            }
        } catch (Exception e) {
            SPHelper.putString(getContext(),"planWalk_QTY", "200");
            e.printStackTrace();
        }
        //====





        initData();
        return root;
    }

    private void xText() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setEnabled(false);
    }

    //Set y axis
    private void yText() {
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(false);
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
    }

    private void text_all(ArrayList<Entry> values) {
        LineDataSet set1;
        set1 = new LineDataSet(values, "step report");
        set1.setMode(LineDataSet.Mode.LINEAR);
        set1.setColor(Color.BLACK);
        set1.setLineWidth(3);
        set1.setCircleRadius(4);
        set1.enableDashedHighlightLine(2,2,1);
        set1.setHighlightLineWidth(2);
        set1.setHighlightEnabled(false);
        set1.setHighLightColor(Color.RED);
        set1.setValueTextSize(15);
        set1.setDrawFilled(false);

        LineData data = new LineData(set1);
        lineChart.setData(data);
        lineChart.invalidate();
    }

    private void initData() {
        //Get the number of planned exercise steps set by the user, if not set, the default is 2000
        final String planWalk_QTY = SPHelper.getString(getContext(), "planWalk_QTY");
        int Step_Today = SPHelper.getInt(getContext(), "Step_Today");
        Step_Today+= 310;
        ValueAnimator animator = ObjectAnimator.ofFloat(0, Step_Today);
        animator.setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentStep = (float)animation.getAnimatedValue();
                stepView.setGoalStep(Integer.parseInt(planWalk_QTY));
                stepView.setCurrentStep((int)currentStep);
            }
        });
        animator.start();
//        stepView.setCurrentStep(Step_Today);

        getChartData();

        setupService();
    }

    private void getChartData() {
        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(getContext(), "jingzhi");
        }
        List<StepData> stepDatas =DbUtils.getQueryAll(StepData.class);

        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<Entry> newValues = new ArrayList<>();
        //adding data
        if (!stepDatas.isEmpty()) {
            int i=0;


                for (; i<7&&i<stepDatas.size(); i++) {
                StepData stepData = stepDatas.get(stepDatas.size()-i-1);
                values.add(new Entry(7-i, Integer.parseInt(stepData.getStep())));

            }

                for (; i<7; i++) {
                    int a = (int) Math.random() * 300;
                    values.add(new Entry(7-i, a));
                }

        }


        xText();
        yText();

        //execute
        if (!values.isEmpty()) {
            Collections.reverse(values);
            text_all(values);
        }
    }

    private boolean isBind = false;
    private void setupService() {
        Intent intent = new Intent(getContext(), StepService.class);
        isBind = getContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        getContext().startService(intent);
    }


    ServiceConnection conn = new ServiceConnection() {
        /**
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //Set initialization data
            String planWalk_QTY = SPHelper.getString(getContext(), "planWalk_QTY");
            int Step_Today = SPHelper.getInt(getContext(), "Step_Today");

            //Set the number of steps to monitor the callback
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planWalk_QTY = SPHelper.getString(getContext(), "planWalk_QTY");
                    stepView.setCurrentStep(stepCount);
                    SPHelper.putInt(getContext(),"Step_Today", stepCount);

                    getChartData();
                }
            });
        }

        /**
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}
