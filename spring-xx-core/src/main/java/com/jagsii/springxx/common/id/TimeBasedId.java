package com.jagsii.springxx.common.id;

import com.jagsii.springxx.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeBasedId {
    private final AtomicInteger NAME_INCREMENT = new AtomicInteger(0);
    public static final TimeBasedId INSTANCE = new TimeBasedId();

    private TimeBasedId() {

    }

    /**
     * Generate id based on dateTime, yyyyMMddHHmmss + incr{4}.
     *
     * @return the generated id.
     */
    public String nextId() {
        int incr = NAME_INCREMENT.getAndIncrement();
        if (incr > 5000) {
            NAME_INCREMENT.set(0);
        }
        return DateUtils.COMPACT_DATETIME_FORMATTER.format(LocalDateTime.now()) + StringUtils.leftPad(incr + "", 4, '0');
    }
}
