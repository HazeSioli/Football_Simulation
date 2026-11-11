package world;

import java.awt.Color;

// Describes the surfaces' name, its display colour, and teh resistance it has on the rolling of the ball. 
// Deceleration uses cm/s^2.

public class Terrain {

    private String name;
    private Color displayColor;

    private final double rollingDeceleration;

    public Terrain(String name, Color displayColor, double rollingDeceleration) {
        if (!Double.isFinite(rollingDeceleration) || rollingDeceleration < 0.0) {
            throw new IllegalArgumentException(
                    " !! UH OH GAMER !! Rolling deceleration must be a (1) finite, and (2) non-negative value");
        }

        this.name = name;
        this.displayColor = displayColor;
        this.rollingDeceleration = rollingDeceleration;
    }

    public String getName() {
        return name;
    }

    public Color getDisplayColor() {
        return displayColor;
    }

    public double getRollingDeceleration() {
        return rollingDeceleration;
    }

}
