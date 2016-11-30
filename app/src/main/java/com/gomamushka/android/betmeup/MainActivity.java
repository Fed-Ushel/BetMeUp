package com.gomamushka.android.betmeup;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = "FED";
    private Integer playersCount;
    private TextView tvPlayers;
    private  Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.main_layout);
        tvPlayers = (TextView) findViewById(R.id.tvPlayers);
        playersCount = 3;
        //Через ресурсы чтобы можно было локализовать строку
        res = getResources();
        String text = res.getString(R.string.players, playersCount);
        updateTextView(tvPlayers, text);
    }

    public void newGame(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra ("playersCount", playersCount);
        startActivity(intent);
    }
    public void increasePlayersCount(View view) {
        playersCount++;
        String text = res.getString(R.string.players, playersCount);
        updateTextView(tvPlayers, text);
    }
    public void reducePlayersCount(View view) {
        playersCount--;
        String text = res.getString(R.string.players, playersCount);
        updateTextView(tvPlayers, text);

    }
    private void updateTextView(TextView tv, String text) {
        tv.setText(text);

    }
}
