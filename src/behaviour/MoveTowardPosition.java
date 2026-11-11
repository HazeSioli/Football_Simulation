package behaviour;

import entities.Player;

// Responsible for storing destination's X/Y coordinates. 
// Actively moves a player towards said location, with moveTowards().
// Z position data is not yet implemented.

public class MoveTowardPosition implements MovementBehaviour {

    private final double targetX;
    private final double targetY;

    public MoveTowardPosition(double targetX, double targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    public void update(Player player, double deltaSeconds) {
        player.moveTowards(targetX, targetY, deltaSeconds);
    }

}
