package com.example.keystone_try.ui.OnBoarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.keystone_try.R;
import com.example.keystone_try.step.utils.DbUtils;
import com.example.keystone_try.step.utils.SPHelper;
import com.example.keystone_try.ui.MainActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private OnBoardingAdapter onBoardingAdapter;
    private LinearLayout layoutOnBoardingIndicators;
    private MaterialButton buttonOnboardingAction;
    private MaterialButton buttonOnboardingSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(this, "jingzhi");
        }

        skipOnBoardingpgae();
        startOnBoardingpage();

    }

    private void setUpOnboardingItems(){
        SPHelper.putInt(getApplicationContext(), "isFirstTime",1);

        List<OnBoardingItem> onBoardingItems = new ArrayList<>();

        OnBoardingItem itemOne = new OnBoardingItem();
//        itemOne.setTitle(R.string.nul);
//        itemOne.setDescription(R.string.nul);
        itemOne.setImage(R.drawable.finalob1);

        OnBoardingItem itemTwo = new OnBoardingItem();
//        itemTwo.setTitle(R.string.nul);
//        itemTwo.setDescription(R.string.nul);
        itemTwo.setImage(R.drawable.finalob2);

        OnBoardingItem itemThree = new OnBoardingItem();
//        itemThree.setTitle(R.string.nul);
//        itemThree.setDescription(R.string.nul);
        itemThree.setImage(R.drawable.finalob3);

        OnBoardingItem itemFour = new OnBoardingItem();
//        itemFour.setTitle(R.string.nul);
//        itemFour.setDescription(R.string.nul);
        itemFour.setImage(R.drawable.finalob4);
//
//        OnBoardingItem itemFive = new OnBoardingItem();
//        itemFive.setTitle(R.string.nul);
//        itemFive.setDescription(R.string.nul);
//        itemFive.setImage(R.drawable.new1);
//
//        OnBoardingItem itemSix = new OnBoardingItem();
//        itemSix.setTitle(R.string.nul);
//        itemSix.setDescription(R.string.nul);
//        itemSix.setImage(R.drawable.new1);

        onBoardingItems.add(itemOne);
        onBoardingItems.add(itemTwo);
        onBoardingItems.add(itemThree);
        onBoardingItems.add(itemFour);
//        onBoardingItems.add(itemFive);
//        onBoardingItems.add(itemSix);

        onBoardingAdapter = new OnBoardingAdapter(onBoardingItems);
    }

    private void setupOnboardingTndicators(){
        ImageView[] indicators = new ImageView[onBoardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for (int i = 0; i < indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.on_boarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnBoardingIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentOnboardingIndicator(int index){
        int childCount = layoutOnBoardingIndicators.getChildCount();
        for (int i = 0; i < childCount; i++){
            ImageView imageView = (ImageView)layoutOnBoardingIndicators.getChildAt(i);
            if (i == index){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.on_boarding_indicator_active)
                );
            }else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.on_boarding_indicator_inactive)
                );
            }
        }

        if (index == onBoardingAdapter.getItemCount() - 1){
            buttonOnboardingAction.setText("Start");
        }else {
            buttonOnboardingAction.setText("Next");
        }
    }

    private void startOnBoardingpage(){
        setContentView(R.layout.activity_page_login);
        layoutOnBoardingIndicators = findViewById(R.id.layoutOnBoardingIndicators);
        buttonOnboardingAction = findViewById(R.id.buttonOnboardAction);
        setUpOnboardingItems();

        buttonOnboardingSkip = findViewById(R.id.buttonOnboardSkip);
        buttonOnboardingSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        final ViewPager2 onboardingViewPager = findViewById(R.id.onBoardingViewPager);
        onboardingViewPager.setAdapter(onBoardingAdapter);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        buttonOnboardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onboardingViewPager.getCurrentItem() +1 < onBoardingAdapter.getItemCount()){
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
                }else {

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
        setupOnboardingTndicators();
        setCurrentOnboardingIndicator(0);
    }

    private void skipOnBoardingpgae(){
        if(SPHelper.getInt(getApplicationContext(),"isFirstTime") == 1){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

    }


}
