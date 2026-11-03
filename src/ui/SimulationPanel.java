package ui;

import java.awt.*;
import entities.Player;
import entities.Ball;
import entities.Team;
import world.Goal;
import world.PlaySpace;
import world.Environment;
import world.GameMap;

public class SimulationPanel extends Panel {

        private GameMap map;

        private Team redTeam;
        private Team blueTeam;
        private Ball ball;

        public SimulationPanel(
                        GameMap map,
                        Team redTeam,
                        Team blueTeam,
                        Ball ball) {

                this.map = map;
                this.redTeam = redTeam;
                this.blueTeam = blueTeam;
                this.ball = ball;
        }

        private void drawPlayers(
                        Graphics g,
                        Player[] players,
                        Color colour,
                        int offsetX,
                        int offsetY) {

                int playerSize = 24;

                g.setColor(colour);

                for (Player player : players) {
                        g.fillOval(
                                        offsetX + (int) player.getXPos() - playerSize / 2,
                                        offsetY + (int) player.getYPos() - playerSize / 2,
                                        playerSize,
                                        playerSize);
                }
        }

        public void paint(Graphics g) {

                PlaySpace playSpace = map.getPlaySpace();
                Environment environment = map.getEnvironment();

                int marginX = (int) map.getMarginX();
                int marginY = (int) map.getMarginY();

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
                                (int) playSpace.getLength(),
                                (int) playSpace.getWidth());

                g.setColor(Color.WHITE);

                // int width = getWidth();
                // int height = getHeight();

                Goal leftGoal = playSpace.getLeftGoal();
                Goal rightGoal = playSpace.getRightGoal();

                // DRAWS THE FIELD'S CENTRE LINE
                // needs to cast measurements as "(int)" for compatibility
                // g.drawLine(width / 2, 0, width / 2, height);
                g.drawLine(
                                marginX + (int) playSpace.getLength() / 2,
                                marginY + 0,
                                marginX + (int) playSpace.getLength() / 2,
                                marginY + (int) playSpace.getWidth());

                // DRAWS THE GOALS
                // g.drawRect(0, 175, 150, 300);
                // g.drawRect(width - 150, 175, 150, 300);

                g.drawRect(
                                marginX + (int) leftGoal.getX(),
                                marginY + (int) leftGoal.getY(),
                                (int) leftGoal.getDepth(),
                                (int) leftGoal.getWidth());

                g.drawRect(
                                marginX + (int) rightGoal.getX(),
                                marginY + (int) rightGoal.getY(),
                                (int) rightGoal.getDepth(),
                                (int) rightGoal.getWidth());

                // g.setColor(Color.RED);

                drawPlayers(
                                g,
                                redTeam.getPlayers(),
                                redTeam.getColor(),
                                marginX,
                                marginY);

                drawPlayers(
                                g,
                                blueTeam.getPlayers(),
                                blueTeam.getColor(),
                                marginX,
                                marginY);

                // Ball Draw
                int ballSize = 12;

                g.setColor(Color.WHITE);
                g.fillOval(
                                marginX + (int) ball.getXPos() - ballSize / 2,
                                marginY + (int) ball.getYPos() - ballSize / 2,
                                ballSize,
                                ballSize);

                g.setColor(Color.BLACK);
                g.drawOval(
                                marginX + (int) ball.getXPos() - ballSize / 2,
                                marginY + (int) ball.getYPos() - ballSize / 2,
                                ballSize,
                                ballSize);
        }
}