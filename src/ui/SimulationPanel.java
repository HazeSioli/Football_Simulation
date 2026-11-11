package ui;

import java.awt.*;
import entities.Player;
import entities.Ball;
import entities.Team;
import world.Goal;
import world.PlaySpace;
import world.Environment;
import world.GameMap;
import world.FieldMarkings;
import javax.swing.JPanel;
import simulation.Match;

// responsible for drawing the world itself.
// base unit of measurement is cm, which are then converted relative to the size of the screen.

public class SimulationPanel extends JPanel {

        private GameMap map;

        private Team redTeam;
        private Team blueTeam;
        private Ball ball;
        private final HUD hud;

        public SimulationPanel(
                        GameMap map,
                        Team redTeam,
                        Team blueTeam,
                        Ball ball,
                        Match match) {

                this.map = map;
                this.redTeam = redTeam;
                this.blueTeam = blueTeam;
                this.ball = ball;
                this.hud = new HUD(redTeam, blueTeam, match);
        }

        private void drawPlayers(
                        Graphics g,
                        Player[] players,
                        Color colour,
                        int offsetX,
                        int offsetY,
                        double scale) {

                g.setColor(colour);

                for (Player player : players) {
                        int centerX = offsetX
                                        + (int) Math.round(player.getXPos() * scale);
                        int centerY = offsetY
                                        + (int) Math.round(player.getYPos() * scale);
                        int bodySize = Math.max(
                                        1, (int) Math.round(player.getBodyRadiusCm() * 2 * scale));
                        int reachSize = Math.max(
                                        1, (int) Math.round(player.getReachRadiusCm() * 2 * scale));

                        g.setColor(new Color(
                                        colour.getRed(),
                                        colour.getGreen(),
                                        colour.getBlue(),
                                        51));

                        g.fillOval(
                                        centerX - reachSize / 2,
                                        centerY - reachSize / 2,
                                        reachSize,
                                        reachSize);

                        g.setColor(colour);

                        g.fillOval(
                                        centerX - bodySize / 2,
                                        centerY - bodySize / 2,
                                        bodySize,
                                        bodySize);
                }
        }

        private void drawFieldMarkings(
                        Graphics g,
                        PlaySpace playSpace,
                        int originX,
                        int originY,
                        double scale) {

                FieldMarkings markings = playSpace.getFieldMarkings();

                int fieldLength = (int) Math.round(playSpace.getLength() * scale);
                int fieldWidth = (int) Math.round(playSpace.getWidth() * scale);

                int centreX = originX
                                + (int) Math.round(playSpace.getLength() / 2 * scale);
                int centreY = originY
                                + (int) Math.round(playSpace.getWidth() / 2 * scale);

                g.setColor(Color.WHITE);

                // OUTER BOUNDARY LLINES
                g.drawRect(originX, originY, fieldLength, fieldWidth);

                // HALFWAY LINE
                g.drawLine(centreX, originY, centreX, originY + fieldWidth);

                // CENTRE CIRCLE
                double radius = markings.getCentreCircleRadius();
                int circleX = originX + (int) Math.round((playSpace.getLength() / 2 - radius) * scale);
                int circleY = originY + (int) Math.round((playSpace.getWidth() / 2 - radius) * scale);
                int circleDiameter = (int) Math.round(radius * 2 * scale);

                g.drawOval(circleX, circleY, circleDiameter, circleDiameter);

                // CENTRE DOT
                int dotSize = 3;
                g.fillOval(
                                centreX - dotSize / 2,
                                centreY - dotSize / 2,
                                dotSize,
                                dotSize);

                // PENALTY AREAS
                double penaltyDepth = markings.getPenaltyAreaDepth();
                double leftGoalWidth = playSpace.getLeftGoal().getWidth();

                double penaltyWidth = leftGoalWidth + 2 * penaltyDepth;
                double penaltyY = (playSpace.getWidth() - penaltyWidth) / 2;

                g.drawRect(
                                originX,
                                originY + (int) Math.round(penaltyY * scale),
                                (int) Math.round(penaltyDepth * scale),
                                (int) Math.round(penaltyWidth * scale));

                g.drawRect(
                                originX + (int) Math.round((playSpace.getLength() - penaltyDepth) * scale),
                                originY + (int) Math.round(penaltyY * scale),
                                (int) Math.round(penaltyDepth * scale),
                                (int) Math.round(penaltyWidth * scale));

                // GOAL AREAS
                double goalAreaDepth = markings.getGoalAreaDepth();
                double goalAreaWidth = leftGoalWidth + 2 * goalAreaDepth;
                double goalAreaY = (playSpace.getWidth() - goalAreaWidth) / 2;

                // LEFT GOAL AREA
                g.drawRect(
                                originX,
                                originY + (int) Math.round(goalAreaY * scale),
                                (int) Math.round(goalAreaDepth * scale),
                                (int) Math.round(goalAreaWidth * scale));
                // RIGHT GOAL AREA
                g.drawRect(
                                originX + (int) Math.round((playSpace.getLength() - goalAreaDepth) * scale),
                                originY + (int) Math.round(goalAreaY * scale),
                                (int) Math.round(goalAreaDepth * scale),
                                (int) Math.round(goalAreaWidth * scale));
        }

        @Override
        protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                PlaySpace playSpace = map.getPlaySpace();
                Environment environment = map.getEnvironment();

                double worldLength = playSpace.getLength() + map.getMarginX() * 2;
                double worldWidth = playSpace.getWidth() + map.getMarginY() * 2;

                double scale = Math.min(
                                getWidth() / worldLength,
                                getHeight() / worldWidth);

                double viewOffsetX = (getWidth() - worldLength * scale) / 2;
                double viewOffsetY = (getHeight() - worldWidth * scale) / 2;

                int marginX = (int) Math.round(viewOffsetX + map.getMarginX() * scale);
                int marginY = (int) Math.round(viewOffsetY + map.getMarginY() * scale);

                g.setColor(environment.getSurroundingTerrain().getDisplayColor());
                g.fillRect(
                                0,
                                0,
                                getWidth(),
                                getHeight());

                g.setColor(environment.getPlayingTerrain().getDisplayColor());
                g.fillRect(
                                marginX,
                                marginY,
                                (int) Math.round(playSpace.getLength() * scale),
                                (int) Math.round(playSpace.getWidth() * scale));

                // Draws the markings on the field, such as the halfway line, centre circle, and
                // penalty areas.
                drawFieldMarkings(
                                g,
                                playSpace,
                                marginX,
                                marginY,
                                scale);

                g.setColor(Color.WHITE);

                // int width = getWidth();
                // int height = getHeight();

                Goal leftGoal = playSpace.getLeftGoal();
                Goal rightGoal = playSpace.getRightGoal();

                // DRAWS THE GOALS
                // g.drawRect(0, 175, 150, 300);
                // g.drawRect(width - 150, 175, 150, 300);

                g.drawRect(
                                marginX + (int) Math.round(leftGoal.getX() * scale),
                                marginY + (int) Math.round(leftGoal.getY() * scale),
                                (int) Math.round(leftGoal.getDepth() * scale),
                                (int) Math.round(leftGoal.getWidth() * scale));

                g.drawRect(
                                marginX + (int) Math.round(rightGoal.getX() * scale),
                                marginY + (int) Math.round(rightGoal.getY() * scale),
                                (int) Math.round(rightGoal.getDepth() * scale),
                                (int) Math.round(rightGoal.getWidth() * scale));

                // g.setColor(Color.RED);

                drawPlayers(
                                g,
                                redTeam.getPlayers(),
                                redTeam.getColor(),
                                marginX,
                                marginY,
                                scale);

                drawPlayers(
                                g,
                                blueTeam.getPlayers(),
                                blueTeam.getColor(),
                                marginX,
                                marginY,
                                scale);

                // Ball Draw
                int ballSize = Math.max(
                                1,
                                (int) Math.round(ball.getRadiusCm() * 2 * scale));

                g.setColor(Color.WHITE);
                g.fillOval(
                                marginX + (int) Math.round(ball.getXPos() * scale) - ballSize / 2,
                                marginY + (int) Math.round(ball.getYPos() * scale) - ballSize / 2,
                                ballSize,
                                ballSize);

                g.setColor(Color.BLACK);
                g.drawOval(
                                marginX + (int) Math.round(ball.getXPos() * scale) - ballSize / 2,
                                marginY + (int) Math.round(ball.getYPos() * scale) - ballSize / 2,
                                ballSize,
                                ballSize);


                double leftGoalEdge = marginX + leftGoal.getX() * scale;
                double rightGoalEdge = marginX + (rightGoal.getX() + rightGoal.getDepth()) * scale;

                hud.draw(g, getWidth(), getHeight(), leftGoalEdge, rightGoalEdge);
        }

}