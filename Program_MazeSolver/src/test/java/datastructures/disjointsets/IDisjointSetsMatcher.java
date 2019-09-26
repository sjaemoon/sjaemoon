package datastructures.disjointsets;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class IDisjointSetsMatcher<T> extends TypeSafeDiagnosingMatcher<IDisjointSets<T>> {
    private final T[] items;
    private final int[] representatives;

    public IDisjointSetsMatcher(T[] items, int[] representatives) {
        if (items.length != representatives.length) {
            throw new IllegalArgumentException("Expected items and representatives arrays do not have equal length.");
        }
        this.items = items;
        this.representatives = representatives;
    }

    @Override
    protected boolean matchesSafely(IDisjointSets<T> disjointSet, Description mismatchDescription) {
        boolean matches = true;
        for (int i = 0; i < this.items.length; i++) {
            T item = this.items[i];
            int expectedRep = this.representatives[i];
            int actualRep = disjointSet.findSet(item);
            if (expectedRep != actualRep) {
                if (!matches) {
                    mismatchDescription.appendText(", ");
                }
                matches = false;
                mismatchDescription.appendValue(item)
                        .appendText(" has representative ").appendValue(actualRep);
            }
        }
        return matches;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("has elements ").appendValue(this.items)
                .appendText(" with representatives ").appendValue(this.representatives);
    }

    public static class PartialIDisjointSetMatcher<T> {
        private final T[] items;

        public PartialIDisjointSetMatcher(T[] items) {
            this.items = items;
        }

        public IDisjointSetsMatcher<T> at(int... representatives) {
            return new IDisjointSetsMatcher<>(this.items, representatives);
        }
    }

    @SafeVarargs
    public static <T> PartialIDisjointSetMatcher<T> hasItems(T... items) {
        return new PartialIDisjointSetMatcher<>(items);
    }
}
