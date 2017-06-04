package com.qiaoyy.mannger.scene;

import com.qiaoyy.mannger.user.Player;

import java.util.List;

/**
 * Created by Henry on 2017/6/4.
 */
public abstract class AbstractScene implements Scene {
    private List<Player> playerList;

    @Override
    public void tick() {
        for (Player player : playerList) {
            player.hearbeat();
        }
    }
}
