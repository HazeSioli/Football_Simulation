package entities;

public class Goalkeeper extends Player {

    public Goalkeeper(int number, double xPos, double yPos, double zPos) {
        super(number, xPos, yPos, zPos);
    }

    @Override
    public void pass() {
        if (!hasPossession(ball)) {
            return;
        }

        int index = Math.random() < 0.5 ? 1 : 2;
        Player receiver = team.getPlayers()[index];

        // ball.moveTo(receiver.getXPos(), receiver.getYPos(), receiver.getZPos());
        
        ball.kickTowards(
                receiver.getXPos(),
                receiver.getYPos(),
                1000.0,
                this);
    }

}