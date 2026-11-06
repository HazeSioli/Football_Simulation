package behaviour;

import java.util.Optional;

import entities.Ball;
import entities.Player;
import world.GameMap;
import world.Terrain;

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
        if (player.wasLastTouchedByOpponent()) {
            MovementBehaviour defending = new ChallengeForBall(
                    ball,
                    map,
                    targetX,
                    targetY);

            player.setMovementBehaviour(defending);
            defending.update(player, deltaSeconds);
            return;
        }

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

                if (passed) {
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

        dribbling.update(player, deltaSeconds);
    }
}
