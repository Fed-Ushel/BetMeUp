package com.gomamushka.android.betmeup;

/**
 * Created by F_Aredakov on 24.11.2016.
 */
public class Game {
    Player[] players;
    Integer numOfPlayers;
    private Boolean isFinished = false; //Закончилась ли игра
    private Integer turnCount;   //Количество ходов
    private Integer totalTurnBet; //Суммарная ставка на ход
    private static final Integer MAX_DOTS_TURN = 3;
    public GameHelper helper;

    public Game(Integer num) {
        numOfPlayers = num;
        players = new Player[num];
        turnCount = 1;
        totalTurnBet = 0;
        //Добавляем к объекту helper
        helper = new GameHelper();


    }
}
