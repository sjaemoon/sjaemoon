package mazes.generators.carvers;

import datastructures.graphs.Edge;
import datastructures.graphs.Graph;
import datastructures.sets.ChainedHashSet;

import datastructures.sets.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;

import java.util.Random;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
public class KruskalMazeCarver implements MazeCarver {
    /**
     * A helper method for constructing a new `Graph`.
     */
    protected Graph<Room, Wall> makeGraph() {
        /*
        Do not change this method; it only exists so that we can override it during grading to test
        your code using our correct version of `Graph`.

        Make sure to use this instead of calling the `Graph` constructor yourself; otherwise, you
        may end up losing extra points if your `Graph` does not behave correctly.
         */
        return new Graph<>();
    }

    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        Random rand = new Random();
        Graph<Room, Wall> graph = makeGraph();
        ISet<Wall> toRemove = new ChainedHashSet<>();
        for (Room room : maze.getRooms()) {
            graph.addVertex(room);
        }
        for (Wall wall : maze.getRemovableWalls()) {
            // store wall as edge data
           graph.addEdge(wall.getRoom1(), wall.getRoom2(), rand.nextDouble(), wall); // random wall for random mst
        }
        ISet<Edge<Room, Wall>> validMaze = graph.findMinimumSpanningTree();
        for (Edge<Room, Wall> edge : validMaze) {
            toRemove.add(edge.getData());
        }
        return toRemove;
    }
}
