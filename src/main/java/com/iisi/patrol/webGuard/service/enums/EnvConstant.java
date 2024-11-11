package com.iisi.patrol.webGuard.service.enums;

public final class EnvConstant {

    private EnvConstant() {
        throw new AssertionError();
    }

    // 本機
    public static final String DEV = "dev";
    // 200, 202
    public static final String PROD = "prod";
    // HGR 練習區
    public static final String PROD_PCC = "prod_pcc";
    // HGR 正式區
    public static final String PROD_DR = "prod_dr";
}
