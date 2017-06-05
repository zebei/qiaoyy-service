package com.qiaoyy.netty;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiaoyy.log.AppLog;
import com.qiaoyy.mannger.dispather.Api;
import com.qiaoyy.mannger.dispather.GameDispather;
import com.qiaoyy.thread.ThreadPool;
import com.qiaoyy.util.MBRequest;

import static io.netty.handler.codec.http.HttpHeaderNames.HOST;
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    @Autowired
    private GameDispather gameDispather;
    
    private WebSocketServerHandshaker handshaker;
    // websocket 服务的 uri
    private static final String WEBSOCKET_PATH = "/websocket";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        AppLog.LOG_INTERFACE.info("channel.active - {}", ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        AppLog.LOG_INTERFACE.info("channel.inactive - {}", ctx.channel().remoteAddress().toString());
        ChannelMgr.getInstance().removeChannel(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        AppLog.LOG_INTERFACE.error("channel.exception - {}", cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 传统的HTTP接入
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
        // WebSocket接入
        else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @SuppressWarnings("deprecation")
    protected void handleHttpRequest(ChannelHandlerContext ctx,
                                     FullHttpRequest req) throws Exception {
        // 如果HTTP解码失败，返回HHTP异常
        if (!req.getDecoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1,
                    BAD_REQUEST));
            return;
        }

        QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
        Map<String, List<String>> params = decoder.parameters();
        AppLog.LOG_COMMON.info("ws.http.params - {}", JSON.toJSONString(params));
        if (!params.containsKey(MBRequest.REQ_USERID)) {
            // 握手连接必须传userid
            AppLog.LOG_COMMON.error("websocket.handshake.err - userid is needed");
            return;
        }
        String userIdString = params.get(MBRequest.REQ_USERID).get(0);
        long userid = 0;
        try {
            userid = Long.parseLong(userIdString);
            ChannelMgr.getInstance().addChannelUser(ctx, userid);
            ChannelMgr.getInstance().updatChannelLastActiveTime(ctx);
        } catch (Exception e) {
            AppLog.LOG_COMMON.error("websocket.handshake.err - userid[{" + userIdString + "}] must be right formate", e);
            return;
        }

        // 构造握手响应返回，本机测试
//        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
//                "ws://localhost:" + WebSocketServer.port + "/websocket", null,
//                false);
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req), null, true);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }

    }
    private static String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HOST) + WEBSOCKET_PATH;
        return "ws://" + location;
    }
    protected void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 过滤非文本消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }

        // 更新心跳时间
        ChannelMgr.getInstance().updatChannelLastActiveTime(ctx);

        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        try {
            JSONObject msgJsonObject = JSON.parseObject(request);
            int apiId = msgJsonObject.getIntValue("game");
            Api api = Api.getByApiId(apiId);
            if (api == null) {
                throw new IllegalArgumentException("Api id error");
            }
            AppLog.LOG_INTERFACE.info(String.format("[%s] [%s] - %s", ctx.channel(), api.getNote(), request));
            ThreadPool.dispatch(api.getThreadType(), () -> {
                gameDispather.dispatch(api, msgJsonObject.getJSONObject("data"), ctx);
            });
        } catch (Exception e) {
            AppLog.LOG_INTERFACE.info(String.format("[%s] received - %s", ctx.channel(), request));
            AppLog.LOG_INTERFACE.error("socket frame error", e);
        }
    }

    /**
     * 发送消息
     *
     * @param ctx
     * @param msg
     */
    public static void writeJSON(ChannelHandlerContext ctx, Object msg) {
        String sentMsg = null;
        if (msg instanceof String) {
            sentMsg = (String) msg;
        } else {
            sentMsg = JSON.toJSONString(msg);
        }
        AppLog.LOG_INTERFACE.info(String.format("[%s] sent - %s", ctx.channel(), sentMsg));
        ctx.channel().writeAndFlush(new TextWebSocketFrame(sentMsg));
    }

    @SuppressWarnings("deprecation")
    protected static void sendHttpResponse(ChannelHandlerContext ctx,
                                           FullHttpRequest req, FullHttpResponse res) {
        // 返回应答给客户端
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            setContentLength(res, res.content().readableBytes());
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 按Channel组发消息
     *
     * @param ctg
     * @param msg
     */
    public static void writeJSON(ChannelGroup ctg, Object msg) {
        String sentMsg = null;
        if (msg instanceof String) {
            sentMsg = (String) msg;
        } else {
            sentMsg = JSON.toJSONString(msg);
        }
        AppLog.LOG_INTERFACE.info(String.format("sent group - %s", sentMsg));
        ctg.writeAndFlush(new TextWebSocketFrame(sentMsg));
    }
}
