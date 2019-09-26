package misc;

import datastructures.lists.DoubleLinkedList;
import datastructures.lists.IList;
import datastructures.priorityqueues.ArrayHeapPriorityQueue;
import datastructures.priorityqueues.IPriorityQueue;

public class Sorter {
    /**
     * This method takes the input list and returns the greatest `k` elements in sorted order, from
     * least to greatest.
     *
     * If the input list contains fewer than `k` elements, return a list containing all
     * `input.length` elements in sorted order.
     *
     * Precondition: `input` does not contain `null`s or duplicate values (according to `equals`)
     * Postcondition: the input list has not been mutated
     *
     * @throws IllegalArgumentException  if k < 0
     * @throws IllegalArgumentException  if input is null
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        /*
        Implementation notes:

        - This static method is a *generic method*. A generic method is similar to a generic class,
          except that the generic parameter is used only within this method.

          You can implement a generic method in basically the same way you implement generic
          classes: just use the `T` generic type as if it were a regular type.

        - You should implement this method by using your `ArrayHeapPriorityQueue` in order to
          achieve O(n log k) runtime.

        */

        if (k < 0 || input == null) {
            throw new IllegalArgumentException();
        }
        if (k > input.size()) {
            k = input.size();
        }

        IPriorityQueue<T> heap = new ArrayHeapPriorityQueue<>();
        IList<T> result = new DoubleLinkedList<>();

        if (k == 0) {
            return result;
        }

        // copy the list
        int n = 0; // keep track of which element of input we are operating on
        for (T element : input) {
            if (n < k) { // keep first k elements in the heap
                heap.add(element);
            } else if (element.compareTo(heap.peekMin()) > 0) { // element > heap.peekMin()
                heap.add(element);
                heap.removeMin();
            }
            // element.compareTo(heap.peekMin()) = 0 : impossible
            // input[i] < heap.peekMin(): do nothing
            n++;
        }

        // store sorted values from the heap
        for (int i = 0; i < k; i++) {
            result.add(heap.removeMin());
        }
        return result;
    }
}
