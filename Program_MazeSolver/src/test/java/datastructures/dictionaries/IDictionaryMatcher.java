package datastructures.dictionaries;

import misc.exceptions.NotYetImplementedException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Map;
import java.util.Objects;

public class IDictionaryMatcher<K, V> extends TypeSafeMatcher<IDictionary<K, V>> {
    private final Map<K, V> map;

    IDictionaryMatcher(Map<K, V> map) {
        this.map = map;
    }

    @Override
    protected boolean matchesSafely(IDictionary<K, V> dictionary) {
        if (dictionary.size() != this.map.size()) {
            return false;
        }
        try {
            for (Map.Entry<K, V> entry : this.map.entrySet()) {
                if (!Objects.equals(dictionary.get(entry.getKey()), entry.getValue())) {
                    return false;
                }
            }
        } catch (NoSuchKeyException e) {
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a dictionary containing ").appendValue(this.map);
    }

    @Override protected void describeMismatchSafely(final IDictionary<K, V> dictionary, final Description description) {
        description.appendText("was a dictionary containing ");

        // Use a custom string representation to try to preserve ordering from the expected map

        // First, include elements in the expected map
        description.appendText("<{");
        String prefix = "";
        int numRemaining = dictionary.size();
        for (K key : this.map.keySet()) {
            try {
                V value = dictionary.get(key);
                description.appendText(prefix);
                description.appendText(Objects.toString(key));
                description.appendText("=");
                description.appendText(Objects.toString(value));
                prefix = ", ";
                numRemaining--;
            } catch (NoSuchKeyException e) {
                // key not present; skip
            }
        }

        // Then, include new elements in the actual dictionary, if any (and if the iterator works)
        if (numRemaining > 0) {
            try {
                for (KVPair<K, V> kv : dictionary) {
                    K key = kv.getKey();
                    if (this.map.containsKey(key)) {
                        continue;
                    }
                    description.appendText(prefix);
                    description.appendText(Objects.toString(key));
                    description.appendText("=");
                    description.appendText(Objects.toString(kv.getValue()));
                    prefix = ", ";
                }
            } catch (NotYetImplementedException e) {
                description.appendText(String.format("}> and %d more key-value pairs.", numRemaining));
                return;
            }
        }

        description.appendText("}>");
    }

    public static <K, V> IDictionaryMatcher<K, V> dictContaining(Map<K, V> map) {
        return new IDictionaryMatcher<>(map);
    }
}
