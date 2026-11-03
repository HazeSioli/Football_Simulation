package app;

import java.awt.*;
import java.awt.event.*;

import javax.swing.Timer; // added to try get the ball to Update every n milliseconds

import entities.Ball;
import entities.Player;
import entities.TeamComp;
import entities.Team;
import ui.SimulationPanel;

import world.GameMap;
import world.PlaySpace;
import world.MapPreset;

import java.io.IOException;
import entities.TeamLoader;

public class Main {

        public static void main(String[] args) {

                GameMap map = new GameMap(MapPreset.DEFAULT);

                PlaySpace playSpace = map.getPlaySpace();

                Player[] redPlayers = loadTeamOrDefault("teams/presets/average.txt", playSpace, true);

                Player[] bluePlayers = loadTeamOrDefault("teams/presets/average.txt", playSpace, false);

                Team redTeam = new Team("Red Team", Color.RED, redPlayers);
                Team blueTeam = new Team("Blue Team", Color.BLUE, bluePlayers);

                Ball ball = new Ball(
                                playSpace.getWidth() / 2,
                                playSpace.getHeight() / 2);

                for (Player player : redPlayers) {
                        player.setMatchDetails(ball, redTeam);
                }

                for (Player player : bluePlayers) {
                        player.setMatchDetails(ball, blueTeam);
                }

                

                EventQueue.invokeLater(() -> {
                        Frame frame = new Frame("Football Simulation"); // <<<<< marked for invokeLater
                        SimulationPanel panel = new SimulationPanel( // <<<<< marked for invokeLater
                                        map,
                                        redTeam,
                                        blueTeam,
                                        ball);

                        panel.setPreferredSize(         // <<<<< marked for invokeLater
                                        new Dimension(
                                                        (int) (playSpace.getWidth() + map.getMarginX() * 2),
                                                        (int) (playSpace.getHeight() + map.getMarginY() * 2)));

                        frame.add(panel); // <<<<< marked for invokeLater
                        frame.pack(); // <<<<< marked for invokeLater
                        frame.setVisible(true); // <<<<< marked for invokeLater

                        frame.addWindowListener( // <<<<< marked for invokeLater
                                new WindowAdapter() {
                                        public void windowClosing(WindowEvent we) {
                                                System.exit(0);
                                        }
                                });

                        Timer timer = new Timer(1000, e -> {     // <<<<< marked for invokeLater
                                Player attacker;

                                if (Math.random() < 0.5) {
                                        attacker = redPlayers[5];
                                } else {
                                        attacker = bluePlayers[5];
                                }

                                ball.moveTo(attacker.getXPos(), attacker.getYPos());
                                panel.repaint();
                        });

                        timer.setRepeats(false);                 // <<<<< marked for invokeLater
                        timer.start();                           // <<<<< marked for invokeLater

                        Timer passingTimer = new Timer(2000, e -> {              // <<<<< marked for invokeLater
                                Player ballHolder = null;

                                for (Player player : redPlayers) {               // <<<<< marked for invokeLater
                                        if (player.hasPossession(ball)) {
                                                ballHolder = player;
                                                break;
                                        }
                                }

                                if (ballHolder == null) {                // <<<<< marked for invokeLater
                                        for (Player player : bluePlayers) {
                                                if (player.hasPossession(ball)) {
                                                        ballHolder = player;
                                                        break;
                                                }
                                        }
                                }

                                if (ballHolder != null) {                // <<<<< marked for invokeLater
                                        ballHolder.pass();
                                        panel.repaint();
                                }
                        });

                        passingTimer.start();            // <<<<< marked for invokeLater

                });
        }

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