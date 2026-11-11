package ui;

import java.awt.Color;
import java.awt.Graphics;

public class MatchPanel {

    private final TextHandler textHandler = new TextHandler();

    public void draw(
            Graphics g,
            int redScore,
            int blueScore,
            double centerX,
            double y,
            double scale) {

        textHandler.draw(
                g,
                redScore + " - " + blueScore,
                centerX,
                y,
                64.0,
                scale,
                Color.WHITE,
                TextHandler.Alignment.CENTER);
    }
}
