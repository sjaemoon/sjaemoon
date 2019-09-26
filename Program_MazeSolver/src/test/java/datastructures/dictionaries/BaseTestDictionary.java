package datastructures.dictionaries;

import misc.BaseTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static datastructures.dictionaries.IDictionaryMatcher.dictContaining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;


/**
 * This class is abstract, so running it directly will prompt you to choose one of its
 * implementations to run instead, each of which will inherit (and re-use) the tests defined
 * here.
 *
 * Note that many tests depend on basic functionality such as `put` (called inside
 * `makeBasicDictionary`) and `get` and `size` (called inside `dictContaining`), so tests for
 * other methods may fail if these basic `IDictionary` methods do not function correctly.
 *
 * Also note: you should not have to look inside the `dictContaining` method - just know that it
 * will function correctly as long as your `get` and `size` methods work. Also know that if a test
 * fails, it will attempt to use your iterator to generate a more descriptive error message. If the
 * iterator is not implemented yet, you will instead get a message that there are more key-value
 * pairs undisplayed.
 *
 * @see TestArrayDictionary
 * @see TestChainedHashDictionary
 */
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public abstract class BaseTestDictionary extends BaseTest {
    protected abstract <K, V> IDictionary<K, V> newDictionary();

    IDictionary<String, String> makeBasicDictionary() {
        IDictionary<String, String> dict = this.newDictionary();
        dict.put("keyA", "valA");
        dict.put("keyB", "valB");
        dict.put("keyC", "valC");
        return dict;
    }

    @Test
    void basicTestConstructor() {
        IDictionary<String, String> dict = this.makeBasicDictionary();
        assertThat(dict, is(dictContaining(Map.of(
                "keyA", "valA",
                "keyB", "valB",
                "keyC", "valC"
        ))));
    }

    @Test
    void basicTestPutUpdatesSize() {
        IDictionary<String, String> dict = this.newDictionary();
        int initSize = dict.size();
        dict.put("keyA", "valA");

        assertThat(dict.size(), is(initSize + 1));
    }

    @Test
    void basicTestPutDuplicateKey() {
        IDictionary<String, String> dict = this.newDictionary();
        assertThat(dict.put("a", "b"), is(nullValue()));
        int size = dict.size();

        assertThat(dict.put("a", "c"), is("b"));
        assertThat(dict.size(), is(size));
        assertThat(dict.get("a"), is("c"));

    }

    @Test
    void testPutDuplicateKeyMultiple() {
        IDictionary<Integer, Integer> dict = this.newDictionary();

        // First insertion
        assertThat(dict.put(3, 4), is(nullValue()));
        assertThat(dict, is(dictContaining(Map.of(3, 4))));
        // Second insertion
        assertThat(dict.put(3, 5), is(4));
        assertThat(dict, is(dictContaining(Map.of(3, 5))));
        // Third insertion
        assertThat(dict.put(3, 4), is(5));
        assertThat(dict, is(dictContaining(Map.of(3, 4))));
    }

    @Test
    void testPutDuplicateKeyMany() {
        IDictionary<String, String> dict = this.newDictionary();
        dict.put("a", "1");
        dict.put("b", "1");
        dict.put("a", "2");
        dict.put("a", "3");
        dict.put("c", "1");
        dict.put("a", "4");
        dict.put("c", "2");

        assertThat(dict, is(dictContaining(Map.of(
                "a", "4",
                "b", "1",
                "c", "2"
        ))));
    }

    @Test
    void testGetNonexistentKeyThrowsException() {
        IDictionary<String, Integer> dict = this.newDictionary();

        assertThrows(NoSuchKeyException.class, () -> dict.get("foo"));

        dict.put("foo", 3);
        dict.put("bar", 3);

        assertThrows(NoSuchKeyException.class, () -> dict.get("qux"));
    }

    @Test
    void testPutAndGetMany() {
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            IDictionary<Integer, Integer> dict = this.newDictionary();
            int cap = 10000;

            for (int i = 0; i < cap; i++) {
                dict.put(i, i * 2);
            }

            for (int i = cap - 1; i >= 0; i--) {
                int value = dict.get(i);
                assertThat(value, is(i * 2));
            }

            assertThat(dict.size(), is(cap));
            assertThat(dict.isEmpty(), is(false));
        });
    }

    @Test
    void testRemoveBasic() {
        IDictionary<String, String> dict = this.makeBasicDictionary();

        assertThat(dict.remove("keyB"), is("valB"));
        assertThat(dict, is(dictContaining(Map.of(
                "keyA", "valA",
                "keyC", "valC"
        ))));

        assertThat(dict.remove("keyA"), is("valA"));
        assertThat(dict, is(dictContaining(Map.of("keyC", "valC"))));

        assertThat(dict.remove("keyC"), is("valC"));
        assertThat(dict, is(dictContaining(Map.of())));
    }

    @Test
    void testPutDuplicateKeyAndRemove() {
        IDictionary<String, String> dict = this.newDictionary();
        dict.put("a", "1");
        dict.put("b", "2");
        dict.put("c", "3");
        dict.put("a", "4");
        dict.put("d", "5");
        dict.put("b", "6");

        assertThat(dict, is(dictContaining(Map.of(
                "a", "4",
                "b", "6",
                "c", "3",
                "d", "5"
        ))));

        assertThat(dict.remove("a"), is("4"));
        assertThat(dict, is(dictContaining(Map.of(
                "b", "6",
                "c", "3",
                "d", "5"
        ))));

        assertThat(dict.remove("b"), is("6"));
        assertThat(dict, is(dictContaining(Map.of(
                "c", "3",
                "d", "5"
        ))));
    }

    @Test
    void testRemoveNonexistent() {
        IDictionary<Integer, String> list = this.newDictionary();
        list.put(3, "a");
        assertThat(list.remove(4), is(nullValue()));
        assertThat(list.remove(3), is("a"));
        assertThat(list.remove(3), is(nullValue()));
    }

    @Test
    void testPutRemoveMany() {
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            int cap = 15000;
            IDictionary<Integer, Integer> dict = this.newDictionary();

            for (int repeats = 0; repeats < 3; repeats++) {
                for (int i = 0; i < cap; i++) {
                    dict.put(i, i * 2);
                }

                for (int i = 0; i < cap; i++) {
                    int value = dict.remove(i);
                    assertThat(value, is(i * 2));
                }
            }
            assertThat(dict.size(), is(0));
            assertThat(dict.isEmpty(), is(true));
        });
    }

    @Test
    void testContainsKeyBasic() {
        IDictionary<String, Integer> dict = this.newDictionary();

        dict.put("a", 1);
        dict.put("b", 2);
        dict.put("c", 3);
        dict.put("a", 4);
        dict.remove("c");
        dict.put("c", 5);
        dict.put("d", 6);
        dict.put("a", 5);
        dict.remove("c");

        assertThat(dict.containsKey("a"), is(true));
        assertThat(dict.containsKey("b"), is(true));
        assertThat(dict.containsKey("c"), is(false));
        assertThat(dict.containsKey("d"), is(true));
        assertThat(dict.containsKey("e"), is(false));
    }

    @Test
    void testEqualKeys() {
        // Force keys to be two separate objects
        String key1 = "abcdefghijklmnopqrstuvwxyz";
        String key2 = key1 + "";

        IDictionary<String, String> dict = this.newDictionary();
        assertThat(dict.put(key1, "value1"), is(nullValue()));

        assertThat(dict.get(key1), is("value1"));
        assertThat(dict.get(key2), is("value1"));
        assertThat(dict.containsKey(key1), is(true));
        assertThat(dict.containsKey(key2), is(true));

        assertThat(dict.put(key2, "value2"), is("value1"));

        assertThat(dict.containsKey(key1), is(true));
        assertThat(dict.containsKey(key2), is(true));

        assertThat(dict.remove(key1), is("value2"));

        assertThat(dict.containsKey(key1), is(false));
        assertThat(dict.containsKey(key2), is(false));
    }

    @Test
    void testNullKey() {
        IDictionary<String, String> dict = this.makeBasicDictionary();

        assertThat(dict.put(null, "1"), is(nullValue()));
        assertThat(dict.put(null, "2"), is("1"));
        assertThat(dict.put("keyD", "valD"), is(nullValue()));
        assertThat(dict.put("keyE", "valE"), is(nullValue()));

        assertThat(dict.size(), is(6));
        assertThat(dict.containsKey("null"), is(false));
        assertThat(dict.containsKey(null), is(true));
        assertThat(dict.get(null), is("2"));

        assertThat(dict.remove("keyD"), is("valD"));

        assertThat(dict.put(null, "3"), is("2"));

        assertThat(dict.remove(null), is("3"));

        assertThat(dict.size(), is(4));
        assertThat(dict.containsKey(null), is(false));
    }

    @Test
    void testNullValue() {
        IDictionary<String, String> dict = this.newDictionary();

        dict.put("keyA", "valA");
        dict.put("keyB", null);
        dict.put("keyC", "valC");
        dict.put("keyD", null);

        assertThat(dict.get("keyB"), nullValue());
        assertThat(dict.get("keyC"), is("valC"));
        assertThat(dict.get("keyD"), nullValue());
    }

    @Test
    void testCustomObjectKeys() {
        IDictionary<Wrapper<String>, String> dict = this.newDictionary();
        dict.put(new Wrapper<>("foo"), "foo");
        dict.put(new Wrapper<>("bar"), "bar");

        assertThat(dict.get(new Wrapper<>("foo")), is("foo"));
        assertThat(dict.get(new Wrapper<>("bar")), is("bar"));

        dict.put(new Wrapper<>("foo"), "hello");

        assertThat(dict.size(), is(2));
        assertThat(dict.get(new Wrapper<>("foo")), is("hello"));
    }

    @Test
    void testGetMany() {
        IDictionary<String, String> dict = this.makeBasicDictionary();
        int cap = 100000;

        for (int i = 0; i < cap; i++) {
            dict.put("keyC", "newValC");
        }

        for (int i = 0; i < cap; i++) {
            assertThat(dict.get("keyC"), is("newValC"));
        }
    }

    @Test
    void testGetOrDefaultExistingKey() {
        IDictionary<String, String> dict = this.makeBasicDictionary();
        assertThat(dict.getOrDefault("keyA", "defaultValue"), is("valA"));
    }

    @Test
    void testGetOrDefaultMissingKey() {
        IDictionary<String, String> dict = this.makeBasicDictionary();
        assertThat(dict.getOrDefault("missingKey", "defaultValue"), is("defaultValue"));
    }

    @Test
    void testGetOrDefaultNullKey() {
        IDictionary<String, String> dict = this.makeBasicDictionary();
        assertThat(dict.getOrDefault(null, "defaultValue"), is("defaultValue"));

        dict.put(null, "dummyValue");
        assertThat(dict.getOrDefault(null, "defaultValue"), is("dummyValue"));

    }

    @Test
    void testGetOrDefaultManyEmptyDictionary() {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            IDictionary<String, String> dict = this.newDictionary();
            for (int i = 0; i < 5000000; i++) {
                assertThat(dict.getOrDefault("unknownKey", "defaultValue"), is("defaultValue"));
            }
        });
    }

    @Test
    void testIterator() {
        IDictionary<String, Integer> dict = this.newDictionary();
        IDictionary<String, Integer> copy = this.newDictionary();
        for (int i = 0; i < 1000; i++) {
            dict.put("" + i, i);
            copy.put("" + i, i);
        }

        for (KVPair<String, Integer> pair : dict) {
            String key = pair.getKey();
            int actualValue = pair.getValue();
            int expectedValue = copy.get(key);

            assertThat(actualValue, is(expectedValue));
            copy.remove(key);
        }

        assertThat(copy.isEmpty(), is(true));
    }

    @Test
    void testIteratorUnusualKeys() {
        IDictionary<String, String> map = this.newDictionary();

        map.put(null, "hello");
        map.put("", "world");

        boolean metNullKey = false;
        boolean metEmptyKey = false;
        int numItems = 0;
        for (KVPair<String, String> pair : map) {
            if (pair.getKey() == null) {
                metNullKey = true;
                assertThat(pair.getValue(), is("hello"));
            } else if (pair.getKey().equals("")) {
                metEmptyKey = true;
                assertThat(pair.getValue(), is("world"));
            }

            numItems += 1;
        }

        assertThat(numItems, is(2));
        assertThat(metNullKey, is(true));
        assertThat(metEmptyKey, is(true));
    }

    @Test
    void testIteratorEndsCorrectly() {
        IDictionary<String, String> dict = this.makeBasicDictionary();

        Iterator<KVPair<String, String>> iter = dict.iterator();

        for (int i = 0; i < dict.size(); i++) {
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
        IDictionary<String, String> dict = makeBasicDictionary();
        Iterator<KVPair<String, String>> iter1 = dict.iterator();
        Iterator<KVPair<String, String>> iter2 = dict.iterator();

        for (int i = 0; i < dict.size(); i++) {
            assertThat(iter1.hasNext(), is(iter2.hasNext()));
            assertThat(iter1.next(), is(iter2.next()));
        }

        assertThat(iter1.hasNext(), is(false));
        assertThat(iter2.hasNext(), is(false));

        dict.put("nextKey1", "val");
        dict.put("nextKey2", "val");

        assertThat(dict.size(), is(5));

        Iterator<KVPair<String, String>> iter3 = dict.iterator();
        Iterator<KVPair<String, String>> iter4 = dict.iterator();

        for (int i = 0; i < dict.size(); i++) {
            assertThat(iter3.hasNext(), is(iter4.hasNext()));
            assertThat(iter3.next(), is(iter4.next()));
        }

        assertThat(iter3.hasNext(), is(false));
        assertThat(iter4.hasNext(), is(false));
    }


    @Test
    void testIteratorOverEmptyDictionary() {
        IDictionary<String, String> dict = this.newDictionary();

        for (int i = 0; i < 10; i++) {
            Iterator<KVPair<String, String>> iter = dict.iterator();
            for (int j = 0; j < 3; j++) {
                assertThat(iter.hasNext(), is(false));
            }
            for (int j = 0; j < 3; j++) {
                assertThrows(NoSuchElementException.class, iter::next);
            }
        }
    }

    @Test
    void testIteratorOverDictionaryWithOneElement() {
        IDictionary<String, String> dict = this.newDictionary();
        dict.put("foo", "bar");

        for (int i = 0; i < 10; i++) {
            Iterator<KVPair<String, String>> iter = dict.iterator();
            for (int j = 0; j < 3; j++) {
                assertThat(iter.hasNext(), is(true));
            }
            KVPair<String, String> pair = iter.next();

            assertThat(pair.getKey(), is("foo"));
            assertThat(pair.getValue(), is("bar"));

            for (int j = 0; j < 3; j++) {
                assertThat(iter.hasNext(), is(false));
            }
            for (int j = 0; j < 3; j++) {
                assertThrows(NoSuchElementException.class, iter::next);
            }
        }
    }

    @Test
    void testIteratorRunsMultipleTimes() {
        IDictionary<String, String> dict = this.newDictionary();
        for (int i = 0; i < 100; i++) {
            dict.put("key" + i, "val" + i);
        }

        List<KVPair<String, String>> expectedOutput = new ArrayList<>();
        for (KVPair<String, String> pair : dict) {
            expectedOutput.add(pair);
        }

        for (int i = 0; i < 3; i++) {
            Iterator<KVPair<String, String>> iter = dict.iterator();
            for (int j = 0; j < expectedOutput.size(); j++) {
                assertThat(iter.hasNext(), is(true));
                assertThat(iter.next(), is(expectedOutput.get(j)));
            }

            assertThat(iter.hasNext(), is(false));
        }
    }

    @Test
    void testIteratorHasNextAfterRemove() {
        IDictionary<Integer, Integer> dict = this.newDictionary();
        dict.put(373, 373);
        assertThat(dict.iterator().hasNext(), is(true));
        dict.remove(373);
        assertThat(dict.iterator().hasNext(), is(false));
    }

    @Test
    void testIteratorRemoveAll() {
        IDictionary<Integer, Integer> dict = this.newDictionary();
        Map<Integer, Integer> expected = new HashMap<>();
        int limit = 100;
        for (int i = 0; i < limit; i++) {
            dict.put(i, i + 1);
            expected.put(i, i + 1);
        }

        for (int i = 0; i < limit; i++) {
            int countPairs = 0;
            for (KVPair<Integer, Integer> pair : dict) {
                assertThat(expected.containsKey(pair.getKey()), is(true));
                assertThat(pair.getValue(), is(expected.get(pair.getKey())));
                countPairs++;
            }
            assertThat(countPairs, is(expected.size()));
            assertThat(dict.remove(i), is(expected.remove(i)));
        }
        assertThat(dict.isEmpty(), is(true));
        assertThat(dict.iterator().hasNext(), is(false));
    }

    @Test
    void testIteratorPutNewKeys() {
        IDictionary<Integer, Integer> dict = this.newDictionary();
        Map<Integer, Integer> expected = new HashMap<>();

        int limit = 100;
        for (int i = 0; i < limit; i++) {
            dict.put(i, i + 1);
            expected.put(i, i + 1);
        }

        for (int i = 0; i < limit; i++) {
            int countPairs = 0;
            for (KVPair<Integer, Integer> pair : dict) {
                assertThat(expected.containsKey(pair.getKey()), is(true));
                assertThat(pair.getValue(), is(expected.get(pair.getKey())));
                countPairs++;
            }
            assertThat(countPairs, is(expected.size()));

            dict.put(limit + i, limit + i + 1);
            expected.put(limit + i, limit + i + 1);
        }
    }

    @Test
    void testIteratorPutDuplicateKeys() {
        IDictionary<Integer, Integer> dict = this.newDictionary();
        Map<Integer, Integer> expected = new HashMap<>();

        int limit = 100;
        for (int i = 0; i < limit; i++) {
            dict.put(i, i + 1);
            expected.put(i, i + 1);
        }

        for (int i = 0; i < limit; i++) {
            int countPairs = 0;
            for (KVPair<Integer, Integer> pair : dict) {
                assertThat(expected.containsKey(pair.getKey()), is(true));
                assertThat(pair.getValue(), is(expected.get(pair.getKey())));
                countPairs++;
            }
            assertThat(countPairs, is(expected.size()));

            dict.put(i, i);
            expected.put(i, i);
        }
    }
}
