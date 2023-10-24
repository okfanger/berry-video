package cn.akfang.berry.gateway;

import cn.akfang.berry.common.model.BaseResponse;
import cn.akfang.berry.common.model.ErrorCode;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static org.springframework.cloud.gateway.filter.NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER;

/**
 * @author wuweifeng wrote on 2018/10/31.
 */
@Component
public class WrapperResponseGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public int getOrder() {
        // -1 is response write filter, must be called before that
        return WRITE_RESPONSE_FILTER_ORDER - 2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                        // 响应过大，防止截断
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer dataBuffer = dataBufferFactory.join(dataBuffers);


                        // probably should reuse buffers
                        byte[] content = new byte[dataBuffer.readableByteCount()];

                        dataBuffer.read(content);
                        //释放掉内存
                        DataBufferUtils.release(dataBuffer);
                        String s = new String(content, StandardCharsets.UTF_8);
                        Object result = null;
                        if (JSONUtil.isTypeJSON(s)) {
                            result = JSONUtil.parse(s);
                        } else if (JSONUtil.isTypeJSONArray(s)) {
                            result = JSONUtil.parseArray(s);
                        } else if (JSONUtil.isTypeJSONObject(s)) {
                            result = JSONUtil.parseObj(s);
                        } else {
                            result = s;
                        }
                        BaseResponse<Object> resp = new BaseResponse<>(
                                ErrorCode.SUCCESS.getCode(), result, "success");

                        byte[] uppedContent = JSONUtil.toJsonStr(resp).getBytes(StandardCharsets.UTF_8);
                        return bufferFactory.wrap(uppedContent);
                    }));
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
        // replace response with decorator
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }
}