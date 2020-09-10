package com.example.keystone_try.ui.notifications;

import android.content.Entity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.keystone_try.R;
import com.example.keystone_try.bean.GameOneScore;
import com.example.keystone_try.bean.GameTwoScore;
import com.example.keystone_try.step.utils.DbUtils;
import com.example.keystone_try.step.utils.SPHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private BarChart barChart;
    private LineChart lineChart;
    private ArrayList<BarChart> barChartArrayList;
    private ArrayList<LineChart> lineChartArrayList;
    private RadioGroup radioGroup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        barChart = root.findViewById(R.id.gameBarChart);
        lineChart = root.findViewById(R.id.gameLineChart);
        barChart.animateXY(1100,1100);
        lineChart.animateXY(2000,2000);
        barChartArrayList = new ArrayList<>();
        lineChartArrayList = new ArrayList<>();
        radioGroup = root.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioGroupSelected();
            }
        });
        drawLineChart(1);
        drawBarChart();
        return root;
    }

    public void setLineChartArrayList(){

    }

    public void setBarChartArrayList(){

    }

    public void drawLineChart(int type){
//        LineDataSet lineDataSet = new LineDataSet(lineChartArrayList, "record");
        switch (type) {
            case 1:{
                if(DbUtils.getLiteOrm()==null){
                    DbUtils.createDb(getContext(), "jingzhi");
                }

                List<GameOneScore> scoreList = DbUtils.getQueryAll(GameOneScore.class);
                Collections.reverse(scoreList);
                ArrayList<Entry> values = new ArrayList<>();
                //添加数据
                if (!scoreList.isEmpty()) {
                    int i=0;
                    for (; i<7&&i<scoreList.size(); i++) {
                        GameOneScore gameOneScore = scoreList.get(i);
                        values.add(new Entry(i+1, gameOneScore.getScore()));
                    }
                    for (; i<7; i++) {
                        values.add(new Entry(i+1, 0));
                    }
                }

                xText();
                yText();

                //执行
                if (!values.isEmpty()) {
                    text_all(values);
                }
            }
            break;
            case 2:{
                if(DbUtils.getLiteOrm()==null){
                    DbUtils.createDb(getContext(), "jingzhi");
                }

                List<GameTwoScore> scoreList = DbUtils.getQueryAll(GameTwoScore.class);
                Collections.reverse(scoreList);
                ArrayList<Entry> values = new ArrayList<>();
                //添加数据
                if (!scoreList.isEmpty()) {
                    int i=0;
                    for (; i<7&&i<scoreList.size(); i++) {
                        GameTwoScore gameTwoScore = scoreList.get(i);
                        values.add(new Entry(i+1, gameTwoScore.getScore()));
                    }
                    for (; i<7; i++) {
                        values.add(new Entry(i+1, 0));
                    }
                }

                xText();
                yText();

                //执行
                if (!values.isEmpty()) {
                    text_all(values);
                }
            }
            break;
            case 3:{

            }
            break;
            default:
                break;
        }
    }

    public void drawBarChart(){
        int oneTimes = SPHelper.getInt(getContext(), "OneTimes");
        int twoTimes = SPHelper.getInt(getContext(), "TwoTimes");

//        ArrayList<String> xVals = new ArrayList<>();
//        xVals.add("Game One");
//        xVals.add("Game Two");

        ArrayList<BarEntry> barEntryArrayList1 = new ArrayList<>();
        ArrayList<BarEntry> barEntryArrayList2 = new ArrayList<>();
        BarEntry barEntry1 = new BarEntry(1, oneTimes);
        BarEntry barEntry2 = new BarEntry(2, twoTimes);
        barEntryArrayList1.add(barEntry1);
        barEntryArrayList2.add(barEntry2);
        BarDataSet barDataSet1 = new BarDataSet(barEntryArrayList1, "Count");
        BarDataSet barDataSet2 = new BarDataSet(barEntryArrayList2, "2048");

        ArrayList<IBarDataSet> threebardata = new ArrayList<>();
        threebardata.add(barDataSet1);
        threebardata.add(barDataSet2);

        BarData bardata = new BarData(threebardata);
        bardata.setValueFormatter(new MonthlyIntegerYValueFormatter());


        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setGranularityEnabled(true);
        yAxisLeft.setGranularity(1f);
        yAxisLeft.setValueFormatter(new IntegerAxisFormatter());
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setGranularityEnabled(true);
        yAxisRight.setGranularity(1f);
        yAxisRight.setValueFormatter(new IntegerAxisFormatter());
        yAxisLeft.setEnabled(false);
        yAxisRight.setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new XaxisFormatter());


        barChart.setScrollBarSize(1);
        barChart.setData(bardata);
    }

    public void radioGroupSelected(){
        lineChart.clear();
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.radioCal:{
                drawLineChart(1);
            }
            break;
            case R.id.radio2048:{
                drawLineChart(2);
            }
            break;
            case R.id.radioImg: {
                drawLineChart(3);
            }
            break;
        }
    }

    private void xText() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
    }

    //设置y轴
    private void yText() {
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(false);
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
    }

    private void text_all(ArrayList<Entry> values) {
        LineDataSet set1;
        set1 = new LineDataSet(values, "Score Report");
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
        data.setValueFormatter(new MonthlyIntegerYValueFormatter());
        lineChart.setData(data);
        lineChart.invalidate();
    }

    public void getResentData(Class cls){
        List<Entity> scoreList = DbUtils.getQueryAll(cls);
        Collections.reverse(scoreList);
        ArrayList<Entry> values = new ArrayList<>();
    }

    public void test(){
        List<GameOneScore> scores = DbUtils.getQueryAll(GameOneScore.class);
        Collections.reverse(scores);
        List<Entry> values = new ArrayList<>();
        //adding data
        if (!scores.isEmpty()) {
            int i=0;
            for (; i<7&&i<scores.size(); i++) {
                GameOneScore gameOneScore = scores.get(i);
                values.add(new Entry(i+1,gameOneScore.getScore()));
            }
            for (; i<7; i++) {
                values.add(new Entry(i+1, 0));
            }
        }
    }

    public class MonthlyIntegerYValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return (int) (value) + "";
        }
    }

    public class IntegerAxisFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return (int) (value) + "";
        }
    }

    public class XaxisFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (value == 1) {
                return "Count";
            } else if (value == 2) {
                return "2048";
            } else if (value == 3) {
                return "Game Three";
            } else {
                return (int)value+"";
            }
        }
    }
}

