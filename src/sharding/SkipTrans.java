/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

/**
 * description:this for SkipTrans Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/6/17]
 * @since 2021/6/17
 */
public class SkipTrans {
    public enum Str2Code {
        NEW_ORDER("TT_NEW_ORDER", jTPCCTData.TT_NEW_ORDER),
        PAYMENT("TT_PAYMENT", jTPCCTData.TT_PAYMENT),
        ORDER_STATUS("TT_ORDER_STATUS", jTPCCTData.TT_ORDER_STATUS),
        STOCK_LEVEL("TT_STOCK_LEVEL", jTPCCTData.TT_STOCK_LEVEL),
        DELIVERY("TT_DELIVERY", jTPCCTData.TT_DELIVERY),
        DELIVERY_BG("TT_DELIVERY_BG", jTPCCTData.TT_DELIVERY_BG),
        NONE("TT_NONE", jTPCCTData.TT_NONE);
        public final String str;
        public final int code;
        Str2Code(String str, int code) {
            this.str = str;
            this.code = code;
        }

        public static Str2Code str2Code(String str) {
            for (Str2Code c: values()) {
                if (c.name().equals(str) || c.str.equals(str)) {
                    return c;
                }
            }
            return NONE;
        }
    }

    public static int wrappedTransType(int transType) {
        if (PropertiesFactory.instance.skipTransTypes.contains(transType)) {
            return Str2Code.NONE.code;
        }
        return transType;
    }
}
