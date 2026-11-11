package entities;

import world.PlaySpace;

// creates default teams; 6 players each.

public class TeamComp {

        // creates team on the left of the field.
        public static Player[] createLeftTeam(PlaySpace playSpace) {

                return new Player[] {
                                new Goalkeeper(
                                                1, // Number on the Team.
                                                playSpace.getLength() / 12, // Spawn point (x position)
                                                playSpace.getWidth() / 2, // Spawn point (y position)
                                                0.0), // Z at 0.0 means Grounded.

                                new Defender(
                                                2,
                                                playSpace.getLength() / 5,
                                                playSpace.getWidth() / 3,
                                                0.0),

                                new Defender(
                                                3,
                                                playSpace.getLength() / 5,
                                                (playSpace.getWidth() * 2) / 3,
                                                0.0),

                                new Midfielder(
                                                4,
                                                (playSpace.getLength() * 2) / 5,
                                                playSpace.getWidth() / 5,
                                                0.0),

                                new Midfielder(
                                                5,
                                                (playSpace.getLength() * 2) / 5,
                                                (playSpace.getWidth() * 4) / 5,
                                                0.0),

                                new Attacker(
                                                6,
                                                (playSpace.getLength() * 3) / 5,
                                                playSpace.getWidth() / 2,
                                                0.0)

                };

        }

        // creates team on the right of the field.
        public static Player[] createRightTeam(PlaySpace playSpace) {

                return new Player[] {
                                new Goalkeeper(
                                                1,
                                                (playSpace.getLength() * 11) / 12,
                                                playSpace.getWidth() / 2,
                                                0.0),

                                new Defender(
                                                2,
                                                (playSpace.getLength() * 4) / 5,
                                                playSpace.getWidth() / 3,
                                                0.0,
                                                400.0, // testing values - max speed cm/s
                                                250), // testing values - max acceleration cm/s^2

                                new Defender(
                                                3,
                                                (playSpace.getLength() * 4) / 5,
                                                (playSpace.getWidth() * 2) / 3,
                                                0.0),

                                new Midfielder(
                                                4,
                                                (playSpace.getLength() * 3) / 5,
                                                playSpace.getWidth() / 5,
                                                0.0),

                                new Midfielder(
                                                5,
                                                (playSpace.getLength() * 3) / 5,
                                                (playSpace.getWidth() * 4) / 5,
                                                0.0),

                                new Attacker(
                                                6,
                                                (playSpace.getLength() * 2) / 5,
                                                playSpace.getWidth() / 2,
                                                0.0)

                };

        }

}
