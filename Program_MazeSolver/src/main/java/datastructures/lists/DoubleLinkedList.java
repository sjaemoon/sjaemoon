package datastructures.lists;
//import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods:
 * @see IList
 * (You should be able to control/command+click "IList" above to open the file from IntelliJ.)
 */
public class DoubleLinkedList<T> implements IList<T> {
    /*
    Warning:
    You may not rename these fields or change their types.
    We will be inspecting these in our secret tests.
    You also may not add any additional fields.

    Note: The fields below intentionally omit the "private" keyword. By leaving off a specific
    access modifier like "public" or "private" they become package-private, which means anything in
    the same package can access them. Since our tests are in the same package, they will be able
    to test these properties directly.
     */
    Node<T> front;
    Node<T> back;
    int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        Node<T> newItem = new Node<>(item);
        if (this.front == null) { // empty case
            this.front = newItem;
            this.back = newItem;
        } else { // general case
            newItem.prev = this.back;
            this.back.next = newItem;
            this.back = newItem;
        }
        this.size++;
    }

    @Override
    public T remove() {
        if (this.front == null) { // front case
            throw new datastructures.EmptyContainerException();
        }
        if (this.front == this.back) {// single element case
            T temp = this.front.data;
            this.front = null;
            this.back = null;
            this.size--;
            return temp;
        }
        // general case
        T temp = this.back.data;
        this.back = this.back.prev;
        this.back.next = null;
        this.size--;
        return temp;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        if (index < this.size / 2) { // middle front case efficient
            Node<T> curr = this.front;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
            return curr.data;
        } else { // middle back case efficient
            Node<T> curr = this.back;
            for (int i = 0; i < this.size - index - 1; i++) {
                curr = curr.prev;
            }
            return curr.data;
        }

    }

    @Override
    public T set(int index, T item) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> newItem = new Node<>(item);
        if (index == 0) { // front case
            T temp = this.front.data;
            newItem.next = this.front.next;
            this.front = newItem;
            if (this.size != 1) { // not single element
                this.front.next.prev = newItem;
            } else { // single element
                this.back = this.front;
            }
            return temp;
        }
        if (index == this.size - 1) { // back case
            T temp = this.back.data;
            newItem.prev = this.back.prev;
            this.back.prev.next = newItem;
            this.back = newItem;
            return temp;
        }
        if (index < this.size / 2) { // middle front case efficient
            Node<T> curr = this.front;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.next;
            }
            T temp = curr.next.data;
            newItem.next = curr.next.next;
            newItem.prev = curr;
            curr.next = newItem;
            curr.next.next.prev = newItem;
            return temp;
        } else { // middle back case efficient
            Node<T> curr = this.back;
            for (int i = 0; i < this.size - index - 2; i++) {
                curr = curr.prev;
            }
            T temp = curr.prev.data;
            newItem.prev = curr.prev.prev;
            newItem.next = curr;
            curr.prev = newItem;
            curr.prev.prev.next = newItem;
            return temp;
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 | index >= this.size + 1) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> newItem = new Node<T>(item);
        if (size == 0) { // empty case
            this.front = newItem;
            this.back = newItem;
        }
        else if (index == 0) { // front case
            newItem.next = this.front;
            this.front.prev = newItem;
            this.front = newItem;

        } else if (index == this.size) { // back case
            newItem.prev = this.back;
            this.back.next = newItem;
            this.back = newItem;
        } else if (index > this.size / 2) { // middle case back efficient
            Node<T> curr = this.back;
            for (int i = 0; i < this.size - index - 2; i++) {
                curr = curr.prev;
            }
            newItem.prev = curr.prev;
            newItem.next = curr;
            curr.prev = newItem;
            curr.prev.prev.next = newItem;
        } else { // middle case front efficient
            Node<T> curr = this.front;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.next;
            }
            newItem.next = curr.next;
            newItem.prev = curr;
            curr.next = newItem;
            curr.next.next.prev = newItem;
        }
        size++;
    }

    @Override
    public T delete(int index) {
        if (index < 0 | index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) { // front case
            T temp = this.front.data;
            this.front = this.front.next;
            if (size == 1) { // front and single element case
                this.back = this.front;
            } else {
                this.front.prev = null;
            }
            size--;
            return temp;
        }
        if (index == this.size - 1) { // back case
            T temp = this.back.data;
            this.back = this.back.prev;
            this.back.next = null;
            size--;
            return temp;
        }
        if (index < this.size / 2) { // middle front case efficient
            Node<T> curr = this.front;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.next;
            }
            T temp = curr.next.data;
            curr.next = curr.next.next;
            curr.next.prev = curr;
            size--;
            return temp;
        } else { // middle back case efficient
            Node<T> curr = this.back;
            for (int i = 0; i < this.size - index - 2; i++) {
                curr = curr.prev;
            }
            T temp = curr.prev.data;
            curr.prev = curr.prev.prev;
            curr.prev.next = curr;
            size--;
            return temp;
        }
    }

    @Override
    public int indexOf(T item) {
        Node<T> curr = this.front;
        if (item == null) { // null case
            for (int i = 0; i < this.size; i++) {
                if (curr.data == null) {
                    return i;
                }
                curr = curr.next;
            }
        } else { // general case
            for (int i = 0; i < this.size; i++) {
                if (curr.data != null && curr.data.equals(item)) {
                    return i;
                }
                curr = curr.next;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        return !(indexOf(other) == -1);
    }

    @Override
    public String toString() {
        //return super.toString();

        /*
        After you've implemented the iterator, comment out the line above and uncomment the line
        below to get a better string representation for objects in assertion errors and in the
        debugger.
        */

        return IList.toString(this);
    }

    @Override
    public Iterator<T> iterator() {
        /*
        Note: we have provided a part of the implementation of an iterator for you. You should
        complete the methods stubs in the DoubleLinkedListIterator inner class at the bottom of
        this file. You do not need to change this method.
        */
        return new DoubleLinkedListIterator<>(this.front);
    }

    static class Node<E> {
        // You may not change the fields in this class or add any new fields.
        final E data;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> next;

        public DoubleLinkedListIterator(Node<T> front) {
            // You do not need to make any changes to this constructor.
            this.next = front;
        }

        /**
         * Returns `true` if the iterator still has elements to look at;
         * returns `false` otherwise.
         */
        public boolean hasNext() {
            return this.next != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T temp = this.next.data;
            this.next = this.next.next;
            return temp;
        }
    }
}
