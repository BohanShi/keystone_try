package com.example.keystone_try.ui.OnBoarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.keystone_try.R;
import com.example.keystone_try.step.utils.DbUtils;
import com.example.keystone_try.ui.MainActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        startOnBoardingpage();

    }

    private void setUpOnboardingItems(){
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

    }

    public String getUUID() {

        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                serial = android.os.Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}
