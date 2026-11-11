package entities;

import behaviour.MovementBehaviour;
import world.PlaySpace;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public abstract class Player extends Entity {

    int number;
    Ball ball;
    Team team;
    private Team opposingTeam;

    double spawnX;
    double spawnY;
    double spawnZ;

    private double bodyRadiusCm = 30.0; // value chosen as a reasonable approximation of the average human body radius
                                        // (cm)
    private double reachRadiusCm = 100.0; // value chosen as a reasonable approximation of the average human arm reach
                                          // (cm)
    private final Movement movement;

    private MovementBehaviour movementBehaviour;
    private MovementBehaviour defaultMovementBehaviour;

    private double touchCooldownRemaining = 0.0;
    private double dribbleLeadSpeed = 100.0; // cm/second above the forward running speed.
    private final double touchRecoverySeconds = 0.3; // temporary testing value :)
    private double passCooldownRemaining = 0.0;
    private final double passCooldownSeconds = 3.0;

    // Player Data -- shared between all soccer players.
    // Combines Movement, as well as behaviours and ball interactions.

    Player(int number, double xPos, double yPos, double zPos) {
        this(number, xPos, yPos, zPos, 500.0, 300.0);
    }

    Player(
            int number,
            double xPos,
            double yPos,
            double zPos,
            double maxSpeed,
            double maxAcceleration) {

        super(xPos, yPos, zPos);
        this.number = number;
        this.spawnX = xPos;
        this.spawnY = yPos;
        this.spawnZ = zPos;
        this.movement = new Movement(maxSpeed, maxAcceleration);
    }

    public void setMatchDetails(Ball ball, Team team, Team opposingTeam) {
        this.ball = ball;
        this.team = team;
        this.opposingTeam = opposingTeam;
    }

    public void setMovementBehaviour(MovementBehaviour movementBehaviour) {
        this.movementBehaviour = movementBehaviour;
    }

    // determines default behaviour based on team role (at the moment default while
    // players lack individual stats).
    public void initialiseDefaultBehaviour(
            PlaySpace playSpace, boolean defendingLeft) {
        configureDefaultBehaviour(playSpace, defendingLeft);
        defaultMovementBehaviour = movementBehaviour;
    }

    public void restoreDefaultBehaviour() {
        setMovementBehaviour(defaultMovementBehaviour);
    }

    public MovementBehaviour getMovementBehaviour() {
        return movementBehaviour;
    }

    public void update(double deltaSeconds) {

        if (deltaSeconds <= 0.0) {
            return;
        }

        // kicking/ball interaction cooldown so they don't spam kick every frame they
        // get.
        touchCooldownRemaining = Math.max(0.0, touchCooldownRemaining - deltaSeconds);
        passCooldownRemaining = Math.max(0.0, passCooldownRemaining - deltaSeconds);

        if (movementBehaviour != null) {
            movementBehaviour.update(this, deltaSeconds);
        } else {
            updateMovement(0.0, 0.0, 0.0, deltaSeconds); // slows the player down
        }
    }

    public abstract void configureDefaultBehaviour(
            PlaySpace playSpace, boolean defendingLeft);

    // Player Movement Data

    // sets a target position that the player will try to move towards at their
    // maximum speed.
    public void moveTowards(
            double targetX,
            double targetY,
            double deltaSeconds) {
        double directionX = targetX - xPos;
        double directionY = targetY - yPos;
        double distance = Math.hypot(directionX, directionY);
        double targetAccuracyCm = 50.0; // determines the threshold of accuracy to their desired location (50.0 = 50 cm,
                                        // so within 0.5m accuracy)

        if (distance <= targetAccuracyCm) {
            updateMovement(0.0, 0.0, 0.0, deltaSeconds);
            return;
        }

        double desiredSpeed = movement.getMaxSpeed();

        updateMovement(
                directionX / distance * desiredSpeed,
                directionY / distance * desiredSpeed,
                0.0,
                deltaSeconds);
    }

    // Reduces speed as they approach their target location; akin to Ease Out.
    // Helpful for positioning.
    public void arriveTowards(
            double targetX,
            double targetY,
            double deltaSeconds) {
        double directionX = targetX - xPos;
        double directionY = targetY - yPos;
        double distance = Math.hypot(directionX, directionY);

        if (distance <= 10.0) {
            updateMovement(0.0, 0.0, 0.0, deltaSeconds);
            return;
        }

        double brakingSpeed = Math.sqrt(2.0 * movement.getMaxAcceleration() * Math.max(0.0, distance - 10.0));
        double approachSpeed = distance / 0.5;
        double desiredSpeed = Math.min(movement.getMaxSpeed(), Math.min(brakingSpeed, approachSpeed));

        updateMovement(
                directionX / distance * desiredSpeed,
                directionY / distance * desiredSpeed,
                0.0,
                deltaSeconds);
    }

    public void updateMovement(
            double desiredVelocityX,
            double desiredVelocityY,
            double desiredVelocityZ,
            double deltaSeconds) {
        movement.updateVelocity(desiredVelocityX, desiredVelocityY, desiredVelocityZ, deltaSeconds);
        xPos += movement.getVelocityX() * deltaSeconds;
        yPos += movement.getVelocityY() * deltaSeconds;
        zPos += movement.getVelocityZ() * deltaSeconds;
    }

    // Game Logic, Getters/Setters

    // horizontal reach + verticality for instnaces where the ball is airborne (not
    // yet implemented)
    public boolean canTouch(Ball ball) {
        double horizontalDistance = Math.hypot(
                ball.getXPos() - xPos,
                ball.getYPos() - yPos);
        return horizontalDistance <= reachRadiusCm + ball.getRadiusCm()
                && Math.abs(ball.getZPos() - zPos) <= ball.getRadiusCm();
    }

    // requires the player to be in range of the ball; stops and records the touch
    // of the ball, but affected by Cooldowns/Recovery.
    public boolean tryControlBall() {
        if (ball == null
                || touchCooldownRemaining > 0.0
                || !canTouch(ball)) {
            return false;
        }

        touchCooldownRemaining = touchRecoverySeconds;
        ball.stop();
        ball.recordTouch(this);
        return true;
    }

    public boolean tryKickTowards(
            double targetX,
            double targetY,
            double speedCmPerSecond) {
        if (ball == null
                || touchCooldownRemaining > 0.0
                || !canTouch(ball)) {
            return false;
        }

        touchCooldownRemaining = touchRecoverySeconds;
        ball.kickTowards(targetX, targetY, speedCmPerSecond, this);
        return true;
    }

    // player tries to calc the required power/speed at which the ball must be to
    // reach a target location.
    public boolean tryPassTo(

            Player receiver,
            double rollingDeceleration) {

        if (passCooldownRemaining > 0.0) {
            return false;
        }
        if (ball == null || receiver == null || receiver == this
                || team == null || receiver.team != team) {
            return false;
        }

        if (!Double.isFinite(rollingDeceleration)
                || rollingDeceleration < 0.0) {
            throw new IllegalArgumentException(
                    "Rolling deceleration MUST be finite And Non-Negative!");
        }

        double distance = Math.hypot(
                receiver.getXPos() - ball.getXPos(),
                receiver.getYPos() - ball.getYPos());

        if (distance <= 0.0) {
            return false;
        }

        double arrivalSpeed = 300.00; // current chosen pass arrival speed (in centimeters/s)
        double launchSpeed = Math.sqrt(
                arrivalSpeed * arrivalSpeed
                        + 2.0 * rollingDeceleration * distance);

        boolean passed = tryKickTowards(
                receiver.getXPos(),
                receiver.getYPos(),
                launchSpeed);

        if (passed) {
            passCooldownRemaining = passCooldownSeconds;
        }

        return passed;
    }

    // controls who the teammate they will pass the ball to is if more than 1m away.
    // only selects nearest ELIGIBLE teammate.
    public Optional<Player> choosePassReceiver(
            double targetX,
            double targetY,
            double minimumProgressCm) {

        if (!Double.isFinite(minimumProgressCm)
                || minimumProgressCm < 0.0) {
            throw new IllegalArgumentException("Minimum progress must be finite and non-negative!");
        }

        if (team == null) {
            return Optional.empty();
        }

        // double currentDistance = Math.hypot(
        // targetX - xPos,
        // targetY - yPos);

        // filter is currently disabled somewhat. would change targeting priority based
        // on proximity to attacker.
        return Arrays.stream(team.getPlayers())
                .filter(candidate -> candidate != null)
                .filter(candidate -> candidate != this)
                .filter(candidate -> Math.hypot(
                        candidate.getXPos() - xPos,
                        candidate.getYPos() - yPos) > 100.0)
                // .filter(candidate -> Math.hypot(
                // targetX - candidate.getXPos(),
                // targetY - candidate.getYPos())
                // <= currentDistance - minimumProgressCm)
                .min(Comparator.comparingDouble(candidate -> Math.hypot(
                        candidate.getXPos() - xPos,
                        candidate.getYPos() - yPos)));
    }

    // checks if opponent is close -- if so they may kick ball to teammate for
    // support.
    public boolean isUnderPressure(double distanceCm) {
        if (!Double.isFinite(distanceCm) || distanceCm < 0.0) {
            throw new IllegalArgumentException(
                    "Pressure distance must be both Finite AND non-negative!");
        }

        if (opposingTeam == null) {
            return false;
        }

        return Arrays.stream(opposingTeam.getPlayers())
                .filter(opponent -> opponent != null)
                .anyMatch(opponent -> Math.hypot(
                        opponent.getXPos() - xPos,
                        opponent.getYPos() - yPos) <= distanceCm);
    }

    // ball touch history. important for game rules and logic!
    public boolean wasLastTouchedByOpponent() {
        if (ball == null || opposingTeam == null) {
            return false;
        }

        Player lastToucher = ball.getLastToucher();

        return lastToucher != null
                && Arrays.stream(opposingTeam.getPlayers())
                        .anyMatch(opponent -> opponent == lastToucher);
    }

    // soccer players know how hard to kick a ball along the ground
    // this is a natural thing to soccer players, so it's something they can do.
    public double calculateGroundKickSpeed(
            double distanceCm,
            double rollingDeceleration) {
        if (!Double.isFinite(distanceCm) || distanceCm < 0.0
                || !Double.isFinite(rollingDeceleration)
                || rollingDeceleration <= 0.0) {
            throw new IllegalArgumentException(
                    "Remember! Distance must be Finite and Non-Negative; "
                            + "Rolling Deceleration must be Finite and Positive.");
        }

        return Math.sqrt(2.0 * rollingDeceleration * distanceCm);
    }

    // can recieve the ball.
    public boolean canReceive(Ball ball) {
        double horizontalDistance = Math.hypot(ball.getXPos() - xPos, ball.getYPos() - yPos);

        return horizontalDistance <= bodyRadiusCm + ball.getRadiusCm()
                && Math.abs(ball.getZPos() - zPos) <= ball.getRadiusCm();
    }

    // Has the ball while not moving.
    public boolean hasPossession(Ball ball) {
        return !ball.isMoving()
                && xPos == ball.getXPos()
                && yPos == ball.getYPos()
                && zPos == ball.getZPos();
    }

    public abstract void pass();

    // Spawn point - restores spawn positions to Player. negates velocity.
    public void resetPosition() {
        movement.stop(); // deletes any momentum :)
        touchCooldownRemaining = 0.0;
        xPos = spawnX;
        yPos = spawnY;
        zPos = spawnZ;
    }

    public double getBodyRadiusCm() {
        return bodyRadiusCm;
    }

    public double getReachRadiusCm() {
        return reachRadiusCm;
    }

    public double getVelocityX() {
        return movement.getVelocityX();
    }

    public double getVelocityY() {
        return movement.getVelocityY();
    }

    public double getDribbleLeadSpeed() {
        return dribbleLeadSpeed;
    }

    public Team getOpposingTeam() {
        return opposingTeam;
    }

    public int getNumber() {
        return number;
    }

}
