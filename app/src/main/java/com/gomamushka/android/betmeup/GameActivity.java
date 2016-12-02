package com.gomamushka.android.betmeup;
/**
 *
 * Main Game Activity class
 * Creates new game, players with attibutes, prepares layouts for each player and puts it on a screen,
 * then run the game
 * todo Может быть имеет смысл вынести подготовку в отдельный класс типа GameHelper и оставить только визуализацию и управление игрой
 *
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;



public class GameActivity extends AppCompatActivity {
    private final String LOG_TAG = "FED";
    //Размер экрана активности в DP
    public int dpHeight;
    public int dpWidth;
    //Родительский лейаут для лейаутов игроков
    LinearLayout playersGameLayout;


    //Лейауты, в которых хранится отображение пользователя
        private RelativeLayout[] playerLayouts;


    //новая игра
    public Game game;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Integer playersCount = intent.getIntExtra("playersCount", 3);

        //Создаем игру
        game = new Game(playersCount);
        setContentView(R.layout.activity_game);
        //Находим родительский лейаут для вставки леейаутов игроков
        playersGameLayout = (LinearLayout) findViewById(R.id.playersGameLayout);
        // Получаем размеры экрана активности
        dpWidth = game.helper.getScreenWidth(this);
        dpHeight = game.helper.getScreenHeigth(this);


        //Инициализируем игроков и лейауты
        playerLayouts = new RelativeLayout[playersCount];
        for (int i = 0; i < playersCount; i++) {
            game.helper.makePlayer(i, game, getApplicationContext());
            makeRelativeLayout(getString(R.string.player_layout_prefix) + i, i);
        }
        //Подключаем лейауты игроков к основному лейауту
        for (int i = 0; i < game.players.length; i++) {
            game.players[i].playerImage.getLayoutParams().height = 40;
            game.players[i].playerImage.getLayoutParams().width = 40;
            playerLayouts[i].addView(game.players[i].playerImage);
            playerLayouts[i].setBackgroundColor(Color.parseColor("#FF0000"));
            Log.d (LOG_TAG, "Width: " + playerLayouts[i].getWidth() + " Height: " + playerLayouts[i].getHeight());
            playersGameLayout.addView(playerLayouts[i]);
        }
        //  Добавили все лейауты, задаем контентвью;

    }

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


    //todo Переделать? класс GameHelper в статический в соответствии с решением

    /**
     *
     *                         Java has static nested classes but it sounds like you're looking for a top-level static class. Java has no way of making a top-level class static but you can simulate a static class like this:
     Declare your class final - Prevents extension of the class since extending a static class makes no sense
     Make the constructor private - Prevents instantiation by client code as it makes no sense to instantiate a static class
     Make all the members and functions of the class static - Since the class cannot be instantiated no instance methods can be called or instance fields accessed
     Note that the compiler will not prevent you from declaring an instance (non-static) member. The issue will only show up if you attempt to call the instance member
     *
     *
     *
     */

    //Создаем RelativeLayout по его ID из файла ids.xml
    public void makeRelativeLayout(String id, int i) {
        RelativeLayout layout = new RelativeLayout(GameActivity.this);
        layout.setId(GameHelper.getResId(id, RelativeLayout.class));
        ViewGroup.LayoutParams layoutParams
                = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);
        playerLayouts[i] = layout;
    }




}
