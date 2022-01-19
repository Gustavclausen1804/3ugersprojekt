import com.google.gson.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

class Score {
    int xPos, yPos = 20, counter = 0, id;
    // public static JsonArray ScoreList = new JsonArray();

    static final int size = 30;

    Score(int id) {
        this.id = id;
        this.xPos = App.xRange * id;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Verdana", 20));
        App.spiller.forEach((p) -> {
            if (id == p.id) {
                gc.fillText(p.name + ": " + counter, xPos, yPos);
            }
        });

    }

    // Stores player Scores in JsonObject as name-value pairs. (String, int) input.
    public static JsonObject toJSONString() {

        // Read the scoreboard.json file, if it exsits, otherwise create new JsonArray.
        JsonArray ScoreList = readJsonArray();
        JsonObject PlayerScoreObject = new JsonObject(); // Create an object for each playername and score.
        App.spiller.forEach((p) -> { // Loop thorough all players, and store the score in the PlayerScoreObject.
            PlayerScoreObject.addProperty(p.name, p.playerScore.counter); // add PlayerScoreObject to the JsonArray
        });
        ScoreList.add(PlayerScoreObject);
        // Update the scoreboard.json
        try (FileWriter file = new FileWriter("scoreboard.json")) {
            file.write(ScoreList.toString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return PlayerScoreObject;

    }

    // Reads the JsonFile.
    public static JsonArray readJsonArray() {
        JsonParser parser = new JsonParser();
        Path path = Paths.get("scoreboard.json");
        JsonArray PlayerScoreArray = new JsonArray();

        // If the scoreboard.json file already exsits then read that as the json array
        // instead.
        if (Files.isRegularFile(path)) {
            try {
            } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
                System.out.println("Scoreboard.json file NOT FOUND!");
            }

            // Reduce the array to the last 10 games.
            if (PlayerScoreArray.size() > 10) {
                int tempSize = PlayerScoreArray.size();
                for (int i = 1; i < tempSize - 9; i++) {
                    PlayerScoreArray.remove(i);
                }
            }
            // JsonObject PlayerScoreObject = (JsonObject) obj;
            // PlayerScoreArray = (JsonArray) PlayerScoreObject.getAsJsonArray();
        }
        return PlayerScoreArray;
    }
}