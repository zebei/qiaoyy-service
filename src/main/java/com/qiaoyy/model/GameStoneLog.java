package com.qiaoyy.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSONObject;
@Table(name = "game_stone_log")
@Entity
public class GameStoneLog {
    @Id
    private long id;
    private long userid;
    private long fightUserId;
    private int userChoice;
    private int fightChoice;
    private int userRoundScore;//user积分变化
    private int fightRoundScore;//fight积分变化
    private long endGameTime;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getUserid() {
        return userid;
    }
    public void setUserid(long userid) {
        this.userid = userid;
    }
    public long getFightUserId() {
        return fightUserId;
    }
    public void setFightUserId(long fightUserId) {
        this.fightUserId = fightUserId;
    }
    public int getUserChoice() {
        return userChoice;
    }
    public void setUserChoice(int userChoice) {
        this.userChoice = userChoice;
    }
    public int getFightChoice() {
        return fightChoice;
    }
    public void setFightChoice(int fightChoice) {
        this.fightChoice = fightChoice;
    }
    public int getUserRoundScore() {
        return userRoundScore;
    }
    public void setUserRoundScore(int userRoundScore) {
        this.userRoundScore = userRoundScore;
    }
    public int getFightRoundScore() {
        return fightRoundScore;
    }
    public void setFightRoundScore(int fightRoundScore) {
        this.fightRoundScore = fightRoundScore;
    }
    public long getEndGameTime() {
        return endGameTime;
    }
    public void setEndGameTime(long endGameTime) {
        this.endGameTime = endGameTime;
    }
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
    
}
