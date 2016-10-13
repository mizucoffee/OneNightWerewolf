package net.mizucoffee.onenightwerewolf;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by KawakawaPlanning on 10/8/16.
 */
public class Jinro {

    //割り振るメソッド
    //保持

    public static int WEREWOLF = 0;
    public static int SEER = 1;
    public static int ROBBER = 2;
    public static int MINION = 3;
    public static int TANNER = 4;
    public static int VILLAGER = 5;

    private int playersNum; //プレイ人数

    ArrayList<String> playerNames; //プレイヤー名前
    ArrayList<Integer> cards; //ランダムにするカード
    ArrayList<Integer> poll;
    ArrayList<Integer> killed;

    public boolean isSwap = false;
    public int swapPlayer;

    public Jinro(){
        playerNames = new ArrayList<>();
        cards       = new ArrayList<>();
        poll        = new ArrayList<>();
        killed      = new ArrayList<>();
    }

    public int getPlayersNum() {
        return playersNum;
    }

    public void setPlayersNum(int playersNum) {
        this.playersNum = playersNum;
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public void addCard(int card){
        cards.add(card);
    }
    public void addPlayer(String playerName){
        playerNames.add(playerName);
    }

    public static String getCardName(int card){
        switch (card){
            case 0: return "人狼";
            case 1: return "占師";
            case 2: return "怪盗";
            case 3: return "狂人";
            case 4: return "吊人";
            case 5: return "村人";
        }
        return "不明";
    }
}
