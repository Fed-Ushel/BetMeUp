package com.gomamushka.android.betmeup;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;




/**
 * Created by F_Aredakov on 01.12.2016.
 * Singleton class GameHelper
 */
public class GameHelper {

    private static GameHelper helper;
    //Размер экрана активности в DP
    public int dpHeight;
    public int dpWidth;

    private final String LOG_TAG = "FED";

    //Изображения, уже использующиеся игроками (для выбора уникального изображения для игрока - 10 игроков)
    private ArrayList<Integer> usedImagesNum = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));

    //Объект  rand для генерации случайных чисел во время игры
    private Random rand = new Random();

    //Префикс файла изображения и их папка
    private static final String IMG_PREFIX = "natur";
    private static final String IMG_FOLDER = "drawable/";

    //Граней на кубике отвечающих за категорию вопроса
     static final Integer DOTS_CATEGORY = 4;

    //Инициализируем экземпляр
    public static void initInstance() {

        if (helper == null) {
            helper = new GameHelper();
        }
    }

    public static GameHelper getInstance() {
        Log.d("FED", "MySingleton::getInstance()");
        return helper;
    }

    private GameHelper () {

    }



    //Создаем ImageView по его src и ID из файла ids.xml
    public static ImageView makeImageView (String id, String src, ViewGroup.LayoutParams lp, Context context) {
        ImageView imageView = new ImageView(context); //GameActivity.this
        //Устанавливаем ID Для ImageView
        imageView.setId(getResId(id, Drawable.class));

        //Получаем ID графического ресурса по его SRC в текстовом виде
        //Отсюда http://stackoverflow.com/questions/6783327/setimageresource-from-a-string
       // Context c = activity.getApplicationContext();
        int resId = context.getResources().getIdentifier(src, null, context.getPackageName());
        imageView.setImageResource(resId);
        imageView.setLayoutParams(lp);
        return imageView;


    }
    // Получаем ID ресурсса по его имени
    // http://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
    //USAGE: getResId("icon", Drawable.class);
    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    //Получаем высоту экрана (В нормальной ориентации)
    public int getScreenHeigth (Activity activity) {
        Configuration configuration = activity.getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp; //The current width of the available screen space, in dp units, corresponding to screen width resource qualifier.
     //   int smallestScreenWidthDp = configuration.smallestScreenWidthDp; //The smallest screen size an application will see in normal operation, corresponding to smallest screen width resource qualifier.
        return screenWidthDp;
    }
    //Получаем ширину экрана (В нормальной ориентации)
    public int getScreenWidth (Activity activity) {
        Configuration configuration = activity.getResources().getConfiguration();
        int screenHeightDp = configuration.screenHeightDp;
        return screenHeightDp;
    }

    //Создаем игрока и его аватарку
    public void makePlayer(int i, Game game, Context context) {
        //Формируем строку ID из файла ids.xml
        //Для доступа к ресурсам снаружи контекста или активности можно использовать Resources.getSystem()
        // НО в этом случае будет доступ только системные ресурсы, но не ресурсы приложения.
        // Для ресурсов приложения обязателен контекст context.getResources()
        String id =  context.getResources().getString(R.string.player_image_prefix) + (i+1) + context.getResources().getString(R.string.player_image_suffix);
        String imSrc = choosePlayerImageSrc();
        // ImageView игрока не используется, вместо это  используется ImageSRC
      //  ImageView iv = GameHelper.makeImageView(id,imSrc , new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT),context);
        game.players[i] = new Player(context.getResources().getString(R.string.player)+(i+1), imSrc);
    }


    //выбираем изображение из ресурсов (1-10) случайным образом и возвращаем SRC изображения
    private String choosePlayerImageSrc() {
        Integer randNum = rand.nextInt(usedImagesNum.size()); //Произвольный индекс массива
        Integer imgNum = usedImagesNum.get(randNum); //Берем число из индекста
        usedImagesNum.remove(imgNum);
        return (IMG_FOLDER + IMG_PREFIX  + imgNum.toString());
    }


    public Integer rollCategoryDice() {
        int x = rand.nextInt(DOTS_CATEGORY);

        Log.d (LOG_TAG, "Rand " + x);
        return x;
    }

    //Таймер
    public void waitSec(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
        }
    }
}
