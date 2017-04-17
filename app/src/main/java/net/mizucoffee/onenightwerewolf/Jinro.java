package net.mizucoffee.onenightwerewolf;

/**
 * Created by Mizucofee on 2017/04/17.
 */

public class Jinro {
    public static int WEREWOLF = 0;
    public static int SEER = 1;
    public static int ROBBER = 2;
    public static int MINION = 3;
    public static int TANNER = 4;
    public static int VILLAGER = 5;

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
