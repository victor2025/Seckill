package com.victor2022.gateway.filter;

import com.victor2022.gateway.services.UserStatusService;
import com.victor2022.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.handler.FilteringWebHandler;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author: victor2022
 * @date: 2022/8/21 下午3:11
 * @description:
 */
@Component
public class LoginFilter implements GatewayFilter, Ordered {

    @Autowired
    private UserStatusService userStatusService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        try {
            // 获取token
            List<String> tokens = request.getHeaders().get("token");
            if(tokens==null||tokens.isEmpty()){
                throw new RuntimeException();
            }
            // 判断用户登录状态
            UserInfo info = new UserInfo();
            info.setToken(tokens.get(0));
            boolean isLogged = userStatusService.isLogged(info);
            if(!isLogged){
                throw new RuntimeException();
            }
        } catch (Exception e) {
            earlyResponse(exchange);
        }
        return chain.filter(exchange);
    }

    /**
     * @param exchange:
     * @return: reactor.core.publisher.Mono<java.lang.Void>
     * @author: victor2022
     * @date: 2022/8/21 下午3:31
     * @description: 若没有权限，则直接返回重定向链接
     */
    private Mono<Void> earlyResponse(ServerWebExchange exchange){
        ServerHttpResponse resp = exchange.getResponse();
        resp.setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
        resp.getHeaders().set("Location","/login");
        return resp.writeWith(null);
    }

    /**
     * @return: int
     * @author: victor2022
     * @date: 2022/8/21 下午3:32
     * @description: 权限验证总是在最前面
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
