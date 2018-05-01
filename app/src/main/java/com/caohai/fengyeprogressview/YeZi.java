package com.caohai.fengyeprogressview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.logging.LogRecord;

public class YeZi {
    private static final int MSG_WATH=0;
    private Context context;
    private Bitmap bitmap;
    private int pointX;
    private int pointY;
    private int minX;
    private int rotate;
    private int speed;
    private boolean isCompet;
    private int defaultHeight;
    private Paint mPaint;
    private Handler handler;
    private int firstX;

    public YeZi(Context context, Bitmap bitmap, int pointX, int pointY, int minX,int speed,int defaultHeight,Paint mPaint) {
        this.context=context;
        this.bitmap = bitmap;
        this.pointX = pointX;
        this.pointY = pointY;
        this.minX = minX;
        this.rotate=0;
        this.speed=speed;
        this.isCompet=false;
        this.defaultHeight=defaultHeight;
        this.mPaint=mPaint;
        firstX=pointX;
    }
    public void onDrawYeZi(Canvas canvas){
        Log.i("mytag","onDrawYeZi:"+pointX);
        Log.i("mytag","minX:"+minX);
        canvas.save();
        canvas.rotate(rotate, pointX, pointY + (float) Math.sin(pointX/2 * 3.14 / 180) * defaultHeight / 4);
        canvas.drawBitmap(bitmap, pointX, pointY + (float) Math.sin(pointX/2 * 3.14 / 180) * defaultHeight / 4, mPaint);
        canvas.restore();
    }
   public void moveYeZi(){
       pointX-=speed;
       rotate+=speed;
       if (pointX <= minX+50) {
           pointX=firstX;
       }
       if (rotate >= 360) {
           rotate = 0;
       }
   }
}
