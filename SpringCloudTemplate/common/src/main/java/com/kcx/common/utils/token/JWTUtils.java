package com.kcx.common.utils.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 */
public class JWTUtils {

    /**
     * 创建用户token令牌
     * @param jwtConfig jwtConfig的子类
     * @param payloadValue 自定义参数，一般为用户id
     * @return
     */
    public static String getToken(JWTBaseConfig jwtConfig,String payloadValue) {
        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();
        //头部信息,仅仅是说明
        Map<String, Object> header = new HashMap<>(2);
        header.put("type", "JWT");
        header.put("alg", "HS512");
        builder.withHeader(header);
        //payload
        builder.withClaim(jwtConfig.getPayload(),payloadValue);
        //过期时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,jwtConfig.getExpireTime());
        builder.withExpiresAt(instance.getTime());
        //加密
        Algorithm algorithm = Algorithm.HMAC512(jwtConfig.getSecret());
        return builder.sign(algorithm);
    }

    /**
     * 校验token是否正确
     * @param jwtConfig jwtConfig的子类
     * @param token 访问令牌
     * @return
     */
    public static DecodedJWT verifyToken(JWTBaseConfig jwtConfig,String token) {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(jwtConfig.getSecret())).build();
            return jwtVerifier.verify(token);
    }

    /**
     * 从token中获取自定义参数，一般为用户id
     */
    public static String getPayloadValue(JWTBaseConfig jwtConfig,String token) {
        return JWT.decode(token).getClaim(jwtConfig.getPayload()).asString();
    }
}

