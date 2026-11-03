package entities;

public abstract class Player extends Entity {

    int number;
    Ball ball;
    Team team;

    double spawnX;
    double spawnY;
    double spawnZ;

    private double bodyRadiusCm = 30.0; // value chosen as a reasonable approximation of the average human body radius in centimeters
    private double reachRadiusCm = 100.0; // value chosen as a reasonable approximation of the average human arm reach radius in centimeters

    Player(int number, double xPos, double yPos, double zPos) {
        super(xPos, yPos, zPos);
        this.number = number;
        this.spawnX = xPos;
        this.spawnY = yPos;
        this.spawnZ = zPos;
    }

    public void setMatchDetails(Ball ball, Team team) {
        this.ball = ball;
        this.team = team;
    }

    public void resetPosition() {
        xPos = spawnX;
        yPos = spawnY;
        zPos = spawnZ;
    }

    public boolean hasPossession(Ball ball) {
        return xPos == ball.xPos && yPos == ball.yPos && zPos == ball.zPos;
    }

    public abstract void pass();

    public double getBodyRadiusCm() {
        return bodyRadiusCm;
    }

    public double getReachRadiusCm() {
        return reachRadiusCm;
    }

}