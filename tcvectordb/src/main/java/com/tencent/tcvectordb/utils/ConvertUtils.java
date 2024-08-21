package com.tencent.tcvectordb.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtils {
    public static List<List<Object>> convertPairToList(List<Pair<Long, Double>> pairList) {
        List<List<Object>> result = new ArrayList<>();
        for (Pair<Long, Double> p : pairList) {
            List<Object> list = new ArrayList<>();
            list.add(p.getLeft());
            list.add(p.getRight());
            result.add(list);
        }
        return result;
    }
}
