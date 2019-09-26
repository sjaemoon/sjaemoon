package mazes.entities;

/**
 * Represents a wall between two rooms.
 *
 * A `Wall` object also serves as an "edge" of sorts between rooms.
 */
public class Wall {
    private final Room room1;
    private final Room room2;
    private final LineSegment dividingLine;

    /**
     * Constructs a wall between the two given rooms, and automatically constructs
     * the distance.
     */
    public Wall(Room room1, Room room2, LineSegment dividingLine) {
        this.room1 = room1;
        this.room2 = room2;
        this.dividingLine = dividingLine;
    }

    /**
     * Returns a pointer to the first room.
     */
    public Room getRoom1() {
        return this.room1;
    }

    /**
     * Returns a pointer to the other room.
     */
    public Room getRoom2() {
        return this.room2;
    }

    /**
     * Returns the physical line dividing the two rooms.
     */
    public LineSegment getDividingLine() {
        return this.dividingLine;
    }

    /**
     * Returns the distance between two rooms.
     */
    public double getDistance() {
        return this.room1.getCenter().distance(this.room2.getCenter());
    }

    /**
     * We compare walls by reference. We deliberately DO NOT include the distance in equals or hashCode,
     * since KruskalMazeCarver involves randomizing the weights of the walls before finding an MST,
     * which could cause inconsistent behavior for walls that were put into maps or sets.
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int result;
        result = room1.hashCode();
        result = 31 * result + room2.hashCode();
        result = 31 * result + dividingLine.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("Wall(room1=%s, room2=%s)", this.room1, this.room2);
    }
}
