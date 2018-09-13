package com.softplan.logvalue.service.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Utility class for generating random Strings.
 */
public final class NullsUtil {

    public static Boolean isNull(Integer value) {
        return value == null || value == 0;
    }

    public static Boolean isNotNull(Integer value) {
        return !isNull(value);
    }

    public static Boolean isNull(Long value) {
        return value == null || value == 0;
    }

    public static Boolean isNotNull(Long value) {
        return !isNull(value);
    }


    public static Boolean isNull(Float value) {
        return value == null || value == 0;
    }

    public static Boolean isNotNull(Float value) {
        return !isNull(value);
    }
        
}
