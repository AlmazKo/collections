package com.almazko.collection;

import java.util.StringJoiner;
import java.util.function.LongConsumer;

/**
 * Created by almaz on 30.06.16.
 */
public class LongMap<V> {

    static int HSIZE = 32;

    @FunctionalInterface
    public interface LongBiConsumer<V> {
        void accept(long t, V value);
    }

    public void forEach(LongBiConsumer<V> action) {
        if (buckets == null) return;

        for (Bucket<V> b : buckets) {
            if (b != null) b.forEach(action);

        }
    }

    private static final class Bucket<V> {
        Node<V> first;
        Node<V> last;
        int size;

        Bucket(Node<V> node) {
            first = last = node;
            size = 1;
        }


        void add(long key, V value) {
            if (key < first.key) {
                first = new Node<>(key, value, first);
                size++;
            } else if (key > last.key) {
                last = last.next = new Node<>(key, value, null);
                size++;
            } else {
                bInsert(first, size / 2, key, value);
            }
        }

        boolean contains(long key) {
            return get(key) != null;
        }

        V get(long key) {
            if (first.key > key || key > last.key) return null;
            if (first.key == key) return first.value;
            if (last.key == key) return last.value;

            return bSearch(first, size / 2, key);
        }

        private void bInsert(Node<V> nd, int pos, long key, V value) {
            Node<V> node = get(nd, pos);


            if (node.key == key) {
                node.value = value;
            } else if (key > node.key) {
                if (pos == 1) {
                    node.next = new Node<>(key, value, node.next);
                    size++;
                    return;
                }
                bInsert(node, pos / 2, key, value);
            } else {
                if (pos == 1){
                    nd.next = new Node<>(key, value, nd.next);
                    size++;
                    return;
                }
                bInsert(first, pos / 2, key, value);
            }
        }


        private V bSearch(Node<V> nd, int pos, long key) {
            Node<V> node = get(nd, pos);

            if (node.key == key) {
                return node.value;
            } else if (key > node.key) {
                if (pos <= 1) return null;

                return bSearch(node, pos / 2, key);
            } else {
                return bSearch(first, pos / 2, key);
            }
        }

        private Node<V> get(Node<V> result, int pos) {

            for (int i = 0; i != pos; i++) {
                result = result.next;

                if (result == null) {
                    System.out.print(1);
                }
            }

            return result;
        }

        public void forEach(LongBiConsumer<V> action) {
            Node<V> node = first;
            while (node != null) {
                action.accept(node.key, node.value);
                node = node.next;
            }
        }

        public void forEachKeys(LongConsumer action) {
            Node<V> node = first;
            while (node != null) {
                action.accept(node.key);
                node = node.next;
            }
        }

        @Override
        public String toString() {
            StringJoiner sj = new StringJoiner(", ");
            forEachKeys(l -> sj.add(l + ""));

            return sj.toString();
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

        @Override
        public String toString() {
            return key + ": " + value;
        }
    }

    private Bucket<V>[] buckets;
    private int size;

    public LongMap() {
    }

    boolean containsKey(long key) {
        Bucket<V> b = getFirstNode(key);
        if (b == null) return false;
        return b.contains(key);
    }

    V get(long key) {
        Bucket<V> b = getFirstNode(key);
        if (b == null) return null;
        return b.get(key);
    }

    private Bucket<V> getFirstNode(long key) {
        if (size == 0) return null;
        return buckets[hash(key)];
    }

    V put(long key, V value) {

        final int hash = hash(key);

        if (buckets == null) buckets = new Bucket[HSIZE];

        Bucket<V> bucket = buckets[hash];

        if (bucket == null) {
            buckets[hash] = new Bucket<V>(new Node<>(key, value, null));
            size++;
            return null;
        } else {
            bucket.add(key, value);
            size++;
        }
        return null;
    }

    public int size() {
        return size;
    }

    static int hash(long key) {
        return (int) (Math.abs(key) % HSIZE);
    }

}
