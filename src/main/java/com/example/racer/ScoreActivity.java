package com.example.racer;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScoreActivity extends DialogFragment implements OnClickListener {

    final String LOG_TAG = "myLogs";
    public Button myRecordsBtn;
    public Button allRecordsBtn;
    public ImageButton backBtn;
    TextView scoreTextView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Score");
        getDialog().setCanceledOnTouchOutside(false);
        View v = inflater.inflate(R.layout.score_layout, null);
        myRecordsBtn = (Button) v.findViewById(R.id.myRecordsBtn);
        myRecordsBtn.setOnClickListener(this);
        allRecordsBtn = (Button) v.findViewById(R.id.allRecordsBtn);
        allRecordsBtn.setOnClickListener(this);
        backBtn = (ImageButton) v.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        scoreTextView = (TextView) v.findViewById(R.id.scoreTextView);
        scoreTextView.setText("-");
        return v;
    }

    public void onClick(View v) {
        if (R.id.myRecordsBtn == v.getId()) {
            getMyScore();
        }
        if (R.id.allRecordsBtn == v.getId()) {
            getAllScore();
        }
        if (R.id.backBtn == v.getId()) {
            dismiss();
        }
    }

    private void getAllScore() {
        scoreTextView.setText("Загружаю...");
        OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.0.100:8080/score";
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
                        final JSONArray jsonArr;

                        scoreTextView.setText("");
                        String resp = response.body().string();
                        jsonArr = new JSONArray(resp);
                        scoreTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                String scoreTableString = "";
                                for (int i = 0; i < jsonArr.length(); i++) {
                                    try {
                                        JSONObject scoreObj = jsonArr.getJSONObject(i);
                                        scoreTableString += (i + 1) + ". " + jsonArr.getJSONObject(i).getString("playerName") + " - " + scoreObj.getString("score") + "\n";
                                        //это всегда выполняется в главном потоке

                                    } catch (Exception ex) {

                                    }
                                }
                                scoreTextView.setText(scoreTableString);
                            }
                        });

                    } catch (Exception ex) {

                    }
                }
            }
        });
    }

    private void getMyScore() {
        if (MainActivity.playerName.length() == 0) {
            scoreTextView.setText("Вы не ввели имя");
            return;
        }
        scoreTextView.setText("Загружаю...");
        OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.0.100:8080/score?name=" + MainActivity.playerName;
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
                        final JSONArray jsonArr;
                        //Log.i("RESPONSE" + MainActivity.playerName, response.body().string());
                        scoreTextView.setText("");
                        String resp = response.body().string();
                        jsonArr = new JSONArray(resp);
                        scoreTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                String scoreTableString = "";
                                for (int i = 0; i < jsonArr.length(); i++) {
                                    try {
                                        JSONObject scoreObj = jsonArr.getJSONObject(i);
                                        scoreTableString += (i + 1) + ". " + jsonArr.getJSONObject(i).getString("playerName") + " - " + scoreObj.getString("score") + "\n";
                                        //это всегда выполняется в главном потоке

                                    } catch (Exception ex) {
                                    }
                                }
                                scoreTextView.setText(scoreTableString);
                            }
                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 1: onCancel");
    }
}
