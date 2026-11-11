package ui;

import java.awt.Color;
import java.awt.Graphics;

import entities.Team;
import simulation.Match;

// HUD.
// Arranges all the necessary overlay information, such as score, team members, etc.
// room to expand and include more relevant information as time goes on. 

public class HUD {

    private final TextHandler textHandler = new TextHandler();
    private final MatchPanel matchPanel = new MatchPanel();
    private final Match match;
    private final TeamPanel leftTeamPanel;
    private final TeamPanel rightTeamPanel;

    public HUD(Team leftTeam, Team rightTeam, Match match) {
        this.match = match;
        leftTeamPanel = new TeamPanel(leftTeam);
        rightTeamPanel = new TeamPanel(rightTeam);
    }

    public void draw(Graphics g, int viewWidth, int viewHeight, double leftGoalEdge, double rightGoalEdge) {

        double hudScale = Math.min(
                1.0, Math.min(
                        viewWidth / 1280.0,
                        viewHeight / 720.0));

        if (hudScale <= 0.0) {
            return;
        }

        // int x = (int) Math.round(20.0 * hudScale);
        // int y = (int) Math.round(20.0 * hudScale);

        int topWidth = (int) Math.round(360.0 * hudScale);
        int topHeight = (int) Math.round(80.0 * hudScale);

        int bottomHeight = (int) Math.round(40.0 * hudScale);

        g.setColor(new Color(0, 0, 0, 230));

        // Top-Aligned UI Element
        g.fillRect(
                (viewWidth - topWidth) / 2,
                0,
                topWidth,
                topHeight);

        // Bottom Aligned UI Element.
        g.fillRect(
                0,
                viewHeight - bottomHeight,
                viewWidth,
                bottomHeight);

        double edgeGap = 12.0 * hudScale;

        double availableHeight = viewHeight - topHeight - bottomHeight - edgeGap * 2;

        double goalGap = 8.0 * hudScale;
        double leftAvailableWidth = Math.max(0.0, leftGoalEdge - goalGap);
        double rightAvailableWidth = Math.max(0.0, viewWidth - rightGoalEdge - goalGap);

        if (availableHeight > 0.0) {
            drawTeamPanel(
                    g, leftTeamPanel, false,
                    viewWidth, viewHeight,
                    availableHeight, leftAvailableWidth, hudScale);

            drawTeamPanel(
                    g, rightTeamPanel, true,
                    viewWidth, viewHeight,
                    availableHeight, rightAvailableWidth, hudScale);
        }

        // Text:

        matchPanel.draw(
                g,
                match.getGameState().getRedScore(),
                match.getGameState().getBlueScore(),
                (viewWidth / (2.0 * hudScale)),
                0.0,
                hudScale);
    }

    private void drawTeamPanel(
            Graphics g,
            TeamPanel panel,
            boolean rightSide,
            int viewWidth,
            int viewHeight,
            double availableHeight,
            double availableWidth,
            double hudScale) {

        double preferredHeight = panel.getPreferredHeight();

        double panelScale = Math.min(
                hudScale,
                Math.min(
                        availableHeight / preferredHeight,
                        availableWidth / TeamPanel.WIDTH));

        if (panelScale <= 0.0) {
            return;
        }

        int pixelWidth = (int) Math.round(
                TeamPanel.WIDTH * panelScale);
        int pixelHeight = (int) Math.round(
                preferredHeight * panelScale);

        int pixelX = rightSide ? viewWidth - pixelWidth : 0;
        int pixelY = (viewHeight - pixelHeight) / 2;

        g.setColor(new Color(0, 0, 0, 230));

        int backingInset = (int) Math.round(
                TeamPanel.BACKING_INSET * panelScale);

        int backingX = rightSide
                ? pixelX + backingInset
                : pixelX;

        g.fillRect(
                backingX,
                pixelY,
                Math.max(0, pixelWidth - backingInset),
                pixelHeight);

        panel.draw(
                g,
                pixelX / panelScale,
                pixelY / panelScale,
                panelScale);

    }

}
