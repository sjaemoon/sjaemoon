package datastructures.sets;

import datastructures.dictionaries.NoSuchKeyException;
import datastructures.lists.IList;
import misc.exceptions.NotYetImplementedException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ISetMatcher<T> extends TypeSafeMatcher<ISet<T>> {
    private final T[] items;

    ISetMatcher(T[] items) {
        this.items = items;
        Set<T> itemSet = new HashSet<>(Arrays.asList(items));
        if (this.items.length != itemSet.size()) {
            throw new IllegalArgumentException("Expected values contains duplicates.");
        }
    }

    @Override
    protected boolean matchesSafely(ISet<T> set) {
        if (set.size() != this.items.length) {
            return false;
        }
        try {
            for (T item : this.items) {
                if (!set.contains(item)) {
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
        description.appendText("a set of size ").appendValue(this.items.length)
                .appendText(" containing <").appendText(Arrays.toString(items)).appendText(">");
    }

    @Override protected void describeMismatchSafely(final ISet<T> set, final Description description) {
        description.appendText("was a set of size ").appendValue(set.size()).appendText(" containing ");

        // Use a custom string representation to try to preserve ordering from the expected items
        Set<T> processed = new HashSet<>();

        // First, include expected items in the set
        description.appendText("<[");
        String prefix = "";
        int numRemaining = set.size();
        for (T item : this.items) {
            if (set.contains(item)) {
                description.appendText(prefix);
                description.appendText(Objects.toString(item));
                prefix = ", ";
                processed.add(item);
            }
        }

        // Then, include unexpected items in the actual set, if any (and if the iterator works)
        if (numRemaining > 0) {
            try {
                for (T item : set) {
                    // Include any duplicate items the iterator outputs, in case that happens
                    if (!processed.remove(item)) {
                        description.appendText(prefix);
                        description.appendText(Objects.toString(item));
                        prefix = ", ";
                    }
                }
            } catch (NotYetImplementedException e) {
                description.appendText(String.format("]> and %d more key-value pairs.", numRemaining));
                return;
            }
        }

        description.appendText("]>");
    }

    @SafeVarargs
    public static <T> ISetMatcher<T> setContaining(T... items) {
        return new ISetMatcher<>(items);
    }

    public static <T> ISetMatcher<T> setContaining(IList<T> items) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) new Object[items.size()];
        int i = 0;
        for (T item : items) {
            array[i] = item;
            i++;
        }
        return new ISetMatcher<>(array);
    }
}
