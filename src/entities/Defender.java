package entities;

public class Defender extends Player {

    public Defender(int number, double xPos, double yPos, double zPos) {
        super(number, xPos, yPos, zPos);
    }

    @Override
    public void pass() {
        if (!hasPossession(ball)) {
            return;
        }

        int index = Math.random() < 0.5 ? 3 : 4;
        Player receiver = team.getPlayers()[index];

        ball.kickTowards(
                receiver.getXPos(),
                receiver.getYPos(),
                1000.0,
                this);
    }

}