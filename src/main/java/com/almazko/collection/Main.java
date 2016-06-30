package com.almazko.collection;

/**
 * Created by almaz on 30.06.16.
 */
public class Main {

    public static void main(String[] args) {


        LongMap<String> map = new LongMap<>();

        map.put(123, "Potato");
        map.put(124, "Yellow");


        assert map.containsKey(123);
        assert map.containsKey(21242);
    }
}
