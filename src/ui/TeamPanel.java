package ui;

import java.awt.Color;
import java.awt.Graphics;

import entities.Team;
import entities.Player;

// draws the team's panels, stacked up with rows of players

public class TeamPanel {

    public static final double PADDING = 12.0;
    public static final double HEADER_HEIGHT = 46.0;
    public static final double ROW_GAP = 6.0;
    public static final double BACKING_INSET = 18.0;

    private static final double LOGO_SIZE = 22.0;
    private static final double LOGO_GAP = 8.0;

    public static final double WIDTH = PlayerPanel.WIDTH + PADDING * 2;

    private final Team team;
    private final TextHandler textHandler = new TextHandler();
    private final PlayerPanel playerPanel = new PlayerPanel();

    public TeamPanel(Team team) {
        this.team = team;
    }

    public double getPreferredHeight() {
        int playerCount = team.getPlayers().length;

        return HEADER_HEIGHT + playerCount * PlayerPanel.HEIGHT + Math.max(0, playerCount - 1) * ROW_GAP + PADDING;
    }

    public void draw(
            Graphics g,
            double x,
            double y,
            double scale) {
        Color teamColour = team.getColor();

        g.setColor(new Color(
                teamColour.getRed(),
                teamColour.getGreen(),
                teamColour.getBlue(),
                140));

        g.fillRect(
                (int) Math.round(x * scale),
                (int) Math.round((y + 4.0) * scale),
                (int) Math.round(WIDTH * scale),
                (int) Math.round(PlayerPanel.HEIGHT * scale));

        // TEMP LOGO SLOT:
        g.setColor(teamColour);
        g.fillRect(
                (int) Math.round((x + PADDING + 8.0) * scale),
                (int) Math.round((y + PADDING) * scale),
                (int) Math.round(LOGO_SIZE * scale),
                (int) Math.round(LOGO_SIZE * scale));

        g.setColor(Color.WHITE);
        g.fillRect(
                (int) Math.round((x + PADDING + 8.0) * scale),
                (int) Math.round((y + PADDING) * scale),
                (int) Math.round(LOGO_SIZE * scale),
                (int) Math.round(LOGO_SIZE * scale));


        textHandler.draw(
                g,
                team.getName(),
                x + PADDING + 8.0 + LOGO_SIZE + LOGO_GAP,
                y + PADDING,
                18.0,
                scale,
                Color.WHITE,
                TextHandler.Alignment.LEFT);

        Player[] players = team.getPlayers();

        for (int i = 0; i < players.length; i++) {
            double rowY = y + HEADER_HEIGHT + i * (PlayerPanel.HEIGHT + ROW_GAP);

            playerPanel.draw(g, players[i], team.getColor(), x + 12.0, rowY, scale);
        }
    }

}
