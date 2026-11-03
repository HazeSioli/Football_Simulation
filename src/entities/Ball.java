package entities;

public class Ball extends Entity {

    private double radiusCm = 11.0; // chosen ball radius.
    
    public Ball(double xPos, double yPos, double zPos) {
        super(xPos, yPos, zPos);
    }

    public double getRadiusCm() {
        return radiusCm;
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
