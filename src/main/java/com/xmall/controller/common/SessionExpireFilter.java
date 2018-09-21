package com.xmall.controller.common;

import com.xmall.common.Const;
import com.xmall.entity.User;
import com.xmall.util.CookieUtil;
import com.xmall.util.JsonUtil;
import com.xmall.util.RedisPoolUtil;
import com.xmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionExpireFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(!StringUtils.isNotBlank(loginToken)){
            User user = JsonUtil.stringToObj(RedisShardedPoolUtil.get(loginToken),User.class);
            if(user != null){
                RedisPoolUtil.setEx(loginToken,JsonUtil.objToString(user), Const.RedisCacheExtTime.REDIS_SESSION_EXTIME);
            }
            chain.doFilter(httpServletRequest,httpServletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
