/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * description:this for TimeFormat Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/5/29]
 * @since 2021/5/29
 */
public class TimeFormat {
    public static String curStrDataTime() {
        return DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
    }
}
