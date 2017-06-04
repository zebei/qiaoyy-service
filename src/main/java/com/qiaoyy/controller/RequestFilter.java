package com.qiaoyy.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.qiaoyy.log.AppLog;
import com.qiaoyy.util.MBRequest;
import com.qiaoyy.util.MBUtil;

/**
 * Created by Henry on 2017/5/31.
 */
@WebFilter(filterName = "requestFilter", urlPatterns = "/*")
public class RequestFilter implements Filter {

    @Override
    public void destroy() {
        AppLog.LOG_COMMON.info("request.filter.destory");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String apiTransaction = MBUtil.getUUID();
        String reqJsonContent = MBUtil.getRequestBodyJson((HttpServletRequest) request);
        String reqJsonHeaders = MBUtil.getRequestHeadersJson((HttpServletRequest) request);
        AppLog.LOG_INTERFACE.info("[req][{}] - {} - {}", apiTransaction, reqJsonHeaders, reqJsonContent);
        request.setAttribute(MBRequest.REQ_UUID, apiTransaction);
        request.setAttribute(MBRequest.REQ_CONTENT, reqJsonContent);
        request.setAttribute(MBRequest.REQ_HEADER, reqJsonHeaders);
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        AppLog.LOG_COMMON.info("request.filter.init");
    }
}
