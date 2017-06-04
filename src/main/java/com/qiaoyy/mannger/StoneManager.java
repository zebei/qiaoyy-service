package com.qiaoyy.mannger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiaoyy.mannger.user.UserManager;
import com.qiaoyy.model.UserModel;
import com.qiaoyy.netty.ChannelMgr;
import com.qiaoyy.netty.WebSocketServerHandler;
import com.qiaoyy.util.MBResponse;
import com.qiaoyy.util.MBResponseCode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StoneManager {
    private static final StoneManager stoneManager = new StoneManager();
    public ConcurrentHashMap<Long, ChannelGroup> pkSceneMap = new ConcurrentHashMap<Long, ChannelGroup>();
    public Map<Long, Map<Long, Integer>> pkChoiceMap = new HashMap<>();
    public List<Long> waitingUsers = new Vector<Long>();

    private StoneManager() {

    }

    public static StoneManager getInstance() {
        return stoneManager;
    }

    public void operationCheck(ChannelHandlerContext ctx, JSONObject data) {
        String operation = data.getString("operation");
        if (operation.equals("join")) {
            joinRoom(ctx, data);
        } else if (operation.equals("changeChoice")) {
            changeChoice(ctx, data);
        }
    }

    private void changeChoice(ChannelHandlerContext ctx, JSONObject data) {
        Long userId = data.getLong("userId");
        Integer userChoice = data.getInteger("choice");
        Map<Long, Integer> roomChoice = pkChoiceMap.get(userId);
        Long fightUserId = 0L;
        for (long key : roomChoice.keySet()) {
            if (key != userId) {
                fightUserId = key;
            }
        }
        roomChoice.put(userId, userChoice);
        pkChoiceMap.put(userId, roomChoice);
        pkChoiceMap.put(fightUserId, roomChoice);
    }

    private void joinRoom(ChannelHandlerContext ctx, JSONObject data) {
        Long userId = data.getLong("userId");
        // 匹配等待队列
        if (waitingUsers.size() == 0) {
            if (pkSceneMap.containsKey(userId)) {
                //点击继续 或重连玩家 逻辑待确认
                Map<Long, Integer> roomChoice = pkChoiceMap.get(userId);
                Long fightUserId = 0L;
                for (long key : roomChoice.keySet()) {
                    if (key != userId) {
                        fightUserId = key;
                    }
                }
                JSONObject userReturnMsg = new JSONObject();
                UserModel userModel = UserManager.getInstance().findByUserid(userId);
                UserModel fightModel = UserManager.getInstance().findByUserid(fightUserId);

                JSONArray userArray = new JSONArray();
                userArray.add(userModel);
                userArray.add(fightModel);
                userReturnMsg.put("roomUsers", userArray);
                userReturnMsg.put("gameTime", 5);

                WebSocketServerHandler.writeJSON(pkSceneMap.get(Long.valueOf(userId)), MBResponse.getMBResponse(MBResponseCode.STONE_START, userReturnMsg));

                return;
            }
            synchronized (waitingUsers) {
                // 没有人，直接加入等待队列
                waitingUsers.add(userId);
            }
            //WebSocketServerHandler.writeJSON(ctx, MBResponse.getMBResponse(MBResponseCode.SUCCESS));
            return;
        }
        if (waitingUsers.contains(Long.valueOf(userId))) {
            //WebSocketServerHandler.writeJSON(ctx,MBResponse.getMBResponse(MBResponseCode.SUCCESS));
            return;
        }

        long fightUserId = 0;
        // 取出第一个人
        synchronized (pkSceneMap) {
            fightUserId = waitingUsers.remove(0);
            //分发频道组
            ChannelGroup channelGroup = new DefaultChannelGroup(
                    GlobalEventExecutor.INSTANCE);
            waitingUsers.remove(fightUserId);
            channelGroup.add(ctx.channel());
            channelGroup.add(ChannelMgr.getInstance().getChannel(fightUserId).channel());
            pkSceneMap.put(userId, channelGroup);
            pkSceneMap.put(fightUserId, channelGroup);
            //用户选项组
            Map<Long, Integer> choiceGroup = new HashMap<>();
            choiceGroup.put(userId, 0);
            choiceGroup.put(fightUserId, 0);
            pkChoiceMap.put(userId, choiceGroup);
            pkChoiceMap.put(fightUserId, choiceGroup);


        }
        JSONObject userReturnMsg = new JSONObject();
        UserModel userModel = UserManager.getInstance().findByUserid(userId);
        UserModel fightModel = UserManager.getInstance().findByUserid(fightUserId);

        JSONArray userArray = new JSONArray();
        userArray.add(userModel);
        userArray.add(fightModel);
        userReturnMsg.put("roomUsers", userArray);
        userReturnMsg.put("gameTime", 5);

        WebSocketServerHandler.writeJSON(pkSceneMap.get(Long.valueOf(userId)), MBResponse.getMBResponse(MBResponseCode.STONE_START, userReturnMsg));

    }
}
