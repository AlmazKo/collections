package com.almazko.collection;

import java.util.Iterator;

/**
 * Created by almaz on 30.06.16.
 */
public class LongMap<V> {

    @FunctionalInterface
    public interface LongBiConsumer<V> {
        void accept(long t, V value);
    }

    public void forEach(LongBiConsumer<V> action) {
        for (Node<V> node : nodes) {
            while (node != null) {
                action.accept(node.key, node.value);
                node = node.next;
            }
        }
    }

    public static final class Entry<V> {
        final long key;
        final V value;

        public Entry(long key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final class Node<V> {
        long key;
        V value;
        Node<V> next;

        Node(long key, V value, Node<V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }


    private Node<V>[] nodes;

    public LongMap() {
        this.nodes = new Node[32];
    }

    boolean containsKey(long key) {
        Node<V> node = getFirstNode(key);

        while (node != null) {
            if (node.key == key) return true;
            node = node.next;
        }

        return false;
    }

    V get(long key) {
        Node<V> node = getFirstNode(key);
        while (node != null) {
            if (node.key == key) return node.value;
            node = node.next;
        }

        return null;
    }

    private Node<V> getFirstNode(long key) {
        return nodes[hash(key)];
    }

    V put(long key, V value) {

        final int hash = hash(key);
        Node<V> node = nodes[hash];

        if (node == null) {
            nodes[hash] = new Node<>(key, value, null);
            return null;
        } else {
            while (node.next != null) {

                if (node.key == key) {
                    V oldValue = node.value;
                    node.value = value;
                    return oldValue;
                }
                node = node.next;
            }

            node.next = new Node<>(key, value, null);
        }
        return null;
    }


    static int hash(long key) {
        return (int) (key % 32);
    }

}
