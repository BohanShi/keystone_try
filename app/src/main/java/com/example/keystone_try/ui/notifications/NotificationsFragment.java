package com.example.keystone_try.ui.notifications;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.keystone_try.R;
import com.example.keystone_try.bean.GameOneScore;

import com.example.keystone_try.bean.GameTwoScore;
import com.example.keystone_try.step.utils.DbUtils;
import com.example.keystone_try.step.utils.SPHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private LineChart scoreChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        scoreChart = root.findViewById(R.id.scorechart);
        scoreChart.animateXY(1000,1000);
        scoreChart.getDescription().setEnabled(false);
        initData();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(getContext(), "jingzhi");
        }

        List<GameTwoScore> scoreList = DbUtils.getQueryAll(GameTwoScore.class);
        Collections.reverse(scoreList);
        ArrayList<Entry> values = new ArrayList<>();
        //adding data
        if (!scoreList.isEmpty()) {
            int i=0;
            for (; i<7&&i<scoreList.size(); i++) {
                GameTwoScore gameTwoScore = scoreList.get(i);
                values.add(new Entry(i+1,gameTwoScore.getScore()));
            }
            for (; i<7; i++) {
                values.add(new Entry(i+1, 0));
            }
        }

        xText();
        yText();

        //execute
        if (!values.isEmpty()) {
            text_all(values);
        }
    }

    private void xText() {
        XAxis xAxis = scoreChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
    }

    //Set y axis
    private void yText() {
        YAxis yAxisLeft = scoreChart.getAxisLeft();
        yAxisLeft.setEnabled(false);
        YAxis yAxisRight = scoreChart.getAxisRight();
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
        scoreChart.setData(data);
        scoreChart.invalidate();
    }

    public class MonthlyIntegerYValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return (int) (value) + "";
        }
    }
}

