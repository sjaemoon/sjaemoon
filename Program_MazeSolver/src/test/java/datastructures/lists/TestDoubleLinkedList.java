package datastructures.lists;

import datastructures.EmptyContainerException;
import misc.BaseTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Duration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import static datastructures.lists.IListMatcher.listContaining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;


/**
 * This file provides some tests for `DoubleLinkedList` methods, excluding the `delete` method.
 *
 * Note that many tests depend on basic functionality such as `add` (called inside `makeBasicList`)
 * and `get` and `size` (called inside `listContaining`), so tests for other methods may fail if
 * these basic `DoubleLinkedList` methods do not function correctly.
 *
 * Also note: you should not have to look inside the `listContaining` method - just know that it
 * will function correctly as long as your `get` and `size` methods work.
 *
 * In general, make sure that tests named with "basic" at the beginning are passing before
 * attempting to debug issues with other tests. (This will not guarantee that the basic methods are
 * functioning correctly, but may help distinguish the issues that tests are designed to check for
 * from any issues with other functionality that the tests require.)
 *
 * Changes in this file will be ignored during grading.
 */
@Tag("project1")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)  // This annotation makes JUnit run tests in alphabetical order
public class TestDoubleLinkedList extends BaseTest {
    /**
     * This method creates a simple list containing three elements to help minimize
     * redundancy later in our tests.
     */
    protected IList<String> makeBasicList() {
        IList<String> list = new DoubleLinkedList<>();

        list.add("a");
        list.add("b");
        list.add("c");

        return list;
    }

    @Test
    void basicTestAddAndGet() {
        IList<String> list = makeBasicList();
        assertThat(list, is(listContaining("a", "b", "c")));
    }

    @Test
    void basicTestAddIncrementsSize() {
        IList<String> list = makeBasicList();
        int initSize = list.size();
        list.add("d");
        assertThat(list.size(), is(initSize + 1));
    }

    @Test
    void basicTestRemoveDecrementsSize() {
        IList<String> list = makeBasicList();
        int initSize = list.size();
        list.remove();
        assertThat(list.size(), is(initSize - 1));
    }

    @Test
    void basicTestSet() {
        IList<String> list = makeBasicList();
        int initSize = list.size();
        assertThat(list.set(1, "d"), is("b"));
        assertThat(list.get(1), is("d"));
        assertThat(list.size(), is(initSize));
    }

    @Test
    void basicTestContains() {
        IList<String> list = makeBasicList();

        assertThat(list.contains("a"), is(true));
        assertThat(list.contains("b"), is(true));
        assertThat(list.contains("c"), is(true));
        assertThat(list.contains("d"), is(false));
    }

    @Test
    void basicTestIndexOf() {
        IList<String> list = makeBasicList();

        assertThat(list.indexOf("a"), is(0));
        assertThat(list.indexOf("b"), is(1));
        assertThat(list.indexOf("c"), is(2));
        assertThat(list.indexOf("d"), is(-1));
    }

    @Test
    void basicTestInsert() {
        IList<String> list = this.makeBasicList();
        list.insert(0, "x");
        assertThat(list, is(listContaining("x", "a", "b", "c")));

        list.insert(2, "y");
        assertThat(list, is(listContaining("x", "a", "y", "b", "c")));

        list.insert(5, "z");
        assertThat(list, is(listContaining("x", "a", "y", "b", "c", "z")));
    }

    @Test
    void testAddAndGetMany() {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            IList<Integer> list = new DoubleLinkedList<>();
            int cap = 1000;
            for (int i = 0; i < cap; i++) {
                list.add(i * 2);
            }
            assertThat(list.size(), is(cap));
            for (int i = 0; i < cap; i++) {
                int value = list.get(i);
                assertThat(value, is(i * 2));
            }
            assertThat(list.size(), is(cap));
        });
    }

    @Test
    void testAddIsEfficient() {
        assertTimeoutPreemptively(Duration.ofSeconds(15), () -> {
            IList<Integer> list = new DoubleLinkedList<>();
            int cap = 5000000;
            for (int i = 0; i < cap; i++) {
                list.add(i * 2);
            }
            assertThat(list.size(), is(cap));
        });
    }

    @Test
    void testRemoveMultiple() {
        IList<String> list = this.makeBasicList();
        assertThat(list.remove(), is("c"));
        assertThat(list, is(listContaining("a", "b")));

        assertThat(list.remove(), is("b"));
        assertThat(list, is(listContaining("a")));

        assertThat(list.remove(), is("a"));
        assertThat(list, is(listContaining(new String[] {})));
    }

    @Test
    void testRemoveMany() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 1000;

        for (int i = 0; i < cap; i++) {
            list.add(i);
        }

        assertThat(list.size(), is(cap));

        for (int i = cap - 1; i >= 0; i--) {
            int value = list.remove();
            assertThat(value, is(i));
        }

        assertThat(list.size(), is(0));
    }

    @Test
    void testAlternatingAddAndRemove() {
        int iterators = 1000;

        IList<String> list = new DoubleLinkedList<>();

        for (int i = 0; i < iterators; i++) {
            String entry = "" + i;
            list.add(entry);
            assertThat(list.size(), is(1));

            String out = list.remove();
            assertThat(out, is(entry));
            assertThat(list.size(), is(0));
        }
    }

    @Test
    void testRemoveFromEndIsEfficient() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            IList<Integer> list = new DoubleLinkedList<>();
            for (int i = 0; i < 10000; i++) {
                list.add(i);
            }

            for (int i = 0; i < 10000; i++) {
                list.add(-1);
                list.remove();
            }
        });
    }

    @Test
    void testRemoveOnEmptyListThrowsException() {
        IList<String> list = this.makeBasicList();
        list.remove();
        list.remove();
        list.remove();
        assertThrows(EmptyContainerException.class, list::remove);
    }

    @Test
    void testGetOutOfBoundsThrowsException() {
        IList<String> list = this.makeBasicList();

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));

        // This should be ok
        list.get(2);

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1000));
    }

    @Test
    void testSet() {
        IList<String> list = this.makeBasicList();

        assertThat(list.set(0, "AAA"), is("a"));
        assertThat(list, is(listContaining("AAA", "b", "c")));

        assertThat(list.set(1, "BBB"), is("b"));
        assertThat(list, is(listContaining("AAA", "BBB", "c")));

        assertThat(list.set(2, "CCC"), is("c"));
        assertThat(list, is(listContaining("AAA", "BBB", "CCC")));
    }

    @Test
    void testSetSingleElement() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("foo");

        assertThat(list.set(0, "bar"), is("foo"));
        assertThat(list, is(listContaining("bar")));

        assertThat(list.set(0, "baz"), is("bar"));
        assertThat(list, is(listContaining("baz")));
    }

    @Test
    void testSetOutOfBoundsThrowsException() {
        IList<String> list = this.makeBasicList();

        assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, "AAA"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(3, "AAA"));
    }

    @Test
    void testSetMany() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            IList<String> list = new DoubleLinkedList<>();
            int cap = 10000;

            for (int i = 0; i < cap; i++) {
                list.add("foo" + i);
            }

            for (int i = 0; i < cap; i++) {
                assertThat(list.set(i, "bar" + i), is("foo" + i));
            }

            for (int i = 0; i < cap; i++) {
                assertThat(list.get(i), is("bar" + i));
            }

            for (int i = cap - 1; i >= 0; i--) {
                assertThat(list.set(i, "qux" + i), is("bar" + i));
            }

            for (int i = cap - 1; i >= 0; i--) {
                assertThat(list.get(i), is("qux" + i));
            }
        });
    }

    @Test
    void testInsertEmptyAndSingleElement() {
        // Lists 1 and 2: insert into empty
        IList<String> list1 = new DoubleLinkedList<>();
        IList<String> list2 = new DoubleLinkedList<>();
        list1.insert(0, "a");
        list2.insert(0, "a");

        // No point in checking both lists
        assertThat(list1, is(listContaining("a")));

        // List 1: insert at front
        list1.insert(0, "b");
        assertThat(list1, is(listContaining("b", "a")));


        // List 2: insert at end
        list2.insert(1, "b");
        assertThat(list2, is(listContaining("a", "b")));

    }

    @Test
    void testInsertOutOfBoundsThrowsException() {
        IList<String> list = this.makeBasicList();

        assertThrows(IndexOutOfBoundsException.class, () -> list.insert(-1, "a"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.insert(4, "a"));
    }

    @Test
    void testInsertAtEndIsEfficient() {
        assertTimeoutPreemptively(Duration.ofSeconds(15), () -> {
            IList<Integer> list = new DoubleLinkedList<>();
            int cap = 5000000;
            for (int i = 0; i < cap; i++) {
                list.insert(list.size(), i * 2);
            }
            assertThat(list.size(), is(cap));
        });
    }

    @Test
    void testInsertNearEndIsEfficient() {
        assertTimeoutPreemptively(Duration.ofSeconds(15), () -> {
            IList<Integer> list = new DoubleLinkedList<>();
            list.add(-1);
            list.add(-2);

            int cap = 5000000;
            for (int i = 0; i < cap; i++) {
                list.insert(list.size() - 2, i * 2);
            }
            assertThat(list.size(), is(cap + 2));
        });
    }

    @Test
    void testInsertAtFrontIsEfficient() {
        assertTimeoutPreemptively(Duration.ofSeconds(15), () -> {
            IList<Integer> list = new DoubleLinkedList<>();
            int cap = 5000000;
            for (int i = 0; i < cap; i++) {
                list.insert(0, i * 2);
            }
            assertThat(list.size(), is(cap));
        });
    }

    @Test
    void testIndexOfAndContainsCorrectlyCompareItems() {
        // Two different String objects, but with equal values
        String item1 = "abcdefghijklmnopqrstuvwxyz";
        String item2 = item1 + "";

        IList<String> list = new DoubleLinkedList<>();
        list.add("foo");
        list.add(item1);

        assertThat(list.indexOf(item2), is(1));
        assertThat(list.contains(item2), is(true));
    }

    @Test
    void testIndexOfAndContainsMany() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            int cap = 1000;
            int stringLength = 100;
            String validChars = "abcdefghijklmnopqrstuvwxyz0123456789";

            // By setting the seed to some arbitrary but constant number, we guarantee
            // this random number generator will produce the exact same sequence of numbers
            // every time we run this test. This helps us keep our tests deterministic, which
            // can help with debugging.
            Random rand = new Random();
            rand.setSeed(12345);

            IList<String> list = new DoubleLinkedList<>();
            IList<String> refList = new DoubleLinkedList<>();

            for (int i = 0; i < cap; i++) {
                StringBuilder entry = new StringBuilder();
                for (int j = 0; j < stringLength; j++) {
                    int charIndex = rand.nextInt(validChars.length());
                    entry.append(validChars.charAt(charIndex));
                }

                list.add(entry.toString());
                if (i % 100 == 0) {
                    refList.add(entry.toString());
                }
            }

            for (int i = 0; i < refList.size(); i++) {
                String entry = refList.get(i);

                assertThat(list.indexOf(entry), is(i * 100));
                assertThat(list.contains(entry), is(true));
            }
        });
    }

    @Test
    void testNullElement() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        assertThat(list.indexOf(null), is(-1));
        assertThat(list.contains(null), is(false));

        list.insert(2, null);
        assertThat(list, is(listContaining(1, 2, null, 3, 4)));
        assertThat(list.indexOf(null), is(2));
        assertThat(list.contains(null), is(true));
        assertThat(list.contains(4), is(true));
    }

    @Test
    void testIteratorBasic() {
        IList<String> list = this.makeBasicList();
        Iterator<String> iter = list.iterator();

        // Get first element
        for (int i = 0; i < 5; i++) {
            assertThat(iter.hasNext(), is(true));
        }
        assertThat(iter.next(), is("a"));

        // Get second
        for (int i = 0; i < 5; i++) {
            assertThat(iter.hasNext(), is(true));
        }
        assertThat(iter.next(), is("b"));

        // Get third
        for (int i = 0; i < 5; i++) {
            assertThat(iter.hasNext(), is(true));
        }
        assertThat(iter.next(), is("c"));

        for (int i = 0; i < 5; i++) {
            assertThat(iter.hasNext(), is(false));
        }

        assertThrows(NoSuchElementException.class, iter::next);

        // Check that the list is unchanged
        assertThat(list, is(listContaining("a", "b", "c")));

    }

    @Test
    void testIteratorOnEmptyList() {
        IList<String> list = new DoubleLinkedList<>();

        for (int i = 0; i < 5; i++) {
            Iterator<String> iter = list.iterator();
            for (int j = 0; j < 5; j++) {
                assertThat(iter.hasNext(), is(false));
            }
            assertThrows(NoSuchElementException.class, iter::next);
        }
        assertThat(list, is(listContaining(new String[] {})));
    }

    @Test
    void testIteratorSingleElement() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("foo");

        for (int i = 0; i < 5; i++) {
            Iterator<String> iter = list.iterator();
            for (int j = 0; j < 5; j++) {
                assertThat(iter.hasNext(), is(true));
            }
            assertThat(iter.next(), is("foo"));
            for (int j = 0; j < 5; j++) {
                assertThat(iter.hasNext(), is(false));
            }
            assertThrows(NoSuchElementException.class, iter::next);
        }
        assertThat(list, is(listContaining("foo")));
    }

    @Test
    void testIteratorMany() {
        IList<String> list = this.makeBasicList();
        String[] expected = {"a", "b", "c"};

        for (int i = 0; i < 5; i++) {
            Iterator<String> iter = list.iterator();
            for (int j = 0; j < expected.length; j++) {
                for (int k = 0; k < 5; k++) {
                    assertThat(iter.hasNext(), is(true));
                }
                assertThat(iter.next(), is(expected[j]));
            }

            for (int j = 0; j < 5; j++) {
                assertThat(iter.hasNext(), is(false));
            }
        }
        assertThat(list, is(listContaining(expected)));

        list.insert(2, "z");
        assertThat(list, is(listContaining("a", "b", "z", "c")));

    }

    @Test
    void testIteratorIsEfficient() {
        assertTimeoutPreemptively(Duration.ofSeconds(15), () -> {
            IList<Integer> list = new DoubleLinkedList<>();
            int cap = 5000000;
            for (int i = 0; i < cap; i++) {
                list.add(i * 2);
            }
            assertThat(list.size(), is(cap));
            int count = 0;
            for (int num : list) {
                assertThat(num, is(count));
                count += 2;
            }
        });
    }

    @Test
    void testCustomObjectValues() {
        IList<Wrapper<Integer>> list = new DoubleLinkedList<>();
        list.add(new Wrapper<>(1));
        list.add(new Wrapper<>(2));
        list.add(new Wrapper<>(3));
        list.add(new Wrapper<>(4));

        assertThat(list, listContaining(new Wrapper<>(1), new Wrapper<>(2),
                new Wrapper<>(3), new Wrapper<>(4)));

        list.set(2, new Wrapper<>(5));

        assertThat(list, listContaining(new Wrapper<>(1), new Wrapper<>(2),
                new Wrapper<>(5), new Wrapper<>(4)));

        list.insert(0, new Wrapper<>(4));
        assertThat(list.indexOf(new Wrapper<>(4)), is(0));
    }
}
