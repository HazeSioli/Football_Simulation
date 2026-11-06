package entities;

public class Ball extends Entity {

    private double radiusCm = 11.0; // chosen ball radius.
    private double velocityX = 0.0;
    private double velocityY = 0.0;
    private double velocityZ = 0.0;
    private Player lastKicker;

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

    public void update(double deltaSeconds) {
        xPos += velocityX * deltaSeconds;
        yPos += velocityY * deltaSeconds;
        zPos += velocityZ * deltaSeconds;
    }

    public void kickTowards(double targetX, double targetY, double speedCmPerSecond, Player kicker) {
        double directionX = targetX - xPos;
        double directionY = targetY - yPos;
        double distance = Math.hypot(directionX, directionY);

        lastKicker = kicker;

        if (distance == 0) {
            velocityX = 0;
            velocityY = 0;
            velocityZ = 0;
            return;
        }

        velocityX = (directionX / distance) * speedCmPerSecond;
        velocityY = (directionY / distance) * speedCmPerSecond;
        velocityZ = 0; // No vertical movement for now, to be added at a later point once it works on
                       // 2d plane.

    }

    public void stop() {
        velocityX = 0;
        velocityY = 0;
        velocityZ = 0;
    }

    public boolean isMoving() {
        return velocityX != 0
                || velocityY != 0
                || velocityZ != 0;
    }

    public void moveTo(double x, double y, double z) {
        xPos = x;
        yPos = y;
        zPos = z;
    }

    public Player getLastKicker() {
        return lastKicker;
    }

}
