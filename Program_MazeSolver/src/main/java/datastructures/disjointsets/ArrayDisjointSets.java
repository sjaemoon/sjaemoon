package datastructures.disjointsets;

import datastructures.dictionaries.IDictionary;
import datastructures.dictionaries.ChainedHashDictionary;

/**
 * @see IDisjointSets for more details.
 */
public class ArrayDisjointSets<T> implements IDisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    int[] pointers;
    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */
    IDictionary<T, Integer> dict;
    int index; // next available index in the pointer array
    private static final int INITIAL_CAPACITY = 10; // initial pointer array capacity

    public ArrayDisjointSets() {
        this.index = 0;
        this.pointers =  new int[INITIAL_CAPACITY];
        this.dict = new ChainedHashDictionary();
    }

    @Override
    public void makeSet(T item) {
        if (this.dict.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        dict.put(item, index);
        // resize pointers when needed
        if (this.index == this.pointers.length - 1) {
            int newLength = this.pointers.length * 2;
            int[] newPointers = new int[newLength];
            for (int i = 0; i < this.pointers.length; i++) {
                newPointers[i] = pointers[i];
            }
            this.pointers = newPointers;
        }
        this.pointers[this.index] = -1;
        index++;
    }

    @Override
    public int findSet(T item) {
        if (!this.dict.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        return this.findRoot(this.dict.get(item)); // return root
    }

    private int findRoot(int i) {
        int parentIndex = this.pointers[i];
        if (parentIndex < 0) { // if at the root
            return i;
        } else {
            this.pointers[i] = this.findRoot(parentIndex);
        }
        return this.pointers[i]; // return root index
    }

    @Override
    public boolean union(T item1, T item2) {

        if (!this.dict.containsKey(item1) || !this.dict.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int root1= findSet(item1);
        int root2 = findSet(item2);
        int rank1 = this.pointers[root1];
        int rank2 = this.pointers[root2];
        if (rank2 >= rank1) { // rank1 >= rank 2
            if (rank1 == rank2) {
                rank1--; // increment rank by 1
            }
            this.pointers[root2] = root1;
        } else { // rank1 < rank2
            this.pointers[root1] = root2;
        }
        return root1 != root2; // true if the items are in different set
    }
}
