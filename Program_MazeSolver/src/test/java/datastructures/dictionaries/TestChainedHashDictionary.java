package datastructures.dictionaries;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

@Tag("project2")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)  // This annotation makes JUnit run tests in alphabetical order
public class TestChainedHashDictionary extends BaseTestDictionary {
    protected <K, V> IDictionary<K, V> newDictionary() {
        return new ChainedHashDictionary<>();
    }

    protected IDictionary<String, String>[] extractChain(IDictionary<String, String> dict) {
        return ((ChainedHashDictionary<String, String>) dict).chains;
    }

    @Test
    void testConstructorChainParameters() {
        IDictionary<String, String> dict = new ChainedHashDictionary<>(100, 1, 1);
        dict.put("foo", "bar");

        IDictionary<String, String>[] chains = extractChain(dict);
        assertThat(chains.length, is(1));

        Object[] chain = ((ArrayDictionary<String, String>) chains[0]).pairs;
        assertThat(chain.length, is(1));
    }

    @Test
    void testConstructorResizingThresholdParameter() {
        IDictionary<String, String> dict = new ChainedHashDictionary<>(100, 1, 1);
        for (int i=0; i < 99; i++) {
            dict.put(i + "", "");
        }

        IDictionary<String, String>[] chains = extractChain(dict);
        assertThat(chains.length, is(1));

        dict.put("99", "");
        dict.put("100", "");

        chains = extractChain(dict);
        assertThat(chains.length, is(greaterThan(1)));
    }

    @Test
    void testContainsMaintainsStateForIterator() {
        IDictionary<String, String> dict = this.makeBasicDictionary();

        char mid = 'N';
        for (char i = 'D'; i < mid; i++) {
            dict.put("key" + i, "val" + i);
        }

        for (char i = mid; i < 'Z'; i++) {
            assertThat(dict.containsKey("key" + i), is(false));
        }

        boolean[] contained = new boolean[mid - 'A'];

        int baseSize = 3;
        for (KVPair<String, String> pair : dict) {
            char key = pair.getKey().charAt(baseSize);
            char val = pair.getValue().charAt(baseSize);
            assertThat(val, is(key));

            int letter = key - 'A';
            assertThat("Invalid letter found: " + key,
                    letter, is(both(greaterThan(-1)).and(lessThan(contained.length))));
            assertThat("Duplicate letter found: " + key, contained[letter], is(false));

            contained[letter] = true;
        }

        for (boolean found : contained) {
            assertThat(found, is(true));
        }
    }

    @Test
    void testBigHashCodesAndNull() {
        IDictionary<Wrapper<String>, Integer> dict = this.newDictionary();
        int start = 10000;
        int values = 500;
        for (int i = start; i < start + values; i++) {
            dict.put(new Wrapper<>("" + i, i), i);
        }

        for (int i = start; i < start + values; i++) {
            assertThat(dict.get(new Wrapper<>("" + i, i)), is(i));

            assertThat(dict.containsKey(new Wrapper<>("no", i)), is(false));
            assertThat(dict.containsKey(new Wrapper<>(null, i)), is(false));
        }

        for (int i = start; i < start + values; i++) {
            dict.put(new Wrapper<>("" + i, i), i + values);
        }

        for (int i = start; i < start + values; i++) {
            assertThat(dict.get(new Wrapper<>("" + i, i)), is(i + values));
        }
        assertThat(dict.containsKey(null), is(false));
    }

    @Test
    void testManyKeysWithSameHashCode() {
        IDictionary<Wrapper<String>, Integer> dict = this.newDictionary();
        for (int i = 0; i < 1000; i++) {
            dict.put(new Wrapper<>("" + i, 0), i);
        }

        assertThat(dict.size(), is(1000));

        for (int i = 999; i >= 0; i--) {
            String key = "" + i;
            assertThat(dict.get(new Wrapper<>(key, 0)), is(i));

            assertThat(dict.containsKey(new Wrapper<>(key + "a", 0)), is(false));
        }

        Wrapper<String> key1 = new Wrapper<>("abc", 0);
        Wrapper<String> key2 = new Wrapper<>("cde", 0);

        dict.put(key1, -1);
        dict.put(key2, -2);

        assertThat(dict.size(), is(1002));
        assertThat(dict.get(key1), is(-1));
        assertThat(dict.get(key2), is(-2));
    }

    @Test
    void testNegativeHashCode() {
        IDictionary<Wrapper<String>, String> dict = this.newDictionary();

        Wrapper<String> key1 = new Wrapper<>("foo", -1);
        Wrapper<String> key2 = new Wrapper<>("bar", -100000);
        Wrapper<String> key3 = new Wrapper<>("baz", 1);
        Wrapper<String> key4 = new Wrapper<>("qux", -4);

        dict.put(key1, "val1");
        dict.put(key2, "val2");
        dict.put(key3, "val3");

        assertThat(dict.containsKey(key1), is(true));
        assertThat(dict.containsKey(key2), is(true));
        assertThat(dict.containsKey(key3), is(true));
        assertThat(dict.containsKey(key4), is(false));

        assertThat(dict.get(key1), is("val1"));
        assertThat(dict.get(key2), is("val2"));
        assertThat(dict.get(key3), is("val3"));

        dict.remove(key1);
        assertThat(dict.containsKey(key1), is(false));
    }

    @Test
    void testStress() {
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            int limit = 1000000;
            IDictionary<Integer, Integer> dict = this.newDictionary();

            for (int i = 0; i < limit; i++) {
                dict.put(i, i);
                assertThat(dict.get(i), is(i));
            }

            for (int i = 0; i < limit; i++) {
                assertThat(dict.containsKey(-1), is(false));
            }

            for (int i = 0; i < limit; i++) {
                dict.put(i, -i);
            }

            for (int i = 0; i < limit; i++) {
                assertThat(dict.get(i), is(-i));
                dict.remove(i);
            }
        });
    }
}
