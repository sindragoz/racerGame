package com.example.racer;

import android.content.Context;
import java.util.Random;

public class EnemyCar extends Car {
    protected float offset = 1.5f; // радиус
    //private float minSpeed = (float) 0.1; // минимальная скорость
    private float maxSpeed = (float) 0.3; // максимальная скорость
    private int roadLineWidth=7;
    public EnemyCar(Context context) {
        Random random = new Random();

        bitmapId = R.drawable.enemy_car1;
        y=0;
        x = offset+random.nextInt(3)*roadLineWidth;
        sizeW = 4;
        sizeH = 5;
        speed = maxSpeed;//minSpeed + (maxSpeed - minSpeed) * random.nextFloat();

        init(context);
    }

    @Override
    public void update() {
        y += speed;
    }

    public boolean isCollision(float carX, float carY, float carSize) {
        return !(((x+sizeW-offset) < carX)||(x > (carX+carSize-offset))||((y+sizeH-offset) < carY)||(y > (carY+carSize-offset)));
    }
}
