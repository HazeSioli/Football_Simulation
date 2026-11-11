package behaviour;

import entities.Player;

// follows teammate at a certian distance.

public class SupportAttack implements MovementBehaviour {

    private final Player teammate;
    private final double offsetX;
    private final double offsetY;

    public SupportAttack(
            Player teammate,
            double offsetX,
            double offsetY) {
        this.teammate = teammate;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public void update(Player player, double deltaSeconds) {
        player.arriveTowards(
                teammate.getXPos() + offsetX,
                teammate.getYPos() + offsetY,
                deltaSeconds);
    }

}
