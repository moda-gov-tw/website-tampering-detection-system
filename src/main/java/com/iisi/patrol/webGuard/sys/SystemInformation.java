package com.iisi.patrol.webGuard.sys;

import com.iisi.patrol.webGuard.service.enums.EnvEnum;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class SystemInformation {

    private final Environment env;

    public SystemInformation(final Environment env) {
        this.env = env;
    }

    /** 是否為 HGR 正式區 */
    public boolean isProd() {
        return this.testEnv(EnvEnum.prod);
    }

    /** 是否不為 HGR 正式區 */
    public boolean isNotProd() {
        return !this.isProd();
    }

    /** 是否為 HGR 練習區 */
    public boolean isUat() {
        return this.testEnv(EnvEnum.uat);
    }

    /** 是否為 SIT 測試區 */
    public boolean isSit() {
        return this.testEnv(EnvEnum.sit);
    }

    /** 是否為本機環境 */
    public boolean isDev() {
        return this.testEnv(EnvEnum.dev);
    }

    public Environment getEnv() {
        return env;
    }

    /** 測試是否為指定環境 */
    public boolean testEnv(final EnvEnum... envEnums) {
        if (ArrayUtils.isEmpty(envEnums)) {
            throw new IllegalArgumentException("env argument is null");
        }
        if (envEnums.length == 1) {
            return this.env.acceptsProfiles(envEnums[0].profile());
        }
        final String[] profiles = Arrays.stream(envEnums)
                .map(EnvEnum::getCode)
                .collect(Collectors.toList())
                .toArray(String[]::new);
        return this.env.acceptsProfiles(Profiles.of(profiles));
    }
}