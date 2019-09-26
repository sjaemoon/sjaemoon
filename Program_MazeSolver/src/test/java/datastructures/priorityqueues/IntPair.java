package datastructures.priorityqueues;

/**
 * An ordered pair of `int`s, comparable based on the sum of the two `int`s.
 *
 * You can put these in your `ArrayHeapPriorityQueue` to test comparison ties (when `compareTo`
 * returns 0).
 *
 * Most comparable Java types (including the built-in `String`, `Integer`, and `Double`, types)
 * have implemented `compareTo` such that `a.compareTo(b) == 0` if and only if `a.equals(b)`.
 * This means that if we want to test values in our heaps that result in sorting ties
 * (`a.compareTo(b) == 0`), we will need to use a custom class such as this one.
 *
 * For example, the following code should throw an `InvalidElementException`:
 *
 * <pre>{@code
 *     IPriorityQueue<String> heap = new ArrayHeapPriorityQueue<>();
 *     heap.add("hello!");
 *     heap.add("hello!"); // throws an exception because "hello!".equals("hello!") is true and our
 *                         // heap doesn't support duplicate values (according to `equals`)
 * }</pre>
 *
 * But for this class,
 *
 * <pre>{@code
 *     IntPair a = new IntPair(1, 2);
 *     IntPair b = new IntPair(0, 3);
 *     a.compareTo(b);  // returns `0`
 *     a.equals(b);     // returns `false`, so both can be put into an `ArrayHeapPriorityQueue`
 * }</pre>
 *
 * Note: because these values are equivalent according to `compareTo`, calling `removeMin` on a
 * heap containing both could return them in any order.
 */
public class IntPair implements Comparable<IntPair> {
    private final int val1;
    private final int val2;

    public IntPair(int value1, int value2) {
        this.val1 = value1;
        this.val2 = value2;
    }

    /**
     * Returns an array of `IntPair` objects specified by the `values` parameter. Each
     * inner array in `values` must have length 2 and represents an `IntPair` to be created.
     *
     * For example,
     * <pre>{@code
     *     createArray(new int[][]{{0, 1},
     *                             {2, 3},
     *                             {4, 5}})
     * }</pre>
     *
     * will return an `IntPair` array of length 3 with values `(0,1)`, `(2,3)`, and `(4,5)`.
     *
     * @throws IllegalArgumentException  if any inner array in `values` has length other than 2
     */
    public static IntPair[] createArray(int[][] values) {
        IntPair[] output = new IntPair[values.length];
        for (int i = 0; i < values.length; i++) {
            if (values[i].length != 2) {
                throw new IllegalArgumentException("row " + i + "of `values` has length "
                        + values.length + " but each inner array in `values` must have length 2");
            }
            output[i] = new IntPair(values[i][0], values[i][1]);
        }
        return output;
    }

    @Override
    public int compareTo(IntPair o) {
        return Integer.compare(val1 + val2, o.val1 + o.val2);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntPair)) {
            return false;
        }
        IntPair temp = (IntPair) obj;
        return val1 == temp.val1 && val2 == temp.val2;
    }

    @Override
    public int hashCode() {
        return this.val1 + 31 * this.val2;
    }

    @Override
    public String toString() {
        return "(" + val1 + "," + val2 + ")";
    }
}
