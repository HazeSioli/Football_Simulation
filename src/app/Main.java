package app;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.Timer; // added to try get the ball to Update every n milliseconds

import entities.Ball;
import entities.Player;
import entities.Team;
import entities.TeamComp;
import entities.TeamLoader;

import ui.SimulationPanel;
import simulation.Match;

import world.GameMap;
import world.MapPreset;
import world.PlaySpace;
import world.Terrain;

// Start the game from Main
// "WELCOME TO THE WORLD", as they say.


public class Main {

        public static void main(String[] args) {

                GameMap map = new GameMap(MapPreset.DEFAULT);
                PlaySpace playSpace = map.getPlaySpace();
                Player[] redPlayers = loadTeamOrDefault("teams/presets/average.txt", playSpace, true);
                Player[] bluePlayers = loadTeamOrDefault("teams/presets/average.txt", playSpace, false);
                Team redTeam = new Team("Red Team", Color.RED, redPlayers);
                Team blueTeam = new Team("Blue Team", Color.BLUE, bluePlayers);

                Ball ball = new Ball(
                                playSpace.getLength() / 2,
                                playSpace.getWidth() / 2,
                                0.0);

                Match match = new Match(redTeam, blueTeam, ball);

                for (Player player : redPlayers) {
                        player.setMatchDetails(ball, redTeam, blueTeam);
                        player.initialiseDefaultBehaviour(playSpace, true);
                }

                for (Player player : bluePlayers) {
                        player.setMatchDetails(ball, blueTeam, redTeam);
                        player.initialiseDefaultBehaviour(playSpace, false);
                }

                EventQueue.invokeLater(() -> { // <<<<< ensures that the GUI is created on the Event Dispatch Thread
                        // (EDT)
                        JFrame frame = new JFrame("Football Simulation");
                        SimulationPanel panel = new SimulationPanel(
                                        map,
                                        redTeam,
                                        blueTeam,
                                        ball,
                                        match);

                        panel.setPreferredSize(
                                        new Dimension(1280, 720)); // Set the preferred size of the panel to 1280x720
                        // pixels - this is due to map scaling issues.

                        frame.add(panel);
                        frame.pack();
                        frame.setVisible(true);

                        frame.addWindowListener(
                                        new WindowAdapter() {
                                                public void windowClosing(WindowEvent we) {
                                                        System.exit(0);
                                                }
                                        });

                        SimulationSetup.setupAttackingDemo(
                                        ball, redPlayers, bluePlayers, map);

                        blueTeam.assignChallenger(ball, map, 200.0, playSpace.getWidth() / 2);

                        // EDT; advances players and ball at a fixed rate of 0.02 SIMULATED seconds, per callback. (its requested every 20ms)
                        Timer movementTimer = new Timer(20, e -> {

                                for (Player player : redPlayers) {
                                        player.update(0.02);
                                }

                                for (Player player : bluePlayers) {
                                        player.update(0.02);
                                }

                                Terrain surface;

                                if (playSpace.contains(ball.getXPos(), ball.getYPos())) {
                                        surface = map.getEnvironment().getPlayingTerrain();
                                } else {
                                        surface = map.getEnvironment().getSurroundingTerrain();
                                }

                                ball.update(0.02, surface.getRollingDeceleration());// Update the ball's position every
                                // 20 milliseconds (0.02 seconds)


                                // Entered Goal determines the scoring team.
                                boolean inLeftGoal = SimulationSetup.isBallInGoal(ball, playSpace.getLeftGoal());
                                boolean inRightGoal = SimulationSetup.isBallInGoal(ball, playSpace.getRightGoal());

                                boolean goalScored = inLeftGoal || inRightGoal;

                                if (goalScored) {

                                        Team scoringTeam = inRightGoal ? redTeam : blueTeam;
                                        Player lastTouchedBy = ball.getLastToucher();
                                        boolean ownGoal = lastTouchedBy != null
                                                        && java.util.Arrays.asList(scoringTeam.getPlayers())
                                                                        .contains(lastTouchedBy) == false;

                                        match.recordGoal(
                                                        lastTouchedBy,
                                                        scoringTeam,
                                                        null,
                                                        ownGoal);

                                        blueTeam.releaseChallenger();

                                        SimulationSetup.resetDemo(
                                                        ball, redPlayers, bluePlayers, map);

                                        blueTeam.assignChallenger(
                                                        ball, map, 200.0, playSpace.getWidth() / 2);

                                        System.out.println("Goal! Restarting simulation.");
                                }

                                panel.repaint();
                        });

                        movementTimer.start();

                        match.start();

                });
        }

        // if there are failures in loading/calidating the custom teams, uses default teams instead.
        private static Player[] loadTeamOrDefault(
                        String filename,
                        PlaySpace playSpace,
                        boolean leftSide) {

                try {
                        return TeamLoader.loadTeam(filename, playSpace, leftSide);
                } catch (IOException | IllegalArgumentException e) {
                        System.out.println(
                                        "Could not load " + filename + ": " + e.getMessage());
                        System.out.println("Using the default team.");
                }

                if (leftSide) {
                        return TeamComp.createLeftTeam(playSpace);
                } else {
                        return TeamComp.createRightTeam(playSpace);
                }
        }

}
