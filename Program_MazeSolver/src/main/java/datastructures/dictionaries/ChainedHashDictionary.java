package datastructures.dictionaries;

// import misc.exceptions.NotYetImplementedException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You'll need to define reasonable default values for each of the following three fields
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 2;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 10;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 3;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.

    Note: The field below intentionally omits the "private" keyword. By leaving off a specific
    access modifier like "public" or "private" it becomes package-private, which means anything in
    the same package can access it. Since our tests are in the same package, they will be able
    to test this property directly.
     */
    IDictionary<K, V>[] chains;
    int size; //number of key-value pairs
    double resizingLoadFactorThreshold; //if the ratio of items to buckets exceeds this, you should resize
    int chainCount; //number of chains/buckets //dont need 'initial'
    int chainCapacity; //initial capacity of each ArrayDictionary inner chain

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    public ChainedHashDictionary(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        this.resizingLoadFactorThreshold = resizingLoadFactorThreshold;
        this.chainCount = initialChainCount;
        this.chainCapacity = chainInitialCapacity;
        this.size = 0;
        this.chains = this.makeArrayOfChains(initialChainCount);
        for (int i = 0; i < this.chains.length; i++) {
            this.chains[i] = new ArrayDictionary<>(this.chainCapacity);
        }
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * `IDictionary<K, V>` objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int arraySize) {
        /*
        Note: You do not need to modify this method. See `ArrayDictionary`'s `makeArrayOfPairs`
        method for more background on why we need this method.
        */
        return (IDictionary<K, V>[]) new IDictionary[arraySize];
    }

    private int getIndex(K key) {
        int i = 0;
        if (key != null) {
            i = key.hashCode() % this.chains.length;
            if (i < 0) {
                i *= -1;
            }
        }
        return i;
    }

    //return true if the chains structure is resized
    private boolean resizeChains() {
        if ((double) this.size / this.chains.length >= resizingLoadFactorThreshold) {
            this.chainCount *= 2;
            IDictionary<K, V>[] oldChains = this.chains;
            this.chains = this.makeArrayOfChains(this.chainCount);
            for (int i = 0; i < this.chains.length; i++) {
                this.chains[i] = new ArrayDictionary<>(this.chainCapacity);
            }
            for (int i = 0; i < oldChains.length; i++) {
                Iterator<KVPair<K, V>> iterator = oldChains[i].iterator();
                while (iterator.hasNext()) {
                    KVPair<K, V> temp = iterator.next();
                    this.put(temp.getKey(), temp.getValue());
                    this.size--;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public V get(K key) {
        int i = this.getIndex(key);
        return this.chains[i].get(key);
    }

    @Override
    public V put(K key, V value) {

        int i = this.getIndex(key);
        if (!this.chains[i].containsKey(key)) { // if new key, increment size
            size++;
        }
        if (resizeChains()) {
            i = this.getIndex(key); // get updated index
        }
        return this.chains[i].put(key, value);
    }

    @Override
    public V remove(K key) {
        int i = this.getIndex(key);
        if (!this.chains[i].containsKey(key)) {
            return null;
        }
        this.size--;
        V toRemove = this.chains[i].remove(key);
        return toRemove;
    }

    @Override
    public boolean containsKey(K key) {
        int i = this.getIndex(key);
        return chains[i].containsKey(key);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    @Override
    public String toString() {
        // return super.toString();

        /*
        After you've implemented the iterator, comment out the line above and uncomment the line
        below to get a better string representation for objects in assertion errors and in the
        debugger.
        */

        return IDictionary.toString(this);
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private Iterator<KVPair<K, V>> iterator;
        private int chainsIndex;
        // You may add more fields and constructor parameters

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.chainsIndex = 0;
            this.iterator = this.chains[0].iterator();
        }

        @Override
        public boolean hasNext() {
            if (this.iterator.hasNext()) {
                return true;
            } else {
                while (!this.iterator.hasNext()) {
                    this.chainsIndex++;
                    if (chainsIndex >= this.chains.length) {
                        return false;
                    }
                    this.iterator = this.chains[this.chainsIndex].iterator();
                }
                return true;
            }
        }

        @Override
        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.iterator.next();
        }
    }
}
