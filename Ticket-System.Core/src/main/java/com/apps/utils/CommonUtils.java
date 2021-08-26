package com.apps.utils;

import java.util.Collection;
import java.util.Map;

public class CommonUtils {
    public static boolean isNullOrEmpty(String content) {
        return content == null || content.trim().length() == 0;
    }

    public static <T> boolean isNullOrEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
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


}
