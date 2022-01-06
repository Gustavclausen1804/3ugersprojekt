import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.HostServices;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

 class MapGeneration{
    //Group mapRoot = new Group();
    
    TilePane tilePane = new TilePane();
    int boxSize = 40;
    
    ArrayList<int[]> housesStart = new ArrayList<int[]>();
    ArrayList<ArrayList<int[]>> houses = new ArrayList<>();

    MapGeneration() {

        // Generere tilfældige tal til kortet.
        Random ran = new Random();
        int max_random_house = ran.nextInt(15);

        for (int i = 0; i < max_random_house; i++) {
            // generating integer
            int x_coordinate = ran.nextInt(26);
            int y_coordinate = ran.nextInt(12);
            int widthh = ran.nextInt(5)+2;
            housesStart.add(new int[3]);
            housesStart.get(i)[0] = x_coordinate;
            housesStart.get(i)[1] = y_coordinate;
            housesStart.get(i)[2] = widthh;
        }
        
        GenerateHouse();

        // Byg dine egne huse medfølgende.
        // Generating integer

        // Method to generate house. x_coordinate, y_coordinate, width.
        // GenerateHouse(buttons, nxt, 10, 3);
        // GenerateHouse(buttons, 11, 14, 5);
        // GenerateHouse(buttons, 20, 5, 2);
        // GenerateHouse(buttons, 25, 12, 2);

         //mapRoot.getChildren().add(tilePane);
    }

    // Koordinater af canvas y: 0-17, x: 0-31

    // Metode som genere huset baseret på x-koordinat, y-koordinat og ønsket bredde
    // af huset.
    public void GenerateHouse() {
        for(int k = 0; k < housesStart.size(); k++){
            int x_coordinate = housesStart.get(k)[0];
            int y_coordinate = housesStart.get(k)[1];;
            int width = housesStart.get(k)[2];
            System.out.println(x_coordinate + " " + y_coordinate + " " + width);

            int max_height_of_house = 18;

            for(int i = 0; i < width; i++){
                houses.add(new ArrayList<>());
                for (int j = 0; j < max_height_of_house; j++) {
                    houses.get(i).add(new int[]{(x_coordinate+i)*boxSize,(y_coordinate+j)*boxSize});
                }
            }
        }  
    }

    public void drawMap(GraphicsContext gc){
        
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,App.width,App.height);
        gc.setFill(Color.BEIGE);
        gc.setStroke(Color.BLACK);

        for(int i = 0; i < houses.size(); i++){
            for(int j = 0; j < houses.get(i).size(); j++){
                gc.fillRect(houses.get(i).get(j)[0],houses.get(i).get(j)[1],boxSize,boxSize);

            }
        }
        

            
    }
}