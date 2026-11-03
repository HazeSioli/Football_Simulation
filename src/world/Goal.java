package world;

/**
 * Describes a goal zone within the playing region.
 * Being inside this region alone does not establish a valid score:
 * Entry through the front openings of the goal is required for a score to be valid.
 */

public class Goal {

    private double x;
    private double y;
    private double depth;
    private double width;

    public Goal(double x, double y, double depth, double width) {
        this.x = x;
        this.y = y;
        this.depth = depth;
        this.width = width;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDepth() {
        return depth;
    }

    public double getWidth() {
        return width;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public void setWidth(double width) {
        this.width = width;
    }

}