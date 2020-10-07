package com.example.keystone_try.ui.OpenData;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.keystone_try.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ThirdFragment extends Fragment {


    private BarChart barChart;
    private ArrayList<BarChart> barChartArrayList;

    DatabaseReference refy = FirebaseDatabase.getInstance().getReference("YearlyData");

    List<YearlyData> yeardata = new ArrayList<YearlyData>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.odthirdfragment,container, false);

        barChart = v.findViewById(R.id.km_tf_pieChart);
        barChart.animateXY(1100,1100);
        barChartArrayList = new ArrayList<>();

        TextView tv = (TextView) v.findViewById(R.id.km_tf_question);
        assert getArguments() != null;
        tv.setText(getArguments().getString("msg"));



        //Add chart to the Page
        // for yearly data
        refy.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    Long year = (Long) keyNode.child("Year").getValue();

                    Long mal = (Long) keyNode.child("Male").getValue();

                    Long femal = (Long) keyNode.child("Female").getValue();

                    Long tot = (Long) keyNode.child("Total").getValue();

                    YearlyData obj = new YearlyData(year, mal, femal, tot);
                    yeardata.add(obj);
                }

                female_stat_calc(v);

                drawbarchart();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


        return v;
    }

    public static ThirdFragment newInstance(String text) {

        ThirdFragment f = new ThirdFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    public void female_stat_calc(View v){
        int total = 0;
        int female_count = 0;
        int i=0;

        for (; i < yeardata.size(); i++) {
            total = (int) (total + yeardata.get(i).getTotal());
            female_count = (int) (female_count + yeardata.get(i).getFemale());
        }

        float value = ((float) (female_count) / (float) total) * 100;

        TextView stat = (TextView) v.findViewById(R.id.km_tf_stat);
        stat.setText("On average, out of the total affected by Dementia over the past years "+ Math.round(value) +  "% are Females");
    }

    //drawing barchart
    public void drawbarchart(){
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setDescription(null);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);

        //data

        List<BarEntry> yVals1 = new ArrayList<BarEntry>();
        List<BarEntry> yVals2 = new ArrayList<BarEntry>();

        int i = 0;
//            for (; i < censusData.size(); i++) {
//                values.add(new Entry(i + 1, censusData.get(i).getMale()));


        for (; i < yeardata.size(); i++) {
            yVals1.add(new BarEntry(i, yeardata.get(i).getMale()));
            yVals2.add(new BarEntry(i, yeardata.get(i).getFemale()));
        }



        BarDataSet barDataSet = new BarDataSet(yVals1,"Male");
        barDataSet.setColor(Color.parseColor("#F44336"));
        BarDataSet barDataSet1 = new BarDataSet(yVals2,"Female");
        barDataSet1.setColors(Color.parseColor("#9C27B0"));

//            List<String> year_x = new ArrayList<String>();
//            for (; i < yeardata.size(); i++) {
//                year_x.add(yeardata.get(i).getYear().toString());
//            }

        BarData data = new BarData(barDataSet,barDataSet1);
        barChart.setData(data);

        String[] year_x = new String[] {"2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018"};

        final XAxis xAxis = barChart.getXAxis();
        final YAxis yAxis = barChart.getAxisRight();

        xAxis.setValueFormatter(new IndexAxisValueFormatter(year_x));
        barChart.getAxisLeft().setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawLabels(true);

        yAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);

        float barSpace = 0.02f;
        float groupSpace = 0.3f;
        int groupCount = 8;
        xAxis.setLabelCount(groupCount);

        //IMPORTANT *****
        data.setBarWidth(0.33f);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barChart.groupBars(0, groupSpace, barSpace);

    }



}
