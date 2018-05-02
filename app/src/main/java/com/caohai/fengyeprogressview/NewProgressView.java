package com.caohai.fengyeprogressview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class NewProgressView extends View {
    //风扇旋转的角度比，越大越快
    private static final int FAN_ROTATE_THAN_VALUE = 5000;
    //当风扇速度变化太小，int之后为零时的默认速度
    private static final int FAN_ROTATE_DEFAULT_SPEED = 10;

    //内容实际宽高
    private int contentWidth, contentHeight;
    //内容最左边的横坐标
    private int left;
    //边框bitmap
    private Bitmap borderBt;
    //风扇Bitmap
    private Bitmap fanBt;
    //边框要画的矩形区域（截取要画的区域）
    private Rect borderR;
    //边框要在画布上画的矩形区域
    private Rect borderCR;
    //风扇所画区域
    private Rect fanR;
    //风扇要画在画板上的区域
    private Rect fanCR;

    //风扇长宽高
    private int fanWidth;
    private int fanHeigh;

    //风扇中心点坐标
    private int fanCenterX;
    private int fanCenterY;

    //最大长度
    private int totalLength;
    //进度值0到1之间
    private float currentProgress;
    //进度变化时上一次的值
    private float lastProgress;
    //当前进度条要走多长
    private float progressLength;

    //风扇旋转角度
    private int fanRotateAngle;
    //风扇旋转速度
    private int fanRotateSpeed;
    private Paint mPaint;
    private Paint progressPaint;
    //左边的半圆弧半径
    private float arcRadius;
    //左边圆弧区域
    private RectF arcR;
    //圆弧扫过的角度的一半
    private float arcDegree;
    //右边矩形进度条区域
    private RectF progressRect;
    private List<NewYeZi> yeZis;
    private Bitmap leafBt;

    public NewProgressView(Context context) {
        super(context);
        init();
    }

    public NewProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NewProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NewProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setColor(Color.RED);

        fanRotateAngle = 0;
        currentProgress = 0f;
        lastProgress = 0f;
        progressLength = 0f;

        borderBt = BitmapFactory.decodeResource(getResources(), R.mipmap.leaf_kuang);
        //扩大两倍绘制（图片太小）
        contentWidth = borderBt.getWidth() * 2;
        contentHeight = borderBt.getHeight() * 2;
        arcRadius = contentHeight / 2;


        fanBt = BitmapFactory.decodeResource(getResources(), R.mipmap.fengshan);
        fanWidth = fanBt.getWidth() * 2;
        fanHeigh = fanBt.getHeight() * 2;


        leafBt = BitmapFactory.decodeResource(getResources(), R.mipmap.leaf);
        yeZis = new ArrayList<>();
    }

    /**
     * 外部调用，设置进度值
     *
     * @param progress
     */
    public void setProgress(float progress) {
        currentProgress = progress;
        fanRotateSpeed = (int) ((currentProgress - lastProgress) * FAN_ROTATE_THAN_VALUE);
        //当速度为零时默认为10
        fanRotateSpeed = fanRotateSpeed == 0 ? FAN_ROTATE_DEFAULT_SPEED : fanRotateSpeed;
        fanRotateAngle += fanRotateSpeed;
        fanRotateAngle = fanRotateAngle >= 360 ? 0 : fanRotateAngle;
        lastProgress = currentProgress;


        progressLength = totalLength * currentProgress;
        if (progressLength <= arcRadius) {
            //当前点在圆弧上的高度
            arcDegree = (int) Math.toDegrees(Math.acos((arcRadius - progressLength) / arcRadius));
        } else {
            progressRect.right = left + progressLength;
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMeasure = 0;

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMeasure = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            widthMeasure = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            widthMeasure = Math.min(widthSize, contentWidth + getPaddingLeft() + getPaddingRight());
        } else {
            widthMeasure = contentWidth + getPaddingLeft() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            heightMeasure = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            heightMeasure = Math.min(widthSize, contentHeight + getPaddingBottom() + getPaddingTop());
        } else {
            heightMeasure = contentHeight + getPaddingBottom() + getPaddingTop();
        }
        setMeasuredDimension(widthMeasure, heightMeasure);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //bitmap要画的区域
        borderR = new Rect(0, 0, contentWidth, contentHeight);
        //bitmap在画布的区域
        left = (w - contentWidth) / 2;
        int top = (h - contentHeight) / 2;
        int right = (w + contentWidth) / 2;
        int bottom = (h + contentHeight) / 2;
        borderCR = new Rect(left, top, right, bottom);


        //风扇参数
        fanR = new Rect(0, 0, fanWidth, fanHeigh);
        int fLeft = right - fanWidth - (bottom - top - fanWidth) / 2;
        int fTop = top + (bottom - top - fanWidth) / 2;
        int fRight = right - (bottom - top - fanWidth) / 2;
        int fBottom = bottom - (bottom - top - fanWidth) / 2;
        fanCenterX = fRight - fanWidth / 2;
        fanCenterY = fBottom - fanHeigh / 2;
        fanCR = new Rect(fLeft, fTop, fRight, fBottom);

        //弧形的区域
        arcR = new RectF(left, fanCenterY - arcRadius, left + arcRadius * 2, fanCenterY + arcRadius);
        //减2时防止分割处没有充满
        progressRect = new RectF(left + arcRadius - 2, fanCenterY - arcRadius, left + arcRadius - 2, fanCenterY + arcRadius);
        //最大值从最左边到风扇中心点
        totalLength = fanCenterX - left;

        for (int i = 0; i < 8; i++) {
            NewYeZi yeZi = new NewYeZi(leafBt, fanCenterX, fanCenterY, fanCenterX - left - 30);
            yeZis.add(yeZi);
        }
    }

    public void initView() {
        progressRect.left = left + arcRadius - 2;
        progressRect.top = fanCenterY - arcRadius;
        progressRect.right = left + arcRadius - 2;
        progressRect.bottom = fanCenterY + arcRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (NewYeZi yeZi : yeZis) {
            yeZi.drawLeaf(canvas);
        }

        drawProgress(canvas);

        canvas.drawBitmap(borderBt, borderR, borderCR, mPaint);

        canvas.save();
        canvas.rotate(fanRotateAngle, fanCenterX, fanCenterY);
        canvas.drawBitmap(fanBt, fanR, fanCR, mPaint);
        canvas.restore();

    }


    private void drawProgress(Canvas canvas) {
        canvas.drawArc(arcR, 180 - arcDegree, arcDegree * 2, false, progressPaint);
        canvas.drawRect(progressRect, progressPaint);
    }
}
