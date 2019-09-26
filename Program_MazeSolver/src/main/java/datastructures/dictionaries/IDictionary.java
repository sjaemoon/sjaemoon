package datastructures.dictionaries;

import java.util.Iterator;

/**
 * Represents a data structure that contains a bunch of key-value mappings. Each key must be unique.
 */
public interface IDictionary<K, V> extends Iterable<KVPair<K, V>> {
    /**
     * Returns the value corresponding to the given key.
     *
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    V get(K key);

    /**
     * Returns the value corresponding to the given key, if the key exists in the map.
     *
     * If the key does *not* contain the given key, returns the default value.
     *
     * Note: This method does not modify the map in any way. The interface also
     *       provides a default implementation, but you may optionally override
     *       it with a more efficient version.
     */
    default V getOrDefault(K key, V defaultValue) {
        if (this.containsKey(key)) {
            return this.get(key);
        } else {
            return defaultValue;
        }
    }

    /**
     * Adds the key-value pair to the dictionary. If the key already exists in the dictionary,
     * replaces its value with the given one and returns the old value. Otherwise, returns `null`.
     *
     * Note that `null` is a valid value in the dictionary, so a `null` return value does not
     * necessarily imply that the key was not contained in the dictionary.
     */
    V put(K key, V value);

    /**
     * Removes the key-value pair corresponding to the given key from the dictionary if it was
     * contained in the dictionary, and returns the value of the deleted pair. If the key was not
     * contained in the dictionary, returns `null`.
     *
     * Note that `null` is a valid value in the dictionary, so a `null` return value does not
     * necessarily imply that the key was not contained in the dictionary.
     */
    V remove(K key);

    /**
     * Returns `true` if the dictionary contains the given key and `false` otherwise.
     */
    boolean containsKey(K key);

    /**
     * Returns the number of key-value pairs stored in this dictionary.
     */
    int size();

    /**
     * Returns `true` if this dictionary is empty and `false` otherwise.
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Returns an iterator that, when used, will yield all key-value pairs
     * contained within this dict.
     */
    Iterator<KVPair<K, V>> iterator();

    static <K, V> String toString(IDictionary<K, V> dictionary) {
        StringBuilder out = new StringBuilder();
        out.append("{");
        String prefix = "";
        for (KVPair<K, V> pair : dictionary) {
            out.append(prefix);
            out.append(pair);
            prefix = ", ";
        }
        out.append("}");
        return out.toString();
    }
}
