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
    boolean  parameterChosen = false;

    Group playerRoot = new Group();
    CustomButton btn = new CustomButton("Shoot");


    String name;
    final int size = 30;
    int id;
    public boolean shootsFired;
    double shootingForce, shootingAngle;
    double[] forceAndAngle;

    int score = 0;

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
                    if(parameterChosen){
                        shoot(shootingAngle,shootingForce);
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
        gc.setFill(Color.BLACK);
        gc.fillRect(xPos, yPos, size, size);
        if (id == App.turn) {
            textDisplay(gc);
        }

        playerScore.draw(gc);
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
            // Double sizeD = Math.sqrt(Math.pow(size / 2, 2) + Math.pow(size / 2, 2));
            Double shootingAngleRadian = Math.toRadians(angle);

            if (App.turn == 2 && enemyEnable == true){
                // double[] shot = enemyDifficulty(enemyShot( xPos, yPos), 1);  //Inner function finds shot by bruteforce, outer function adds variability to shot.
                double[] shot = enemyShot( xPos, yPos);  //Inner function finds shot by bruteforce, outer function adds variability to shot.

                angle = shot[0];
                force = shot[1];
                System.gc();
            }
            this.playerShot = new Shot(xPos, yPos, true, true, id);
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
         // if it is the players turn, text is shown asking for the angle of their shot
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

    public static double round(double value, int places) {

        java.math.BigDecimal decimal = java.math.BigDecimal.valueOf(value);
        decimal = decimal.setScale(places, java.math.RoundingMode.HALF_UP);

        return decimal.doubleValue();
    }
    
      public double[] enemyShot(int x, int y) {
            System.out.println("Enemy, shoot()");

            int iteration = 2500; // 1000/30FPS = 30sec simulering, but devided by refinement factor

            int iterationAngle = 90; // Antal udførte kast
            int iterationForce = 20; // Antal udførte kast

            double[] angleMinMax = { 0, 180 };
            double[] forceMinMax = { 10, 40 };

            //Determent the players position:
            double xAim = App.spiller.get(0).xPos;
            double yAim = App.spiller.get(0).yPos;

            System.out.println("X: "+xAim+ ", " +(xAim+size));
                        System.out.println("Y: "+yAim+ ", " +(yAim+size));



            ArrayList<ArrayList<Double>> liste = new ArrayList<ArrayList<Double>>();

            for (int iA = 0; iA < iterationAngle; iA++) {
                  for (int iF = 0; iF < iterationForce; iF++) {

                        // Calculate the step size.
                        double AngleStep = ((angleMinMax[1] - angleMinMax[0]) / iterationAngle);
                        double ForceStep = ((forceMinMax[1] - forceMinMax[0]) / iterationForce);

                        // Set-up for the variables for the next shot in line.
                        double angleNext = angleMinMax[0] + AngleStep * iA;
                        double forceNext = forceMinMax[0] + ForceStep * iF;

                        Shot simulation = new Shot(x, y, true, false, this.id);

                        simulation.applyForce(angleNext,forceNext);

                        int collisionQuantification = 0;

                        ArrayList<Double> data = new ArrayList<Double>(); // Initialize and reset arraylist for every iteration
                        for (int i = 0; i < iteration; i++) {

                            if (simulation.wallCollision(false)) { // Attempt to quantify the amount of collision.
                                    collisionQuantification++;
                              }

                              // Save infomation if balls hits the target.
                            if (simulation.playerCollision(false)) {

                                    data.add(angleNext);
                                    data.add(forceNext);
                                    data.add(Double.valueOf(collisionQuantification));

                                    liste.add(data);
                                    break;
                              }
                              // Simulate the ball according to applied forces on the map.
                              simulation.updateShot();
                        }
                        simulation = null;  //Remove pointer to shot-object.
                  }
            }


            double[] bestShot = { 0, forceMinMax[1]+1, 0 };

            boolean cleanShot = false; // Flag if a clean hit upon the target is possible.

            for (int i = 0; i < liste.size(); i++) {
                  if (liste.get(i).get(1) < bestShot[1] && liste.get(i).get(2) <= 1) { // Lowest force and no
                                                                                       // wall collision
                        bestShot[0] = liste.get(i).get(0);
                        bestShot[1] = liste.get(i).get(1);
                        bestShot[2] = liste.get(i).get(2);
                        cleanShot = true;
                  }
            }

            System.out.println("Antal mulige skud: " +liste.size() + ", cleanShot:" + cleanShot);


            if (!cleanShot) {
                  for (int i = 0; i < liste.size(); i++) {
                        if (liste.get(i).get(2) < bestShot[2]) { // Lowest amount of wall collision
                              bestShot[0] = liste.get(i).get(0);
                              bestShot[1] = liste.get(i).get(1);
                              bestShot[2] = liste.get(i).get(2);
                        }
                  }
            }
            System.out.println("Modstander: " + bestShot[0] + " " + bestShot[1]);
            return bestShot;

      }
    public double[] enemyDifficulty(double[] shot, int difficulty){
        Random rand = new Random();

        double deltaAngle;
        double deltaForce;
        
        switch (difficulty) {
            case 1:     //Easy
            deltaAngle = 3;
            deltaForce = 3;
                break;
            case 2:     //Medium
            deltaAngle = 2;
            deltaForce = 2;
                break;
            case 3:     //Hard
            deltaAngle = 1;
            deltaForce = 1;
                break;
            default:
            deltaAngle = 0;
            deltaForce = 0;
                break;
        }
        deltaAngle = (rand.nextDouble()*2*deltaAngle)-deltaAngle;
        deltaForce = (rand.nextDouble()*2*deltaForce)-deltaForce;

        shot[0] += deltaAngle;
        shot[1] += deltaForce;

        return shot;
    }




}