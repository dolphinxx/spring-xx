package com.jagsii.springxx.common.utils;

import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static final DateTimeFormatter COMPACT_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");
    public static final DateTimeFormatter COMPACT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter COMPACT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
}
