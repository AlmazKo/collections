package com.almazko.collection;

import org.openjdk.jmh.annotations.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Contains {
    static String[] values;
    static long[] keys;
    static Long[] keys2;

    static {


        values = new String[10_000];
        keys = new long[10_000];
        keys2 = new Long[10_000];
        int i = 0;
        for (long v = 10_000_000_000L; v < 10_000_010_000L; v++) {
            keys[i] = v;
            keys2[i] = v;
            values[i++] = v + "";
        }

    }


//    @Benchmark
//    public boolean emptyHashMap() {
//        HashMap<Long, String> map = new HashMap<>();
//        return map.containsKey(1L);
//    }
//
//    @Benchmark
//    public boolean emptyLongMap() {
//        LongMap<String> map = new LongMap<>();
//        return map.containsKey(1L);
//    }

    @Benchmark
    public int putHashMap() {
        HashMap<Long, String> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys2[i], values[i]);
        }

        return map.size();

    }

    @Benchmark
    public int putLongMap() {
        LongMap<String> map = new LongMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }

        return map.size();
    }
}