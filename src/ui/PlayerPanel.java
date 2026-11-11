package ui;

import java.awt.Color;
import java.awt.Graphics;

import entities.Player;

// reuable player data panel. currently tracks shirt number and role. 

public class PlayerPanel {

    public static final double WIDTH = 196.0;
    public static final double HEIGHT = 38.0;

    private final TextHandler textHandler = new TextHandler();

    public void draw(
            Graphics g,
            Player player,
            Color teamColour,
            double x,
            double y,
            double scale) {

        g.setColor(new Color(
                teamColour.getRed(),
                teamColour.getGreen(),
                teamColour.getBlue(),
                45));

        g.fillRect(
                (int) Math.round(x * scale),
                (int) Math.round(y * scale),
                (int) Math.round(WIDTH * scale),
                (int) Math.round(HEIGHT * scale));

        String label = "#" + player.getNumber() + " " + player.getClass().getSimpleName();

        textHandler.draw(
                g,
                label,
                x + 8.0,
                y + 9.0,
                14.0,
                scale,
                Color.WHITE,
                TextHandler.Alignment.LEFT);

    }

}
