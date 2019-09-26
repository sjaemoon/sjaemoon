package datastructures.lists;

import datastructures.EmptyContainerException;

import java.util.Iterator;

/**
 * Represents a data structure that contains an ordered and indexable sequence of elements.
 */
public interface IList<T> extends Iterable<T> {
    /**
     * Adds the given item to the *end* of this IList.
     */
    void add(T item);

    /**
     * Removes and returns the item from the *end* of this IList.
     *
     * @throws EmptyContainerException if the container is empty and there is no element to remove.
     */
    T remove();

    /**
     * Returns the item located at the given index.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    T get(int index);

    /**
     * Overwrites the item located at the given index with the new item. Returns the item replaced.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    T set(int index, T item);

    /**
     * Inserts the given item at the given index. If there already exists an element
     * at that index, shift over that element and any subsequent elements one index
     * higher.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size() + 1
     */
    void insert(int index, T item);

    /**
     * Deletes the item at the given index. If there are any elements located at a higher
     * index, shift them all down by one. Returns the item deleted.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    T delete(int index);

    /**
     * Returns the index corresponding to the first occurrence of the given item
     * in the list.
     *
     * If the item does not exist in the list, return -1.
     */
    int indexOf(T item);

    /**
     * Returns the number of elements in the container.
     */
    int size();

    /**
     * Returns `true` if this container contains no elements, and `false` otherwise.
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Returns `true` if this container contains the given element, and `false` otherwise.
     */
    boolean contains(T other);

    /**
     * Returns an iterator over the contents of this list.
     */
    Iterator<T> iterator();

    static <T> String toString(IList<T> list) {
        StringBuilder out = new StringBuilder("[");
        String prefix = "";
        for (T item : list) {
            out.append(prefix);
            out.append(item);
            prefix = ", ";
        }
        out.append("]");
        return out.toString();
    }
}
