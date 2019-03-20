package com.example.racer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Matrix;

public class Car {
    protected float x; // координаты
    protected float y;
    protected float sizeW;
    protected float sizeH;
    protected float speed;
    protected int bitmapId;
    protected Bitmap bitmap;
    protected boolean first=false;
    private int turnAngle=0;
    private boolean isTurning=false;
    private Context context;
    void init(Context context) {
        Bitmap cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        bitmap = Bitmap.createScaledBitmap(
                cBitmap, (int)(sizeW * GameView.unitW), (int)(sizeH * GameView.unitH), false);
        cBitmap.recycle();
        this.context=context;
    }

    void update(){
    }
    public void updateSprite(int spriteId){
        if(spriteId!=bitmapId) {
            bitmapId=spriteId;
            Bitmap cBitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
            bitmap = Bitmap.createScaledBitmap(
                    cBitmap, (int) (sizeW * GameView.unitW), (int) (sizeH * GameView.unitH), false);
            cBitmap.recycle();
        }
    }
    boolean animateMoving(String action,int targetValue,int step){
        switch(action){
            case "turn":
                turnAngle+=step;
                isTurning=Math.abs(turnAngle)<targetValue;
            case "":
                break;
        }
        return false;
    }
    public void driveLeft(){
            turnAngle=-15;
    }

    public void driveRight(){
            turnAngle = 15;
    }

    public void stopTurning(){
            turnAngle =0;

    }

    public static Bitmap RotateCar(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    void drow(Paint paint, Canvas canvas){
        canvas.drawBitmap(RotateCar(bitmap, turnAngle), x*GameView.unitW, y*GameView.unitH, paint);
    }
}
