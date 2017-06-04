package com.qiaoyy.netty;

import com.qiaoyy.core.AppInit;
import com.qiaoyy.core.Globals;
import com.qiaoyy.log.AppLog;
import com.qiaoyy.mannger.user.Player;
import com.qiaoyy.thread.ThreadType;
import io.netty.channel.ChannelHandlerContext;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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

    /**
     * 心跳时间
     */
    private static long heartbeatTime = 1 * 1000;

    /**
     * 心跳过期时间
     */
    public static int overdueTime = 3 * 60 * 1000;

    private ChannelMgr() {
        channelMap = new ConcurrentHashMap<String, ChannelUser>();
        playerMap = new ConcurrentHashMap<Long, Player>();
    }

    public void init() {
        if (AppInit.run.getEnvironment().containsProperty("app.channel.heartbeat")) {
            heartbeatTime = Integer.parseInt(AppInit.run.getEnvironment().getProperty("app.channel.heartbeat"));
        }
        if (AppInit.run.getEnvironment().containsProperty("app.channel.overdue")) {
            overdueTime = Integer.parseInt(AppInit.run.getEnvironment().getProperty("app.channel.overdue"));
        }
    }

    public void startChannelTick() {
        AppLog.LOG_COMMON.info("channel.heartbeat.check.start");
        Globals.getScheduleService().scheduleWithFixedDelay(ThreadType.CHANNEL_HEART_BEAT_THREAD, () -> tick(), 0, heartbeatTime);
    }

    @Override
    public void tick() {
        if (AppLog.LOG_COMMON.isDebugEnabled()) {
            AppLog.LOG_COMMON.debug("channel.heartbeat.check...");
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

    public void updateChannelHearbeatTime(ChannelHandlerContext ctx) {
        channelMap.get(ctx.channel().id().asShortText()).updateLastActiveTime();
    }

    /**
     * @throws
     * @Title: closeAllChannel
     * @Description: 关闭所有Channel void
     */
    public void closeAllChannel() {
        channelMap.clear();
        playerMap.clear();
        AppLog.LOG_COMMON.info("关闭所有channel");
    }

    /**
     * @param ctx void
     * @throws
     * @Title: removeChannel
     * @Description: 移除channel
     */
    public void removeChannel(ChannelHandlerContext ctx) {
        ChannelUser channelUser = channelMap.get(ctx.channel().id().asShortText());
        channelMap.remove(channelUser.channelId);
        playerMap.remove(channelUser.player.getUserid());
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
            if (u.player.getUserid() == userid) {
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
        return channelMap.get(userId);
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
