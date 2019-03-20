package com.example.racer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable{
    public static int maxX = 20; // размер по горизонтали
    public static int maxY = 28; // размер по вертикали
    public static float unitW = 0; // пикселей в юните по горизонтали
    public static float unitH = 0; // пикселей в юните по вертикали
    public boolean firstTime = true;
    private boolean gameRunning = true;
    private boolean isCollision=false;
    public static int metersGone=0;
    private PlayerCar playerCar;
    private Thread gameThread = null;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private ArrayList<EnemyCar> enemyCars = new ArrayList<>(); // тут будут харанится астероиды
    private final int CAR_INTERVAL = 50; // время через которое появляются астероиды (в итерациях)
    private int currentTime = 0;
    Bitmap bitmap;// = BitmapFactory.decodeResource(getResources(), R.drawable.road);
    Bitmap scaledBitmap ;//= Bitmap.createScaledBitmap(bitmap, canvas.getWidth(),    canvas.getHeight(), true);
    private int roadPositionY=0;
    private int roadMoveStep=30;

    public GameView(Context context) {
        super(context);
        //инициализируем обьекты для рисования
        surfaceHolder = getHolder();
        paint = new Paint();
        // инициализируем поток
        gameThread = new Thread(this);
        gameThread.start();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.road);
    }

    @Override
    public void run() {

        while (MainActivity.gameRunning) {
            if(!MainActivity.gamePaused) {
                update();
                draw();
                checkCollision();
                checkIfNewCar();
                control();
            }
        }
    }

    private void update() {
        if(!firstTime) {
            playerCar.update();
            for (EnemyCar enemyCar : enemyCars) {
                enemyCar.update();
            }
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            if(firstTime){
                metersGone=0;
                isCollision=false;
                firstTime = false;
                unitW = surfaceHolder.getSurfaceFrame().width()/maxX;
                unitH = surfaceHolder.getSurfaceFrame().height()/maxY;
                playerCar = new PlayerCar(getContext());
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, canvas.getWidth(),    canvas.getHeight()+300, true);
                enemyCars = new ArrayList<>();
            }
            if(isCollision){
                playerCar.updateSprite( R.drawable.btoom);
                MainActivity.stopGame(true);
            }
            metersGone++;
            if(MainActivity.isSpeedPressed){
                metersGone+=2;
            }
            MainActivity.scoreMetersTextView.setText("Пройдено: "+metersGone+" м");
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            if(roadPositionY>=0){
                roadPositionY=-300;
            }
            else{
              roadPositionY+=roadMoveStep;
            }
            canvas.drawBitmap(scaledBitmap, 0, roadPositionY, paint);
            this.invalidate();
            playerCar.drow(paint, canvas);

            for(EnemyCar enemyCar : enemyCars){
                enemyCar.drow(paint, canvas);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            if(firstTime) {
                gameThread.sleep(20);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkCollision(){
        for (EnemyCar enemyCar : enemyCars) {
            if(enemyCar.isCollision(playerCar.x, playerCar.y, playerCar.sizeW)){
                isCollision=true;

                // TODO добавить анимацию взрыва
            }
        }
    }

    private void checkIfNewCar(){
        if(currentTime >= CAR_INTERVAL){
            EnemyCar enemyCar = new EnemyCar(getContext());
            enemyCars.add(enemyCar);
            currentTime = 0;
        }else{
            currentTime ++;
        }
    }

}
