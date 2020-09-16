package com.example.keystone_try.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.example.keystone_try.Constant;

public class ChartView extends View{
    public int XPoint=40;    //X coordinate of origin
    public int YPoint= Constant.point.y/2;     //X coordinate of origin
    public int XScale=55;     //X scale length
    public int YScale=40;     //Y scale length
    public int XLength=Constant.point.x-100;        //X axis length
    public int YLength=Constant.point.y/2-100;        //Y axis length
    public String[] XLabel;    //X scale
    public String[] YLabel;    //Y scale
    public String[] Data;      //data
    public String Title;    //Displayed title
    public ChartView(Context context)
    {
        super(context);
    }
    public void SetInfo(String[] XLabels,String[] YLabels,String[] AllData,String strTitle)
    {
        XLabel=XLabels;
        YLabel=YLabels;
        Data=AllData;
        Title=strTitle;
        XScale=XLength/AllData.length;//Actual X scale length
        YScale=YLength/YLabels.length;
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);//Override the onDraw method

        //canvas.drawColor(Color.WHITE);//Set background color
        Paint paint= new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);//Anti-aliasing
        paint.setColor(Color.BLACK);//colour
        Paint paint1=new Paint();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setAntiAlias(true);//Anti-aliasing
        paint1.setColor(Color.DKGRAY);
        paint.setTextSize(12);  //Set the axis text size
        //Set Y axis
        canvas.drawLine(XPoint, YPoint-YLength, XPoint, YPoint, paint);   //Axis
        for(int i=0;i*YScale<YLength ;i++)
        {
            canvas.drawLine(XPoint,YPoint-i*YScale, XPoint+5, YPoint-i*YScale, paint);  //Scale
            try
            {
                canvas.drawText(YLabel[i] , XPoint-30, YPoint-i*YScale+5, paint);  //Text
            }
            catch(Exception e)
            {
            }
        }
        canvas.drawLine(XPoint,YPoint-YLength,XPoint-3,YPoint-YLength+6,paint);  //arrow
        canvas.drawLine(XPoint,YPoint-YLength,XPoint+3,YPoint-YLength+6,paint);
        //Set font size, angle, etc.
        paint.setTextSize(20);
        drawText(canvas,"单位:kWh", XPoint-5, YPoint-YLength+YScale-5, paint,-90);

        //设置X轴
        paint.setTextSize(12);
        canvas.drawLine(XPoint,YPoint,XPoint+XLength,YPoint,paint);   //Axis
        for(int i=0;i*XScale<XLength;i++)
        {
            canvas.drawLine(XPoint+i*XScale, YPoint, XPoint+i*XScale, YPoint-5, paint);  //Scale
            try
            {
//                canvas.drawText(XLabel[i], XPoint + i * XScale - 10,
//                        YPoint + 20, paint); // Text
                drawText(canvas,XLabel[i], XPoint + i * XScale,
                        YPoint + 40, paint,-45); // Text
                // 数据值
                if (i > 0 && YCoord(Data[i - 1]) != -999
                        && YCoord(Data[i]) != -999) // Make data valid
                    canvas.drawLine(XPoint + (i - 1) * XScale,
                            YCoord(Data[i - 1]), XPoint + i * XScale,
                            YCoord(Data[i]), paint);
                canvas.drawCircle(XPoint + i * XScale, YCoord(Data[i]), 2,
                        paint);
            } catch (Exception e) {
            }
        }
        canvas.drawLine(XPoint+XLength,YPoint,XPoint+XLength-6,YPoint-3,paint);    //arrow
        canvas.drawLine(XPoint+XLength,YPoint,XPoint+XLength-6,YPoint+3,paint);
        //Set title position
        paint.setTextSize(28);
        canvas.drawText(Title, XLength/2-28, 50, paint);
    }
    //Set text display direction
    void drawText(Canvas canvas ,String text , float x ,float y,Paint paint ,float angle){
        if(angle != 0){
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, paint);
        if(angle != 0){
            canvas.rotate(-angle, x, y);
        }
    }

    private int YCoord(String y0)  //Calculate the Y coordinate when drawing, return -999 when there is no data
    {
        int y;
        try
        {
            y=Integer.parseInt(y0);
        }
        catch(Exception e)
        {
            return -999;    //Return on error -999
        }
        try
        {
            return YPoint-y*YScale/Integer.parseInt(YLabel[1]);
        }
        catch(Exception e)
        {
        }
        return y;
    }
}