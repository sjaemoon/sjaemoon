package datastructures.graphs;

import datastructures.lists.IList;
import datastructures.sets.ChainedHashSet;
import datastructures.sets.ISet;
import datastructures.dictionaries.IDictionary;
import datastructures.dictionaries.ChainedHashDictionary;
import datastructures.lists.DoubleLinkedList;
import datastructures.disjointsets.ArrayDisjointSets;
import datastructures.disjointsets.IDisjointSets;
import datastructures.priorityqueues.ArrayHeapPriorityQueue;
import datastructures.priorityqueues.IPriorityQueue;
import datastructures.dictionaries.KVPair;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * @param <V> the type of the vertices
 * @param <E> the type of the additional data contained in edges
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E> {
    /*
    Feel free to add as many fields, private helper methods, and private inner classes as you want.

    And of course, as always, you may also use any of the data structures and algorithms we've
    implemented so far.

    Note: If you plan on adding a new class, please be sure to make it a private static inner class
    contained within this file. Our testing infrastructure works by copying specific files from your
    project to ours, and if you add new files, they won't be copied and your code will not compile.
    */
    IDictionary<V, IList<Edge<V, E>>> graph;
    int numEdges;
    /**
     * Constructs a new empty graph.
     */
    public Graph() {
        this.graph = new ChainedHashDictionary<>();
    }

    /**
     * Adds a vertex to this graph. If the vertex is already in the graph, does nothing.
     */
    public void addVertex(V vertex) {
        if (!this.graph.containsKey(vertex)) {
            this.graph.put(vertex, new DoubleLinkedList<>());
        }
    }

    /**
     * Adds a new edge to the graph, with null data.
     *
     * Every time this method is (successfully) called, a unique edge is added to the graph; even if
     * another edge between the same vertices and with the same weight and data already exists, a
     * new edge will be created and added (where `newEdge.equals(oldEdge)` is false).
     *
     * @throws IllegalArgumentException  if `weight` is negative
     * @throws IllegalArgumentException  if either vertex is not contained in the graph
     */
    public void addEdge(V vertex1, V vertex2, double weight) {
        this.addEdge(vertex1, vertex2, weight, null);
    }

    /**
     * Adds a new edge to the graph with the given data.
     *
     * Every time this method is (successfully) called, a unique edge is added to the graph; even if
     * another edge between the same vertices and with the same weight and data already exists, a
     * new edge will be created and added (where `newEdge.equals(oldEdge)` is false).
     *
     * @throws IllegalArgumentException  if `weight` is negative
     * @throws IllegalArgumentException  if either vertex is not contained in the graph
     */
    public void addEdge(V vertex1, V vertex2, double weight, E data) {
        if (weight < 0 || !this.graph.containsKey(vertex1) || !this.graph.containsKey(vertex2)) {
            throw new IllegalArgumentException();
        }
        Edge<V, E> edge = new Edge<V, E>(vertex1, vertex2, weight, data);
        this.graph.get(vertex1).add(edge);
        this.graph.get(vertex2).add(edge);
        this.numEdges++;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return this.graph.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return this.numEdges;
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of this graph.
     *
     * If there exists multiple valid MSTs, returns any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<Edge<V, E>> findMinimumSpanningTree() {
        ISet<Edge<V, E>> mst = new ChainedHashSet<>();
        IDisjointSets<V> vertices = new ArrayDisjointSets<>();
        IPriorityQueue<Edge<V, E>> edges = new ArrayHeapPriorityQueue<>();
        for (KVPair<V, IList<Edge<V, E>>> item : this.graph) {
            for (Edge<V, E> edge: item.getValue()) {
                if (!edges.contains(edge)) {
                    edges.add(edge);
                }
            }
            vertices.makeSet(item.getKey());
        }
        while (!edges.isEmpty()) {
            Edge<V, E> edge = edges.removeMin();
            V u = edge.getVertex1();
            V v = edge.getVertex2();
            if (vertices.findSet(u) != vertices.findSet(v)) {
                mst.add(edge);
                vertices.union(u, v);
            }
        }
        return mst;
    }

    /**
     * Returns the edges that make up the shortest path from `start` to `end`.
     *
     * The first edge in the output list will be the edge leading out of the `start` node; the last
     * edge in the output list will be the edge connecting to the `end` node.
     *
     * Returns an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from `start` to `end`
     * @throws IllegalArgumentException if `start` or `end` is null or not in the graph
     */
    public IList<Edge<V, E>> findShortestPathBetween(V start, V end) {
        if (start == null || !this.graph.containsKey(start) ||
                end == null || !this.graph.containsKey(end)) {
            throw new IllegalArgumentException();
        }
        IList<Edge<V, E>> shortestPath = new DoubleLinkedList<>();
        if (start == end) {
            return shortestPath;
        }
        IDictionary<V, Vertex<V, E>> vDict = new ChainedHashDictionary<>();
        IPriorityQueue<Vertex<V, E>> mpq = new ArrayHeapPriorityQueue<>();
        // add start vertex
        Vertex<V, E> u = new Vertex<V, E>(start, 0, null, null);
        mpq.add(u);
        vDict.put(start, u);
        // add rest of the vertices
        for (KVPair<V, IList<Edge<V, E>>> item : this.graph) {
            V vertex = item.getKey();
            if (vertex != start) {
                Vertex<V, E> v = new Vertex<V, E>(vertex, Double.POSITIVE_INFINITY, null, null);
                mpq.add(v);
                vDict.put(vertex, v);
            }
        }
        while (!mpq.isEmpty() && u.vertex != end) {
            u = mpq.removeMin();
            for (Edge<V, E> edge : this.graph.get(u.vertex)) { // exit if end vertex is processed
                V otherVertex  = edge.getOtherVertex(u.vertex);
                Vertex<V, E> v = vDict.get(otherVertex);
                if (mpq.contains(v)) {
                    double oldDist = v.dist;
                    double newDist = u.dist + edge.getWeight();
                    if (newDist < oldDist) {
                        Vertex<V, E> newV = new Vertex<V, E>(otherVertex, newDist, u, edge);
                        vDict.put(otherVertex, newV); // update dictionary with new Vertex obj
                        mpq.replace(v, newV); // update mpq with updated dist and predecessor
                    }
                }
            }
        }
        if (vDict.get(end).predecessor == null) { // if end vertex is not found
            throw new NoPathExistsException();
        }
        V prev = end;
        // store the predecessor from the end vertex until the start vertex is found (in reverse order)
        while (prev != start) {
            shortestPath.insert(0, vDict.get(prev).shortestEdge);
            prev = vDict.get(prev).predecessor.vertex;
        }
        return shortestPath;
    }

    private static class Vertex<V, E>
            implements Comparable<Vertex<V, E>> {
        private final V vertex;
        private final double dist;
        private final Vertex<V, E> predecessor;
        private final Edge<V, E> shortestEdge;

        public Vertex(V vertex, double distance, Vertex<V, E> predecessor, Edge<V, E> shortestEdge) {
            this.vertex = vertex;
            this.dist = distance;
            this.predecessor = predecessor;
            this.shortestEdge = shortestEdge;
        }

        public int compareTo(Vertex<V, E> other) {
            double diff = this.dist - other.dist;
            if (diff >= 0) {
                return 1;
            }
            else { // diff < 0
                return 0;
            }
        }
    }
}