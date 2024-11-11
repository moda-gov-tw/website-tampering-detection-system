package com.iisi.patrol.webGuard.service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Profiles;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 系統環境
 */
@Getter
@RequiredArgsConstructor
public enum EnvEnum {

    dev(EnvConstant.DEV, "本機"),

    sit(EnvConstant.PROD, "200, 202"),

    uat(EnvConstant.PROD_PCC, "HGR 練習區"),

    prod(EnvConstant.PROD_DR, "HGR 正式區"),

    ;

    /** 對應 profile */
    private final String code;
    private final String desc;
    private static final Map<String, EnvEnum> MAP;

    static {
        final Map<String, EnvEnum> map = new HashMap<>();
        for (EnvEnum env : values()) {
            map.put(env.code, env);
        }
        MAP = Collections.unmodifiableMap(map);
    }

    public Profiles profile() {
        return Profiles.of(code);
    }

    public static Optional<EnvEnum> lookup(final String code) {
        return Optional.ofNullable(MAP.get(code));
    }
}
