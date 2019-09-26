package datastructures.lists;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Arrays;

public class IListMatcher<T> extends TypeSafeMatcher<IList<T>> {
    private final T[] items;

    IListMatcher(T[] items) {
        this.items = items;
    }

    @Override
    protected boolean matchesSafely(IList<T> list) {
        int size = list.size();
        if (size != this.items.length) {
            return false;
        }
        Object[] array = new Object[size];
        for (int i = 0; i < size; i++) {
            array[i] = list.get(i);
        }
        return Arrays.equals(array, this.items);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a list containing ").appendText(Arrays.toString(this.items));
    }

    @Override
    protected void describeMismatchSafely(final IList<T> list, final Description description) {
        description.appendText("was a list containing ").appendText(list.toString());
    }

    @SafeVarargs
    public static <T> IListMatcher<T> listContaining(T... items) {
        return new IListMatcher<>(items);
    }

    public static <T> IListMatcher<T> listContaining(IList<T> items) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) new Object[items.size()];
        int i = 0;
        for (T item : items) {
            array[i] = item;
            i++;
        }
        return new IListMatcher<>(array);
    }
}
