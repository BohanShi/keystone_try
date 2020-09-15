package com.example.keystone_try.ui.OpenData;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.keystone_try.R;
import com.example.keystone_try.bean.GameOneScore;
import com.example.keystone_try.bean.GameTwoScore;
import com.example.keystone_try.step.utils.DbUtils;
import com.example.keystone_try.ui.notifications.NotificationsFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.text.method.LinkMovementMethod.getInstance;

public class OpenData extends Fragment {

    //insert codes for data extraction

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Census");

    List<String> age_group = new ArrayList<String>();
    List<Long> male = new ArrayList<Long>();
    List<Long> female = new ArrayList<Long>();
    List<Long> total = new ArrayList<Long>();

    private LineChart lineChart;
    ArrayList<LineChart> lineChartList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupHyperlink(view);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_open_data, container, false);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot keyNode : snapshot.getChildren())
                {
                    String ageGroup = (String) keyNode.child("Age_Group").getValue();
                    age_group.add(ageGroup);

                    Long mal = (Long) keyNode.child("Male").getValue();
                    male.add(mal);

                    Long femal = (Long) keyNode.child("Female").getValue();
                    female.add(femal);

                    Long tot = (Long) keyNode.child("Total").getValue();
                    total.add(tot);
                }
                Log.i("Age_grp ", String.valueOf(age_group));
                Log.i("Male ", String.valueOf(male));
                Log.i("Female ", String.valueOf(female));
                Log.i("Total ", String.valueOf(total));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //ref= null;



        //Chart Creation



//        lineChart = root.findViewById(R.id.OD_linechart);
//        lineChart.animateXY(1100,1100);
//        lineChartList = new ArrayList<>();
//
//        drawLineChart(1);
        return root;
    }
//
//        public void drawLineChart(int type) {
////        LineDataSet lineDataSet = new LineDataSet(lineChartArrayList, "record");
//            ArrayList<Entry> values = new ArrayList<>();
//
//            xText();
//            yText();
//            int i = 0;
//            for (; i < 7 && i < male.size(); i++) {
//                values.add(new Entry(i + 1, male.get(i)));
//            }
//            text_all(values);
//        }
//
//        private void xText() {
//
//            XAxis xAxis = lineChart.getXAxis();
//            xAxis.setPosition(XAxis.XAxisPosition.TOP);
//        }
//
//        private void yText() {
//            YAxis yAxisLeft = lineChart.getAxisLeft();
//            yAxisLeft.setEnabled(false);
//            YAxis yAxisRight = lineChart.getAxisRight();
//            yAxisRight.setEnabled(false);
//        }
//
//        private void text_all(ArrayList<Entry> values) {
//            LineDataSet set1;
//            set1 = new LineDataSet(values, "Total Males");
//            set1.setLabel("Age_group");
//            set1.setMode(LineDataSet.Mode.LINEAR);
//            set1.setColor(Color.BLACK);
//            set1.setLineWidth(3);
//            set1.setCircleRadius(4);
//            set1.enableDashedHighlightLine(2,2,1);
//            set1.setHighlightLineWidth(2);
//            set1.setHighlightEnabled(false);
//            set1.setHighLightColor(Color.RED);
//            set1.setValueTextSize(15);
//            set1.setDrawFilled(false);
//
//            LineData data = new LineData(set1);
//            data.setValueFormatter(new NotificationsFragment.MonthlyIntegerYValueFormatter());
//            lineChart.setData(data);
//            lineChart.invalidate();
//        }


    private void setupHyperlink(View v)  {

        TextView link = v.findViewById(R.id.OD_Content5);
       //link.setText(Html.fromHtml(getString(R.string.OD_link01)));
        link.setMovementMethod(LinkMovementMethod.getInstance());


    }


}
