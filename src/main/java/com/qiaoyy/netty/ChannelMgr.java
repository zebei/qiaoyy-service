package com.qiaoyy.netty;

import io.netty.channel.ChannelHandlerContext;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.qiaoyy.core.AppInit;
import com.qiaoyy.log.AppLog;
import com.qiaoyy.mannger.user.Player;

/**
 * @author 何金成
 * @ClassName: ChannelMgr
 * @Description: Channel管理类
 * @date 2015年12月25日 下午3:34:06
 */
public class ChannelMgr implements Tickable {
    public ConcurrentHashMap<String, ChannelUser> channelMap;
    public ConcurrentHashMap<Long, Player> playerMap;
    private static ChannelMgr inst;

    private volatile boolean inited = false;

    /**
     * 心跳过期时间
     */
    public static int overdueTime = 3 * 60 * 1000;

    private ChannelMgr() {
        channelMap = new ConcurrentHashMap<String, ChannelUser>();
        playerMap = new ConcurrentHashMap<Long, Player>();
    }

    public void init() {
        if (AppInit.run.getEnvironment().containsProperty("app.channel.overdue")) {
            overdueTime = Integer.parseInt(AppInit.run.getEnvironment().getProperty("app.channel.overdue"));
        }
        inited = true;
    }

    @Override
    public void tick() {
        if (!inited) {
            return;
        }
        channelMap.values().forEach(ChannelUser::hearbeat);
    }

    public static ChannelMgr getInstance() {
        if (inst == null) {
            inst = new ChannelMgr();
        }
        return inst;
    }

    /**
     * @param ctx
     * @param userId
     * @return ChannelUser
     * @throws
     * @Title: addChannelUser
     * @Description: channel管理
     */
    public ChannelUser addChannelUser(ChannelHandlerContext ctx, long userId) {
        String channelId = ctx.channel().id().asShortText();
        if (channelMap.containsKey(channelId) || playerMap.containsKey(userId)) {
            throw new IllegalArgumentException("userId already exists");
        }
        ChannelUser ret = new ChannelUser();
        ret.channelId = channelId;
        ret.ctx = ctx;
        ret.player = Player.buildPlayer(userId);
        channelMap.put(channelId, ret);
        playerMap.put(ret.player.getUserid(), ret.player);
        return ret;
    }

    public void updatChannelLastActiveTime(ChannelHandlerContext ctx) {
        channelMap.get(ctx.channel().id().asShortText()).updateLastActiveTime();
    }

    /**
     * @throws
     * @Title: closeAllChannel
     * @Description: 关闭所有Channel void
     */
    public void closeAllChannel() {
        for (ChannelUser user : channelMap.values()) {
            user.ctx.close();
        }
        channelMap.clear();
        playerMap.clear();
        AppLog.LOG_NET.info("channel.all.close");
    }

    /**
     * @param ctx void
     * @throws
     * @Title: removeChannel
     * @Description: 移除channel
     */
    public void removeChannel(ChannelHandlerContext ctx) {
        ChannelUser channelUser = channelMap.get(ctx.channel().id().asShortText());
        if (channelUser!=null) {
            channelUser.ctx.close();
            channelUser = channelMap.remove(channelUser.channelId);
            playerMap.remove(channelUser.player.getUserid());
            if (channelUser != null) {
                AppLog.LOG_NET.info("channel.close - userid:{} - channelId:{}", channelUser.player.getUserid(), channelUser.channelId);
            }
        }
    }

    /**
     * @param userid long
     * @throws
     * @Title: removeChannel
     * @Description: 移除channel
     */
    public void removeChannelByUserId(long userid) {
        playerMap.remove(userid);
        Iterator<ChannelUser> it = channelMap.values().iterator();
        while (it.hasNext()) {
            ChannelUser u = it.next();
            if (u.player.getUserid().equals(userid)) {
                it.remove();
                u.ctx.close();
                AppLog.LOG_NET.info("channel.remove - userid:{} - channelId:{}", userid, u.channelId);
            }
        }
    }

    /**
     * 根据useri找到ChannelUser
     *
     * @param userId
     * @return
     */
    public ChannelUser findByUserId(Long userId) {
        Iterator<ChannelUser> it = channelMap.values().iterator();
        while (it.hasNext()) {
            ChannelUser u = it.next();
            if (u.player.getUserid().equals(userId)) {
              return u;  
            }
        }
        return null;
    }

    /**
     * @param userId
     * @return ChannelHandlerContext
     * @throws
     * @Title: getChannel
     * @Description: 根据userid获取Channel
     */
    public ChannelHandlerContext getChannel(Long userId) {
        ChannelUser cu = findByUserId(userId);
        if (cu == null) {
            return null;
        }
        return cu.ctx;
    }

    /**
     * @return List<ChannelUser>
     * @throws
     * @Title: getAllChannels
     * @Description: 获取所有Channel
     */
    public List<ChannelUser> getAllChannels() {
        List<ChannelUser> list = new LinkedList<ChannelUser>();
        for (ChannelUser user : channelMap.values()) {
            list.add(user);
        }
        return list;
    }


}
