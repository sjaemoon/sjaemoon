package datastructures.priorityqueues;

import datastructures.EmptyContainerException;
// import datastructures.dictionaries.ArrayDictionary;
import datastructures.dictionaries.ChainedHashDictionary;
import datastructures.dictionaries.IDictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeapPriorityQueue<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    /*
    You MUST use this field to store the contents of your heap.
    You may NOT rename this field or change its type: we will be inspecting it in our secret tests.
    */
    T[] heap;
    IDictionary<T, Integer> indices;
    int heapSize;
    // Feel free to add more fields and constants.

    public ArrayHeapPriorityQueue() {
        this.heap = this.makeArrayOfT(1);
        this.heapSize = 0;
        this.indices = new ChainedHashDictionary<>();
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type `T`.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        /*
        This helper method is basically the same one we gave you in `ArrayDictionary` and
        `ChainedHashDictionary`.

        As before, you do not need to understand how this method works, and should not modify it in
         any way.
        */
        return (T[]) (new Comparable[arraySize]);
    }

    @Override
    public T removeMin() {
        if (this.heapSize == 0) { // if no element in queue
            throw new EmptyContainerException();
        }
        T toRemove = this.heap[0];
        this.heap[0] = this.heap[this.heapSize - 1]; // replace with last element
        this.heapSize--; // lazy deletion
        this.percolateDown(0);
        this.indices.remove(toRemove); // update dictionary
        return toRemove;
    }

    @Override
    public T peekMin() {
        if (this.heapSize == 0) { // if no element in queue
            throw new EmptyContainerException();
        }
        return this.heap[0];
    }

    @Override
    public void add(T item) {
        if (item == null) {  // if item is null
            throw new IllegalArgumentException();
        }
        if (this.contains(item)) { // if item is already in queue
            throw new InvalidElementException();
        }
        if (this.heapSize >= this.heap.length) { // if array heap is filled
            T[] largerHeap = this.makeArrayOfT(this.heap.length * 2);
            for (int i = 0; i < this.heapSize; i++) {
                largerHeap[i] = this.heap[i];
            }
            this.heap = largerHeap;
        }
        this.heapSize++;
        this.heap[this.heapSize - 1] = item;
        this.percolateUp(this.heapSize - 1);
    }

    @Override
    public boolean contains(T item) {
        if (item == null) {  // if item is null
            throw new IllegalArgumentException();
        }
        return this.indices.containsKey(item);
    }

    @Override
    public void remove(T item) {
        if (item == null) {  // if item is null
            throw new IllegalArgumentException();
        }
        if (!this.contains(item)) { // if item is not in queue
            throw new InvalidElementException();
        }
        int index = this.indices.get(item);
        if (index == this.heapSize - 1) {
            this.heapSize--;
        } else {
            this.swap(index, this.heapSize - 1);
            this.heapSize--;
            this.percolate(index);
        }
        this.indices.remove(item);
    }

    @Override
    public void replace(T oldItem, T newItem) {
        if (oldItem == null || newItem == null) {
            throw new IllegalArgumentException();
        }
        if (!this.contains(oldItem) || this.contains(newItem)) {
            throw new InvalidElementException();
        }
        int index = this.indices.remove(oldItem);
        this.heap[index] = newItem;
        this.indices.put(newItem, index);
        this.percolate(index);
    }

    @Override
    public int size() {
        return this.heapSize;
    }

    private void percolateUp(int index) {
        int parentIndex = (index - 1) / NUM_CHILDREN;
        // if given child is smaller than its parent
        if (this.leq(this.heap[index], this.heap[parentIndex]) && index > 0) {
            this.swap(index, parentIndex);
            this.percolateUp(parentIndex);
        }
        // finished updating heap, update dictionary
        this.indices.put(this.heap[index], index);
    }

    private void percolateDown(int index) {
        int childIndex = NUM_CHILDREN * index + 1;
        int upperBound = Math.min(childIndex + NUM_CHILDREN - 1, this.heapSize - 1);
        int smallestChildIndex = childIndex;
        boolean needPercolateDown = false;
        // iterate through children and get smallest child as long as children exist
        while (childIndex <= upperBound) {
            // if any of the children is smaller than the parent
            if (!needPercolateDown && this.leq(this.heap[childIndex], this.heap[index])) {
                needPercolateDown = true;
            }
            // update if a smaller child is found
            if (this.leq(this.heap[childIndex], this.heap[smallestChildIndex])) {
                smallestChildIndex = childIndex;
            }
            childIndex++;
        }
        if (needPercolateDown) {
            this.swap(index, smallestChildIndex);
            this.percolate(smallestChildIndex);
        }
        // finished updating heap, update dictionary
        this.indices.put(this.heap[index], index);
    }

    private void percolate(int index) {
        if (index == 0) {
            this.percolateDown(index);
        } else {
            int parentIndex = (index - 1) / NUM_CHILDREN;
            if (this.leq(this.heap[index], this.heap[parentIndex])) {
                this.percolateUp(index);
            } else {
                this.percolateDown(index);
            }
        }
    }

    private void swap(int a, int b) {
        T temp = this.heap[b];
        this.heap[b] = this.heap[a];
        this.heap[a] = temp;
    }

    private boolean leq(T a, T b) {
        return a.compareTo(b) <= 0;
    }

    @Override
    public String toString() {
        return IPriorityQueue.toString(this);
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayHeapIterator<>(this.heap, this.size());
    }

    private static class ArrayHeapIterator<T extends Comparable<T>> implements Iterator<T> {

        private final T[] heap;
        private final int size;
        private int index;

        ArrayHeapIterator(T[] heap, int size) {
            this.heap = heap;
            this.size = size;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return this.index < this.size;
        }

        @Override
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            T output = heap[this.index];
            this.index++;
            return output;
        }
    }
}
