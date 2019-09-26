package datastructures.dictionaries;

// import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.

    Note: The field below intentionally omits the "private" keyword. By leaving off a specific
    access modifier like "public" or "private" it becomes package-private, which means anything in
    the same package can access it. Since our tests are in the same package, they will be able
    to test this property directly.
     */
    Pair<K, V>[] pairs;
    int size;
    int arrayCap;

    // You may add extra fields or helper methods though!

    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    // feel free to reuse what value you were using originally here

    public ArrayDictionary() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayDictionary(int initialCapacity) {
        this.arrayCap = initialCapacity;
        pairs = makeArrayOfPairs(initialCapacity);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain `Pair<K, V>`
     * objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
         arrays and generics interact. Do not modify this method in any way.
        */
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    private int getIndexOfKey(K key) {
        if (key == null) { // null case
            for (int i = 0; i < this.size; i++) {
                if (this.pairs[i].key == null) {
                    return i;
                }
            }
        } else { // general case
            for (int i = 0; i < this.size; i++) {
                if (this.pairs[i].key != null && this.pairs[i].key.equals(key)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public V get(K key) {
        int index = this.getIndexOfKey(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        }
        return this.pairs[index].value;
    }

    @Override
    public V put(K key, V value) {
        int index = this.getIndexOfKey(key);
        if (index == -1) { // key not found case
            if (size + 1 > this.arrayCap) { // array is full
                Pair<K, V>[] biggerPairs = this.makeArrayOfPairs(this.arrayCap * 2);
                for (int i = 0; i < this.arrayCap; i++) {
                    biggerPairs[i] = this.pairs[i];
                }
                this.pairs = biggerPairs;
                this.arrayCap *= 2;
            }
            Pair<K, V> newPair = new Pair<K, V>(key, value);
            this.pairs[this.size] = newPair;
            this.size++;
            return null;
        } else { // general case
            V temp = this.pairs[index].value;
            this.pairs[index].value = value;
            return temp;
        }
    }

    @Override
    public V remove(K key) {
        int index = this.getIndexOfKey(key);
        if (index == -1) {
            return null;
        } else {
            V temp = this.pairs[index].value;
            this.pairs[index] = this.pairs[this.size - 1]; // no deletion
            size--;
            return temp;
        }
    }

    @Override
    public boolean containsKey(K key) {
        return this.getIndexOfKey(key) >= 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<>(this.pairs, this.size);
    }

    @Override
    public String toString() {
        return super.toString();

        /*
        After you've implemented the iterator, comment out the line above and uncomment the line
        below to get a better string representation for objects in assertion errors and in the
        debugger.
        */

        // return IDictionary.toString(this);
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("%s=%s", this.key, this.value);
        }
    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        // You'll need to add some fields
        Pair<K, V>[] dict;
        int index;
        int size;

        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.dict = pairs;
            this.index = 0;
            this.size = size;
        }

        @Override
        public boolean hasNext() {
            return index < this.size;
        }

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            index++;
            return new KVPair<K, V>(
                    this.dict[this.index - 1].key,
                    this.dict[this.index - 1].value);
        }
    }
}
