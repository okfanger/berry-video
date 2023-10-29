package cn.akfang.berry.gateway;

import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.utils.BerryJWTUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 鉴权过滤器 验证token
 */
@Component
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered {
    private static final String AUTHORIZE_TOKEN = "Authorization";
    private static final List<String> whiteList = Arrays.asList(
            "/misc/wx",
            "/user/wx/ticket",
            "/user/wx/login",
            "/misc/oss/upload/callback",
            "/misc/oss/transform/callback"
    );

    private boolean checkPathInWhiteList(String path) {
        return whiteList.stream().anyMatch(path::startsWith);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        if (checkPathInWhiteList(request.getURI().getPath())) {
            return chain.filter(exchange);
        }
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(AUTHORIZE_TOKEN);
        if (StrUtil.isEmpty(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        try {
            ServerHttpRequest host = exchange.getRequest()
                    .mutate()
                    .header(AuthConstants.EXCHANGE_AUTH_HEADER,
                            String.valueOf(BerryJWTUtil.parseHeaderToken(token)
                                    .get("id"))).build();
            ServerWebExchange build = exchange.mutate().request(host).build();
            return chain.filter(build);
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}