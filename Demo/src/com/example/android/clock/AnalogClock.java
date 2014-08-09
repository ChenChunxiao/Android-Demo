package com.example.android.clock;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class AnalogClock extends View {
    private Paint mPaint;
    private Paint mSmallPaint;
    private Timer mTimer;
    private final String TAG = "AnalogClock";

    private static int mDialRadius = 300; // 表盘半径
    private static int mDialEdgeWidth = 30; // 表盘边缘的size
    private static int mPadding = 0; // 表盘外边距
    private static int mWidth = mDialRadius * 2 + mDialEdgeWidth + mPadding * 2;
    private static int mHeight = mWidth;
    private static final int mPrimaryColor = Color.WHITE;

    public AnalogClock(Context context) {
        super(context);
        init();
    }

    public AnalogClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AnalogClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setAntiAlias(true);

        mSmallPaint = new Paint(mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth / 2, mHeight / 2);

        drawDial(canvas);
        drawTime(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = resolveSizeAndState(mWidth, widthMeasureSpec, 1);
        // int measuredHeight = resolveSizeAndState(mHeight, heightMeasureSpec, 1);

        setMeasuredDimension(measuredWidth, measuredWidth);

        resolveDialSize();
    }

    // 根据View的size，重新计算合适的表盘半径
    private void resolveDialSize() {
        mWidth = getMeasuredWidth();
        mHeight = mWidth;

        int min = Math.min(mWidth, mHeight);

        if (mDialRadius * 2 + mDialEdgeWidth > min) {
            mDialRadius = (min - mDialEdgeWidth - mPadding) / 2;
        }
    }

    private void drawDial(Canvas canvas) {
        // 表盘
        resetPaint(mPaint, 0xffbdbdbd, mDialEdgeWidth, Style.STROKE);
        canvas.drawCircle(0, 0, mDialRadius, mPaint);

        resetPaint(mPaint, mPrimaryColor, 3, Style.FILL);
        resetPaint(mSmallPaint, mPrimaryColor, 1, Style.FILL);

        canvas.save();
        for (int i = 0; i < 60; ++i) {
            if (i % 5 == 0) {
                final float startY = mDialRadius - mDialEdgeWidth / 2 - 15f;
                final float stopY = mDialRadius - mDialEdgeWidth / 2;
                canvas.drawLine(0f, startY, 0f, stopY, mPaint);
                // canvas.drawText(String.valueOf(i / 5 + 1), -8f, 230f, mSmallPaint);
            } else {
                final float startY = mDialRadius - mDialEdgeWidth / 2 - 10f;
                final float stopY = mDialRadius - mDialEdgeWidth / 2;
                canvas.drawLine(0f, startY, 0f, stopY, mSmallPaint);
            }
            canvas.rotate(6f, 0f, 0f);
        }
        canvas.restore();
    }

    private void drawTime(Canvas canvas) {
        // 中心原点
        mSmallPaint.setColor(Color.GRAY);
        mSmallPaint.setStyle(Style.FILL);
        canvas.drawCircle(0, 0, 12f, mSmallPaint);
        mSmallPaint.setColor(mPrimaryColor);
        mSmallPaint.setStyle(Style.FILL);
        canvas.drawCircle(0, 0, 8f, mSmallPaint);

        // 指针：时、分、秒
        final int second = Calendar.getInstance().get(Calendar.SECOND);
        final float minutes = Calendar.getInstance().get(Calendar.MINUTE) + (float) second / 60;
        final float hour = Calendar.getInstance().get(Calendar.HOUR) + (float) minutes / 60;

        final float secondRadian = (float) Math.PI * 2 * second / 60;
        final float minuteRadian = (float) Math.PI * 2 * minutes / 60;
        final float hourRadian = (float) Math.PI * 2 * hour / 12;

        drawPointer(canvas, secondRadian, mDialRadius * 0.8f);
        drawPointer(canvas, minuteRadian, mDialRadius * 0.65f);
        drawPointer(canvas, hourRadian, mDialRadius * 0.5f);
    }

    private void drawPointer(Canvas canvas, float radian, float radius) {
        float startX = (float) Math.sin(radian) * radius;
        float startY = -(float) Math.cos(radian) * radius;

        float stopX = -(float) Math.sin(radian) * 24;
        float stopY = (float) Math.cos(radian) * 24;

        resetPaint(mPaint, mPrimaryColor, 3, Style.FILL);
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
    }

    private void resetPaint(Paint paint, int color, int width, Paint.Style style) {
        paint.setStyle(style);
        paint.setColor(color);
        paint.setStrokeWidth(width);
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(TAG, "attach");
        super.onAttachedToWindow();
        startTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(TAG, "detach");
        stopTimer();
        super.onDetachedFromWindow();
    }

    private void startTimer() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                postInvalidate();
            }
        }, 0, 500);
    }

    private void stopTimer() {
        mTimer.cancel();
        mTimer = null;
    }
}
