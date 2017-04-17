package net.mizucoffee.onenightwerewolf;

import java.util.ArrayList;

public class Room {

    public Room(){
    }


    private String roomId;             // 不変 ルームID
    private int phase;                 // 可変 フェーズ
    private ArrayList<String> players; // 不変 プレイヤー名 ポジション==プレイヤーID
    private ArrayList<Integer> cards;  // 可変 プレイヤーのカード
    private ArrayList<Integer> swap;   // 可変 スワップ先プレイヤーID
    private ArrayList<Integer> seer;   // 可変 占い先プレイヤーID
    private ArrayList<Integer> poll;   // 可変 投票先プレイヤーID
    private ArrayList<Integer> point;  // 可変 プレイヤーのポイント
    private int playerNum;             // 不変 プレイヤー人数
    private int playerCount;           // 可変 クラスチェック時及び投票時のプレイヤーID
    private int countDown;             // 可変 残時間

    private int werewolf;
    private int seerc;
    private int robber;
    private int minion;
    private int tanner;
    private int villager;

    public int getWerewolf() {
        return werewolf;
    }

    public void setWerewolf(int werewolf) {
        this.werewolf = werewolf;
    }

    public int getSeerc() {
        return seerc;
    }

    public void setSeerc(int seerc) {
        this.seerc = seerc;
    }

    public int getRobber() {
        return robber;
    }

    public void setRobber(int robber) {
        this.robber = robber;
    }

    public int getMinion() {
        return minion;
    }

    public void setMinion(int minion) {
        this.minion = minion;
    }

    public int getTanner() {
        return tanner;
    }

    public void setTanner(int tanner) {
        this.tanner = tanner;
    }

    public int getVillager() {
        return villager;
    }

    public void setVillager(int villager) {
        this.villager = villager;
    }

    public int getCountDown() {
        return countDown;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public ArrayList<Integer> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Integer> cards) {
        this.cards = cards;
    }

    public ArrayList<Integer> getSwap() {
        return swap;
    }

    public void setSwap(ArrayList<Integer> swap) {
        this.swap = swap;
    }

    public ArrayList<Integer> getSeer() {
        return seer;
    }

    public void setSeer(ArrayList<Integer> seer) {
        this.seer = seer;
    }

    public ArrayList<Integer> getPoll() {
        return poll;
    }

    public void setPoll(ArrayList<Integer> poll) {
        this.poll = poll;
    }

    public ArrayList<Integer> getPoint() {
        return point;
    }

    public void setPoint(ArrayList<Integer> point) {
        this.point = point;
    }
}