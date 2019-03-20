package com.example.racer;

import android.content.Context;

public class PlayerCar extends Car {
    boolean carIsTurned=false;
    private float minCarBottomOffset;
    public PlayerCar(Context context) {
        bitmapId = R.drawable.my_car2;
        sizeW = 5;
        sizeH=6;
        x=7;
        minCarBottomOffset=GameView.maxY - sizeH - 3;
        y=GameView.maxY - sizeH - 3;
        speed = (float) 0.3;
        init(context);
}

    @Override
    public void update() {
        if(MainActivity.isLeftPressed && x >= 0){
            x -= speed;
                driveLeft();
        }else
        if(MainActivity.isRightPressed && x <= GameView.maxX - 5){
            x += speed;

                driveRight();

        }
        else{
            stopTurning();
        }
        if (MainActivity.isSpeedPressed){
                updateSprite(R.drawable.my_car2_boosted1);
                if(y>=GameView.maxY/2) {
                    y -= 0.2f;
                }else
                    if(y>0){
                        y-=0.1f;
                    }
            //stopTurning();
        }
        else{
            updateSprite( R.drawable.my_car2);
            if(y<minCarBottomOffset){
                y+=0.25f;
            }
        }

    }
}
