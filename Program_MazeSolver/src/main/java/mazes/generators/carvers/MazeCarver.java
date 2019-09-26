package mazes.generators.carvers;

import datastructures.sets.ChainedHashSet;
import datastructures.sets.ISet;
import mazes.entities.Maze;
import mazes.entities.Wall;

/**
 * Represents a class that accepts some initial maze created by a `BaseMazeGenerator`,
 * and returns a list of every single wall that should be removed.
 */
public interface MazeCarver {
    /**
     * Accepts a "maze" where there exists a wall between every single room.
     *
     * Returns a list of every single wall that should be removed to form an
     * actually valid maze.
     */
    ISet<Wall> returnWallsToRemove(Maze maze);

    /**
     * Accepts a "maze" where there exists a wall between every single room,
     * and returns a new maze that contains paths between rooms and is actually solvable.
     */
    default Maze generateMaze(Maze initialMaze) {
        /*
        Note: this method is already implemented for you. You do not need to modify it (though you
        should make sure you understand what it's doing).
        */

        ISet<Wall> toRemove = this.returnWallsToRemove(initialMaze);

        ISet<Wall> newWalls = new ChainedHashSet<>();
        for (Wall wall : initialMaze.getRemovableWalls()) {
            if (!toRemove.contains(wall)) {
                newWalls.add(wall);
            }
        }

        return new Maze(initialMaze.getRooms(), newWalls, initialMaze.getUnremovableWalls());
    }
}
