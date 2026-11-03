package ui;

import java.awt.*;
import entities.Player;
import entities.Ball;
import entities.Team;
import world.Goal;
import world.PlaySpace;
import world.Environment;
import world.GameMap;
import javax.swing.JPanel;

public class SimulationPanel extends JPanel {

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

                g.setColor(Color.WHITE);

                // int width = getWidth();
                // int height = getHeight();

                Goal leftGoal = playSpace.getLeftGoal();
                Goal rightGoal = playSpace.getRightGoal();

                // DRAWS THE FIELD'S CENTRE LINE
                // needs to cast measurements as "(int)" for compatibility
                // g.drawLine(width / 2, 0, width / 2, height);
                g.drawLine(
                                marginX + (int) Math.round(playSpace.getLength() / 2 * scale),
                                marginY,
                                marginX + (int) Math.round(playSpace.getLength() / 2 * scale),
                                marginY + (int) Math.round(playSpace.getWidth() * scale));

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
        }


}