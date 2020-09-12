package com.example.keystone_try.ui.OnBoarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keystone_try.R;

import java.util.List;

public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingAdapter.OnbarodingViewHolder> {

    private List<OnBoardingItem> onBoardingItems;

    public OnBoardingAdapter(List<OnBoardingItem> onBoardingItems) {
        this.onBoardingItems = onBoardingItems;
    }

    @NonNull
    @Override
    public OnbarodingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnbarodingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_onboarding, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnbarodingViewHolder holder, int position) {
        holder.setOnBoardingData(onBoardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onBoardingItems.size();
    }

    class OnbarodingViewHolder extends RecyclerView.ViewHolder{

        private TextView textTitle;
        private TextView textDescription;
        private ImageView imageOnBoarding;

        public OnbarodingViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitleOnboarding);
            textDescription = itemView.findViewById(R.id.textDescOnboarding);
            imageOnBoarding = itemView.findViewById(R.id.imageOnboarding);
        }

        void setOnBoardingData(OnBoardingItem onBoardingItem){
            textTitle.setText(onBoardingItem.getTitle());
            textDescription.setText(onBoardingItem.getDescription());
            imageOnBoarding.setImageResource(onBoardingItem.getImage());
        }
    }


}
