package com.gomamushka.android.betmeup;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActionBar отключен в Values/style.xml через тему
        setContentView(R.layout.main_layout);


    }

    private void NewGame() {

    }
}
