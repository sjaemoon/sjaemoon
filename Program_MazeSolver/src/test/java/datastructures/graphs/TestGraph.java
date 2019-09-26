package datastructures.graphs;

import datastructures.lists.DoubleLinkedList;
import datastructures.lists.IList;
import datastructures.sets.ISet;
import misc.BaseTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static datastructures.graphs.ShortestPathMatcher.hasShortestPath;
import static datastructures.sets.ISetMatcher.setContaining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("project4")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestGraph extends BaseTest {
    /**
     * A dummy edge class to make it easier for us to specify many edges to insert into a `Graph`.
     */
    public static class SimpleEdge<V> extends Edge<V, Void> {
        public SimpleEdge(V vertex1, V vertex2, double weight) {
            super(vertex1, vertex2, weight);
        }
    }

    /**
     * A convenience method for constructing a new `SimpleEdge`, since having to type
     * `new SimpleEdge<>(...)` everywhere would be clunky and annoying.
     */
    public static SimpleEdge<String> edge(String v1, String v2, double weight) {
        return new SimpleEdge<>(v1, v2, weight);
    }

    /**
     * Creates a graph with the given vertices and edges, where the data for each edge is null.
     */
    public static <V> Graph<V, Void> buildGraph(IList<V> vertices, IList<SimpleEdge<V>> edges) {
        Graph<V, Void> graph = new Graph<>();
        for (V vertex : vertices) {
            graph.addVertex(vertex);
        }
        for (SimpleEdge<V> edge : edges) {
            graph.addEdge(edge.getVertex1(), edge.getVertex2(), edge.getWeight());
        }
        return graph;
    }

    /**
     * Creates a graph with the given vertices and edges, where the data for each edge is the
     * integer corresponding to its index in `edges`.
     */
    public static <V> Graph<V, Integer> buildGraphWithData(IList<V> vertices, IList<SimpleEdge<V>> edges) {
        Graph<V, Integer> graph = new Graph<>();
        for (V vertex : vertices) {
            graph.addVertex(vertex);
        }
        int id = 0;
        for (SimpleEdge<V> edge : edges) {
            graph.addEdge(edge.getVertex1(), edge.getVertex2(), edge.getWeight(), id);
            id++;
        }
        return graph;
    }

    /**
     * Returns a simple graph.
     */
    public static Graph<String, Void> buildSimpleGraph() {
        IList<String> vertices = new DoubleLinkedList<>();
        vertices.add("a");
        vertices.add("b");
        vertices.add("c");
        vertices.add("d");

        IList<SimpleEdge<String>> edges = new DoubleLinkedList<>();
        edges.add(edge("a", "b", 2));
        edges.add(edge("a", "c", 3));

        edges.add(edge("c", "d", 1));

        return buildGraph(vertices, edges);
    }

    /**
     * Returns a graph with self-loops and parallel edges.
     */
    public static Graph<String, Void> buildNonSimpleGraph() {
        IList<String> vertices = new DoubleLinkedList<>();
        vertices.add("a");
        vertices.add("b");
        vertices.add("c");
        vertices.add("d");
        vertices.add("e");

        IList<SimpleEdge<String>> edges = new DoubleLinkedList<>();
        edges.add(edge("a", "b", 2));
        edges.add(edge("a", "c", 3));
        edges.add(edge("a", "e", 2));

        edges.add(edge("b", "b", 1)); // self-loop
        edges.add(edge("b", "c", 0));
        edges.add(edge("b", "d", 4));

        edges.add(edge("c", "c", 0)); // self-loop
        edges.add(edge("c", "d", 2)); // parallel edge
        edges.add(edge("c", "d", 1)); // parallel edge
        edges.add(edge("c", "e", 3));

        return buildGraph(vertices, edges);
    }

    /**
     * Returns a graph with disconnected components.
     */
    public static Graph<String, Void> buildDisconnectedGraph() {
        IList<String> vertices = new DoubleLinkedList<>();
        vertices.add("a");
        vertices.add("b");
        vertices.add("c");
        vertices.add("d");
        vertices.add("e");
        vertices.add("f");
        vertices.add("g");

        vertices.add("h");
        vertices.add("i");
        vertices.add("j");
        vertices.add("k");

        IList<SimpleEdge<String>> edges = new DoubleLinkedList<>();
        edges.add(edge("a", "b", 1));
        edges.add(edge("a", "c", 4));
        edges.add(edge("a", "d", 7));
        edges.add(edge("a", "g", 9));

        edges.add(edge("b", "c", 2));

        edges.add(edge("c", "d", 3));
        edges.add(edge("c", "f", 0));

        edges.add(edge("d", "d", 3)); // self-loop
        edges.add(edge("d", "g", 8));

        edges.add(edge("e", "f", 1));
        edges.add(edge("e", "g", 2)); // parallel edge
        edges.add(edge("e", "g", 3)); // parallel edge
        edges.add(edge("e", "g", 3)); // parallel edge

        edges.add(edge("h", "i", 3));
        edges.add(edge("h", "j", 1));
        edges.add(edge("h", "k", 1));

        edges.add(edge("i", "j", 4));
        edges.add(edge("i", "k", 2)); // parallel edge
        edges.add(edge("i", "k", 6)); // parallel edge

        edges.add(edge("j", "k", 3));

        return buildGraph(vertices, edges);
    }

    @Test
    void testSizeMethods() {
        Graph<String, Void> graph1 = buildSimpleGraph();
        assertThat(graph1.numVertices(), is(4));
        assertThat(graph1.numEdges(), is(3));

        Graph<String, Void> graph2 = buildNonSimpleGraph();
        assertThat(graph2.numVertices(), is(5));
        assertThat(graph2.numEdges(), is(10));

        Graph<String, Void> graph3 = buildDisconnectedGraph();
        assertThat(graph3.numVertices(), is(11));
        assertThat(graph3.numEdges(), is(20));
    }

    @Test
    void testFindMstSimple() {
        IList<String> vertices = new DoubleLinkedList<>();
        vertices.add("a");
        vertices.add("b");
        vertices.add("c");
        vertices.add("d");

        IList<SimpleEdge<String>> edges = new DoubleLinkedList<>();
        edges.add(edge("a", "b", 2)); // id: 0
        edges.add(edge("a", "c", 3)); // id: 1

        edges.add(edge("c", "d", 1)); // id: 2

        Graph<String, Integer> graph = buildGraphWithData(vertices, edges);

        ISet<Integer> mst = Edge.edgesToSetOfData(graph.findMinimumSpanningTree());

        assertThat(mst, is(setContaining(0, 1, 2)));
    }

    @Test
    void testFindShortestPathSimple() {
        Graph<String, Void> graph = buildSimpleGraph();

        assertThat(graph, hasShortestPath(2, "a", "b"));
        assertThat(graph, hasShortestPath(2, "b", "a"));
        assertThat(graph, hasShortestPath(4, "a", "c", "d"));
        assertThat(graph, hasShortestPath(4, "d", "c", "a"));
    }

    @Test
    void testFindShortestPathComplex() {
        Graph<String, Void> graph = buildNonSimpleGraph();

        assertThat(graph, hasShortestPath(3, "a", "b", "c", "d"));
        assertThat(graph, hasShortestPath(3, "d", "c", "b", "a"));
        assertThat(graph, hasShortestPath(4, "d", "c", "e"));
        assertThat(graph, hasShortestPath(4, "e", "c", "d"));
        assertThat(graph, hasShortestPath(2, "a", "e"));
        assertThat(graph, hasShortestPath(2, "e", "a"));
    }

    @Test
    void testFindShortestPathSameStartAndEnd() {
        Graph<String, Void> graph = buildNonSimpleGraph();
        assertThat(graph, hasShortestPath(0, "a"));
    }

    @Test
    void testFindShortestPathDisconnectedComponents() {
        Graph<String, Void> graph = buildDisconnectedGraph();

        assertThat(graph, hasShortestPath(6, "a", "b", "c", "f", "e", "g"));
        assertThat(graph, hasShortestPath(2, "i", "k"));

        assertThrows(NoPathExistsException.class, () -> graph.findShortestPathBetween("a", "i"));
        assertThrows(NoPathExistsException.class, () -> graph.findShortestPathBetween("i", "a"));
    }
}
