package com.qiaoyy.mannger.user;

import com.qiaoyy.netty.Hearbeat;

/**
 * 玩家内存数据
 * <p>
 * Created by Henry on 2017/6/4.
 */
public class Player implements Hearbeat {
    private Long userid;

    private int roomId;

    public static Player buildPlayer(Long userid) {
        Player player = new Player();
        player.setUserid(userid);
        return player;
    }

    @Override
    public void hearbeat() {

    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getUserid() {
        return userid;
    }
}
