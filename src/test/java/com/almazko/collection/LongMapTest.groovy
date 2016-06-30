package com.almazko.collection

import spock.lang.Specification

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

    def "Get with collisions"() {
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
