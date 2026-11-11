package behaviour;

import entities.Player;

// helps the players know how they need to move during updates. consider it the strategy interface that Player accesses and uses.

public interface MovementBehaviour {

    void update(Player player, double deltaSeconds);

}
