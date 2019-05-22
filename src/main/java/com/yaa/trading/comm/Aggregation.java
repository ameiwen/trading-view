package com.yaa.trading.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aggregation {
    private int value;
    private AggregationType type;
    private static final List<Aggregation> aggregationList = new ArrayList<>();
    private static final Map<AggregationType, List<Aggregation>> aggregationMap = new HashMap<>();

    static {

        final Aggregation minute1 = new Aggregation(AggregationType.m, 1);
        final Aggregation minute5 = new Aggregation(AggregationType.m, 5);
        final Aggregation hour1 = new Aggregation(AggregationType.h, 1);
        final Aggregation day1 = new Aggregation(AggregationType.d, 1);

        // 按时间顺序
        aggregationList.add(minute1);
        aggregationList.add(minute5);
        aggregationList.add(hour1);
        aggregationList.add(day1);

        resolveMap(AggregationType.m, minute1);
        resolveMap(AggregationType.m, minute5);
        resolveMap(AggregationType.h, hour1);
        resolveMap(AggregationType.d, day1);
    }

    public Aggregation() {

    }

    public Aggregation(AggregationType type, int value) {
        this.type = type;
        this.value = value;
    }

    public AggregationType getType() {
        return type;
    }

    public void setType(AggregationType type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static List<Aggregation> getDefinedAggregation() {
        return aggregationList;
    }

    public static boolean containsValue(AggregationType type, int expectValue) {
        List<Aggregation> list = aggregationMap.get(type);
        if (list != null) {
            for (Aggregation aggregation : list) {
                if (aggregation.getValue() == expectValue) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void resolveMap(AggregationType type, Aggregation aggregation) {
        List<Aggregation> list = aggregationMap.get(type);
        if (list == null) {
            list = new ArrayList<>();
            aggregationMap.put(type, list);
        }
        list.add(aggregation);
    }
}
