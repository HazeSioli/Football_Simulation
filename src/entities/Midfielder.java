package entities;

public class Midfielder extends Player {

    public Midfielder(int number, double xPos, double yPos, double zPos) {
        super(number, xPos, yPos, zPos);
    }

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

}