package cn.akfang.berry.common.utils;


import cn.hutool.jwt.JWTUtil;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BerryJWTUtil {

    private static final String key = "berry";

    public static String createToken(Map payload) {
        return JWTUtil.createToken(payload, key.getBytes(StandardCharsets.UTF_8));
    }

    public static Map parseToken(String token) {
        if (!JWTUtil.verify(token, key.getBytes(StandardCharsets.UTF_8))) {
            throw new RuntimeException("Token验证失败");
        }
        return JWTUtil.parseToken(token).getPayloads().toBean(Map.class);
    }
}
