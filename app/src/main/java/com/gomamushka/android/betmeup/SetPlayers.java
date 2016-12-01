package com.gomamushka.android.betmeup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SetPlayers extends AppCompatActivity {
    private final String LOG_TAG = "FED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.setplayers_layout);
        //Получаем количество игроков и размещаем в ТекстВью. Пока в виде строки. Как в инт кастить непонятно
        Intent intent = getIntent();
        Integer playersCount = intent.getIntExtra("playersCount", 3);
        TextView tv = (TextView) findViewById(R.id.setplayers_textview);
        tv.setText("Игроков: " + playersCount);
        String[] names = new String[playersCount];
        for (int i=0; i < names.length; i++ ) {
            Log.d(LOG_TAG, "I = " + i);
            names[i] = "Player " + (i+1);
        }

        ListView lvPlayers = (ListView) findViewById(R.id.setplayers_players_listview);
        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);

        // присваиваем адаптер списку
        lvPlayers.setAdapter(adapter);
    }
}
