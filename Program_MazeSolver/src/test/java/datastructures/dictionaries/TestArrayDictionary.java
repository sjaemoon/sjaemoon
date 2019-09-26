package datastructures.dictionaries;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/**
 * This file provides some tests for ArrayDictionary methods.
 *
 * Note that many tests depend on basic functionality such as put and get since the
 * `makeBasicDictionary` and `assertDictMatches` helper methods use these methods, so tests for
 * other methods may fail if these basic `ArrayDictionary` methods do not function correctly.
 *
 * In general, make sure that tests named with "basic" at the beginning are passing before attempting
 * to debug issues with other tests. (This will not guarantee that the basic methods are functioning
 * correctly, but may help distinguish between issues that tests are attempting to check for and
 * issues with other functionality that the tests require.)
 *
 * Changes in this file will be ignored during grading.
 */
@Tag("project1")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestArrayDictionary extends BaseTestDictionary {
    @Override
    protected <K, V> IDictionary<K, V> newDictionary() {
        return new ArrayDictionary<>();
    }

    /**
     * If you fail this test but pass the others, you probably didn't implement the optimization
     * described on the assignment webpage.
     */
    @Test
    void testRemoveEfficient() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            IDictionary<Integer, Integer> dict = newDictionary();
            int cap = 20000;

            for (int i = 0; i < cap; i++) {
                dict.put(i, i * 2);
            }

            // correctly implemented, should be ~2ms vs ~1000ms
            assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
                // Remove front as buffer
                for (int i = 0; i < 15; i++) {
                    dict.remove(i);
                }

                // Remove elements that should be shifted into front 15 slots
                // continuously (1/5 of cap total)
                for (int i = cap - 1; i > cap * 4 / 5; i--) {
                    dict.remove(i);
                }
            }, "Remove not efficient");
        });
    }
}
