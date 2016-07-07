package com.almazko.collection

import spock.lang.Specification

import java.util.stream.IntStream

import static java.util.stream.IntStream.range

/**
 * Created by almaz on 30.06.16.
 */
class LongMapTest extends Specification {
    def "Get empty"() {
        given: 'Empty map'
        def map = new LongMap<String>()

        expect:
        map.size() == 0
        !map.containsKey(0)
        map.get(0) == null
    }


    def "Get"() {
        given: 'map [1:"test"]'
        def map = new LongMap<String>()

        when:
        map.put(1, "test")

        then:
        map.size() == 1
        map.containsKey(1)
        map.get(1) == "test"
        !map.containsKey(0)
        map.get(0) == null
    }

    def "Get with collisions ordered"() {
        given: 'map with 4 items same hash'
        def map = new LongMap<String>()

        when:
        map.put(32, "test1")
        map.put(64, "test2")
        map.put(96, "test3")
        map.put(128, "test4")
        map.put(160, "test5")

        then:
        map.containsKey(32)
        map.get(32) == "test1"
        map.get(64) == "test2"
        map.get(96) == "test3"
        map.get(128) == "test4"
        map.get(160) == "test5"
    }

    def "Get with collisions not ordered"() {
        given: 'map with 4 items same hash'
        def map = new LongMap<String>()

        when:

        map.put(96, "test3")
        map.put(32, "test1")
        map.put(64, "test2")
        map.put(160, "test5")
        map.put(128, "test4")

        then:
        map.containsKey(32)
        map.get(32) == "test1"
        map.get(64) == "test2"
        map.get(96) == "test3"
        map.get(128) == "test4"
        map.get(160) == "test5"
    }

    def "Get with negative"() {
        given: 'map with 4 items same hash'
        def map = new LongMap<String>()

        when:

        map.put(10, "test3")
        map.put(-32, "test1")
        map.put(0, "test2")
        map.put(88, "test5")
        map.put(64, "test4")

        then:
        map.containsKey(10)
        map.containsKey(-32)
        map.containsKey(0)
        map.containsKey(88)
        map.containsKey(64)
    }


    def "Big data"() {
        given: 'map with 4 items same hash'
        def map = new LongMap<String>()
        def rand = new Random(1)
        def size = 50

        def values = new String[size];
        def keys = new long[size];

        range(0, size).forEach({
            keys[it] = ((long)rand.nextInt() * 32)
            values[it] = keys[it] + ""
        })

        when:
        for (int i = 0; i < size; i++) {
            map.put(keys[i], values[i]);
        }

        then:
        notThrown(Exception)
    }


    def "Get with collisions1"() {
        given: 'map with 4 items different hash'
        def map = new LongMap<String>()

        when:
        map.put(1, "test1")
        map.put(2, "test2")
        map.put(3, "test3")
        map.put(4, "test4")

        then:
        map.containsKey(1)
        map.containsKey(2)
        map.containsKey(3)
        map.containsKey(4)
    }

    def "iterate"() {
        given: 'Create mock-consumer'
        def map = new LongMap<String>()
        map.put(77, "test3")
        map.put(32, "test1")
        map.put(64, "test2")
        LongMap.LongBiConsumer<String> mock = Mock()

        when:
        map.forEach(mock)

        then:
        1 * mock.accept(32, "test1")
        1 * mock.accept(64, "test2")
        1 * mock.accept(77, "test3")
    }
}
