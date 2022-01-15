import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonTester {
    public static void main(String[] args) {
        String jsonString = "{\"name\":\"Mahesh\", \"age\":21}";

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Player player = gson.fromJson(jsonString, Player.class);
        System.out.println(player);

        jsonString = gson.toJson(player);
        System.out.println(jsonString);
    }
}
