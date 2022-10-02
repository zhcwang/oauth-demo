package com.felix.oauth2server.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CachesEnum {
    /**
     * 使用默认值
     */
    DefaultCache,
    /**
     * 过期。最大容量使用默认值
     */
    SmsCaptchaCache(60 * 3),
    /**
     * 指定过期时间和最大容量
     */
    GraphCaptchaCache(60 * 5, 100000),
    /**
     * 指定过期时间和最大容量
     */
    CaptchaTimesCache(60 * 5, 100000),
    /**
     * 指定过期时间和最大容量
     */
    Oauth2ClientCache(60 * 60 * 2, 20),
    Oauth2AuthorizationCodeCache(60 * 3, 100000),
    Oauth2AuthorizationCodeFailureTimesCache(60 * 3, 100000),
    ;

    CachesEnum(int ttl) {
        this.ttl = ttl;
    }

    private int maxSize = 100000;
    private int ttl = 60 * 5;
}
