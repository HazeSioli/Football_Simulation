package entities;

public class Ball extends Entity {

    public Ball(double xPos, double yPos, double zPos) {
        super(xPos, yPos, zPos);
    }

    void move(double x, double y, double z) {
        xPos += x;
        yPos += y;
        zPos += z;
    }

    public void moveTo(double x, double y, double z) {
        xPos = x;
        yPos = y;
        zPos = z;
    }

}
