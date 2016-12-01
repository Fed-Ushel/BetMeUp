package com.gomamushka.android.betmeup;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by F_Aredakov on 21.11.2016.
 */
public class Player {
    public String name;
    public Account account;
    ImageView playerImage;
    RelativeLayout playerLayout;
    public int age; //задания зависят от возраста
    public int bet; //сумма ставки
    public int choice; // сможет игрок или нет (пока 0 или 1)
    public int winlost; //смог или не смог (пока 0 или 1)

    //статистика игрока - 4 типа заданий
    public int strength;
    public int dexterity;
    public int luck;
    public int intelligence;

    public Player(String name, ImageView iv) {
        this.bet = 0;
        this.name = name;
        this.playerImage = iv;
        this.account = new Account();
    }

}
