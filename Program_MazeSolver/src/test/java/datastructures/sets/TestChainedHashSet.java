package datastructures.sets;

import misc.BaseTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Duration;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static datastructures.sets.ISetMatcher.setContaining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

@Tag("project2")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)  // This annotation makes JUnit run tests in alphabetical order
public class TestChainedHashSet extends BaseTest {
    protected ISet<String> makeBasicSet() {
        ISet<String> set = new ChainedHashSet<>();
        set.add("itemA");
        set.add("itemB");
        set.add("itemC");
        return set;
    }

    @Test
    void testAddAndContainsBasic() {
        ISet<String> set = this.makeBasicSet();
        assertThat(set, is(setContaining("itemA", "itemB", "itemC")));
    }

    @Test
    void testAddDuplicateItems() {
        ISet<Integer> set = new ChainedHashSet<>();
        assertThat(set.add(3), is(true));
        assertThat(set.add(3), is(false));
        assertThat(set.add(3), is(false));
    }


    @Test
    void testContainsDuplicateItems() {
        ISet<Integer> set = new ChainedHashSet<>();
        set.add(3);
        assertThat(set, is(setContaining(3)));
        set.add(3);
        assertThat(set, is(setContaining(3)));
        set.add(3);
        assertThat(set, is(setContaining(3)));
    }

    @Test
    void testAddAndContainsManyDuplicateItems() {
        ISet<String> set = new ChainedHashSet<>();

        assertThat(set.add("a"), is(true));
        assertThat(set.add("b"), is(true));
        assertThat(set.add("a"), is(false));
        assertThat(set.add("a"), is(false));
        assertThat(set.add("c"), is(true));
        assertThat(set.add("a"), is(false));
        assertThat(set.add("c"), is(false));

        assertThat(set, is(setContaining("a", "b", "c")));
    }

    @Test
    void testContainsNonexistent() {
        ISet<String> set = new ChainedHashSet<>();

        assertThat(set.contains("foo"), is(false));

        set.add("foo");
        set.add("bar");

        assertThat(set.contains("qux"), is(false));
    }

    @Test
    void testAddAndContainsMany() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            ISet<Integer> set = new ChainedHashSet<>();
            int cap = 10000;

            for (int i = 0; i < cap; i++) {
                set.add(i);
            }

            for (int i = cap - 1; i >= 0; i--) {
                assertThat(set.contains(i), is(true));
                if (i != 0) {
                    assertThat(set.contains(-i), is(false));
                }
            }

            assertThat(set.size(), is(cap));
            assertThat(set.isEmpty(), is(false));
        });
    }

    @Test
    void testRemoveBasic() {
        ISet<String> set = this.makeBasicSet();

        assertThat(set.remove("itemB"), is(true));
        assertThat(set.remove("itemA"), is(true));
        assertThat(set.remove("itemC"), is(true));

        assertThat(set, is(setContaining()));
    }

    @Test
    void testRemoveDuplicateItems() {
        ISet<String> set = new ChainedHashSet<>();
        set.add("a");
        set.add("b");
        set.add("c");
        set.add("a");
        set.add("d");
        set.add("b");

        assertThat(set, is(setContaining("a", "b", "c", "d")));

        set.remove("a");
        assertThat(set, is(setContaining("b", "c", "d")));

        set.remove("b");
        assertThat(set, is(setContaining("c", "d")));
    }

    @Test
    void testRemoveNonexistentItems() {
        ISet<Integer> set = new ChainedHashSet<>();
        set.add(3);
        assertThat(set.remove(4), is(false));
        assertThat(set.remove(3), is(true));
        assertThat(set.remove(3), is(false));
    }

    @Test
    void testContainsAfterAddAndRemove() {
        ISet<String> set = new ChainedHashSet<>();

        set.add("a");
        set.add("b");
        set.add("c");
        set.add("a");
        set.remove("c");
        set.add("c");
        set.add("d");
        set.add("a");
        set.remove("c");

        assertThat(set.contains("a"), is(true));
        assertThat(set.contains("b"), is(true));
        assertThat(set.contains("c"), is(false));
        assertThat(set.contains("d"), is(true));
        assertThat(set.contains("e"), is(false));
    }

    @Test
    void testEqualItems() {
        // Force items to be two separate objects
        String item1 = "abcdefghijklmnopqrstuvwxyz";
        String item2 = item1 + "";

        ISet<String> set = new ChainedHashSet<>();
        set.add(item1);

        assertThat(set.contains(item1), is(true));
        assertThat(set.contains(item2), is(true));

        set.remove(item2);

        assertThat(set.contains(item1), is(false));
        assertThat(set.contains(item2), is(false));
    }

    @Test
    void testNullItem() {
        ISet<String> set = this.makeBasicSet();

        set.add(null);

        assertThat(set.size(), is(4));
        assertThat(set.contains(null), is(true));

        set.remove(null);
        assertThat(set.contains(null), is(false));
    }

    @Test
    void testContainsMany() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            ISet<String> set = this.makeBasicSet();
            int cap = 100000;

            for (int i = 0; i < cap; i++) {
                set.add("itemC");
            }

            for (int i = 0; i < cap; i++) {
                assertThat(set.contains("itemC"), is(true));
            }
        });
    }

    @Test
    void testIterator() {
        ISet<String> set = new ChainedHashSet<>();
        ISet<String> copy = new ChainedHashSet<>();
        for (int i = 0; i < 1000; i++) {
            set.add("" + i);
            copy.add("" + i);
        }

        for (String item : set) {
            copy.remove(item);
        }

        assertThat(copy.isEmpty(), is(true));
    }

    @Test
    void testIteratorUnusualItems() {
        ISet<String> set = new ChainedHashSet<>();

        set.add(null);
        set.add("");

        boolean metNullitem = false;
        boolean metEmptyitem = false;
        int numItems = 0;
        for (String item : set) {
            if (item == null) {
                metNullitem = true;
            } else if (item.equals("")) {
                metEmptyitem = true;
            }

            numItems += 1;
        }

        assertThat(numItems, is(2));
        assertThat(metNullitem, is(true));
        assertThat(metEmptyitem, is(true));
    }

    @Test
    void testIteratorEndsCorrectly() {
        ISet<String> set = this.makeBasicSet();

        Iterator<String> iter = set.iterator();

        for (int i = 0; i < set.size(); i++) {
            for (int j = 0; j < 1000; j++) {
                assertThat(iter.hasNext(), is(true));
            }
            iter.next();
        }

        for (int j = 0; j < 1000; j++) {
            assertThat(iter.hasNext(), is(false));
        }

        assertThrows(NoSuchElementException.class, iter::next);
    }

    @Test
    void testIteratorsAreIndependent() {
        ISet<String> set = makeBasicSet();
        Iterator<String> iter1 = set.iterator();
        Iterator<String> iter2 = set.iterator();

        for (int i = 0; i < set.size(); i++) {
            assertThat(iter1.hasNext(), is(iter2.hasNext()));
            assertThat(iter1.next(), is(iter2.next()));
        }

        assertThat(iter1.hasNext(), is(false));
        assertThat(iter2.hasNext(), is(false));

        set.add("nextitem1");
        set.add("nextitem2");

        assertThat(set.size(), is(5));
        Iterator<String> iter3 = set.iterator();
        Iterator<String> iter4 = set.iterator();

        for (int i = 0; i < set.size(); i++) {
            assertThat(iter3.hasNext(), is(iter4.hasNext()));
            assertThat(iter3.next(), is(iter4.next()));
        }

        assertThat(iter3.hasNext(), is(false));
        assertThat(iter4.hasNext(), is(false));
    }

    @Test
    void testManyItemsWithSameHashCode() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            ISet<Wrapper<String>> set = new ChainedHashSet<>();
            for (int i = 0; i < 1000; i++) {
                set.add(new Wrapper<>("" + i, 0));
            }

            assertThat(set.size(), is(1000));

            for (int i = 999; i >= 0; i--) {
                String item = "" + i;
                assertThat(set.contains(new Wrapper<>(item + "a", 0)), is(false));
            }

            Wrapper<String> item1 = new Wrapper<>("abc", 0);
            Wrapper<String> item2 = new Wrapper<>("cde", 0);

            set.add(item1);
            set.add(item2);

            assertThat(set.size(), is(1002));
        });
    }

    @Test
    void testNegativeHashCode() {
        ISet<Wrapper<String>> set = new ChainedHashSet<>();

        Wrapper<String> item1 = new Wrapper<>("foo", -1);
        Wrapper<String> item2 = new Wrapper<>("bar", -100000);
        Wrapper<String> item3 = new Wrapper<>("baz", 1);
        Wrapper<String> item4 = new Wrapper<>("qux", -4);

        set.add(item1);
        set.add(item2);
        set.add(item3);

        assertThat(set.contains(item1), is(true));
        assertThat(set.contains(item2), is(true));
        assertThat(set.contains(item3), is(true));
        assertThat(set.contains(item4), is(false));

        set.remove(item1);
        assertThat(set.contains(item1), is(false));
    }

    @Test
    void testStress() {
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            int limit = 1000000;
            ISet<Integer> set = new ChainedHashSet<>();

            for (int i = 0; i < limit; i++) {
                set.add(i);
                assertThat(set.contains(i), is(true));
            }

            for (int i = 0; i < limit; i++) {
                assertThat(set.contains(-1), is(false));
            }

            for (int i = 0; i < limit; i++) {
                set.add(i);
            }

            for (int i = 0; i < limit; i++) {
                set.remove(i);
                assertThat(set.contains(i), is(false));
            }
        });
    }
}
