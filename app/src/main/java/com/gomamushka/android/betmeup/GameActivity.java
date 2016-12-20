package com.gomamushka.android.betmeup;
/**
 *
 * Main Game Activity class
 * Creates new game, players with attibutes, prepares layouts for each player and puts it on a screen,
 * then run the game
 * todo - Можно не заморачиваться с созданием лейаутов программно, а надуть вью несклько раз, после чего получить
 * доступ к дочерним объектам согласно http://stackoverflow.com/questions/8395168/android-get-children-inside-a-view
 */

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;


public class GameActivity extends AppCompatActivity   implements DialogTask.NoticeDialogListener{
    private final String LOG_TAG = "FED";
    //Устанавливаем начальные значения ID для View игроков
    //todo Нужно ограничить диапазон поиска ID как указано http://stackoverflow.com/questions/8460680/how-can-i-assign-an-id-to-a-view-programmatically
    private final int playersViewNameId = 1000;
    private final int playersViewAccId = 1100;

    //Размер экрана активности в DP
    public int dpHeight;
    public int dpWidth;
    //Родительский лейаут для лейаутов игроков
    LinearLayout playersGameLayout;

    //Массив ImageView игроков в Активности (не путать с ImageView игрока)
    ArrayList<ImageButton> playersIbList = new ArrayList<ImageButton>();

    //Массив TextView игроков в Актисности
    ArrayList<TextView> playersTvList = new ArrayList<TextView>();

    // игра
    private Game game;

    //Размер лейаута на 1 игрока
    private int playerLayoutWidth;

    //ImageView активного игрока
    private ImageView activePlayerIv;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Integer playersCount = intent.getIntExtra("playersCount", 3);
            //Используем методы и переменные приложения BetMeUp
            BetMeUpApp app = ((BetMeUpApp) getApplicationContext());
            //Получаем доступ к БД через класс приложения BetMeUp -app.db

        //Создаем игру

        app.createGame(playersCount, getApplicationContext());
            game = Game.getInstance();
        setContentView(R.layout.activity_game);
        //Находим родительский лейаут для вставки леейаутов игроков


        playersGameLayout = (LinearLayout) findViewById(R.id.playersGameLayout);
        // Получаем размеры экрана активности
        final float scale =  getResources().getDisplayMetrics().density;
        dpWidth = game.helper.getScreenWidth(this);
        dpHeight = game.helper.getScreenHeigth(this);
            Log.d(LOG_TAG, "width: " + dpWidth);
            Log.d(LOG_TAG, "height: " + dpHeight);
            Log.d(LOG_TAG, "scale=" + scale);
        //Устанавливаем размер Лейаута в списке игроков
        playerLayoutWidth = (int) (dpHeight / playersCount * scale);

        //Находим изображение активного игрока (кто делает ход)
        activePlayerIv = (ImageView) findViewById(R.id.ivActivePlayer_game_activity);
        LayoutInflater inflater = LayoutInflater.from(this);

            // Формирем лейауты игроков
        for (int i = 0; i < playersCount; i++) {
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.game_player_layout, playersGameLayout, true);
            View playerLayout = ((ViewGroup) layout).getChildAt(i);

                //Находим дочерние элементы для каждого лейаута игрока, обновляем содержимое и добавляем в массив
            for (int index = 0; index < ((ViewGroup) playerLayout).getChildCount(); ++index) {
                View nextChild = ((ViewGroup) playerLayout).getChildAt(index);
                    //todo Попробовать использовать для ImageButton разные изображения для разных статусов https://developer.android.com/reference/android/widget/ImageButton.html
                if (nextChild instanceof ImageView) {
                    //Обновляем картинку игрока
                    ImageButton ib = (ImageButton) nextChild;
                    // Вопрос - если добавить сначла в массив а потом изменить SRC изменится ли SRC у элемента в массиве
                    //Получаем идентификатор картинки и добавляем его в ИмеджВью Лейаута игрока
                    int resId = this.getResources().getIdentifier(game.players[i].playerImageSrc, null, this.getPackageName());
                    ib.setImageResource(resId);
                    //Обновляем размеры картинки, чтобы влезли все картинки игроков.
                    //todo Ограничить максимальный размер картинки и распределить игроков по экрану
                    ib.getLayoutParams().width =  playerLayoutWidth;
                    ib.getLayoutParams().height = playerLayoutWidth;
                    playersIbList.add(ib);

                } else if (nextChild instanceof TextView) {
                    //Обновляем имя игрока и его счет  (пока TextView)
                    TextView tv = (TextView) nextChild;
                    int tvId = tv.getId();
                    if(tvId == R.id.tvPlayerName_game_player_layout) {
                            tv.setText(game.players[i].name);
                            tv.setId(playersViewNameId + i);
                    } else if (tvId == R.id.playerAccountAmount_game_player_layout) {
                        tv.setText(getString(R.string.acc) + " " + String.valueOf(game.players[i].account.amount));
                        tv.setId(playersViewAccId + i);
                        Log.d(LOG_TAG, "ID: " + (playersViewAccId + i));
                    }

                }

            }


        }
        //EOF цикла
            //Выбираем первого игрока
            setActivePlayer(0);


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



//Устанавливаем изображение (Сверху в центре) активного игрока
private void setActivePlayer(int apId) {
    game.activePlayerId = apId;
    game.activePlayer = game.players[apId];
    int resId = this.getResources().getIdentifier(game.players[apId].playerImageSrc, null, this.getPackageName());
    activePlayerIv.setImageResource(resId);
}

   public void doTurn(View v) {
       game.doTurn();
       //tv must be final to be used in inner class - для того чтобы исчезало через 3 секунды
       // http://stackoverflow.com/questions/11424753/why-do-variables-passed-to-runnable-need-to-be-final
       final TextView tv =  (TextView) findViewById(R.id.tvActivityCategory_activity_game);
       final TextView taskTextView = (TextView) findViewById(R.id.tvTaskText_activity_game);


       tv.setVisibility(View.VISIBLE);
       taskTextView.setVisibility(View.GONE);
       tv.setText(game.getTurnCategory());
       taskTextView.setText(game.getTurnTask());
       //tv исчезает
       tv.postDelayed(new Runnable() { public void run() { tv.setVisibility(View.GONE); } }, 3000);
       taskTextView.postDelayed(new Runnable() { public void run() { taskTextView.setVisibility(View.VISIBLE); } }, 4000);

       showTask();

}
    private void showTask() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialogTask");
        if (prev != null) {
            ft.remove(prev);
        }
        // добавляем фрагменг в бэк стек - в нашем слуаче недобавляем
        // ft.addToBackStack(null);
        // Create and show the dialog.
        DialogFragment dialogTask =  new DialogTask();
        //из фрагмента нельзя выйти кнопкой назад
        dialogTask.setCancelable(false);
        dialogTask.show(ft, "dialogTask");
    }

    // Возвращаем данные из диалога
    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.d(LOG_TAG, "Позитив");
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

        Log.d(LOG_TAG, "У игрока : " + (playersViewAccId + game.activePlayerId));
        @SuppressWarnings("ResourceType") TextView tv = (TextView) findViewById(playersViewAccId + game.activePlayerId);
        Log.d(LOG_TAG, "У икрока:  " + game.activePlayer.account.amount);
        String st = Integer.toString(game.activePlayer.account.amount);
        tv.setText(st);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "MainActivity: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "MainActivity: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "MainActivity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "MainActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MainActivity: onDestroy()");
    }
}
