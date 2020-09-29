package com.small.small.utils;

import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @desc 校验工具类
 * @date: 2020-06-30 17:46
 **/
public class CheckUtil {
    public static boolean isNull(List list) {
        return list == null || list.size() == 0 ? true : false;
    }

    public static boolean isNull(Long l) {
        return l == null || l == 0;
    }

    public static boolean isNull(Collection collection) {
        return collection == null || collection.size() == 0 ? true : false;
    }

    public static boolean isNull(String str) {
        return str == null || str.length() <= 0;
    }

    public static boolean isNullSet(Set set) {
        return set == null || set.size() == 0 ? true : false;
    }

    public static boolean isNull(byte[] bytes) {
        return bytes == null || bytes.length == 0 ? true : false;
    }

    public static boolean hasNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean allEmpty(String... strs) {
        for (String str : strs) {
            if (!StringUtils.isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean blankStr(String str) {
        return str == null || str.length() == 0 ? true : false;
    }

    public static boolean isBlank(String str) {
        return str == null || "".equals(str) ? true : false;
    }

    public static boolean isNull(Object object) {
        return object == null ? true : false;
    }

    public static boolean isNullMap(Map map) {
        return map == null || map.size() == 0 ? true : false;
    }

    public static String getString(Object o) {
        return o == null ? null : o.toString();
    }

    public static Double getDouble(Object o) {
        try {
            return o == null ? null : Double.valueOf(o.toString());
        } catch (Exception e){
            return null;
        }
    }

    public static boolean checkLocation(String location) {
        if (isNull(location)) {
            return false;
        }

        if (location.indexOf(",") <= 0 || location.endsWith(",")) {
            return false;
        }
        return true;
    }

    /**
     * 检查若干个区间是否完整覆盖0-999999999，且无重叠
     *
     * @param list
     * @return
     */
    public static String checkInterval(List<double[]> list) {
        list.sort(Comparator.comparing(s -> s[0]));
        if (list.get(0)[0] != 0.0 || list.get(list.size() - 1)[1] != 999999999.0) {
            return "下界与上界不完整";
        }
        for (int i = 0; i < list.size() - 1; ++i) {
            Double end = list.get(i)[1];
            Double start = list.get(i + 1)[0];
            if (end < start) {
                return "下界与上界不完整";
            }
            if (end > start) {
                return "下界与上界存在重叠";
            }
        }
        return null;
    }
}
