package com.qiaoyy.mannger.game;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiaoyy.mannger.user.UserManager;
import com.qiaoyy.model.UserModel;
import com.qiaoyy.netty.ChannelMgr;
import com.qiaoyy.netty.WebSocketServerHandler;
import com.qiaoyy.util.MBResponse;
import com.qiaoyy.util.MBResponseCode;

@Service
public class StoneManager {
    
    @Autowired
    private UserManager userManager;
    
    public ConcurrentHashMap<Long, ChannelGroup> pkSceneMap = new ConcurrentHashMap<Long, ChannelGroup>();
    public Map<Long, Map<Long, Integer>> pkChoiceMap = new HashMap<>();
    public List<Long> waitingUsers = new Vector<Long>();
    private static int gametime = 5;
  


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
                UserModel userModel = userManager.findByUserid(userId);
                UserModel fightModel = userManager.findByUserid(fightUserId);

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
//        if (waitingUsers.contains(Long.valueOf(userId))) {
//            //WebSocketServerHandler.writeJSON(ctx,MBResponse.getMBResponse(MBResponseCode.SUCCESS));
//            return;
//        }

        Long fightUserId = 0L;
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
        UserModel userModel = userManager.findByUserid(userId);
        UserModel fightModel = userManager.findByUserid(fightUserId);

        JSONArray userArray = new JSONArray();
        userArray.add(userModel);
        userArray.add(fightModel);
        userReturnMsg.put("roomUsers", userArray);
        userReturnMsg.put("gameTime", gametime);

        WebSocketServerHandler.writeJSON(pkSceneMap.get(Long.valueOf(userId)), MBResponse.getMBResponse(MBResponseCode.STONE_START, userReturnMsg));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                endGame(userId);
            }
        }, 0 , gametime*1000);
        
    }
    private void endGame(Long userId) {
        Map<Long, Integer> roomChoice = pkChoiceMap.get(userId);
        Long fightUserId = 0L;
        for (long key : roomChoice.keySet()) {
            if (key != userId) {
                fightUserId = key;
            }
        }
        Map<Long, Integer> resultMap=new HashMap<>();
        synchronized (pkChoiceMap) {
           resultMap = pkChoiceMap.remove(userId);
           pkChoiceMap.remove(fightUserId);
        }
        if (resultMap.get(userId)==resultMap.get(fightUserId)) {
            //平局
            
        }else if (resultMap.get(userId)>resultMap.get(fightUserId)||(resultMap.get(userId)==1&&resultMap.get(fightUserId)==3)) {
            //userid获胜
        }else {
            //userid失败
        }
        
    }
}
