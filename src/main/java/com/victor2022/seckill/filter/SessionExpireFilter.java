package com.victor2022.seckill.filter;
import com.victor2022.seckill.common.param.Const;
import com.victor2022.seckill.entity.User;
import com.victor2022.seckill.config.redis.UserKey;
import com.victor2022.seckill.common.util.CookieUtil;
import com.victor2022.seckill.service.impl.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 重新设置用户session在redis的有效期
 */
@Component
public class SessionExpireFilter implements Filter {
    @Autowired
    RedisService redisService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);

        if(StringUtils.isNotEmpty(loginToken)){
            //判断logintoken是否为空或者""；
            //如果不为空的话，符合条件，继续拿user信息
            User user = redisService.get(UserKey.getByName,loginToken, User.class);
            if(user != null){
                //如果user不为空，则重置session的时间，即调用expire命令
                redisService.expire(UserKey.getByName , loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }


    @Override
    public void destroy() {

    }
}
