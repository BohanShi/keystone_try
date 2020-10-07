package com.example.keystone_try.ui.OpenData;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.keystone_try.R;

public class DefaultFragment extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.oddefaultfragment,container, false);

        TextView tv = (TextView) v.findViewById(R.id.km_df_text);
        assert getArguments() != null;
        tv.setText(getArguments().getString("msg"));

        setupHyperlink(v);

        return v;
    }

    public static DefaultFragment newInstance(String text) {

        DefaultFragment f = new DefaultFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }


    private void setupHyperlink(View v)  {

        TextView link = v.findViewById(R.id.km_df_link);
        //link.setText(Html.fromHtml(getString(R.string.OD_link01)));
        link.setMovementMethod(LinkMovementMethod.getInstance());


    }
}
