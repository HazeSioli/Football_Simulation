package entities;

/**
 * all positions and Dimensions are measured in centimeters (cm).
 * this is a base class that is shared amongst many entities.
 */

public class Entity {

    double xPos; // Length (left and right)
    double yPos; // Width (up and down)
    double zPos; // Height (/Elevation)

    // sets the spawn position of whatever happens to be using it; player, ball,
    // etc.
    Entity(double xPos, double yPos, double zPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }

    public double getXPos() {
        return xPos;
    }

    public double getYPos() {
        return yPos;
    }

    public double getZPos() {
        return zPos;
    }

}
