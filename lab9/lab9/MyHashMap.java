package lab9;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by hideyoshitakahashi on 3/16/17.
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;
        Entry(K key, V value) {
            this(key, value, null);
        }
        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public V getValue() {
            return value;
            //V[] values = new V[]();
        }

        public K getKey() {
            return key;
        }

        public void add(Entry<K, V> ent) {
            Entry<K, V> pointer = this;
            while (pointer.next != null) {
                pointer = pointer.next;
            }
            pointer.next = ent;
        }
    }

    private int size;
    private int capacity;
    private double loadFactor; // size/capacity
    private ArrayList<Entry<K, V>> bins;
    private Set<K> keyset;

    public MyHashMap() {
        this(8, 3);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 3);
    }

    public MyHashMap(int initialCap, double loadF) {
        if (initialCap < 1 || loadF <= 0) {
            throw new IllegalArgumentException();
        }
        bins = new ArrayList<Entry<K, V>>(initialCap);
        for (int i = 0; i < initialCap; i++) {
            bins.add(new Entry<K, V>(null, null));
        }
        size = 0; // # of item inserted
        capacity = initialCap;
        loadFactor = loadF;
        keyset = new HashSet<K>();

    }

    /** Removes all of the mappings from this map. */
    public void clear() {
        bins = new ArrayList<Entry<K, V>>(capacity);
        for (int i = 0; i < capacity; i++) {
            bins.add(new Entry<K, V>(null, null));
        }
        keyset = new HashSet<K>();
        size = 0;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        return keyset.contains(key);
    }

    public V get(K key) {
        if (!(keyset.contains(key))) {
            return null;
        }
        int hash = key.hashCode() & 0x7fffffff;
        int index = hash % capacity;
        Entry<K, V> entry = bins.get(index);
        Entry<K, V> pointer = entry.next;
        while (pointer != null) {
            if (pointer.getKey().equals(key)) {
                return pointer.getValue();
            }
            pointer = pointer.next;

        }
        return null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */

    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        if (keyset.contains(key)) {
            if (get(key).equals(value)) {
                return;
            }
            Entry<K, V> newEntry = new Entry<K, V>(key, value);
            int hash = key.hashCode() & 0x7fffffff;
            int index = hash % capacity;
            Entry<K, V> pointer = bins.get(index).next;
            while (!(pointer.key.equals(key))) {
                pointer = pointer.next;
            }
            pointer.value = value;
            return;
        }
        size++;
        Entry<K, V> newEntry = new Entry<K, V>(key, value);
        int hash = key.hashCode() & 0x7fffffff;
        int index = hash % capacity;
        Entry<K, V> pointer = bins.get(index);
        pointer.add(newEntry);
        keyset.add(key);
    }

    /* Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        return keyset;
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        if (!(keyset.contains(key))) {
            return null;
        } else {
            keyset.remove(key);
            int hash = key.hashCode() & 0x7fffffff;
            int index = hash % capacity;
            Entry<K, V> ent = bins.get(index);
            Entry<K, V> prevEnt = ent;
            while (ent.key != key) {
                prevEnt = ent;
                ent = ent.next;
            }
            prevEnt.next = ent.next;
            size--;
            return ent.value;
        }
    }
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("no good");
    }

    public Iterator iterator() {
        //return new KeyIterator<K>();
        return keyset.iterator();
    }

    private void resize() {
        if (!((double) size / (double) capacity > loadFactor)) {
            return;
        }
        int newCapacity = capacity * 2;
        ArrayList<Entry<K, V>> newBins = new ArrayList<>(capacity * 2);
        for (K key : keyset) {
            V val = this.get(key);
            Entry<K, V> ent = new Entry(key, val);
            int hash = key.hashCode() & 0x7fffffff;
            int index = hash % newCapacity;
            newBins.add(index, ent);
        }

    }

/*
    private class KeyIterator<K> implements Iterator<K>{
        public K next(){

        }
        public boolean hasNext(){

        }
    }
    */
    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    /*public V remove(K key, V value){

    }*/

}
