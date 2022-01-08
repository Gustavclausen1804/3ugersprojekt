import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.HostServices;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

class MapGeneration {
    // Group mapRoot = new Group();

    static int boxSize = 40;
    static int buildColorsAmount = 2; //Different colors of houses
    static int buildAmount = 3; //amount of pictures pr color

    //Creates two arraylists which are used for the generation of houses
    ArrayList<int[]> housesStart = new ArrayList<int[]>(); 
    static ArrayList<ArrayList<int[]>> houses = new ArrayList<>();

    MapGeneration() {

        // Generere tilfældige tal til kortet.
        Random ran = new Random();
        int max_random_house = (App.width / boxSize);

        int widthh = 0;
        int x_coordinate_in_loop = 0;
        for (int i = 0; i < max_random_house; i++) {
            // generating integer
            x_coordinate_in_loop = x_coordinate_in_loop + widthh; // ran.nextInt(App.width / boxSize);
            int y_coordinate = ran.nextInt((App.height / boxSize) - ((App.height / boxSize) / 5)); // blocks from
                                                                                                   // the bottom
                                                                                                   // of the
                                                                                                   // stage.

            int minHeight = (App.height / boxSize) / 5; // blocks from the top of the stage.
            if (y_coordinate < minHeight) {
                y_coordinate = minHeight + ran.nextInt(minHeight - 2);
                System.out.println(y_coordinate);
            }
            
            int maxWidth = 5;
            int minWidth = 3;
            int random = ran.nextInt(maxWidth);
            if (random < minWidth) {
                random = minWidth;
            }
            widthh = random;
            int colorID = ran.nextInt(buildColorsAmount);
            housesStart.add(new int[4]); // adds an array with 4 spaces to the houses start arraylist
            housesStart.get(i)[0] = x_coordinate_in_loop; // represents the x coordinate for the top left of the house
            housesStart.get(i)[1] = y_coordinate; // represents the y coordinate for the top left of the house
            housesStart.get(i)[2] = widthh; // represents the width of the house (number of blocks)
            housesStart.get(i)[3] = colorID;
        }

        GenerateHouse();

        // Byg dine egne huse medfølgende.
        // Generating integer

        // Method to generate house. x_coordinate, y_coordinate, width.
        // GenerateHouse(buttons, nxt, 10, 3);
        // GenerateHouse(buttons, 11, 14, 5);
        // GenerateHouse(buttons, 20, 5, 2);
        // GenerateHouse(buttons, 25, 12, 2);

        // mapRoot.getChildren().add(tilePane);
    }

    // Koordinater af canvas y: 0-17, x: 0-31

    // Metode som genere huset baseret på x-koordinat, y-koordinat og ønsket bredde
    // af huset.
    public void GenerateHouse() {
        Random ran = new Random();
        for (int k = 0; k < housesStart.size(); k++) {
            // creates variables so it is easier to keep track of code
            int x_coordinate = housesStart.get(k)[0];
            int y_coordinate = housesStart.get(k)[1];
            int width = housesStart.get(k)[2];
            int colorID = housesStart.get(k)[3];
            
            

            int max_height_of_house = (App.height / boxSize);

            for (int i = 0; i < width; i++) {
                houses.add(new ArrayList<>()); // adds an arraylist to the which represents each column of the house
                for (int j = 0; j < max_height_of_house; j++) {
                    int imageID =ran.nextInt(buildAmount);

                    houses.get(i).add(new int[] { (x_coordinate + i) * boxSize, (y_coordinate + j) * boxSize, colorID, imageID});   // Adds an array with 4 spaces
                                                                                                                                    //which holds the x- , y-postion
                                                                                                                                    //colorId and ImageId of each block 
                }
            }
        }
    }

    
    Image img11 = new Image("buildings/YellowA.png");
    Image img12 = new Image("buildings/YellowW.png");
    Image img13 = new Image("buildings/YellowWC.png");
    Image img21 = new Image("buildings/blueW.png");
    Image img22 = new Image("buildings/blueWC.png");
    Image img;
  
 

    public void drawMap(GraphicsContext gc) {
       

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, App.width, App.height);
        gc.setFill(Color.BEIGE);
        gc.setStroke(Color.BLACK);

        for (int i = 0; i < houses.size(); i++) {
                        
            for (int j = 0; j < houses.get(i).size(); j++) {
                
                if (houses.get(i).get(j)[2]==1){
                    if (houses.get(i).get(j)[3]==1){
                        img=img11;
                    }else if(houses.get(i).get(j)[3]==2){
                        img=img12;
                    }else{
                        img=img13;
                    }
                     
                }else{
                    if (houses.get(i).get(j)[3]==1){
                        img=img21;
                    }else{
                        img=img22;}
                    
                }
                //gc.fillRect(houses.get(i).get(j)[0], houses.get(i).get(j)[1], boxSize, boxSize);
                gc.drawImage(img, houses.get(i).get(j)[0], houses.get(i).get(j)[1]);
                gc.strokeRect(houses.get(i).get(j)[0], houses.get(i).get(j)[1], boxSize, boxSize);

            }
        }

    }
}
