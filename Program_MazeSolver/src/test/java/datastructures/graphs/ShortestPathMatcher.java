package datastructures.graphs;

import datastructures.lists.IList;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Objects;

public class ShortestPathMatcher<V extends Comparable<V>, E> extends TypeSafeDiagnosingMatcher<Graph<V, E>> {
    private final double cost;
    private final V[] path;

    public ShortestPathMatcher(double cost, V[] path) {
        this.cost = cost;
        this.path = path;
    }

    @Override
    protected boolean matchesSafely(Graph<V, E> graph, Description mismatchDescription) {
        IList<Edge<V, E>> actualPath = graph.findShortestPathBetween(
                this.path[0],
                this.path[this.path.length - 1]);
        double actualCost = 0;
        for (Edge<V, E> edge : actualPath) {
            actualCost += edge.getWeight();
        }
        boolean matches = Math.abs(this.cost - actualCost) < 0.0001 && checkPathVerticesMatch(actualPath, this.path);

        if (!matches) {
            mismatchDescription.appendText("has shortest path from ").appendValue(this.path[0])
                    .appendText(" to ").appendValue(this.path[this.path.length - 1])
                    .appendText(" with cost ").appendValue(actualCost)
                    .appendText(" and ");
            describePath(actualPath, mismatchDescription);
        }

        return matches;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("has shortest path from ").appendValue(this.path[0])
                .appendText(" to ").appendValue(this.path[this.path.length - 1])
                .appendText(" with cost ").appendValue(this.cost)
                .appendText(" and ");

        if (this.path.length < 2) {
            description.appendText("no edges ");
            return;
        }

        if (this.path.length == 2) {
            description.appendText("edge [");
        } else {
            description.appendText("edges [");
        }
        describeEdge(this.path[0], this.path[1], description);
        for (int i = 1; i < this.path.length - 1; i++) {
            description.appendText(", ");
            describeEdge(this.path[i], this.path[i + 1], description);
        }
        description.appendText("]");
    }

    @SafeVarargs
    public static <V extends Comparable<V>, E> ShortestPathMatcher<V, E> hasShortestPath(double cost, V... path) {
        return new ShortestPathMatcher<>(cost, path);
    }

    private static <V, E> boolean checkPathVerticesMatch(IList<Edge<V, E>> path, V[] expectedPath) {
        if (expectedPath.length - 1 != path.size()) {
            return false;
        }

        V curr = expectedPath[0];
        for (int i = 0; i < path.size(); i++) {
            Edge<V, E> edge = path.get(i);

            if (!expectedPath[i].equals(edge.getVertex1()) && !expectedPath[i].equals(edge.getVertex2())) {
                return false;
            }

            V next = edge.getOtherVertex(curr);
            if (!expectedPath[i + 1].equals(next)) {
                return false;
            }

            curr = next;
        }

        return true;
    }

    private static <V extends Comparable<V>, E> void describePath(IList<Edge<V, E>> path, Description description) {
        if (path.isEmpty()) {
            description.appendText("no edges");
            return;
        }
        if (path.size() == 1) {
            description.appendText("edge [");
        } else {
            description.appendText("edges [");
        }
        V prevVertex = null;
        // it's possible to do this iteration with an iterator, but the code is more readable using `get`
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) {
                description.appendText(", ");
            }

            Edge<V, E> curr = path.get(i);
            V v1 = curr.getVertex1();
            V v2 = curr.getVertex2();

            if (v1.equals(prevVertex)) {
                prevVertex = describeEdge(v1, v2, description);
                continue;
            }
            if (v2.equals(prevVertex)) {
                prevVertex = describeEdge(v2, v1, description);
                continue;
            }
            // if `prevVertex` is not in `curr`, check next edge to see if it contains any vertex in `curr`
            if (i < path.size() - 1) {
                Edge<V, E> next = path.get(i+1);
                V n1 = next.getVertex1();
                V n2 = next.getVertex2();
                if (v1.equals(n1) || v1.equals(n2)) {
                    prevVertex = describeEdge(v2, v1, description);
                    continue;
                }
                if (v2.equals(n1) || v2.equals(n2)) {
                    prevVertex = describeEdge(v1, v2, description);
                    continue;
                }
            }
            // if `next` doesn't have anything either, just start with the lesser-valued vertex
            if (v1.compareTo(v2) < 0) {
                prevVertex = describeEdge(v1, v2, description);
            } else {
                prevVertex = describeEdge(v2, v1, description);
            }

        }
        description.appendText("]");
    }

    private static <V> V describeEdge(V v1, V v2, Description description) {
        description.appendText("(");
        description.appendText(Objects.toString(v1));
        description.appendText(",");
        description.appendText(Objects.toString(v2));
        description.appendText(")");
        return v2;
    }
}
