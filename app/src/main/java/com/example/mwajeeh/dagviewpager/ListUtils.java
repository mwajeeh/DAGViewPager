package com.example.mwajeeh.dagviewpager;

import java.util.List;

public class ListUtils {
    public static boolean isNotEmpty(List<?> list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static int count(List<?> list) {
        return list == null ? 0 : list.size();
    }

    public static boolean indexInRange(int position, List<?> list) {
        return !isEmpty(list) && position >= 0 && position < list.size();
    }
}
