package lab8;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by hideyoshitakahashi on 3/9/17.
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {

        private K key;
        private V value;

        private Node left;
        private Node right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public V getV() {
            return this.value;
        }

        public K getK() {
            return this.key;
        }

    }

    private Node root;
    private int size;

    public BSTMap() {
        this.clear();
    }

    /** Removes all of the mappings from this map. */
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        Node pointer = this.root;

        while (pointer != null) {
            int diff = key.compareTo(pointer.getK());

            if (diff < 0) {
                pointer = pointer.left;
            } else if (diff > 0) {
                pointer = pointer.right;
            } else {
                return true;
            }
        }

        return false;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */

    public V get(K key) {
        return getHelper(key, this.root);
    }

    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }

        int diff = key.compareTo(p.getK());
        if (diff < 0) {
            return getHelper(key, p.left);
        } else if (diff > 0) {
            return getHelper(key, p.right);
        } else {
            return p.getV();
        }

    }

    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        root = putHelper(key, value, this.root);
    }

    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size++;
            return new Node(key, value);
        }
        int diff = key.compareTo(p.getK());
        if (diff < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (diff > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            p.value = value;
        }
        return p;
    }

   /* public void printInOrder() {
        if (root == null) {
            return;
        }
        printInOrder()
    }

*/

    public Set<K> keySet() {
        throw new UnsupportedOperationException("KeySet");
    }
    public Iterator iterator() {
        throw new UnsupportedOperationException("iterator");
    }
    public V remove(K key) {
        throw new UnsupportedOperationException("remove");
    }
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("remove");
    }

    private Node removeHelper(Node p, K key) {
        if (p == null) {
            return null;
        }
        int diff = key.compareTo(p.key);
        if (diff > 0) {
            p.right = removeHelper(p.right, key);
        } else if (diff < 0) {
            p.left = removeHelper(p.left, key);
        } else {
            return deleteNode(p);
        }

        return p;
    }

    private Node deleteNode(Node p) {
        this.size--;

        if (p.left == null && p.right == null) {
            return null; // p を受け取って、何も返さない、結果としてrecursionを登っていった先でnullが入って終わり
        } else if (p.left == null) {
            return p.right;
        } else if (p.right == null) {
            return p.left;
        } else {
            Node t = p;
            //t/ =
        }
    }




}

