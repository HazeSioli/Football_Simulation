package ui;

import java.awt.Font;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;


public class TextHandler {

    public enum Alignment {     // Alignment/Justification of text.
        LEFT,
        CENTER,
        RIGHT
    }

    public void draw(
            Graphics g,
            String text,
            double x,
            double y,
            double fontSize,
            double scale,
            Color color,
            Alignment alignment) {

        int pixelX = (int) Math.round(x * scale);
        int pixelY = (int) Math.round(y * scale);
        int pixelFontSize = Math.max(
                1, (int) Math.round(fontSize * scale));

        Font previousFont = g.getFont();
        Color previousColor = g.getColor();

        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, pixelFontSize));

        FontMetrics metrics = g.getFontMetrics();

        int textWidth = metrics.stringWidth(text);

        switch (alignment) {
            case CENTER:
                pixelX -= textWidth / 2;
                break;
            case RIGHT:
                pixelX -= textWidth;
                break;
            case LEFT:
                break;
        }

        int baselineY = pixelY + metrics.getAscent();
        int shadowOffset = Math.max(1, (int) Math.round(2.0 * scale));

        g.setColor(new Color(0, 0, 0, 180));
        g.drawString(text, pixelX + shadowOffset, baselineY + shadowOffset);

        g.setColor(color);
        g.drawString(text, pixelX, baselineY);

        g.setFont(previousFont);
        g.setColor(previousColor);

    }

}
