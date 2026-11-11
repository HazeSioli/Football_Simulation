package entities;

import behaviour.HoldFormation;
import world.PlaySpace;

public class Midfielder extends Player {

    public Midfielder(int number, double xPos, double yPos, double zPos) {
        super(number, xPos, yPos, zPos);
    }

    public Midfielder(
            int number,
            double xPos,
            double yPos,
            double zPos,
            double maxSpeed,
            double maxAcceleration) {
        super(number, xPos, yPos, zPos, maxSpeed, maxAcceleration);
    }

    // passes to their teams' attacker
    @Override
    public void pass() {
        if (!hasPossession(ball)) {
            return;
        }

        Player receiver = team.getPlayers()[5];

        ball.kickTowards(
                receiver.getXPos(),
                receiver.getYPos(),
                1000.0,
                this);
    }

    // Sets a target 30% (of the distance between the ball and their spawn point)
    // towards the ball.
    @Override
    public void configureDefaultBehaviour(
            PlaySpace playSpace, boolean defendingLeft) {
        setMovementBehaviour(
                new HoldFormation(
                        ball,
                        spawnX,
                        spawnY,
                        0.30));
    }

}