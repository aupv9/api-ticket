package com.apps.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CommonUtils {
    public static boolean isNullOrEmpty(String content) {
        return content == null || content.trim().length() == 0;
    }

    public static <T> boolean isNullOrEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isNullOrEmpty(T obj){
        return obj == null;
    }

    public static <T> boolean isNullOrEmpty(T[] collection) {
        return collection == null || collection.length == 0;
    }

    public static <K, V> boolean isNullOrEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    public static <T> int size(Collection<T> collection) {
        return isNullOrEmpty(collection) ? 0 : collection.size();
    }

    public static String safeTrim(String input) {
        return isNullOrEmpty(input) ? "" : input.trim();
    }

    public static <T> T tryGetValue(T data, T defaulValue) {
        return data == null ? defaulValue : data;
    }

    public static boolean toBool(Boolean val) {
        return val != null && val;
    }


    /**
     * Returns the string representation of the specified object
     * as returned by the object's <code>toString()</code> method,
     * or null if the object is null.
     *
     * @param obj an object.
     * @return the string representation of the specified object.
     */
    public static String safeToString(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    private static final List<TimeUnit> timeUnits = Arrays.asList(TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES,
            TimeUnit.SECONDS, TimeUnit.MILLISECONDS);
    public static String toHumanReadableDuration(final long millis) {
        if (millis < 1000) {
            return millis + " " + shortName(TimeUnit.MILLISECONDS);
        }
        final StringBuilder builder = new StringBuilder();
        long acc = millis;
        for (final TimeUnit timeUnit : timeUnits) {
            final long convert = timeUnit.convert(acc, TimeUnit.MILLISECONDS);
            if (convert > 0) {
                // builder.append(convert).append(' ').append(timeUnit.name().toLowerCase()).append(", ");
                builder.append(convert).append(shortName(timeUnit)).append(", ");
                acc -= TimeUnit.MILLISECONDS.convert(convert, timeUnit);
            }
        }
        return builder.substring(0, builder.length() - 2);
    }

    private static String shortName(TimeUnit unit) {
        switch (unit) {
            case NANOSECONDS:
                return "ns";
            case MICROSECONDS:
                return "μs";
            case MILLISECONDS:
                return "ms";
            default:
                return unit.name().substring(0, 1).toLowerCase();
        }
    }


}
