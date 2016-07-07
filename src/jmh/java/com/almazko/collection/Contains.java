package com.almazko.collection;

import org.openjdk.jmh.annotations.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class Contains {

    private static final int SIZE_DATA = 10000;
    private String[] values;
    private long[] keys;
    private Long[] keys2;
    private Random rand = new Random(0);


    @Setup(Level.Trial)
    public void init() {
        values = new String[SIZE_DATA];
        keys = new long[SIZE_DATA];
        keys2 = new Long[SIZE_DATA];
        int i = 0;
        for (long v = 10_000_000_000L; i <SIZE_DATA; v++, i++) {
            keys[i] = rand.nextInt();
            keys2[i] = keys[i];
            values[i] = keys[i] + "";
        }

//        System.out.println(Arrays.toString(keys));
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