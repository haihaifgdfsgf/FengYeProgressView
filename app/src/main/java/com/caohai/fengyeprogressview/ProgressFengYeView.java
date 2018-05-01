package com.caohai.fengyeprogressview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by 曹海 on 2018/4/28.
 * QQ：185493676
 */

public class ProgressFengYeView extends View {
    private Context context;
    private Paint mPaint;
    private int defaultWidth, width, fWidth, fCenterX, left;
    private int defaultHeight, height, fHeight, fCenterY;
    private Bitmap kBitamp;
    private Bitmap fBitmap;
    private Bitmap yBitmap;
    private Rect kR, fR, yR;
    private Rect cR, fcR, ycR;
    private RectF progressR;
    private int rotate;
    private boolean rotateFlag = false;
    private int speed, progress;
    private int maxProgress;
    private int yPointX, yPointY;
    private YeZi mYeZi;
    private YeZi mYeZi1;
    public ProgressFengYeView(Context context) {
        super(context);
        init(context);
    }

    public ProgressFengYeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressFengYeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressFengYeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context mContext) {
        this.context=context;
        rotate = 0;
        speed = 10;
        progress = 0;
        kBitamp = BitmapFactory.decodeResource(getResources(), R.mipmap.leaf_kuang);
        fBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.fengshan);
        yBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.leaf);
        //长宽扩大两倍
        defaultWidth = kBitamp.getWidth() * 2;
        defaultHeight = kBitamp.getHeight() * 2;
        fWidth = fBitmap.getWidth() * 2;
        fHeight = fBitmap.getHeight() * 2;
        Log.i("mytag", "defaultWidth:" + defaultWidth);
        Log.i("mytag", "defaultHeight:" + defaultHeight);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
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
            widthMeasure = Math.min(widthSize, defaultWidth + getPaddingLeft() + getPaddingRight());
        } else {
            widthMeasure = defaultWidth + getPaddingLeft() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            heightMeasure = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            heightMeasure = Math.min(widthSize, defaultHeight + getPaddingBottom() + getPaddingTop());
        } else {
            heightMeasure = defaultHeight + getPaddingBottom() + getPaddingTop();
        }
        setMeasuredDimension(widthMeasure, heightMeasure);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        Log.i("mytag", "defaultWidth:" + defaultWidth);
        Log.i("mytag", "height:" + height);
        //bitmap要画的区域
        kR = new Rect(0, 0, defaultWidth, defaultHeight);
        //bitmap在画布的区域
        left = (w - defaultWidth) / 2;
        int top = (h - defaultHeight) / 2;
        int right = (w + defaultWidth) / 2;
        int bottom = (h + defaultHeight) / 2;
        maxProgress = defaultWidth + left - defaultHeight / 2;
        progress = left;
        Log.i("mytag", "maxProgress:" + maxProgress);
        cR = new Rect(left, top, right, bottom);
        progressR = new RectF(left, top, progress, bottom);
        fR = new Rect(0, 0, fWidth, fHeight);
        int fLeft = right - fWidth - (bottom - top - fWidth) / 2;
        int fTop = top + (bottom - top - fWidth) / 2;
        int fRight = right - (bottom - top - fWidth) / 2;
        int fBottom = bottom - (bottom - top - fWidth) / 2;
        fCenterX = fRight - fWidth / 2;
        fCenterY = fBottom - fHeight / 2;
        yPointX = fCenterX;
        yPointY = fCenterY;
        fcR = new Rect(fLeft, fTop, fRight, fBottom);


        int yWidth = yBitmap.getWidth() * 2;
        int yHeight = yBitmap.getHeight() * 2;
        yR = new Rect(0, 0, yWidth, yHeight);
//        ycR=new Rect()
       mYeZi= new YeZi(context,yBitmap,yPointX,yPointY,left,speed,defaultHeight,mPaint);
       mYeZi1= new YeZi(context,yBitmap,yPointX+defaultHeight/4,yPointY+5,left,speed,defaultHeight,mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.save();
//        canvas.rotate(rotate, yPointX, yPointY + (float) Math.sin(yPointX * 3.14 / 180) * defaultHeight / 4);
//        canvas.drawBitmap(yBitmap, yPointX, yPointY + (float) Math.sin(yPointX * 3.14 / 180) * defaultHeight / 4, mPaint);
//        canvas.restore();
        canvas.save();
        mYeZi.onDrawYeZi(canvas);
        mYeZi1.onDrawYeZi(canvas);
        canvas.restore();
        mPaint.setColor(Color.parseColor("#aa0000"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        progressR.right = progress;
       canvas.drawRect(progressR, mPaint);
        mPaint.setXfermode(null);
        canvas.drawBitmap(kBitamp, kR, cR, mPaint);
        drawFengShan(canvas);

    }

    private void drawFengShan(final Canvas canvas) {
        canvas.save();
        canvas.rotate(rotate, fCenterX, fCenterY);
        canvas.drawBitmap(fBitmap, fR, fcR, mPaint);
        canvas.restore();
        if (!rotateFlag) {
            rotateFlag = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (rotateFlag) {
                        rotate += speed;
                        progress += 1;
                        yPointX -= speed;
                        mYeZi.moveYeZi();
                        mYeZi1.moveYeZi();
                        if (rotate >= 360) {
                            rotate = 0;
                        }
                        if (progress >= maxProgress) {
                            progress = left;
                        }
                        if (yPointX <= left) {
                            yPointX = fCenterX;
                        }
                        postInvalidate();
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

    }
}
