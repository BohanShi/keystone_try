package com.example.keystone_try.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.keystone_try.R;

public class StepView extends View {

    private int back_color = Color.YELLOW;
    private int cover_color = Color.BLUE;
    private int font_color = Color.BLUE;
    private int border_width;
    private int font_size;

    private Paint back_paint;
    private Paint cover_paint;
    private Paint text_paint;

    private int currentStep;
    private int goalStep = 8000;

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
        invalidate();
    }

    public void setGoalStep(int goalStep) {
        this.goalStep = goalStep;
        invalidate();
    }

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        back_color = array.getColor(R.styleable.StepView_back_color, back_color);
        cover_color = array.getColor(R.styleable.StepView_cover_color, cover_color);
        font_color = array.getColor(R.styleable.StepView_font_color, font_color);
        border_width = (int) array.getDimension(R.styleable.StepView_border_width, border_width);
        font_size = array.getDimensionPixelSize(R.styleable.StepView_font_size, font_size);
        array.recycle();

        back_paint = new Paint();
        back_paint.setAntiAlias(true);
        back_paint.setColor(back_color);
        back_paint.setStrokeWidth(border_width);
        back_paint.setStyle(Paint.Style.STROKE);
        back_paint.setStrokeCap(Paint.Cap.ROUND);

        cover_paint = new Paint();
        cover_paint.setAntiAlias(true);
        cover_paint.setColor(cover_color);
        cover_paint.setStrokeWidth(border_width);
        cover_paint.setStyle(Paint.Style.STROKE);
        cover_paint.setStrokeCap(Paint.Cap.ROUND);

        text_paint = new Paint();
        text_paint.setAntiAlias(true);
        text_paint.setColor(cover_color);
        text_paint.setTextSize(font_size);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width > height ? height : width, width > height ? height : width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth()/2;
        int radius = (getWidth() - border_width)/2;
        RectF rectF = new RectF((float)(border_width/2), (float)(border_width/2), center+radius, center+radius);
        canvas.drawArc( rectF, 135, 270, false, back_paint);

        float radio = (float) currentStep/goalStep;
        if (radio > 1){
            radio = 1;
        }
        canvas.drawArc( rectF, 135, (float) (270 * radio), false, cover_paint);

        String steps = currentStep+"";
        Rect rect = new Rect();
        text_paint.getTextBounds(steps, 0, steps.length(), rect);
        int dx = getWidth()/2 - rect.width()/2;
        Paint.FontMetricsInt fontMetricsInt = text_paint.getFontMetricsInt();
        int dy = fontMetricsInt.bottom - fontMetricsInt.top;
        int baseline = getHeight()/2 - dy/2 + 100;
        canvas.drawText(steps, dx, baseline, text_paint);

    }
}
