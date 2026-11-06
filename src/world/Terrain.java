package world;

import java.awt.Color;

public class Terrain {

    private String name;
    private Color displayColor;

    public Terrain(String name, Color displayColor) {
        this.name = name;
        this.displayColor = displayColor;
    }

    public String getName() {
        return name;
    }

    public Color getDisplayColor() {
        return displayColor;
    }

}
