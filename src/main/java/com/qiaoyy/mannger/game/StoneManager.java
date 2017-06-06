package com.qiaoyy.mannger.game;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiaoyy.mannger.user.UserManager;
import com.qiaoyy.model.GameStoneLog;
import com.qiaoyy.model.UserModel;
import com.qiaoyy.netty.WebSocketServerHandler;
import com.qiaoyy.repository.GameStoneRepository;
import com.qiaoyy.repository.UserRepository;
import com.qiaoyy.util.MBResponse;
import com.qiaoyy.util.MBResponseCode;

@Service
public class StoneManager {
    
    @Autowired
    private UserManager userManager;
    @Autowired
    private GameStoneRepository gameStoneRepository;
    @Autowired
    private UserRepository userRepository;
    
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
//        if (waitingUsers.size()==0) {
//            waitingUsers.add(10000019L);
//            
//        }
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

                WebSocketServerHandler.writeJSON(pkSceneMap.get(Long.valueOf(userId)), MBResponse.getMBResponse(MBResponseCode.WEBSOCKET_STONE_START, userReturnMsg));

                return;
            }
            synchronized (waitingUsers) {
                // 没有人，直接加入等待队列
                waitingUsers.add(userId);
            }
            return;
        }
        //单用户本地测试注掉
        if (waitingUsers.contains(Long.valueOf(userId))) {
            return;
        }

        Long fightUserId = 0L;
        // 取出第一个人
        synchronized (pkSceneMap) {
            fightUserId = waitingUsers.remove(0);
            //分发频道组
            ChannelGroup channelGroup = new DefaultChannelGroup(
                    GlobalEventExecutor.INSTANCE);
            waitingUsers.remove(fightUserId);
            channelGroup.add(ctx.channel());
            channelGroup.add(ctx.channel());
            //channelGroup.add(ChannelMgr.getInstance().getChannel(fightUserId).channel());
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

        WebSocketServerHandler.writeJSON(pkSceneMap.get(Long.valueOf(userId)), MBResponse.getMBResponse(MBResponseCode.WEBSOCKET_STONE_START, userReturnMsg));
        try {
            Thread.sleep(gametime*1000);
            endGame(userId);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
          
        
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
        //db add log
        GameStoneLog gameStoneLog=new GameStoneLog();
        gameStoneLog.setUserid(userId);
        gameStoneLog.setFightUserId(fightUserId);
        gameStoneLog.setUserChoice(resultMap.get(userId));
        gameStoneLog.setFightChoice(resultMap.get(fightUserId));
        gameStoneLog.setUserRoundScore(1);//增加1分  配置待定
        gameStoneLog.setFightRoundScore(1);
        gameStoneLog.setEndGameTime(System.currentTimeMillis());
        gameStoneRepository.saveAndFlush(gameStoneLog);
        //发送结果
        if (resultMap.get(userId)==0||resultMap.get(fightUserId)==0) {
            sendEndGameMessage(resultMap,userId,fightUserId,0,0);
        }
        if (resultMap.get(userId)==resultMap.get(fightUserId)) {
            //平局
           sendEndGameMessage(resultMap,userId,fightUserId,0,0);
        }else if ((resultMap.get(userId)!=3&&resultMap.get(userId)>resultMap.get(fightUserId))||(resultMap.get(userId)==1&&resultMap.get(fightUserId)==3)) {
            //userid获胜
            userRepository.updateScoreById(1, userId);
            userRepository.updateScoreById(-1, fightUserId);
            sendEndGameMessage(resultMap,userId,fightUserId,1,-1);
            
        }else {
            //userid失败
            userRepository.updateScoreById(-1, userId);
            userRepository.updateScoreById(1, fightUserId);
            sendEndGameMessage(resultMap,userId,fightUserId,-1,1);
        }
        
    }
    private void sendEndGameMessage(Map<Long, Integer> resultMap, Long userId, Long fightUserId, int userRoundScore, int fightRoundScore) {
        JSONArray userArray = new JSONArray();
        UserModel userModel = userManager.findByUserid(userId);
        UserModel fightModel = userManager.findByUserid(fightUserId);
        JSONObject userJsonObject=new JSONObject();
        userJsonObject.put("userid", userModel.getId());
        userJsonObject.put("roundScore", userRoundScore);
        userJsonObject.put("totalScore", userModel.getScore());
        userJsonObject.put("choice", resultMap.get(userId));
        userArray.add(userJsonObject);
        
        JSONObject fightJsonObject=new JSONObject();
        fightJsonObject.put("userid", fightModel.getId());
        fightJsonObject.put("roundScore", fightRoundScore);
        fightJsonObject.put("totalScore", fightModel.getScore());
        fightJsonObject.put("choice", resultMap.get(fightUserId));
        userArray.add(fightJsonObject);
        WebSocketServerHandler.writeJSON(pkSceneMap.get(Long.valueOf(userId)), MBResponse.getMBResponse(MBResponseCode.WEBSOCKET_STONE_END, userArray));
    }
}
