package com.example.racer;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends DialogFragment implements OnClickListener {

    final String LOG_TAG = "myLogs";
    public Button continueButton;
    public Button newGameButton;
    private static ScoreActivity scoreTable;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Menu");
        getDialog().setCanceledOnTouchOutside(false);
        View v = inflater.inflate(R.layout.menu_layout, null);
        continueButton= (Button)v.findViewById(R.id.continueBtn);
        continueButton.setOnClickListener(this);
        newGameButton= (Button)v.findViewById(R.id.newGameBtn);
        newGameButton.setOnClickListener(this);
        v.findViewById(R.id.scoreBtn).setOnClickListener(this);
        v.findViewById(R.id.exitBtn).setOnClickListener(this);
        newGameButton.setEnabled(false);
        continueButton.setEnabled(!MainActivity.firstStart);
        scoreTable=new ScoreActivity();
        TextView nameTV=v.findViewById(R.id.nameTV);
        nameTV.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                newGameButton.setEnabled(s.length()!=0);
                MainActivity.playerName=s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        return v;
    }
    public void onClick(View v) {
        if(R.id.continueBtn==v.getId()){
            MainActivity.startGame();
        }
        if(R.id.newGameBtn==v.getId()){
            MainActivity.startNewGame();
        }
        if(R.id.scoreBtn==v.getId()){
            scoreTable.show(getFragmentManager(), "score");
        }
        if(R.id.exitBtn==v.getId()){
            getActivity().finish();
            System.exit(0);
        }

    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        MainActivity.startGame();
    }
}
