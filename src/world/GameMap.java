package world;

public class GameMap {

    private PlaySpace playSpace;
    private Environment environment;

    private double marginX;
    private double marginY;

    public GameMap(MapPreset preset) {

        switch (preset) {

            case FIFA_PRO:
                playSpace = new PlaySpace(105.0, 68.0);
                environment = new Environment();
                marginX = 10.0;
                marginY = 10.0;
                break;

            case DEFAULT:
            default:
                playSpace = new PlaySpace(1000.0, 650.0);
                environment = new Environment();
                marginX = 150.0;
                marginY = 100.0;
                break;
        }
    }

    public PlaySpace getPlaySpace() {
        return playSpace;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public double getMarginX() {
        return marginX;
    }

    public double getMarginY() {
        return marginY;
    }

}
