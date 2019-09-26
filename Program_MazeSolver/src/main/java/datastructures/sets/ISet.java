package datastructures.sets;

import java.util.Iterator;

/**
 * Represents a data structure that contains a unique collection of items.
 */
public interface ISet<T> extends Iterable<T> {
    /**
     * If the given item is not in the set, adds it to the set and returns `true`. Otherwise,
     * returns `false`.
     */
    boolean add(T item);

    /**
     * If the given item is in the set, removes it from the set and returns `true`. Otherwise,
     * returns `false`.
     */
    boolean remove(T item);

    /**
     * Returns `true` if the set contains this item and `false` otherwise.
     */
    boolean contains(T item);

    /**
     * Returns the number of items contained within this set.
     */
    int size();

    /**
     * Returns `true` if this set contains no items and `false` otherwise.
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Returns an iterator over the contents of this set.
     */
    Iterator<T> iterator();

    static <T> String toString(ISet<T> set) {
        StringBuilder out = new StringBuilder("[");
        String prefix = "";
        for (T item : set) {
            out.append(prefix);
            out.append(item);
            prefix = ", ";
        }
        out.append("]");
        return out.toString();
    }
}
