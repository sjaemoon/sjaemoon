package datastructures.sets;

import datastructures.dictionaries.ChainedHashDictionary;
import datastructures.dictionaries.IDictionary;
import datastructures.dictionaries.KVPair;
//import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;

/**
 * @see ISet for more details on what each method is supposed to do.
 */
public class ChainedHashSet<T> implements ISet<T> {
    // This should be the only field you need
    private IDictionary<T, Boolean> map;

    public ChainedHashSet() {
        // No need to change this method
        this.map = new ChainedHashDictionary<>();
    }

    // TODO: Think about null value items
    @Override
    public boolean add(T item) {
        if (!this.contains(item)) {
            this.map.put(item, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(T item) {
        if (this.contains(item)) {
            this.map.remove(item);
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(T item) {

        return this.map.containsKey(item);
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public Iterator<T> iterator() {
        return new SetIterator<>(this.map.iterator());
    }

    @Override
    public String toString() {
        // return super.toString();

        /*
        After you've implemented the iterator, comment out the line above and uncomment the line
        below to get a better string representation for objects in assertion errors and in the
        debugger.
        */

        return ISet.toString(this);
    }

    private static class SetIterator<T> implements Iterator<T> {
        // This should be the only field you need
        private Iterator<KVPair<T, Boolean>> iter;

        public SetIterator(Iterator<KVPair<T, Boolean>> iter) {
            // No need to change this method.
            this.iter = iter;
        }

        @Override
        public boolean hasNext() {
            return this.iter.hasNext();
        }

        @Override
        public T next() {
            return this.iter.next().getKey();
        }
    }
}
