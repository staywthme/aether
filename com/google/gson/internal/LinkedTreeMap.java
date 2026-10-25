/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

public final class LinkedTreeMap<K, V>
extends AbstractMap<K, V>
implements Serializable {
    private static final Comparator<Comparable> NATURAL_ORDER = new Comparator<Comparable>(){

        @Override
        public int compare(Comparable comparable, Comparable comparable2) {
            return comparable.compareTo(comparable2);
        }
    };
    private final Comparator<? super K> comparator;
    private final boolean allowNullValues;
    Node<K, V> root;
    int size = 0;
    int modCount = 0;
    final Node<K, V> header;
    private EntrySet entrySet;
    private KeySet keySet;

    public LinkedTreeMap() {
        this(NATURAL_ORDER, true);
    }

    public LinkedTreeMap(boolean bl) {
        this(NATURAL_ORDER, bl);
    }

    public LinkedTreeMap(Comparator<? super K> comparator, boolean bl) {
        this.comparator = comparator != null ? comparator : NATURAL_ORDER;
        this.allowNullValues = bl;
        this.header = new Node(bl);
    }

    @Override
    public final int size() {
        return this.size;
    }

    @Override
    public final V get(Object node) {
        if ((node = this.findByObject(node)) != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public final boolean containsKey(Object object) {
        return this.findByObject(object) != null;
    }

    @Override
    public final V put(K object, V v2) {
        if (object == null) {
            throw new NullPointerException("key == null");
        }
        if (v2 == null && !this.allowNullValues) {
            throw new NullPointerException("value == null");
        }
        object = this.find(object, true);
        Object v3 = ((Node)object).value;
        ((Node)object).value = v2;
        return v3;
    }

    @Override
    public final void clear() {
        this.root = null;
        this.size = 0;
        ++this.modCount;
        Node<K, V> node = this.header;
        node.prev = node;
        this.header.next = node.prev;
    }

    @Override
    public final V remove(Object node) {
        if ((node = this.removeInternalByKey(node)) != null) {
            return node.value;
        }
        return null;
    }

    final Node<K, V> find(K k2, boolean bl) {
        Node<K, V> node;
        Object object;
        Comparator<K> comparator = this.comparator;
        Node<K, V> node2 = this.root;
        int n2 = 0;
        if (node2 != null) {
            object = comparator == NATURAL_ORDER ? (Comparable)k2 : null;
            while (true) {
                if ((n2 = object != null ? object.compareTo(node2.key) : comparator.compare(k2, node2.key)) == 0) {
                    return node2;
                }
                node = n2 < 0 ? node2.left : node2.right;
                if (node == null) break;
                node2 = node;
            }
        }
        if (!bl) {
            return null;
        }
        object = this.header;
        if (node2 == null) {
            if (comparator == NATURAL_ORDER && !(k2 instanceof Comparable)) {
                throw new ClassCastException(k2.getClass().getName() + " is not Comparable");
            }
            Object object2 = object;
            node = new Node<K, V>(this.allowNullValues, node2, k2, object2, ((Node)object2).prev);
            this.root = node;
        } else {
            Object object3 = object;
            node = new Node<K, V>(this.allowNullValues, node2, k2, object3, ((Node)object3).prev);
            if (n2 < 0) {
                node2.left = node;
            } else {
                node2.right = node;
            }
            this.rebalance(node2, true);
        }
        ++this.size;
        ++this.modCount;
        return node;
    }

    final Node<K, V> findByObject(Object object) {
        try {
            if (object != null) {
                return this.find(object, false);
            }
            return null;
        }
        catch (ClassCastException classCastException) {
            return null;
        }
    }

    final Node<K, V> findByEntry(Map.Entry<?, ?> entry) {
        Node<K, V> node = this.findByObject(entry.getKey());
        boolean bl = node != null && this.equal(node.value, entry.getValue());
        if (bl) {
            return node;
        }
        return null;
    }

    private boolean equal(Object object, Object object2) {
        return Objects.equals(object, object2);
    }

    final void removeInternal(Node<K, V> node, boolean bl) {
        if (bl) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node.parent;
        if (node2 != null && node3 != null) {
            node4 = node2.height > node3.height ? node2.last() : node3.first();
            this.removeInternal(node4, false);
            int n2 = 0;
            node2 = node.left;
            if (node2 != null) {
                n2 = node2.height;
                node4.left = node2;
                node2.parent = node4;
                node.left = null;
            }
            int n3 = 0;
            node3 = node.right;
            if (node3 != null) {
                n3 = node3.height;
                node4.right = node3;
                node3.parent = node4;
                node.right = null;
            }
            node4.height = Math.max(n2, n3) + 1;
            this.replaceInParent(node, node4);
            return;
        }
        if (node2 != null) {
            this.replaceInParent(node, node2);
            node.left = null;
        } else if (node3 != null) {
            this.replaceInParent(node, node3);
            node.right = null;
        } else {
            this.replaceInParent(node, null);
        }
        this.rebalance(node4, false);
        --this.size;
        ++this.modCount;
    }

    final Node<K, V> removeInternalByKey(Object node) {
        if ((node = this.findByObject(node)) != null) {
            this.removeInternal(node, true);
        }
        return node;
    }

    private void replaceInParent(Node<K, V> node, Node<K, V> node2) {
        Node node3 = node.parent;
        node.parent = null;
        if (node2 != null) {
            node2.parent = node3;
        }
        if (node3 != null) {
            if (node3.left == node) {
                node3.left = node2;
                return;
            }
            assert (node3.right == node);
            node3.right = node2;
            return;
        }
        this.root = node2;
    }

    private void rebalance(Node<K, V> node, boolean bl) {
        while (node != null) {
            Node node2;
            int n2;
            Node node3 = node.left;
            int n3 = node3 != null ? node3.height : 0;
            int n4 = n3 - (n2 = (node2 = node.right) != null ? node2.height : 0);
            if (n4 == -2) {
                Node node4 = node2.left;
                Node node5 = node2.right;
                n2 = node5 != null ? node5.height : 0;
                n3 = node4 != null ? node4.height : 0;
                if ((n3 = n3 - n2) == -1 || n3 == 0 && !bl) {
                    this.rotateLeft(node);
                } else {
                    assert (n3 == 1);
                    this.rotateRight(node2);
                    this.rotateLeft(node);
                }
                if (bl) break;
            } else if (n4 == 2) {
                Node node6 = node3.left;
                Node node7 = node3.right;
                n2 = node7 != null ? node7.height : 0;
                n3 = node6 != null ? node6.height : 0;
                if ((n3 = n3 - n2) == 1 || n3 == 0 && !bl) {
                    this.rotateRight(node);
                } else {
                    assert (n3 == -1);
                    this.rotateLeft(node3);
                    this.rotateRight(node);
                }
                if (bl) break;
            } else if (n4 == 0) {
                node.height = n3 + 1;
                if (bl) {
                    return;
                }
            } else {
                assert (n4 == -1 || n4 == 1);
                node.height = Math.max(n3, n2) + 1;
                if (!bl) break;
            }
            node = node.parent;
        }
    }

    private void rotateLeft(Node<K, V> node) {
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node3.left;
        Node node5 = node3.right;
        node.right = node4;
        if (node4 != null) {
            node4.parent = node;
        }
        this.replaceInParent(node, node3);
        node3.left = node;
        node.parent = node3;
        node.height = Math.max(node2 != null ? node2.height : 0, node4 != null ? node4.height : 0) + 1;
        node3.height = Math.max(node.height, node5 != null ? node5.height : 0) + 1;
    }

    private void rotateRight(Node<K, V> node) {
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node2.left;
        Node node5 = node2.right;
        node.left = node5;
        if (node5 != null) {
            node5.parent = node;
        }
        this.replaceInParent(node, node2);
        node2.right = node;
        node.parent = node2;
        node.height = Math.max(node3 != null ? node3.height : 0, node5 != null ? node5.height : 0) + 1;
        node2.height = Math.max(node.height, node4 != null ? node4.height : 0) + 1;
    }

    @Override
    public final Set<Map.Entry<K, V>> entrySet() {
        EntrySet entrySet = this.entrySet;
        if (entrySet != null) {
            return entrySet;
        }
        this.entrySet = new EntrySet();
        return this.entrySet;
    }

    @Override
    public final Set<K> keySet() {
        KeySet keySet = this.keySet;
        if (keySet != null) {
            return keySet;
        }
        this.keySet = new KeySet();
        return this.keySet;
    }

    private Object writeReplace() throws ObjectStreamException {
        return new LinkedHashMap(this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException {
        throw new InvalidObjectException("Deserialization is unsupported");
    }

    final class KeySet
    extends AbstractSet<K> {
        KeySet() {
        }

        @Override
        public final int size() {
            return LinkedTreeMap.this.size;
        }

        @Override
        public final Iterator<K> iterator() {
            return new LinkedTreeMapIterator(){

                @Override
                public K next() {
                    return this.nextNode().key;
                }
            };
        }

        @Override
        public final boolean contains(Object object) {
            return LinkedTreeMap.this.containsKey(object);
        }

        @Override
        public final boolean remove(Object object) {
            return LinkedTreeMap.this.removeInternalByKey(object) != null;
        }

        @Override
        public final void clear() {
            LinkedTreeMap.this.clear();
        }
    }

    class EntrySet
    extends AbstractSet<Map.Entry<K, V>> {
        EntrySet() {
        }

        @Override
        public int size() {
            return LinkedTreeMap.this.size;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new LinkedTreeMapIterator(){

                @Override
                public Map.Entry<K, V> next() {
                    return this.nextNode();
                }
            };
        }

        @Override
        public boolean contains(Object object) {
            return object instanceof Map.Entry && LinkedTreeMap.this.findByEntry((Map.Entry)object) != null;
        }

        @Override
        public boolean remove(Object node) {
            if (!(node instanceof Map.Entry)) {
                return false;
            }
            if ((node = LinkedTreeMap.this.findByEntry(node)) == null) {
                return false;
            }
            LinkedTreeMap.this.removeInternal(node, true);
            return true;
        }

        @Override
        public void clear() {
            LinkedTreeMap.this.clear();
        }
    }

    abstract class LinkedTreeMapIterator<T>
    implements Iterator<T> {
        Node<K, V> next;
        Node<K, V> lastReturned;
        int expectedModCount;

        LinkedTreeMapIterator() {
            this.next = LinkedTreeMap.this.header.next;
            this.lastReturned = null;
            this.expectedModCount = LinkedTreeMap.this.modCount;
        }

        @Override
        public final boolean hasNext() {
            return this.next != LinkedTreeMap.this.header;
        }

        final Node<K, V> nextNode() {
            Node node = this.next;
            if (node == LinkedTreeMap.this.header) {
                throw new NoSuchElementException();
            }
            if (LinkedTreeMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            this.next = node.next;
            this.lastReturned = node;
            return this.lastReturned;
        }

        @Override
        public final void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            }
            LinkedTreeMap.this.removeInternal(this.lastReturned, true);
            this.lastReturned = null;
            this.expectedModCount = LinkedTreeMap.this.modCount;
        }
    }

    static final class Node<K, V>
    implements Map.Entry<K, V> {
        Node<K, V> parent;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> next;
        Node<K, V> prev;
        final K key;
        final boolean allowNullValue;
        V value;
        int height;

        Node(boolean bl) {
            Node node;
            this.key = null;
            this.allowNullValue = bl;
            node = node.next = (node.prev = this);
        }

        Node(boolean bl, Node<K, V> node, K k2, Node<K, V> node2, Node<K, V> node3) {
            this.parent = node;
            this.key = k2;
            this.allowNullValue = bl;
            this.height = 1;
            this.next = node2;
            this.prev = node3;
            node3.next = this;
            node2.prev = this;
        }

        @Override
        public final K getKey() {
            return this.key;
        }

        @Override
        public final V getValue() {
            return this.value;
        }

        @Override
        public final V setValue(V v2) {
            if (v2 == null && !this.allowNullValue) {
                throw new NullPointerException("value == null");
            }
            V v3 = this.value;
            this.value = v2;
            return v3;
        }

        @Override
        public final boolean equals(Object object) {
            if (object instanceof Map.Entry) {
                object = (Map.Entry)object;
                return (this.key == null ? object.getKey() == null : this.key.equals(object.getKey())) && (this.value == null ? object.getValue() == null : this.value.equals(object.getValue()));
            }
            return false;
        }

        @Override
        public final int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public final String toString() {
            return this.key + "=" + this.value;
        }

        public final Node<K, V> first() {
            Node<K, V> node = this;
            Node<K, V> node2 = node.left;
            while (node2 != null) {
                node = node2;
                node2 = node.left;
            }
            return node;
        }

        public final Node<K, V> last() {
            Node<K, V> node = this;
            Node<K, V> node2 = node.right;
            while (node2 != null) {
                node = node2;
                node2 = node.right;
            }
            return node;
        }
    }
}

