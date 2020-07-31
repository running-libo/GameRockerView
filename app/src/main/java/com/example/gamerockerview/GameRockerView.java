package com.example.gamerockerview;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * create by libo
 * create on 2020/7/30
 * description
 */
public class GameRockerView extends View {
    private Paint outerCirclePaint;
    private Paint innerCirclePaint;
    /** 内圆中心x坐标 */
    private double centerX;
    /** 内圆中心y坐标 */
    private double centerY;
    /** view宽高大小，设定宽高相等 */
    private int size;
    /** 外圆半径 */
    private int outerCircleRadius;
    /** 内圆半径 */
    private int innerCircleRadius;

    public GameRockerView(Context context) {
        super(context);
        init();
    }

    public GameRockerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(getResources().getColor(R.color.green));
        outerCirclePaint.setAntiAlias(true);
        outerCirclePaint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.INNER));

        innerCirclePaint = new Paint();
        innerCirclePaint.setAlpha(130);
        innerCirclePaint.setColor(getResources().getColor(R.color.deep_green));
        innerCirclePaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        size = getMeasuredWidth();
        setMeasuredDimension(size, size);

        centerX = size/2;
        centerY = size/2;
        outerCircleRadius = size/2;
        innerCircleRadius = size/4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(size/2, size/2, outerCircleRadius, outerCirclePaint);

        canvas.drawCircle((float) centerX, (float) centerY, innerCircleRadius, innerCirclePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleEvent(event);
                Log.i("minfo", "按下了");
                break;
            case MotionEvent.ACTION_MOVE:
                handleEvent(event);
                Log.i("minfo", "正在移动");
                break;
            case MotionEvent.ACTION_UP:
                Log.i("minfo", "抬起了");
                restorePosition();
                break;
        }

        return true;
    }

    /**
     * 处理手势事件
     */
    private void handleEvent(MotionEvent event) {
        double distance = Math.sqrt(Math.pow(event.getX()-size/2, 2) + Math.pow(event.getY()-size/2, 2)); //触摸点与view中心距离
        if (distance < outerCircleRadius-innerCircleRadius) {
            //在自由域之内
            Log.i("minfo", "自由域之内");
            //在自由域之内，触摸点实时作为内圆圆心
            centerX = event.getX();
            centerY = event.getY();
            invalidate();
        } else {
            Log.i("minfo", "自由域之外");
            //在自由域之外，内圆圆心在触摸点与外圆圆心的线段上
//            updateInnerCircelCenter(event);
        }
    }

    /**
     * 在自由域外更新内圆中心坐标
     */
    private void updateInnerCircelCenter(MotionEvent event) {
        double distance = Math.sqrt(Math.pow(event.getX()-size/2, 2) + Math.pow(event.getY()-size/2, 2));  //当前触摸点到圆心的距离
        int innerDistance = outerCircleRadius-innerCircleRadius;  //内圆圆心到中心点距离
        //相似三角形的性质，
        centerX = event.getX()*innerDistance/distance;
        centerY = event.getY()*innerDistance/distance;

        invalidate();
    }

    /**
     * 恢复内圆到view中心位置
     */
    private void restorePosition() {
        centerX = size/2;
        centerY = size/2;
        invalidate();
    }

}
