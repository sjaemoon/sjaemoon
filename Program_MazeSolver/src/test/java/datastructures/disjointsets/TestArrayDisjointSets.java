package datastructures.disjointsets;

import misc.BaseTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Duration;

import static datastructures.disjointsets.IDisjointSetsMatcher.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

@Tag("project4")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestArrayDisjointSets extends BaseTest {
    public static <T> int[] getPointers(IDisjointSets<T> disjointSet) {
        return ((ArrayDisjointSets<T>) disjointSet).pointers;
    }

    public static <T> IDisjointSets<T> createDisjointSets(T[] items) {
        IDisjointSets<T> disjointSets = new ArrayDisjointSets<>();
        for (T item : items) {
            disjointSets.makeSet(item);
        }
        return disjointSets;
    }

    @Test
    void testMakeSetAndFindSetSimple() {
        String[] items = new String[] {"a", "b", "c", "d", "e"};
        IDisjointSets<String> disjointSets = createDisjointSets(items);

        /*
        Note: the `hasItems(...).at(...)` matcher uses `IDisjointSets.findSet`, which may mutate the
        data structure, so we run the check multiple times to make sure that `findSet` doesn't
        mutate anything incorrectly.
         */
        for (int i = 0; i < 5; i++) {
            assertThat(disjointSets, hasItems(items).at(0, 1, 2, 3, 4));
        }
    }

    @Test
    void testUnionSimple() {
        String[] items = new String[] {"a", "b", "c", "d", "e"};
        IDisjointSets<String> disjointSets = createDisjointSets(items);

        disjointSets.union("a", "b");
        int id1 = disjointSets.findSet("a");
        assertThat(id1, either(is(0)).or(is(1)));
        assertThat(disjointSets.findSet("b"), is(id1));

        disjointSets.union("c", "d");
        int id2 = disjointSets.findSet("c");
        assertThat(id2, either(is(2)).or(is(3)));
        assertThat(disjointSets.findSet("d"), is(id2));

        assertThat(disjointSets.findSet("e"), is(4));
    }

    @Test
    void testUnionUnequallySizedSets() {
        String[] items = new String[] {"a", "b", "c", "d", "e"};
        IDisjointSets<String> disjointSets = createDisjointSets(items);

        disjointSets.union("a", "b");
        int id = disjointSets.findSet("a");

        disjointSets.union("a", "c");

        for (int i = 0; i < 5; i++) {
            assertThat(disjointSets, hasItems(items).at(id, id, id, 3, 4));
        }
    }

    @Test
    void testUnionManySetsAndFindRepeatedly() {
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
            IDisjointSets<Integer> disjointSets = new ArrayDisjointSets<>();
            disjointSets.makeSet(0);

            int numItems = 5000;
            for (int i = 1; i < numItems; i++) {
                disjointSets.makeSet(i);
                disjointSets.union(0, i);
            }

            int id = disjointSets.findSet(0);
            Integer[] items = new Integer[numItems];
            int[] reps = new int[numItems];
            for (int i = 0; i < numItems; i++) {
                items[i] = i;
                reps[i] = id;
            }

            int trials = 6000;
            for (int j = 0; j < trials; j++) {
                assertThat(disjointSets, hasItems(items).at(reps));
            }
        });
    }
}
