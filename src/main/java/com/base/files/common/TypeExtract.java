package com.base.files.common;

import com.google.gson.GsonBuilder;

final public class TypeExtract {

    static Long stringToLong(final String str) {
        return Long.parseLong(str, 10);
    }

    static Integer stringToInt(final String str) {
        return Integer.parseInt(str, 10);
    }

    public static String getStringFromObj(final Object obj) {
        final String str = (String) obj;

        if (str == null || str.length() == 0) {
            return null;
        }
        return str;
    }

    public static Boolean getBooleanFromObj(final Object obj) {

        return (Boolean) obj;
    }

    public static Double getDoubleFromObj(final Object obj) {

        return (Double) obj;
    }

    public static Integer getIntegerFromObj(final Object str) {
        if (str == null) {
            return null;
        }
        if (str instanceof Integer) {
            return (Integer) str;
        }
        if (str instanceof Double) {
            final Double d = (Double) str;
            return d.intValue();
        }
        // String fall back
        return stringToInt((String) str);
    }

    public static Long getLongFromObj(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Double) {
            final Double d = (Double) obj;
            return d.longValue();
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        // String fall back
        return stringToLong((String) obj);
    }

    public static String getString(final String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        return str;
    }

    public static Double getDouble(final String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        return Double.parseDouble(str);
    }

    public static Integer getInteger(String str) {
        if (StringUtil.isNullOrWhiteSpace(str)) {
            return null;
        }
        return stringToInt(str);
    }

    public static Long getLong(String str) {
        if (StringUtil.isNullOrWhiteSpace(str)) {
            return null;
        }
        return stringToLong(str);
    }

    public static Boolean getBoolean(final String string) {
        if (string == null) {
            return null;
        }

        if ("0".equals(string)) {
            return false;
        }
        if ("1".equals(string)) {
            return true;
        }

        return Boolean.valueOf(string);
    }

    public static String getJsonFromObj(final Object obj) {
        return new GsonBuilder().create().toJson(obj);
    }

    public static Long getVariableLongFromObj(final Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof String) {
            return Long.parseLong(obj + "");
        }
        if (obj instanceof Double) {
            return ((Double) obj).longValue();
        }
        throw new IllegalArgumentException("Cannot cast '" + obj + "' of type '" + obj.getClass() + "' to Long");
    }

}
