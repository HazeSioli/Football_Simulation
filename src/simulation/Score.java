package simulation;

// stores the values for both teams' goals. increments, resets. 
// is not the detector of goals.

public class Score {

    private int redScore;
    private int blueScore;

    public Score() {
        this.redScore = 0;
        this.blueScore = 0;
    }

    public int getRedScore() {
        return redScore;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public void addRedGoal() {
        redScore++;
    }

    public void addBlueGoal() {
        blueScore++;
    }

    public void reset() {
        redScore = 0;
        blueScore = 0;
    }

    public String toString() {
        return redScore + " - " + blueScore;
    }
}