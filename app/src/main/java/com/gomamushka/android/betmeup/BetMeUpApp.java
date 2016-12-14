package com.gomamushka.android.betmeup;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by F_Aredakov on 08.12.2016.
 */
public class BetMeUpApp extends Application {
    public DbHelper dbh;
    public SQLiteDatabase db;


    @Override
    public void onCreate() {
        super.onCreate();
         GameHelper.initInstance();
        DbHelper.initInstance(getApplicationContext());
        dbh = DbHelper.getInstance();
        // подключаемся к БД
        db = dbh.getWritableDatabase();
        dbh.onUpgrade(db, 1, 2);
        //db.close();
            }

    public void createGame(Integer num, Context c) {
        Game.initInstance(num, c);
    }
}
