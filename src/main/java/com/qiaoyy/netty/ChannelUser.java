package com.qiaoyy.netty;

import com.qiaoyy.core.Globals;
import com.qiaoyy.mannger.user.Player;
import io.netty.channel.ChannelHandlerContext;

public class ChannelUser implements Hearbeat {
    public String channelId;
    public Player player;
    public ChannelHandlerContext ctx;

    private long lastActiveTime;

    @Override
    public void hearbeat() {
        if (lastActiveTime != 0 && (Globals.getTimeService().now() - lastActiveTime > ChannelMgr.overdueTime)) {
            // 超过心跳时间未活动
            ChannelMgr.getInstance().removeChannelByUserId(player.getUserid());
        }
        // 玩家心跳
        player.hearbeat();
    }

    public void updateLastActiveTime() {
        this.lastActiveTime = Globals.getTimeService().now();
    }
}
