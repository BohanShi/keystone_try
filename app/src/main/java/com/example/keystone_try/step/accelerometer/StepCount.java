package com.example.keystone_try.step.accelerometer;

public class StepCount implements StepCountListener{

    private int count = 0;
    private int mCount = 0;
    private StepValuePassListener mStepValuePassListener;
    private long timeOfLastPeak = 0;
    private long timeOfThisPeak = 0;
    private StepDetector stepDetector;

    public StepCount() {
        stepDetector = new StepDetector();
        stepDetector.initListener(this);
    }
    public StepDetector getStepDetector(){
        return stepDetector;
    }

    /*
     * Take ten steps in a row to start counting steps count
     * If you walk for less than 9 steps and stay for more than 3 seconds, the count will be cleared
     * */
    @Override
    public void countStep() {
        this.timeOfLastPeak = this.timeOfThisPeak;
        this.timeOfThisPeak = System.currentTimeMillis();
        if (this.timeOfThisPeak - this.timeOfLastPeak <= 3000L) {
            if (this.count < 9) {
                this.count++;
            } else if (this.count == 9) {
                this.count++;
                this.mCount += this.count;
                notifyListener();
            } else {
                this.mCount++;
                notifyListener();
            }
        } else {//time out
            this.count = 1;
        }

    }

    public void initListener(StepValuePassListener listener) {
        this.mStepValuePassListener = listener;
    }

    public void notifyListener() {
        if (this.mStepValuePassListener != null)
            this.mStepValuePassListener.stepChanged(this.mCount);
    }


    public void setSteps(int initValue) {
        this.mCount = initValue;
        this.count = 0;
        timeOfLastPeak = 0;
        timeOfThisPeak = 0;
        notifyListener();
    }
}