package behaviour;

import java.util.Optional;

import entities.Ball;
import entities.Player;
import world.GameMap;
import world.Terrain;
import world.Goal;

// allows coordination of attacking decisons by the players.
// whether that is to shoot at the goal when near, to pass to ally when pressured, or to just run it down.

public class AttackWithBall implements MovementBehaviour {

    private final double targetX;
    private final double targetY;
    private final Ball ball;
    private final GameMap map;
    private final DribbleTowardsPosition dribbling;

    public AttackWithBall(
            Ball ball,
            GameMap map,
            double targetX,
            double targetY) {
        this.ball = ball;
        this.map = map;
        this.dribbling = new DribbleTowardsPosition(ball, targetX, targetY, map);
        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    public void update(Player player, double deltaSeconds) {
        if (player.wasLastTouchedByOpponent()) { // if an OPPONENT touched the ball last, then immediately change
                                                 // behaviour to CHALLENGING.
            MovementBehaviour defending = new ChallengeForBall(
                    ball,
                    map,
                    targetX,
                    targetY);

            player.setMovementBehaviour(defending);
            defending.update(player, deltaSeconds);
            return;
        }

        // Choose the goal in our attacking direction.
        boolean attackingRight = targetX > map.getPlaySpace().getLength() / 2;

        Goal goal = attackingRight
                ? map.getPlaySpace().getRightGoal()
                : map.getPlaySpace().getLeftGoal();

        // Aim inside the centre of the goal.
        double goalX = goal.getX() + goal.getDepth() / 2;
        double goalY = goal.getY() + goal.getWidth() / 2;

        double distanceToGoal = Math.hypot(
                goalX - ball.getXPos(),
                goalY - ball.getYPos());

        // attempts to make the shot IF within 1800cm of the goal's center, if the
        // player can reach/touch the ball
        if (distanceToGoal <= 1800.0 && player.canTouch(ball)) {
            boolean shot = player.tryKickTowards(
                    goalX,
                    goalY,
                    1500.0); // current shot speed!

            if (shot) {
                player.restoreDefaultBehaviour();
                player.updateMovement(0.0, 0.0, 0.0, deltaSeconds);
                return;
            }
        }

        // if opponent is within 500cm / 5m, considers passing if the active player has
        // the ball/can reach it.
        if (player.canTouch(ball) && player.isUnderPressure(500.0)) {
            Optional<Player> choice = player.choosePassReceiver(
                    targetX,
                    targetY,
                    200.0);

            if (choice.isPresent()) {
                Player receiver = choice.get();
                Terrain surface;

                if (map.getPlaySpace().contains(
                        ball.getXPos(), ball.getYPos())) {
                    surface = map.getEnvironment().getPlayingTerrain();
                } else {
                    surface = map.getEnvironment().getSurroundingTerrain();
                }

                boolean passed = player.tryPassTo(
                        receiver, surface.getRollingDeceleration());

                if (passed) { // the receievr approaches the pass, attacking. ball passer switches to their
                              // new anchor position.
                    receiver.setMovementBehaviour(
                            new ReceiveBall(
                                    ball,
                                    player,
                                    new AttackWithBall(ball, map, targetX, targetY)));

                    player.setMovementBehaviour(
                            new HoldFormation(
                                    ball,
                                    player.getXPos(),
                                    player.getYPos(),
                                    0.2));

                    player.updateMovement(0.0, 0.0, 0.0, deltaSeconds);
                    return;
                }
            }
        }

        // behaviour to fallback on, not the default behaviour of a player.
        dribbling.update(player, deltaSeconds);
    }
}
