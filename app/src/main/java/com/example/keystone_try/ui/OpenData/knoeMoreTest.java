package com.example.keystone_try.ui.OpenData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.keystone_try.R;

public class knoeMoreTest extends Fragment {



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
//        setContentView(R.layout.know_more_quiz);




        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.know_more_quiz, container, false);

        ViewPager2 pager = root.findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getActivity()));
        return root;
    }

    static class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch(position) {

                case 0: return FirstFragment.newInstance("Would you like to know about Dementia and some interesting facts about it!!");
                case 1: return SecondFragment.newInstance("What exactly is Dementia?");
                case 2: return ThirdFragment.newInstance("Who gets Dementia?");
                case 3: return FouthFragment.newInstance("What can be done to prevent?");
//                case 4: return ThirdFragment.newInstance("ThirdFragment, Instance 3");
                default: return DefaultFragment.newInstance("To still know more about Dementia Click the link below!!");
            }
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }



}
