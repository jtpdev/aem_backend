package com.reactapp.core.core.utils;

public class ParamUtils {

    public static final String INTEGER_PATTERN = "^\\d+$";
    public static final String DECIMAL_PATTERN = "^\\d*\\.\\d+|\\d+\\.\\d*$";

    public static boolean isInteger(String value) {
        return value != null && value.matches(INTEGER_PATTERN);
    }

    public static boolean isDecimal(String value) {
        return value != null && value.matches(DECIMAL_PATTERN);
    }

}
