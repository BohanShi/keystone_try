package com.example.keystone_try.game2;

/**
 * leaned from Youtuber AtoTalKs
 */
class AnimationCell extends Cell {
    public final int[] extras;
    private final int animationType;
    private final long animationTime;
    private final long delayTime;
    private long timeElapsed;

    /**
     * constructor of class
     * @param x Cell x axis value
     * @param y Cell y axis value
     * @param animationType which kind of animation, remove number, add new, or merge
     * @param length animation time
     * @param delay animation delay time
     * @param extras numbers
     */
    public AnimationCell(int x, int y, int animationType, long length, long delay, int[] extras) {
        super(x, y);
        this.animationType = animationType;
        animationTime = length;
        delayTime = delay;
        this.extras = extras;
    }

    public int getAnimationType() {
        return animationType;
    }

    /**
     * add new number
     * @param timeElapsed new number flag
     */
    public void tick(long timeElapsed) {
        this.timeElapsed = this.timeElapsed + timeElapsed;
    }

    /**
     * is animation finished
     * @return
     */
    public boolean animationDone() {
        return animationTime + delayTime < timeElapsed;
    }

    /**
     * get process of animation
     * @return
     */
    public double getPercentageDone() {
        return Math.max(0, 1.0 * (timeElapsed - delayTime) / animationTime);
    }

    /**
     * is animation activated
     * @return
     */
    public boolean isActive() {
        return (timeElapsed >= delayTime);
    }

}
