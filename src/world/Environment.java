package world;

import java.awt.Color;

public class Environment {

    private Terrain playingTerrain;
    private Terrain surroundingTerrain;

    public Environment() {
        playingTerrain = new Terrain("Grass", Color.GREEN, 100.0); // 100 is test value; loses 100cm (/1m) of speed per
                                                                   // second of horizontal speed while rolling.
        surroundingTerrain = new Terrain("Surrounding Grass", new Color(0, 110, 0), 100.0);
    }

    public Terrain getPlayingTerrain() {
        return playingTerrain;
    }

    public Terrain getSurroundingTerrain() {
        return surroundingTerrain;
    }

}