package misc;

import datastructures.lists.DoubleLinkedList;
import datastructures.lists.IList;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static datastructures.lists.IListMatcher.listContaining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@Tag("project3")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestSorter extends BaseTest {
    @Test
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(5, list);
        assertThat(top, is(listContaining(15, 16, 17, 18, 19)));
    }

    @Test
    public void testSingleElement() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(0);
        IList<Integer> top = Sorter.topKSort(1, list);
        assertThat(top, is(listContaining(0)));
    }

    @Test
    public void testNullElement() {
        IList<Integer> list = null;
        assertThrows(IllegalArgumentException.class, () -> { Sorter.topKSort(0, list); });
    }

    @Test
    public void testEmptyInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        IList<Integer> top = Sorter.topKSort(0, list);
        assertThat(top.isEmpty(), is(true));
    }

    @Test
    public void testLargerList() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 1000; i++) { // 0-999
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(5, list);
        assertThat(top, is(listContaining(995, 996, 997, 998, 999)));
    }

    //Jason: added on 8/16
    @Test
    public void testSmallList() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) { // 0-9
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(4, list);
        assertThat(top, is(listContaining(6, 7, 8, 9)));
        assertThat(list, is(listContaining(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));

        assertThat(list.indexOf(0), is(0));
        assertThat(list.indexOf(1), is(1));
        assertThat(list.indexOf(2), is(2));
        assertThat(list.indexOf(3), is(3));
        assertThat(list.indexOf(4), is(4));
        assertThat(list.indexOf(5), is(5));
        assertThat(list.indexOf(6), is(6));
        assertThat(list.indexOf(7), is(7));
    }

    @Test
    public void testReverseList() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 999; i >= 0; i--) { // 0-999
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(5, list);
        assertThat(top, is(listContaining(995, 996, 997, 998, 999)));
    }

    //Jason: modified this to fix our bug 8/16
    @Test
    public void testRandomOrderList() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(2);
        list.add(8);
        list.add(3);
        list.add(0);
        list.add(7);
        list.add(5);
        list.add(4);
        list.add(6);
        list.add(9);
        list.add(1);

        assertThat(list.get(0), is(2));
        assertThat(list.get(1), is(8));
        assertThat(list.get(2), is(3));
        assertThat(list.get(3), is(0));
        assertThat(list.get(4), is(7));
        assertThat(list.get(5), is(5));
        assertThat(list.get(6), is(4));
        assertThat(list.get(7), is(6));

        IList<Integer> top = Sorter.topKSort(5, list);
        assertThat(top, is(listContaining(5, 6, 7, 8, 9)));

        assertThat(list, is(listContaining(2, 8, 3, 0, 7, 5, 4, 6, 9, 1)));

        assertThat(list.get(0), is(2));
        assertThat(list.get(1), is(8));
        assertThat(list.get(2), is(3));
        assertThat(list.get(3), is(0));
        assertThat(list.get(4), is(7));
        assertThat(list.get(5), is(5));
        assertThat(list.get(6), is(4));
        assertThat(list.get(7), is(6));
        assertThat(list.get(8), is(9));
        assertThat(list.get(9), is(1));

        assertThat(list.indexOf(2), is(0));
        assertThat(list.indexOf(8), is(1));
        assertThat(list.indexOf(3), is(2));
        assertThat(list.indexOf(0), is(3));
        assertThat(list.indexOf(7), is(4));
        assertThat(list.indexOf(5), is(5));
        assertThat(list.indexOf(4), is(6));
        assertThat(list.indexOf(6), is(7));
        assertThat(list.indexOf(9), is(8));
        assertThat(list.indexOf(1), is(9));
    }

    @Test
    public void testInvalidK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) { // 0-9
            list.add(i);
        }
        assertThrows(IllegalArgumentException.class, () -> { Sorter.topKSort(-1, list); });
    }

    @Test
    public void testLargeK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 5; i++) { // 0-9
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(6, list);
        assertThat(top, is(listContaining(0, 1, 2, 3, 4)));
    }

    @Test
    public void testZeroK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) { // 0-9
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(0, list);
        assertThat(top.isEmpty(), is(true));
    }

    @Test
    public void testMaxK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) { // 0-9
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(10, list);
        assertThat(top, is(listContaining(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }
}
