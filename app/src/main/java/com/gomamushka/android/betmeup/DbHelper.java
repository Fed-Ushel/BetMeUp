package com.gomamushka.android.betmeup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by F_Aredakov on 11.12.2016.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static DbHelper dbHelper;

    private final static String LOG_TAG = "FED";
    //Обозначаем переменные статичными чтобы использовать в конструкторе (Instance variable не может исползоваться?)

    private final static int DB_VER = 1;
    private final static String DB_NAME = "betmeup.db";
    final String TABLE_NAME = "betmeup";
    /**
     Атрибуты задания
     1 - категория (Int)
     2- категория возраста c
     3 - задание (Text)
     4 - ответ (Text)
     5 - изображение URL (Text)
     6 - время на исполнение (Int)
     */
    final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            "( _id INTEGER PRIMARY KEY , " +
            " task_category INTEGER, " +
            " age_category INTEGER, " +
            " task TEXT, " +
            " solution TEXT, " +
            " image_url TEXT, " +
            " timer INTEGER)";
    final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private Context mCtx;



    private DbHelper(Context c) {
        super(c, DB_NAME, null, DB_VER);
        this.mCtx = c;
        //  getData();


    }

    public static DbHelper getInstance() {
        return dbHelper;
    }

    public static void initInstance (Context c) {
        if (dbHelper == null) {
            dbHelper = new DbHelper(c);

        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        fillData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    //Обрабатываем XML данные
    private ArrayList<String[]> getData() {
        int xmlName = 0;
        String[] recordData = new String[6];
        ArrayList<String[]> records = new ArrayList<String[]>();
        try {

            XmlPullParser xpp = prepareXml();
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                Log.d(LOG_TAG, "EVENT_TYPE: name = " + eventType);
                if (eventType == XmlPullParser.START_TAG) {
                    xmlName = 0;
                    Log.d(LOG_TAG, "START_TAG: name = " + xpp.getName());
                    switch (xpp.getName()) {
                        case "task_category":
                            xmlName = 1;
                            break;
                        case "age_category":
                            xmlName = 2;
                            break;
                        case "task_name":
                            xmlName = 3;
                            break;
                        case "solution":
                            xmlName = 4;
                            break;
                        case "image_url":
                            xmlName = 5;
                            break;
                        case "timer":
                            xmlName = 6;
                            break;
                    }
                }
                else if(eventType==XmlPullParser.TEXT) {
                    switch (xmlName) {
                        case 1:
                            recordData[0] = xpp.getText();
                            break;
                        case 2:
                            recordData[1] = xpp.getText();
                            break;
                        case 3:
                            recordData[2] = xpp.getText();
                            break;
                        case 4:
                            recordData[3] = xpp.getText();
                            break;
                        case 5:
                            recordData[4] = xpp.getText();
                            break;
                        case 6:
                            recordData[5] = xpp.getText();
                            break;
                    }
                }
                if (recordData[5] != null) {
                    records.add(recordData);
                    recordData = new String[6];
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }


    public void fillData(SQLiteDatabase db){
        ArrayList<String[]> data = getData();
        if( db != null ){
            ContentValues values;

            for(String[] dat:data){
                //       Log.d(LOG_TAG, "array = " + Arrays.toString(dat) );
                values = new ContentValues();
                values.put("task_category", dat[0]);
                values.put("age_category", dat[1]);
                values.put("task", dat[2]);
                values.put("solution", dat[3]);
                values.put("image_url", dat[4]);
                values.put("timer", dat[5]);
                long rowID = db.insert(TABLE_NAME, null, values);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);

            }
        }
        else {
            Log.d(LOG_TAG,"db null");
        }
    }

    public void selectData (SQLiteDatabase db, int id) {
        Cursor c = db.query(TABLE_NAME, null, "_id = ?", new String[] {Integer.toString(id)},null, null, null);
        Log.d (LOG_TAG, "Строк " + c.getCount());
        if(c.moveToFirst()){
            do{
                // Extract data.
                Log.d (LOG_TAG, "image" + c.getString(c.getColumnIndex("solution")));
            }while (c.moveToNext());
        }
        c.close();


        Log.d (LOG_TAG, "image" + c.getString(3));
        while (c.moveToNext()) {

        }
    }

    public String[] selectRandomTaskbyCategory (SQLiteDatabase db, int category) {
        String[] taskStrings = new String[5];

        category++;
        //todo Избавится от +1 - поменять в XML столбец категории - от0 до 3 а не от 1 до 4
        Cursor c = db.query (TABLE_NAME,
                null,
                "task_category = ?",
                new String[] {Integer.toString(category)},
                null,
                null,
                "RANDOM() LIMIT 1");
        if (c.moveToFirst()) {
            taskStrings[0] = c.getString(c.getColumnIndex("age_category"));
            taskStrings[1] = c.getString(c.getColumnIndex("task"));
            taskStrings[2] = c.getString(c.getColumnIndex("solution"));
            taskStrings[3] = c.getString(c.getColumnIndex("image_url"));
            taskStrings[4] = c.getString(c.getColumnIndex("timer"));
        }
        Log.d (LOG_TAG, "Таск " + taskStrings[1] + " Время " + taskStrings[4]);
        return taskStrings;
    }

    //Получаем XML данные для БД из файла tasks_data_ru в папке res/xml
    private XmlPullParser prepareXml() {
        return mCtx.getResources().getXml(R.xml.tasks_data_ru);
    }

}
