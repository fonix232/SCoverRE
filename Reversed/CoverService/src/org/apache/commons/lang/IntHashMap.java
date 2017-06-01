package org.apache.commons.lang;

class IntHashMap {
    private transient int count;
    private float loadFactor;
    private transient Entry[] table;
    private int threshold;

    private static class Entry {
        int hash;
        int key;
        Entry next;
        Object value;

        protected Entry(int hash, int key, Object value, Entry next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public IntHashMap() {
        this(20, 0.75f);
    }

    public IntHashMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public IntHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException(new StringBuffer().append("Illegal Capacity: ").append(initialCapacity).toString());
        } else if (loadFactor <= 0.0f) {
            throw new IllegalArgumentException(new StringBuffer().append("Illegal Load: ").append(loadFactor).toString());
        } else {
            if (initialCapacity == 0) {
                initialCapacity = 1;
            }
            this.loadFactor = loadFactor;
            this.table = new Entry[initialCapacity];
            this.threshold = (int) (((float) initialCapacity) * loadFactor);
        }
    }

    public int size() {
        return this.count;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public boolean contains(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Entry[] tab = this.table;
        int i = tab.length;
        while (true) {
            int i2 = i - 1;
            if (i <= 0) {
                return false;
            }
            for (Entry e = tab[i2]; e != null; e = e.next) {
                if (e.value.equals(value)) {
                    return true;
                }
            }
            i = i2;
        }
    }

    public boolean containsValue(Object value) {
        return contains(value);
    }

    public boolean containsKey(int key) {
        Entry[] tab = this.table;
        int hash = key;
        for (Entry e = tab[(Integer.MAX_VALUE & hash) % tab.length]; e != null; e = e.next) {
            if (e.hash == hash) {
                return true;
            }
        }
        return false;
    }

    public Object get(int key) {
        Entry[] tab = this.table;
        int hash = key;
        for (Entry e = tab[(Integer.MAX_VALUE & hash) % tab.length]; e != null; e = e.next) {
            if (e.hash == hash) {
                return e.value;
            }
        }
        return null;
    }

    protected void rehash() {
        int oldCapacity = this.table.length;
        Entry[] oldMap = this.table;
        int newCapacity = (oldCapacity * 2) + 1;
        Entry[] newMap = new Entry[newCapacity];
        this.threshold = (int) (((float) newCapacity) * this.loadFactor);
        this.table = newMap;
        int i = oldCapacity;
        while (true) {
            int i2 = i - 1;
            if (i > 0) {
                Entry old = oldMap[i2];
                while (old != null) {
                    Entry e = old;
                    old = old.next;
                    int index = (e.hash & Integer.MAX_VALUE) % newCapacity;
                    e.next = newMap[index];
                    newMap[index] = e;
                }
                i = i2;
            } else {
                return;
            }
        }
    }

    public Object put(int key, Object value) {
        Entry[] tab = this.table;
        int hash = key;
        int index = (hash & Integer.MAX_VALUE) % tab.length;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash) {
                Object old = e.value;
                e.value = value;
                return old;
            }
        }
        if (this.count >= this.threshold) {
            rehash();
            tab = this.table;
            index = (hash & Integer.MAX_VALUE) % tab.length;
        }
        tab[index] = new Entry(hash, key, value, tab[index]);
        this.count++;
        return null;
    }

    public Object remove(int key) {
        Entry[] tab = this.table;
        int hash = key;
        int index = (Integer.MAX_VALUE & hash) % tab.length;
        Entry prev = null;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash) {
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    tab[index] = e.next;
                }
                this.count--;
                Object oldValue = e.value;
                e.value = null;
                return oldValue;
            }
            prev = e;
        }
        return null;
    }

    public synchronized void clear() {
        Entry[] tab = this.table;
        int index = tab.length;
        while (true) {
            index--;
            if (index < 0) {
                this.count = 0;
            } else {
                tab[index] = null;
            }
        }
    }
}
