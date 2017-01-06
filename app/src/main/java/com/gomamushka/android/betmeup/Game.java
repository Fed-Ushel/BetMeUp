package com.gomamushka.android.betmeup;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by F_Aredakov on 24.11.2016.
 */
public class Game {
    private static Game game;



    protected Player[] players;
    protected Integer numOfPlayers;

    protected int activePlayerId;
    protected Player activePlayer;
    protected Player betPlayer;

    protected boolean isAllPlayersSet = false;// Все ли игроки сделали ставки


    private Boolean isFinished = false; //Закончилась ли игра
    protected Integer turnCount;   //Количество ходов
    private String turnTask; //Задание на ход
    public Integer turnTime; //Время на выполнение задания
    public Integer totalTurnBet; //Суммарная ставка на ход
    public Integer turnCategory; // Категория вопроса

    //Добавляем к объекту helper
    GameHelper helper = GameHelper.getInstance();

    //Получаем доступ к переменным и методам приложения
    private BetMeUpApp app;

    //Инициализируем активность чтобы получить к ней доступ из другого класса
    private GameActivity gameActivity;

    public String[] activityCategory;

        /* Задания для игры. Вынести в базу данных
        У  Заданий 4 категории:
        - 1  Сила
        - 2 Ловкость
        - 3 Удача
         -4 Интеллект
         Атрибуты задания
         1 - категория
         2- категория возраста
         3 - задание
         4 - ответ
         5 - изображение
         6 - время на исполнение
     */
        public static void initInstance(Integer num, Context c) {
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

        //Создаем игроков
        for (int i = 0; i < numOfPlayers; i++) {
            this.helper.makePlayer(i, this, c);
        }



        //Получаем доступ к методам и переменным приложения
        app = ((BetMeUpApp) c);

       activityCategory = new String[4];
        activityCategory[0] = c.getString(R.string.strength_category);
        activityCategory[1] = c.getString(R.string.dexterity_category);
        activityCategory[2] = c.getString(R.string.luck_category);
        activityCategory[3] = c.getString(R.string.intelligence_category);

    }
    //Выбираем задачу и время на решение
    public void setTurnTask () {
        String[] taskStrings = new String[5];
        setTurnCategory();
        //todo Берем количество не выпадавших заданий по категории (_ID выпадавших надо хранить в массиве) и выбираем случайное задание
        taskStrings = app.dbh.selectRandomTaskbyCategory(app.db, this.turnCategory);
        this.turnTask = taskStrings[1];
        setTurnTime(taskStrings[4]);

    }
    private void setTurnTime (String time) {
        this.turnTime = 180;
        if (Integer.valueOf(time) != 0) {
            this.turnTime = Integer.valueOf(time);
        }
    }

    //Выбрасываем кубик категрии
    private void setTurnCategory () {
        this.turnCategory = this.helper.rollCategoryDice();
    }






    public String getTurnCategory () {
        return activityCategory[this.turnCategory];
    }

    public String getTurnTask() {
        return this.turnTask;
    }

    //Выбираем игрока делающего ставку (первого в массиве players у которого признак isPlayerSet = false)
     void setBetPlayer() {

        for (int i = 0; i < players.length; i++ ) {
             if (!players[i].isPlayerSet) {
                game.betPlayer = players[i];
                break;
            }

        }
    }

    //Находим Index игрока в массиве по его значению
    Integer getPlayerIndex(Player p) {
        return Arrays.asList(players).indexOf(p);
    }
    void setIsAllPlayersSet() {
        this.isAllPlayersSet = true;
    }

}


