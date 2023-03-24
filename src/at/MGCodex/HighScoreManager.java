package at.MGCodex;

import java.io.*;
import java.nio.file.*;
import java.util.*;

// AI generated and modified
public class HighScoreManager {
    private static final int MAX_HIGHSCORES = 5;
    private static final String HIGHSCORES_FILE = "txt\\highscore.txt";

    public static void addHighscore(int score, String name) {
        ArrayList<HighScore> highScores = getHighscores();

        highScores.add(new HighScore(score, name));
        highScores.sort(Comparator.comparingInt(h -> -h.score));

        if (highScores.size() > MAX_HIGHSCORES) {
            highScores.remove(MAX_HIGHSCORES);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(HIGHSCORES_FILE))) {
            for (HighScore highScore : highScores) {
                writer.write(highScore.name + " " + highScore.score + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error writing high scores: " + e.getMessage());
        }
    }

    public static ArrayList<HighScore> getHighscores() {
        ArrayList<HighScore> highScores = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(HIGHSCORES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String name = parts[0];
                int score = Integer.parseInt(parts[1]);
                highScores.add(new HighScore(score, name));
            }
        } catch (NoSuchFileException e) {
            // File not found, return empty high score list
        } catch (IOException e) {
            System.err.println("Error reading high scores: " + e.getMessage());
        }
        return highScores;
    }
}
