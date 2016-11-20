package com.gomamushka.android.betmeup;

import android.app.ActionBar;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = "FED";
    private Integer playerCount;
    private TextView tvPlayers;
    private  Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActionBar отключен в Values/style.xml через тему
        setContentView(R.layout.main_layout);
        tvPlayers = (TextView) findViewById(R.id.tvPlayers);
        playerCount = 3;
        //Через ресурсы чтобы можно было локализовать строку
        res = getResources();
        String text = res.getString(R.string.players, playerCount);
        updateTextView(tvPlayers, text);
    }

    private void NewGame() {

    }
    public void increasePlayersCount(View view) {
        playerCount++;
        String text = res.getString(R.string.players, playerCount);
        updateTextView(tvPlayers, text);
    }
    public void reducePlayersCount(View view) {
        playerCount--;
        Log.d(LOG_TAG, "Игроков:" + playerCount);
        String text = res.getString(R.string.players, playerCount);
        updateTextView(tvPlayers, text);

    }
    private void updateTextView(TextView tv, String text) {
        tv.setText(text);

    }
}
