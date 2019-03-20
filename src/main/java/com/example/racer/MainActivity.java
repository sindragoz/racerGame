package com.example.racer;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    public static boolean isLeftPressed = false; // нажата левая кнопка
    public static boolean isRightPressed = false; // нажата правая кнопка
    public static boolean isSpeedPressed = false;
    private static MenuActivity mainMenu;
    public static boolean firstStart = true;
    public static boolean gameRunning = true;
    public static boolean gamePaused = true;
    static GameView gameView;
    public static TextView scoreMetersTextView;
    public static String playerName = "";
    static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = new GameView(this);
        LinearLayout gameLayout = (LinearLayout) findViewById(R.id.gameLayout);
        gameLayout.addView(gameView);
        mainMenu = new MenuActivity();
        ImageButton leftButton = (ImageButton) findViewById(R.id.leftButton);
        ImageButton rightButton = (ImageButton) findViewById(R.id.rightButton);
        ImageButton speedButton = (ImageButton) findViewById(R.id.speedButton);
        ImageButton menuButton = (ImageButton) findViewById(R.id.menuButton);
        leftButton.setOnTouchListener(this);
        rightButton.setOnTouchListener(this);
        speedButton.setOnTouchListener(this);
        menuButton.setOnTouchListener(this);
        scoreMetersTextView = (TextView) findViewById(R.id.metersTextBox);
        fm = getFragmentManager();
        mainMenu.show(fm, "menu");
//        mainMenu.disableContinueBtn(true);
    }

    public static void startGame() {
        mainMenu.dismiss();
        gamePaused = false;
    }

    public static void stopGame(boolean gameOver) {
        if (!gameOver) {
            gamePaused = true;
            return;
        } else {
            saveScore();
            mainMenu.show(fm, "menu");
            gamePaused = true;
        }
    }

    private static void saveScore() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.0.100:8080/save-score?name=" + playerName + "&score=" +GameView.metersGone;
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("SERVER RESPONSE", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
//                        final JSONArray jsonArr;
//                        String resp = response.body().string();
//                        jsonArr = new JSONArray(resp);
                        //  mainMenu.post(new Runnable() {
                        // @Override
                        //  public void run() {
//                        String scoreTableString = "";
//                        for (int i = 0; i < jsonArr.length(); i++) {
//                            try {
//                                JSONObject scoreObj = jsonArr.getJSONObject(i);
//                                scoreTableString += (i + 1) + ". " + jsonArr.getJSONObject(i).getString("playerName") + " - " + scoreObj.getString("score") + "\n";
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
//                        }

                        //    }
                        // });

                    } catch (Exception ex) {

                    }
                }
            }
        });
    }

    public static void startNewGame() {

        gameView.firstTime = true;
        mainMenu.dismiss();
        gamePaused = false;
    }

    public boolean onTouch(View button, MotionEvent motion) {
        switch (button.getId()) {
            case R.id.leftButton:
                switch (motion.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isLeftPressed = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isLeftPressed = false;
                        break;
                }
                break;
            case R.id.rightButton:
                switch (motion.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isRightPressed = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isRightPressed = false;
                        break;
                }
                break;
            case R.id.speedButton:
                switch (motion.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isSpeedPressed = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isSpeedPressed = false;
                        break;
                }
                break;
            case R.id.menuButton:
                switch (motion.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        firstStart = false;
                        mainMenu.show(getFragmentManager(), "menu");
                        stopGame(false);

                        break;
                }

        }
        return true;
    }
}

