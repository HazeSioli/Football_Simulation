package entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import world.PlaySpace;

// Loads the Player's Roles + Their Shirt numbers from a text file.

public class TeamLoader {

    public static Player[] loadTeam(
            String filename,
            PlaySpace playSpace,
            boolean leftSide) throws IOException {

        // Currently requires exactly 6 team members, as follows:
        String[] expectedRoles = {
                "Goalkeeper", "Defender", "Defender",
                "Midfielder", "Midfielder", "Attacker"
        };

        int[] numbers = new int[6];

        // Opens file, automatically closing it, even if the loading attempt does not
        // succeed.
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            for (int i = 0; i < expectedRoles.length; i++) {
                String line = reader.readLine();

                if (line == null) {
                    throw new IllegalArgumentException(
                            "The team file must contain six players.");
                }

                // each line MUST contain a Role and a Number separated by a Comma.
                String[] parts = line.split(",", -1);

                if (parts.length != 2 ||
                        !parts[0].trim().equals(expectedRoles[i])) {
                    throw new IllegalArgumentException(
                            "Invalid player role or format on line " + (i + 1));
                }

                numbers[i] = Integer.parseInt(parts[1].trim());

                // shirt numbers must be bnoth unique (within the team) and positive.
                if (numbers[i] <= 0) {
                    throw new IllegalArgumentException(
                            "Player numbers must be positive.");
                }

                for (int j = 0; j < i; j++) {
                    if (numbers[i] == numbers[j]) {
                        throw new IllegalArgumentException(
                                "Player numbers must be unique within a team.");
                    }
                }
            }

            // rejects any entrants beyond the initial six.
            if (reader.readLine() != null) {
                throw new IllegalArgumentException(
                        "The team file must contain exactly six lines.");
            }
        }

        // creates the default lineup.
        Player[] players;

        if (leftSide) {
            players = TeamComp.createLeftTeam(playSpace);
        } else {
            players = TeamComp.createRightTeam(playSpace);
        }

        for (int i = 0; i < players.length; i++) {
            players[i].number = numbers[i];
        }

        return players;
    }
}