package com.qiaoyy.mannger.dispather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.qiaoyy.mannger.game.StoneManager;
import com.qiaoyy.netty.WebSocketServerHandler;

import io.netty.channel.ChannelHandlerContext;

/**
 * The type GameDispather.
 *
 * @author 何金成
 * @ClassName: GameDispather
 * @Description: 消息路由分发
 * @date 2015年12月14日 下午7:12:57
 */
@Component
public class GameDispather {
    @Autowired
    private  StoneManager stoneManager;
  
    

    /**
     * Init mgr.
     */
    public void initMgr() {
    }
    
 
    /**
     * 消息路由分发
     *
     * @param api
     * @param data
     */
    public  void dispatch(Api api, JSONObject data, ChannelHandlerContext ctx) {
        switch (api) {
            case TEST:
                // TEST CODE
                WebSocketServerHandler.writeJSON(ctx, data);
                break;
            case STONE:
                stoneManager.operationCheck(ctx, data);
                break;
        }
    }
}
