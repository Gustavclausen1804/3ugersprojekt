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

class Player extends App {

    Shot playerShot;

    int xPos, yPos;
    ArrayList<Shot> skud = new ArrayList<Shot>();
    boolean myTurn, angleChosen, ForceChosen;

    Group playerRoot = new Group();
    CustomButton btn = new CustomButton("Shoot");


    String name;
    final int size = 30;
    int id;
    public boolean shootsFired;
    double shootingForce, shootingAngle;

    int score = 0;

    public Player(int yPos, int id, String name) {
        this.xPos = App.xRange * id;
        this.yPos = yPos;
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
                    if(parameterChosen){
                        shoot();
                        btn.setVisible(false);
                    }
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        playerRoot.getChildren().add(btn);

    }

    public void draw(GraphicsContext gc) {

        // Draws the players
        gc.setFill(Color.BLACK);
        gc.fillRect(xPos, yPos, size, size);
        textDisplay(gc);
    }
    public void startLocation() {
        this.xPos = App.xRange*id;

    }

    public void move(){
        if(frameCount % 20 == 0){
            for(int i = 0; i < MapGeneration.houses.size();i++){ // Loops through the column of blocks
                for(int j = 0; j < MapGeneration.houses.get(i).size(); j++){
                    int mapX =MapGeneration.houses.get(i).get(j)[0], 
                    mapY = MapGeneration.houses.get(i).get(j)[0],
                    mapSize = MapGeneration.boxSize;
                    if(xPos > mapX && xPos < mapX+ mapSize){
                        xPos =mapX;
                    }
                    int topbuilding = App.height;
                    if(xPos == mapX){
                        for(int k = 0; k < MapGeneration.houses.get(i).size(); k++){
                            if(MapGeneration.houses.get(i).get(k)[0] == mapX){
                                if(MapGeneration.houses.get(i).get(k)[1]<topbuilding){
                                    topbuilding = MapGeneration.houses.get(i).get(k)[1];
                                }
                            }
                        }
                        yPos = topbuilding-size;
                    }
                }
            }
        }
    }

    public void shoot(double angle, double force) {
            Double sizeD = Math.sqrt(Math.pow(size / 2, 2) + Math.pow(size / 2, 2));
            Double shootingAngleRadian = Math.toRadians(angle);
            // skud.add(new Shot(xPos + (size / 2) + (sizeD * Math.cos(shootingAngleRadian)), yPos + (size / 2) + (sizeD * Math.sin(shootingAngleRadian) * (-1)), true, false));
            this.playerShot = new Shot(xPos+20, yPos+2, true, true);
            // this.playerShot = new Shot(xPos + (size / 2) + (sizeD * Math.cos(shootingAngleRadian)), yPos + (size / 2) + (sizeD * Math.sin(shootingAngleRadian) * (-1)), true, true);

            playerShot.applyForce(angle, force);
            
    }

    public boolean removeShot(){
        if (this.playerShot.getRemoveShot()){
            System.out.println("Player, removeShot()");
            this.playerShot = null;
            return true;
        }
        return false;
    }

    void textDisplay(GraphicsContext gc) {
        if (myTurn) { // if it is the players turn, text is shown asking for the angle of their shot
                      // and then the force
            if(!parameterChosen){
             forceAndAngle = App.getForcesFromMouse(new double[]{xPos+size/2,yPos+size/2}, new double[] {App.MouseX,App.MouseY});
            }
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("Verdana", 15));



            //PointerInfo a = MouseInfo.getPointerInfo();
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
                
                

                gc.fillText(name + ": angle " + (int)forceAndAngle[1], 640, 100);
                gc.fillText(name + ": Force " + round(forceAndAngle[0], 2), 640, 120);
                if(parameterChosen){
                    gc.setFont(Font.font("Verdana", 10));
                    gc.fillText("To go back press Any Key",640,210);
                }
                
        

        }
    }

    public static double round(double value, int places) {

        java.math.BigDecimal decimal = java.math.BigDecimal.valueOf(value);
        decimal = decimal.setScale(places, java.math.RoundingMode.HALF_UP);

        return decimal.doubleValue();
    }
    
}