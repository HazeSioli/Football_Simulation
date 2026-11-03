package world;

/**
 * Describes the legal playing region and it's goal zones.
 * These boundaries inform rule decisions; they do not physically
 * prevent players or the ball from moving outside of them.
 * 
 * World Axis: X = Field Length, Y = Field Width
 * Z = Height (not used in this simulation at this time) (but hopefully soon!)
 * 
 */

public class PlaySpace {

    private double length;  // Was "width" but changed to "length" to match the field length terminology
    private double width;  // Was "height" but changed to "width" to match the field width terminology

    private Goal leftGoal;
    private Goal rightGoal;

    public PlaySpace(double length, double width) {
        this.length = length;
        this.width = width;

        double goalDepth = 150;
        double goalWidth = 300;

        double goalY = (width - goalWidth) / 2;

        leftGoal = new Goal(
                0,
                goalY,
                goalDepth,
                goalWidth);

        rightGoal = new Goal(
                length - goalDepth,
                goalY,
                goalDepth,
                goalWidth);
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public Goal getLeftGoal() {
        return leftGoal;
    }

    public Goal getRightGoal() {
        return rightGoal;
    }

}