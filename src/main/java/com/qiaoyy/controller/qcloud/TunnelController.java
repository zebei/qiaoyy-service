package com.qiaoyy.controller.qcloud;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.tunnel.TunnelHandleOptions;
import com.qcloud.weapp.tunnel.TunnelService;
import com.qiaoyy.qcloud.ChatTunnelHandler;

/**
 * 使用 SDK 提供信道服务
 */
@Controller
public class TunnelController {

    /**
     * 把所有的请求交给 SDK 处理，提供 TunnelHandler 处理信道事件
     */
    @RequestMapping(value = "/tunnel")
    @ResponseBody
    public void createTunnel(HttpServletRequest request, HttpServletResponse response) {

        // 创建信道服务处理信道相关请求
        TunnelService tunnelService = new TunnelService(request, response);

        try {
            // 配置是可选的，配置 CheckLogin 为 true 的话，会在隧道建立之前获取用户信息，以便业务将隧道和用户关联起来
            TunnelHandleOptions options = new TunnelHandleOptions();
            options.setCheckLogin(true);

            // 需要实现信道处理器，ChatTunnelHandler 是一个实现的范例
            tunnelService.handle(new ChatTunnelHandler(), options);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
}
