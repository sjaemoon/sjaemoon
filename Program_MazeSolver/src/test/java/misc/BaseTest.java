package misc;

import java.util.Objects;

public class BaseTest {
    /**
     * This is a wrapper class for arbitrary objects that additionally allows us to
     * define a custom hash code.
     *
     * If no hash code is provided, the object's existing hash code is used instead.
     *
     * It is up to the user to make sure that the hash codes assigned are valid.
     * (E.g., the user must ensure that two Wrapper objects with equal inner objects
     * also have equal hash codes).
     */
    protected static class Wrapper<T> {
        private T inner;
        private int hashCode;

        public Wrapper(T inner) {
            this(inner, inner == null ? 0 : inner.hashCode());
        }

        public Wrapper(T inner, int hashCode) {
            this.inner = inner;
            this.hashCode = hashCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }

            Wrapper<?> wrapper = (Wrapper<?>) o;

            return Objects.equals(inner, wrapper.inner);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }
    }
}
