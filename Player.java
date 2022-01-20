import java.util.ArrayList;
import java.util.Map;

import javax.swing.SwingUtilities;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.text.Font;
import java.util.Random;

class Player extends App {

    Shot playerShot;

    Score playerScore;

    int xPos, yPos;
    boolean parameterChosen = false;

    Group playerRoot = new Group();
    CustomButton btn = new CustomButton("Shoot");

    String name;
    final int size = 30;
    int id;
    public boolean shootsFired;
    double shootingForce, shootingAngle;
    double[] forceAndAngle;

    int animationTimer = 0;
    int currentImage = 0;

    public Player(int id, String name) {
        this.xPos = App.xRange * id;
        this.id = id;
        this.name = name;

        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btn.setLayoutX(640);
        btn.setLayoutY(140);
        btn.setVisible(false);

        
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                // starts gameloop
                try {
                    if (parameterChosen) {
                        shootsFired = true;
                        shoot(shootingAngle, shootingForce);
                        btn.setVisible(false);
                        parameterChosen = false;
                    }
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        playerRoot.getChildren().add(btn);

        playerScore = new Score(id);

    }

    public void draw(GraphicsContext gc) {

        // Draws the players
        gc.drawImage(App.playerImage.get(id%4)[currentImage], xPos,
        yPos);
        if (animationTimer % 10 == 0 && currentImage < App.playerImage.get(id%4).length) {
            currentImage++;
        }
        if (currentImage == App.playerImage.get(id%4).length - 1) {
            currentImage = 0;
        }
        animationTimer++;



        if(!App.gameEnded){
            if (id == App.turn) {
                textDisplay(gc);
            }
        }

        playerScore.draw(gc);
    }

    public void startLocation() {
        this.xPos = App.xRange * id;

    }

    public void move() {
        if (frameCount % 20 == 0) {
            for (int i = 0; i < MapGeneration.houses.size(); i++) { // Loops through the column of blocks
                for (int j = 0; j < MapGeneration.houses.get(i).size(); j++) {
                    int mapX = MapGeneration.houses.get(i).get(j)[0],
                            mapY = MapGeneration.houses.get(i).get(j)[0],
                            mapSize = MapGeneration.boxSize;
                    if (xPos > mapX && xPos < mapX + mapSize) {
                        xPos = mapX;
                    }
                    int topbuilding = App.height;
                    if (xPos == mapX) {
                        for (int k = 0; k < MapGeneration.houses.get(i).size(); k++) {
                            if (MapGeneration.houses.get(i).get(k)[0] == mapX) {
                                if (MapGeneration.houses.get(i).get(k)[1] < topbuilding) {
                                    topbuilding = MapGeneration.houses.get(i).get(k)[1];
                                }
                            }
                        }
                        yPos = topbuilding - size;
                    }
                }
            }
        }
    }

    public void shoot(double angle, double force) {
            this.playerShot = new Shot(xPos+size/2, yPos+size/2, true, true, id);   //Create a shot object
            playerShot.applyForce(angle, force);                                    //Apply a force onto the shot
    }

<<<<<<< Updated upstream
=======
    public void shoot(){    //Function which gets overriden by Enemy-subclass and makes App.java stable.
    }

>>>>>>> Stashed changes
    public boolean playerRemoveShot() {
        if (this.playerShot.removeShotFlag) {       //Check the state of the shot's remove-flag
            this.playerShot = null;                 //Set the pointer for the shot to nothing
            return true;                            //Return upon removing
        }
        return false;                               //Default return false
    }

    void textDisplay(GraphicsContext gc) {
        // if it is the players turn, text is shown asking for the angle of their shot
        // and then the force
        if (!parameterChosen) {
            forceAndAngle = App.getForcesFromMouse(new double[] { xPos + size / 2, yPos + size / 2 },
                    new double[] { App.MouseX, App.MouseY });
        }
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Verdana", 15));
    
        // PointerInfo a = MouseInfo.getPointerInfo();
        // Point point = new Point(a.getLocation());
        // SwingUtilities.convertPointFromScreen(point, e.getComponent());
        // System.out.println(point.x + "," + point.y);

        // Draws the arrow which show the angle of the shot
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);

        double angle = Math.toRadians(forceAndAngle[1]);

        double arrowLength = 20.;
        gc.strokeLine(xPos + size / 2, yPos + size / 2, (xPos + (size / 2)) + Math.cos(angle) * arrowLength,
                (yPos + (size / 2)) + Math.sin(angle) * arrowLength * (-1));

        gc.fillText(name + ": angle " + (int) forceAndAngle[1], 640, 100);
        gc.fillText(name + ": Force " + round(forceAndAngle[0], 2), 640, 120);
        if (parameterChosen) {
            gc.setFont(Font.font("Verdana", 10));
            gc.fillText("To go back press Any Key", 640, 210);

        }

    }


    public static double round(double value, int places) {

        java.math.BigDecimal decimal = java.math.BigDecimal.valueOf(value);
        decimal = decimal.setScale(places, java.math.RoundingMode.HALF_UP);

        return decimal.doubleValue();
    }
    






}
