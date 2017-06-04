package com.qiaoyy.mannger.dispather;

import com.alibaba.fastjson.JSONObject;
import com.qiaoyy.mannger.StoneManager;
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
public class GameDispather {
    private static GameDispather gameDispather = new GameDispather();

    private GameDispather() {
    }

    /**
     * Init mgr.
     */
    public void initMgr() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static GameDispather getInstance() {
        if (null == gameDispather) {
            gameDispather = new GameDispather();
        }
        return gameDispather;
    }

    /**
     * 消息路由分发
     *
     * @param api
     * @param data
     */
    public void dispatch(Api api, JSONObject data, ChannelHandlerContext ctx) {
        switch (api) {
            case TEST:
                // TEST CODE
                WebSocketServerHandler.writeJSON(ctx, data);
                break;
            case STONE:
                StoneManager.getInstance().operationCheck(ctx, data);
                break;
        }
    }
}
