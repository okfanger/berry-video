package cn.akfang.berry.common.utils;


import cn.hutool.jwt.JWTUtil;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BerryJWTUtil {


    private static final String SECRET_KEY = "berry";

    public static String createToken(Map payload) {
        return JWTUtil.createToken(payload, SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public static Map parseToken(String token) {
        if (!JWTUtil.verify(token, SECRET_KEY.getBytes(StandardCharsets.UTF_8))) {
            throw new RuntimeException("Token验证失败");
        }
        return JWTUtil.parseToken(token).getPayloads().toBean(Map.class);
    }

    public static Map parseHeaderToken(String token) {
        return parseToken(StringUtils.replace(token, "Bearer ", "").trim());
    }
}
