package com.example.keystone_try.Util;

import com.example.keystone_try.R;

import java.util.ArrayList;

public class MetionString {

   public int returnValue(){
        ArrayList<Integer> stringList = new ArrayList<>();
        stringList.add(R.string.metion1);
        stringList.add(R.string.metion2);
        stringList.add(R.string.metion3);
       /// stringList.add(R.string.metion4);
      //  stringList.add(R.string.metion5);
      //  stringList.add(R.string.metion6);
       // stringList.add(R.string.metion7);
       // stringList.add(R.string.metion8);

        int a = (int) (Math.random()*stringList.size());

        return stringList.get(a);
    }
}
