package net.mizucoffee.onenightwerewolf;

import java.util.ArrayList;

public class Jinro {
    public static final int WEREWOLF = 0;
    public static final int SEER = 1;
    public static final int MINION = 2;
    public static final int ROBBER = 3;
    public static final int TANNER = 4;
    public static final int VILLAGER = 5;

    //勝利パターン
    public static final int VILLAGER_NORMAL = 10;
    public static final int WEREWOLF_NORMAL = 11;
    public static final int WEREWOLF_MINION = 12;
    public static final int TANNER_NORMAL   = 13;
    public static final int VILLAGER_HEIWA  = 14;
    public static final int VILLAGER_TANNER = 15;

    public static String getCardName(int card){
        switch (card){
            case 0: return "人狼";
            case 1: return "占師";
            case 2: return "狂人";
            case 3: return "怪盗";
            case 4: return "吊人";
            case 5: return "村人";
        }
        return "不明";
    }

    //勝利判定
    public static int checkWinner(ArrayList<Integer> poll,ArrayList<Integer> cards,ArrayList<Integer> swap){
        ArrayList<Integer> max = new ArrayList<>();

        int maxNum = 0;

        for (int i = 0; i < poll.size(); i++) {
            int value = poll.get(i);
            int count = 0;

            for (int j = i; j < poll.size(); j++)
                if (poll.get(j) == value)
                    count++;

            if (count > maxNum) {
                maxNum = count;
                max = new ArrayList<>();
                max.add(value);
            }

            if (count == maxNum) {
                boolean flag = true;
                for (int k : max) if (value == k) flag = false;
                if (flag) max.add(value);
            }
        }

        ArrayList<Integer> card = new ArrayList<>();

        for (int i:cards) card.add(i);
        int minion = 0;
        for (int i = 0; i != poll.size(); i++)
            if(cards.get(i)==Jinro.ROBBER){
                card.set(i, cards.get(swap.get(minion)));
                card.set(swap.get(minion),Jinro.ROBBER);
                minion++;
            }

        boolean isHeiwa = max.size() == poll.size();
        boolean werewolf = false;
        for (int i = 0;i != poll.size();i++) if(cards.get(i) == WEREWOLF) werewolf = true;
        boolean tanner  = false;
        for (int i = 0;i != poll.size();i++) if(cards.get(i) == TANNER) tanner = true;

        for (int i:max) if(cards.get(i) == TANNER) return TANNER_NORMAL;

        if(!isHeiwa){
            if(werewolf){
                for (int i:max) if(card.get(i) == WEREWOLF) return VILLAGER_NORMAL;
                for (int i:max) if(card.get(i) == MINION)   return WEREWOLF_MINION;
                return WEREWOLF_NORMAL;
            } else if(tanner){
                return VILLAGER_TANNER;
            } else {
                return WEREWOLF_NORMAL;
            }
        } else {
            if(werewolf){
                return WEREWOLF_NORMAL;
            } else {
                return VILLAGER_HEIWA;
            }
        }
    }
}
