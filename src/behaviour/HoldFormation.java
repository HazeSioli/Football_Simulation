package behaviour;

import entities.Ball;
import entities.Player;

// helps entities, usually players, understand relation to the ball as an anchor (often spawn point but not always!)

public class HoldFormation implements MovementBehaviour {

    private final Ball ball;
    private final double anchorX;
    private final double anchorY;
    private final double ballInfluence;

    public HoldFormation(
            Ball ball,
            double anchorX,
            double anchorY,
            double ballInfluence) {
        this.ball = ball;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.ballInfluence = ballInfluence; // ballInfluence is a percentage/fraction from 1.00 to 0.00.
    }

    @Override
    public void update(Player player, double deltaSeconds) {
        double targetX = anchorX
                + (ball.getXPos() - anchorX) * ballInfluence;
        double targetY = anchorY
                + (ball.getYPos() - anchorY) * ballInfluence;

        player.arriveTowards(targetX, targetY, deltaSeconds);
    }

}
