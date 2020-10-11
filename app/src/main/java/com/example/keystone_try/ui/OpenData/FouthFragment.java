package com.example.keystone_try.ui.OpenData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.keystone_try.R;

public class FouthFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.odfourthfragment,container, false);

        TextView tv = (TextView) v.findViewById(R.id.km_frf_question);
        assert getArguments() != null;
        tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static FouthFragment newInstance(String text) {

        FouthFragment f = new FouthFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}