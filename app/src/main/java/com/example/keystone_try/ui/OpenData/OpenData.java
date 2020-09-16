package com.example.keystone_try.ui.OpenData;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.keystone_try.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.text.method.LinkMovementMethod.getInstance;

public class OpenData extends Fragment {

    //insert codes for data extraction
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Census");
    DatabaseReference refy = FirebaseDatabase.getInstance().getReference("YearlyData");

    List<Fetch_data> censusData = new ArrayList<Fetch_data>();
    List<YearlyData> yeardata = new ArrayList<YearlyData>();

    private LineChart lineChart;
    ArrayList<LineChart> lineChartList;
    private BarChart barChart;
    private ArrayList<BarChart> barChartArrayList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupHyperlink(view);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_open_data, container, false);

        //lineChart = root.findViewById(R.id.OD_barchart);
//        lineChart.animateXY(1100,1100);
//        lineChartList = new ArrayList<>();
        barChart = root.findViewById(R.id.OD_barchart);
        barChart.animateXY(1100,1100);
        barChartArrayList = new ArrayList<>();

        //For the census data
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    String ageGroup = (String) keyNode.child("Age_Group").getValue();
//                    age_group.add(ageGroup);

                    Long mal = (Long) keyNode.child("Male").getValue();
//                    male.add(mal);

                    Long femal = (Long) keyNode.child("Female").getValue();
//                    female.add(femal);

                    Long tot = (Long) keyNode.child("Total").getValue();
//                    total.add(tot);

                    Fetch_data obj = new Fetch_data(ageGroup, mal, femal, tot);
                    censusData.add(obj);
                }
                //drawLineChart(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // for yearly data
        refy.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    Long year = (Long) keyNode.child("Year").getValue();
//                    age_group.add(year);

                    Long mal = (Long) keyNode.child("Male").getValue();
//                    male.add(mal);

                    Long femal = (Long) keyNode.child("Female").getValue();
//                    female.add(femal);

                    Long tot = (Long) keyNode.child("Total").getValue();
//                    total.add(tot);

                    YearlyData obj = new YearlyData(year, mal, femal, tot);
                    yeardata.add(obj);
                }
                drawbarchart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

//    public void print_census(){
//        for(Fetch_data cur: censusData){
//            Log.i("test_agegroup", cur.getAge_group());
//        }
//    }

////Chart Creation

//            int i = 0;
//            for (; i < censusData.size(); i++) {
//                values.add(new Entry(i + 1, censusData.get(i).getMale()));




    private void setupHyperlink(View v)  {

        TextView link = v.findViewById(R.id.OD_Content5);
       //link.setText(Html.fromHtml(getString(R.string.OD_link01)));
        link.setMovementMethod(LinkMovementMethod.getInstance());


    }
//    Bar chart
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

            List<String> year_x = new ArrayList<String>();
            for (; i < yeardata.size(); i++) {
                year_x.add(yeardata.get(i).getYear().toString());
            }

        BarData data = new BarData(barDataSet,barDataSet1);
        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(year_x));
        barChart.getAxisLeft().setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawLabels(true);

        barChart.isDrawBordersEnabled();


        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);

        float barSpace = 0.02f;
        float groupSpace = 0.3f;
        int groupCount = 8;

        //IMPORTANT *****
        data.setBarWidth(0.15f);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barChart.groupBars(0, groupSpace, barSpace);
    }

    private List<Long> getYear(List<Long> year) {
        return year;
    }


}
