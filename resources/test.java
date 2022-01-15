package resources;

public static void toJSONString() {
    // JsonObject obj = new JsonObject();

    try {
        try (FileWriter fileWriter = new FileWriter(
                "C:/Users/gusta/Documents/JavaFX_kursus/Videogame_project/resources/scoreboard.json");
                JsonWriter jsonWriter = new JsonWriter(fileWriter)) {
            App.spiller.forEach((p) -> {
                try {
                    jsonWriter.beginObject();
                    jsonWriter.name(p.name).value(p.playerScore.counter);
                    jsonWriter.endObject();
                } catch (IOException e) {
                    System.out.println(e);
                }
            });
        }
        System.out.println("Write JSON file successfully at");
    } catch (IOException e) {
        System.out.print(e);
    }

}