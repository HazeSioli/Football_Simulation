package behaviour;

import entities.Ball;
import entities.Player;
import world.Goal;

// helps by keeping a fixed X (left/right) position, tracking the ball along Y (up and down)
// also limits movement to the width of the soccer goal. much like a foosball table.
// can still move beyond this limit if they have momentum in movement.

public class GuardGoal implements MovementBehaviour {

    private final Ball ball;
    private final Goal goal;
    private final double positionX;

    public GuardGoal(Ball ball, Goal goal, double positionX) {
        this.ball = ball;
        this.goal = goal;
        this.positionX = positionX;
    }

    @Override
    public void update(Player player, double deltaSeconds) {
        double targetY = Math.max(
                goal.getY(),
                Math.min(
                        ball.getYPos(),
                        goal.getY() + goal.getWidth()));

        player.arriveTowards(positionX, targetY, deltaSeconds);
    }

}
