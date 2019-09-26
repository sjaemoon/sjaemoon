package datastructures.graphs;

import datastructures.lists.DoubleLinkedList;
import datastructures.lists.IList;
import datastructures.sets.ChainedHashSet;
import datastructures.sets.ISet;

/**
 * Represents a weighted, undirected edge between two vertices.
 *
 * This class is meant to be used internally by `Graph` and returned by some of its methods.
 *
 * @param <V> the type of the vertices
 * @param <E> the type of the additional data contained in the edge
 */
public class Edge<V, E> implements Comparable<Edge<V, E>> {
    private final V vertex1;
    private final V vertex2;
    private final double weight;
    private final E data;

    /**
     * Constructs a new `Edge` between the given vertices with the given weight, and null data.
     */
    protected Edge(V vertex1, V vertex2, double weight) {
        this(vertex1, vertex2, weight, null);
    }

    /**
     * Constructs a new `Edge` between the given vertices, and with the given weight and data.
     */
    protected Edge(V vertex1, V vertex2, double weight, E data) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;
        this.data = data;
    }

    /**
     * A helper method to extract a list of `E` edge data from `Edge` objects.
     */
    public static <V, E> IList<E> edgesToListOfData(Iterable<Edge<V, E>> edges) {
        IList<E> data = new DoubleLinkedList<>();
        for (Edge<V, E> edge : edges) {
            data.add(edge.getData());
        }
        return data;
    }

    /**
     * A helper method to extract a set of `E` edge data from `Edge` objects.
     */
    public static <V, E> ISet<E> edgesToSetOfData(Iterable<Edge<V, E>> edges) {
        ISet<E> data = new ChainedHashSet<>();
        for (Edge<V, E> edge : edges) {
            data.add(edge.getData());
        }
        return data;
    }

    /**
     * Returns one of the vertices in the edge.
     */
    public V getVertex1() {
        return this.vertex1;
    }

    /**
     * Returns the other vertex in the edge.
     */
    public V getVertex2() {
        return this.vertex2;
    }

    /**
     * Returns the numerical weight of this edge.
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * Returns the extra data of this edge.
     */
    public E getData() {
        return this.data;
    }

    /**
     * Given a vertex that is a part of this edge, returns the other vertex.
     *
     * Note: this method exists mainly because when doing graph traversals, it's
     * relatively common to have a vertex and an edge and want to find the other
     * vertex that's in the edge so we can continue traversing.
     *
     * This method lets us do that relatively cleanly.
     *
     * @throws IllegalArgumentException  if `vertex` is null
     * @throws IllegalArgumentException  if `vertex` is not a part of this edge
     */
    public V getOtherVertex(V vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException();
        }

        V v1 = this.vertex1;
        V v2 = this.vertex2;

        if (vertex.equals(v1)) {
            return v2;
        } else if (vertex.equals(v2)) {
            return v1;
        } else {
            throw new IllegalArgumentException("Vertex is not a part of this edge.");
        }
    }

    /**
     * Returns whether `o` is the same instance as `this`.
     *
     * Deliberately uses references, so that multiple edges between the same vertices and with
     * the same weights are necessarily not equal.
     */
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = vertex1.hashCode();
        result = 31 * result + vertex2.hashCode();
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * Compare to another edge based on edge weight.
     *
     * Note that this means that `edge1.compareTo(edge2) == 0` does not imply that
     * `edge1.equals(edge2)`.
     */
    @Override
    public int compareTo(Edge<V, E> other) {
        return Double.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return String.format("Edge(v1=%s, v2=%s, weight=%s, data=%s)",
                this.vertex1, this.vertex2, this.weight, this.data);
    }
}
