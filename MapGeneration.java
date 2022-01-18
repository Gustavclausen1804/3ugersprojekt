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
    static int buildColorsAmount = 4; // Different colors of houses
    static int buildAmount = 4; // amount of pictures pr color

    // Creates two arraylists which are used for the generation of houses
    ArrayList<int[]> housesStart = new ArrayList<int[]>();
    static ArrayList<ArrayList<int[]>> houses = new ArrayList<>();

    MapGeneration() {

        // Generere tilfældige tal til kortet.
        Random ran = new Random();
        int max_random_house = (App.width / boxSize); // Max houses on the map.

        int width_of_house = 0; // width of the house.
        int x_coordinate_in_loop = 0;
        for (int i = 0; i < max_random_house; i++) {
            // generating integer
            x_coordinate_in_loop = x_coordinate_in_loop + width_of_house; // top left X_coordinate of the house.
            int y_coordinate = ran.nextInt((App.height / boxSize) - ((App.height / boxSize) / 5)); // top left
                                                                                                   // y_coordnat.
                                                                                                   // Integer Given in
                                                                                                   // Blocks
                                                                                                   // from
                                                                                                   // the bottom
                                                                                                   // of the
                                                                                                   // stage.

<<<<<<< Updated upstream
            int minHeight = (App.height / boxSize) / 5; // blocks from the top of the stage.
=======
            int minHeight = (App.height / boxSize) / 2; // Integer given in blocks from the top of the stage.
>>>>>>> Stashed changes
            if (y_coordinate < minHeight) {
                y_coordinate = minHeight + ran.nextInt(minHeight - 2);
            }

            int maxWidth = 5;
            int minWidth = 3;
            int random = ran.nextInt(maxWidth);
            if (random < minWidth) {
                random = minWidth;
            }
            width_of_house = random;
            int colorID = ran.nextInt(buildColorsAmount);
            housesStart.add(new int[4]); // adds an array with 4 spaces to the houses start arraylist
            housesStart.get(i)[0] = x_coordinate_in_loop; // represents the x coordinate for the top left of the house
            housesStart.get(i)[1] = y_coordinate; // represents the y coordinate for the top left of the house
            housesStart.get(i)[2] = width_of_house; // represents the width of the house (number of blocks)
            housesStart.get(i)[3] = colorID;
            int antalBlocks = 0;
            for (int j = 0; j < housesStart.size(); j++) {
                antalBlocks += housesStart.get(j)[2];
            }
            if (antalBlocks >= max_random_house) {
                i = max_random_house + 1;
            }
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
<<<<<<< Updated upstream
        System.out.println(housesStart.size());
=======

>>>>>>> Stashed changes
        Random ran = new Random();
        for (int k = 0; k < housesStart.size(); k++) {
            // creates variables so it is easier to keep track of code
            int x_coordinate = housesStart.get(k)[0];
            int y_coordinate = housesStart.get(k)[1];
            int width = housesStart.get(k)[2];
            int colorID = housesStart.get(k)[3];
<<<<<<< Updated upstream
            
            
=======
>>>>>>> Stashed changes

            int max_height_of_house = (App.height / boxSize);

            for (int i = 0; i < width; i++) {
                houses.add(new ArrayList<>()); // adds an arraylist to the which represents each column of the house
                for (int j = 0; j < max_height_of_house; j++) {
                    int imageID = ran.nextInt(buildAmount);

                    houses.get(i).add(
                            new int[] { (x_coordinate + i) * boxSize, (y_coordinate + j) * boxSize, colorID, imageID }); // Adds
                                                                                                                         // an
                                                                                                                         // array
                                                                                                                         // with
                                                                                                                         // 4
                                                                                                                         // spaces
                                                                                                                         // which
                                                                                                                         // holds
                                                                                                                         // the
                                                                                                                         // x-
                                                                                                                         // ,
                                                                                                                         // y-postion
                                                                                                                         // colorId
                                                                                                                         // and
                                                                                                                         // ImageId
                                                                                                                         // of
                                                                                                                         // each
                                                                                                                         // block
                }
            }
        }
<<<<<<< Updated upstream
    }

    
    Image img11 = new Image("buildings/yellowA.png");
    Image img12 = new Image("buildings/yellowW.png");
    Image img13 = new Image("buildings/yellowWC.png");
    Image img14 = new Image("buildings/yellowWL.png");
    Image img21 = new Image("buildings/blueA.png");
    Image img22 = new Image("buildings/blueW.png");
    Image img23 = new Image("buildings/blueWC.png");
    Image img24 = new Image("buildings/blueWL.png");
    Image img31 = new Image("buildings/purpleA.png");
    Image img32 = new Image("buildings/purpleW.png");
    Image img33 = new Image("buildings/purpleWC.png");
    Image img34 = new Image("buildings/purpleWL.png");
    Image img41 = new Image("buildings/greenA.png");
    Image img42 = new Image("buildings/greenW.png");
    Image img43 = new Image("buildings/greenWC.png");
    Image img44 = new Image("buildings/greenWL.png");
    Image img;
  
 

=======
        for (int i = 0; i < buildColorsAmount; i++) {
            for (int j = 0; j < buildAmount; j++) {
                houseImageArray[i][j] = new Image("/buildings/" + i + "" + j + ".png");
            }
        }
    }

>>>>>>> Stashed changes
    public void drawMap(GraphicsContext gc) {

<<<<<<< Updated upstream
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, App.width, App.height);
        gc.setFill(Color.BEIGE);
=======
        gc.drawImage(App.backGroundImage, 0, 0);
>>>>>>> Stashed changes
        gc.setStroke(Color.BLACK);

        for (int i = 0; i < houses.size(); i++) {

            for (int j = 0; j < houses.get(i).size(); j++) {
<<<<<<< Updated upstream
                
                if (houses.get(i).get(j)[2]==1){
                    if (houses.get(i).get(j)[3]==1){
                        img = img11;
                    }else if(houses.get(i).get(j)[3]==2){
                        img = img12;
                    }else if(houses.get(i).get(j)[3]==3){
                        img = img13;
                    }else{
                        img = img14;
                    }
                     
                }else if (houses.get(i).get(j)[2]==2){
                    if (houses.get(i).get(j)[3]==1){
                        img = img21;
                    }else if(houses.get(i).get(j)[3]==2){
                        img = img22;
                    }else if(houses.get(i).get(j)[3]==3){
                        img = img23;
                    }else{
                        img = img24;
                    }

                }else if (houses.get(i).get(j)[2]==3){
                    if (houses.get(i).get(j)[3]==1){
                        img = img31;
                    }else if(houses.get(i).get(j)[3]==2){
                        img = img32;
                    }else if(houses.get(i).get(j)[3]==3){
                        img = img33;
                    }else{
                        img = img34;
                    }

                }else{
                    if (houses.get(i).get(j)[3]==1){
                        img = img41;
                    }else if(houses.get(i).get(j)[3]==2){
                        img = img42;
                    }else if(houses.get(i).get(j)[3]==3){
                        img = img43;
                    }else{
                        img = img44;
                    }
                    
                }
                //gc.fillRect(houses.get(i).get(j)[0], houses.get(i).get(j)[1], boxSize, boxSize);
                gc.drawImage(img, houses.get(i).get(j)[0], houses.get(i).get(j)[1]);
=======
                houseImage = houseImageArray[houses.get(i).get(j)[2]][houses.get(i).get(j)[3]];

                gc.drawImage(houseImage, houses.get(i).get(j)[0], houses.get(i).get(j)[1]);
>>>>>>> Stashed changes
                gc.strokeRect(houses.get(i).get(j)[0], houses.get(i).get(j)[1], boxSize, boxSize);

            }
        }
        mapMove();
    }

    void mapMove() {
        if (App.frameCount % 20 == 0) {
            for (int i = 0; i < houses.size(); i++) {
                for (int j = 0; j < houses.get(i).size(); j++) {
                    if (houses.get(i).get(j)[1] + boxSize < App.height) {
                        if (houses.get(i).get(j)[1] + boxSize != houses.get(i).get(j + 1)[1]) {
                            houses.get(i).get(j)[1] += boxSize;
                        }
                    }
                }
            }
        }
    }

}
