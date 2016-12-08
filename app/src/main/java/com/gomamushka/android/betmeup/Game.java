package com.gomamushka.android.betmeup;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by F_Aredakov on 24.11.2016.
 */
public class Game {
    private static Game game;



    Player[] players;
    Integer numOfPlayers;
    public int activePlayer;
    private Boolean isFinished = false; //Закончилась ли игра
    private Integer turnCount;   //Количество ходов
    private Integer totalTurnBet; //Суммарная ставка на ход
    public Integer turnCategory; // Категория вопроса

    //Добавляем к объекту helper
    GameHelper helper = GameHelper.getInstance();

    public String[] activityCategory;

        /* Задания для игры. Вынести в базу данных
        У  Заданий 4 категории:
        - Сила
        - Ловкость
        - Удача
         - Интеллект
         Атрибуты задания
         1 - категория
         2- категория возраста
         3 - задание
         4 - ответ
         5 - изображение
         6 - время на исполнение
     */
        public static void initInstance(Integer num, Context c) {
            Log.d("FED", "MySingleton::InitInstance()");
            if (game == null) {
                game = new Game(num,c);
            }
        }

    public static Game getInstance() {
        Log.d("FED", "MySingleton::getInstance()");
        return game;
    }
    private Game(Integer num, Context c) {
        numOfPlayers = num;
        players = new Player[num];
        turnCount = 1;
        totalTurnBet = 0;
        activePlayer = 1;

       activityCategory = new String[4];
        activityCategory[0] = c.getString(R.string.dexterity_category);
        activityCategory[1] = c.getString(R.string.intelligence_category);
        activityCategory[2] = c.getString(R.string.luck_category);
        activityCategory[3] = c.getString(R.string.strength_category);


    }

    //Устанавливаем категорию вопроса
    public void setTurnCategory () {
        this.turnCategory = this.helper.rollCategoryDice();
    }
    public String getTurnCategory () {
        return activityCategory[this.turnCategory];
    }
}
