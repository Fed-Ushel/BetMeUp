package com.gomamushka.android.betmeup;
/**
 *
 * Main Game Activity class
 * Creates new game, players with attibutes, prepares layouts for each player and puts it on a screen,
 * then run the game
 * todo Может быть имеет смысл вынести подготовку в отдельный класс типа GameHelper и оставить только визуализацию и управление игрой
 *
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class GameActivity extends AppCompatActivity {
    private final String LOG_TAG = "FED";
    LinearLayout playersGameLayout;

    //Изображения, уже использующиеся игроками (для выбора уникального изображения для игрока - 10 игроков)
        private ArrayList<Integer> usedImagesNum = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));

    //Лейауты, в которых хранится отображение пользователя
        private RelativeLayout[] playerLayouts;

    //Префикс файла изображения и их папка
    private static final String IMG_PREFIX = "natur";
    private static final String IMG_FOLDER = "drawable/";
    //новая игра
    public Game game;

    //todo Получить размер экрана и смасштабировать изображения в зависимости от количества игроков и размера экрана
    //Размер экрана в DP
    public float dpHeight;
    public float dpWidth;




    //Объект  rand для генерации случайных чисел во время игры
    private static Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        Integer playersCount = intent.getIntExtra("playersCount", 3);

        playersGameLayout = (LinearLayout) findViewById(R.id.playersGameLayout);
        //Создаем игру
        game = new Game(playersCount);

        //Инициализируем игроков и лейауты
        playerLayouts = new RelativeLayout[playersCount];
        for (int i= 0; i < playersCount; i++) {
            makePlayer(i);
            makeRelativeLayout(getString(R.string.player_layout_prefix) + i, i );
        }
        //Подключаем лейауты игроков к основному лейауту
        for (int i = 0; i < game.players.length; i++) {
            playerLayouts[i].addView(game.players[i].playerImage);
           playersGameLayout.addView(playerLayouts[i]);
        }
      //  inflateView(i+1, choosePlayerImageSrc());


/*
 Добавляем элементы поля (Игроков)
 Скоперайчено с
 http://android-er.blogspot.ru/2012/06/programmatically-create-layout-and-view.html

 Проблема в том, что надуть через листвью в нашем случае не совсем правильно - в зависимости от кол-ва игроков
 будет либо скролиться, либо не растягиваться на весь экран. К тому же горизонтального листвью не бывает.
 Надуть какой то view.xml несколько раз подряд не
 получается, т.к. ID у элементов внутри view не будут уникальными и обрабатывать клики на них будет невозожмно.

 Решение предлагает

 В файле ids.xml храняться ID для элементов View каждого игрока (максимально 10)
 В inflateView(номер игрока) создаются разные View для игрока и прикрепляются к родительскому LinearLayout
 getResId преобразовывает текст в ссылку на ID ресурса (другим способом передать ссылку на ресурс в layout.setId неполучается.

*/
    }

    //
    private void inflateView(int x, String src) {






    }

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

    //выбираем изображение из ресурсов (1-10) случайным образом и возвращаем SRC изображения
    private String choosePlayerImageSrc() {
        Integer randNum = rand.nextInt(usedImagesNum.size()); //Произвольный индекс массива
        Integer imgNum = usedImagesNum.get(randNum); //Берем число из индекста
        usedImagesNum.remove(imgNum);
        return (IMG_FOLDER + IMG_PREFIX  + imgNum.toString());
    }
    //Создаем ImageView по его src и ID из файла ids.xml
    private ImageView makeImageView (String id, String src, LayoutParams lp) {
        ImageView imageView = new ImageView(GameActivity.this);
        //Устанавливаем ID Для ImageView
        imageView.setId(getResId(id, Drawable.class));

        //Получаем ID графического ресурса по его SRC в текстовом виде
        //Отсюда http://stackoverflow.com/questions/6783327/setimageresource-from-a-string
        Context c = getApplicationContext();
        int resId = c.getResources().getIdentifier(src, null, c.getPackageName());
        imageView.setImageResource(resId);
        imageView.setLayoutParams(lp);
        return imageView;
    }

    //Создаем RelativeLayout по его ID из файла ids.xml
    private void makeRelativeLayout(String id, int i) {
        RelativeLayout layout = new RelativeLayout(GameActivity.this);
        layout.setId(getResId(id, RelativeLayout.class));
        LayoutParams layoutParams
                = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);
        playerLayouts[i] = layout;
    }

    //Создаем игрока
    private void makePlayer(int i) {
    //Формируем строку ID из файла ids.xml
    String id = getString(R.string.player_image_prefix) + (i+1) + getString(R.string.player_image_suffix);
    ImageView iv = makeImageView(id, choosePlayerImageSrc(), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    game.players[i] = new Player(getString(R.string.player)+(i+1), iv);
    }
}
