package com.caohai.fengyeprogressview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

public class NewYeZi {
    //A表示振动y=Asin（ωx）的振幅，ω周期
    //振幅
    private int amplitude;
    //周期
    private int cycle;
    private Bitmap leafBt;
    private int startX;
    private int startY;
    private int rotateDegree;
    private int speed;
    private Random random;
    private int moveX;
    private int moveY;
    private int currentDegree;
    private int moveMax;

    public NewYeZi(Bitmap leafBt, int startX, int startY, int moveMax) {
        this.leafBt = leafBt;
        this.startX = startX;
        this.startY = startY;
        this.moveMax = moveMax;
        this.cycle = moveMax;
        random = new Random();
        rotateDegree = random.nextInt(3 + 3 + 1) - 3;
        rotateDegree = rotateDegree == 0 ? 1 : rotateDegree;
        speed = random.nextInt(5 - 2 + 1) + 2;
        moveX = 0;
        moveY = 0;
        currentDegree = 0;
        amplitude = random.nextInt(40 - 5 + 1) + 5;

    }

    public void drawLeaf(Canvas canvas) {
        moveY = (int) (amplitude * Math.sin(Math.toRadians(-cycle) / (2 * Math.PI) * Math.toRadians(-moveX)));
        Log.i("mytag", "amplitude:" + amplitude);
        Log.i("mytag", "moveY:" + Math.sin(Math.sin(Math.toRadians(moveX)) * (cycle / (2 * Math.PI))));

        canvas.save();
        canvas.rotate(currentDegree, startX - moveX, moveY + startY);
        canvas.drawBitmap(leafBt, startX - moveX, moveY + startY, null);
        canvas.restore();


        moveX += speed;
        // moveX = moveX >= moveMax ? 0 : moveX;
        if (moveX >= moveMax) {
            moveX = 0;
            //重新初始化
            speed = random.nextInt(5 - 2 + 1) + 2;
            amplitude = random.nextInt(40 - 5 + 1) + 5;
        }
        currentDegree += rotateDegree;
        //currentDegree = currentDegree >= 360 ? 0 : currentDegree;
        if (currentDegree >= 360) {
            currentDegree = 0;
            rotateDegree = random.nextInt(3 + 3 + 1) - 3;
            rotateDegree = rotateDegree == 0 ? 1 : rotateDegree;
        }
    }
}
