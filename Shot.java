import java.awt.Canvas;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import java.util.ArrayList;


public class Shot{
    public double xDir = 0;
    public double yDir = 0;

    public double ballXPos;
    public double ballYPos;

    public double gravityForce = 9.82;
    public double dragCoefficient = 0.47;
    public double massObject = 1;

    public static final int BALL_R = 15;   
    public int explosion_R = 100;

    public int shooterId;

    public boolean show = true;
    public boolean move = true;

    boolean removeShotFlag = false;

    Shot(int x, int y, boolean move, boolean show){
        this.ballXPos = x;
        this.ballYPos = y;
        this.move = move;
        this.show = show;
    }
    

    public boolean wallCollisionTEST(){
    //     for(int i = 0; i < MapGeneration.houses.size();i++){ // Loops through the column of blocks
    //         for(int j = 0; j < MapGeneration.houses.get(i).size(); j++){ // Loops through the row of blocks
    //             int xPosWall = MapGeneration.houses.get(i).get(j)[0]; //gets the x postion of the corner of each block
    //             int yPosWall = MapGeneration.houses.get(i).get(j)[1]; //gets the y postion of the corner of each block
    //             int wallSize = MapGeneration.boxSize; // gets the size of each block
                
    //             //Skal skrives om
    //             if((ballXPos >= xPosWall && ballXPos<= xPosWall+wallSize) 
    //                 && (ballYPos>= yPosWall && ballYPos <= yPosWall+wallSize)){  //Checks if the shot hits a block
    //                     return true;
    //                 }
    //         } 
    // }
    return false;
}

      //Enemy shot
      public double[] enemyShoot(int x, int y, int xGoal, int yGoal){
        int iteration = 1000;   //  1000/30FPS = 30sek simulering

        int iterationAngle = 180;  //Antal udførte kast
        int iterationForce = 50;  //Antal udførte kast

        double[] angleMinMax = {0,360};
        double[] forceMinMax = {0,100};





        ArrayList<ArrayList<Double> > liste = new ArrayList<ArrayList<Double> >();
        
        for (int iA = 0; iA < iterationAngle; iA++){
            for (int iF = 0; iF < iterationForce; iF++){

                //Reset shot
                this.ballXPos = x;
                this.ballYPos = y;
                this.xDir = 0;
                this.yDir = 0;

                //Calculate the step size.
                double AngleStep = ((angleMinMax[1]-angleMinMax[0])/iterationAngle);
                double ForceStep = ((forceMinMax[1]-forceMinMax[0])/iterationForce);
                
                //Set-up for the variables for the next shot in line.
                double angleNext = angleMinMax[0]+AngleStep*iA;
                double forceNext = forceMinMax[0]+ForceStep*iF;
                
                this.xDir += Math.cos(Math.toRadians(angleNext))*(forceNext);
                this.yDir += -Math.sin(Math.toRadians(angleNext))*(forceNext);

                int collisionCounter = 0;

                ArrayList<Double> data = new ArrayList<Double>();   //Initialize and reset arraylist for every iteration.
                for (int i = 0; i < iteration; i++){

                    double distance = Math.sqrt(Math.pow((xGoal-x),2)+Math.pow((yGoal-y),2));

                    if (wallCollisionTEST()){   //Attempt to quantify the amount of collision.
                        collisionCounter++;
                    }

                    //Save shot infomation if balls hits the target.
                    if ((this.ballXPos > 750) && (this.ballYPos < 50)){                         //TODO: Collision mellem bold og player skal være her
                        data.add(angleNext);
                        data.add(forceNext);
                        data.add(Double.valueOf(collisionCounter));
                        data.add(Double.valueOf(i));

                        liste.add(data);
                        break;
                }
                //Simulate the ball according to applied forces on the map.
                updateShot();   
            }
            }
        }

        double[] bestShot = {0,0,0,iteration};

        boolean cleanShot = false;  //Flag if a clean hit upon the target is possible.

        for (int i = 0; i < liste.size(); i++) {
            if (liste.get(i).get(3) < bestShot[3] && liste.get(i).get(2) == 0){  //Lowest iteration number and no wall collision
                bestShot[0] = liste.get(i).get(0);
                bestShot[1] = liste.get(i).get(1);
                bestShot[2] = liste.get(i).get(2);
                bestShot[3] = liste.get(i).get(3);
                cleanShot = true;
            }
        }
        if (!cleanShot){
            for (int i = 0; i < liste.size(); i++) {
                if (liste.get(i).get(2) < bestShot[2]){  //Lowest amount of wall collision
                    bestShot[0] = liste.get(i).get(0);
                    bestShot[1] = liste.get(i).get(1);
                    bestShot[2] = liste.get(i).get(2);
                    bestShot[3] = liste.get(i).get(3);
                }
            }
        }
        return bestShot;
    }

    public void drawShot(GraphicsContext gc){
        DrawDir(gc);


        collision();

        //Ball
        gc.setFill(Color.BLACK);
        // gc.fillRect(200, 200, 30, 30);
        gc.fillOval(ballXPos,ballYPos,BALL_R,BALL_R);
    }

    public void DrawDir(GraphicsContext gc){
        //Vertical
        gc.setFill(Color.RED);
        if (yDir >= 0) {
            gc.fillRect(ballXPos, ballYPos + BALL_R / 2, BALL_R, yDir * 10);
        } else {
            gc.fillRect(ballXPos, (ballYPos + BALL_R / 2) - Math.abs(yDir * 10), BALL_R, Math.abs(yDir * 10));
        }

        //Horisontal
        gc.setFill(Color.GREEN);
        if (xDir >= 0) {
            gc.fillRect(ballXPos, ballYPos, xDir * 10, BALL_R);
        } else {
            gc.fillRect(ballXPos-Math.abs(xDir * 10), ballYPos, Math.abs(xDir * 10), BALL_R);
        }
    }

    public void applyForce(double angle, double force){
        this.xDir += Math.cos(Math.toRadians(angle))*force;
        this.yDir += -Math.sin(Math.toRadians(angle))*force;
    }

    public void applyForce(String direction, double force){
        switch (direction) {
            case "upConstant":
                yDir -= force;
                break;

            case "downConstant":
                yDir += force;

                break;

            case "rightConstant":
                xDir += force;
                break;

            case "leftConstant":
                xDir -= force;
                break;

            case "follow":
                double angle = getAngle();
 
                xDir += Math.cos(Math.toRadians(angle))*(force);
                yDir += Math.sin(Math.toRadians(angle))*(force);   
                break;                    
        
            default:
                break;
        }
    }

    public void updateShot(){

        //Update the balls position according to its direction vector components
        this.ballXPos += xDir;
        this.ballYPos += yDir;

        //Make sure it is within the screen, if not, correct it.
        if (this.ballXPos > App.width-1){  //Account for the 0-offset
            this.ballXPos = App.width-1;
            this.xDir *= -1;
        }
        if (this.ballXPos < 0){
            this.ballXPos = 0;
            this.xDir *= -1;
        }
        if (this.ballYPos > App.height-1){
            this.ballYPos = App.height-1;
            this.yDir *= -1;
        }
        if (this.ballYPos < 0){
            this.ballYPos = 0;
            this.yDir *= -1;
        }
            //Wind resistiance
            if(getSpeed() > 0){
                applyForce("follow", -(Math.pow(getSpeed(), 2)/500));
            }
            //Gravity
            applyForce("downConstant", (massObject*gravityForce)/75);
        }

    public void changeMove(boolean state){
        this.move = state;
    }

    public double[] getPosition(){  //Returns the coordiantes of the shot.
        double[] array = {ballXPos,ballYPos};
        return array;  
    }

    public double[] GetDirection(){ //Returns the components of the directional vector
        double[] array = {xDir,yDir};
        return array;  
    }

    public double getAngle(){   //Returns the angle of the directional vector
        //Calculate angle between components
        double angle = Math.toDegrees(Math.atan((yDir)/xDir));   

        //Account for their placement, [-180:180]
        if (xDir >= 0 && yDir >= 0){
            angle = angle;
        } else if (xDir >= 0 && yDir <= 0){
            angle = angle;
        } else if (xDir <= 0 && yDir >= 0){
            angle = 180+angle;
        } else if (xDir <= 0 && yDir <= 0){
            angle = angle-180;
        }
        return angle;
        
    }
    public double getSpeed(){   //Returns the length of the directional vector
        return Math.sqrt((Math.pow(xDir, 2)+Math.pow(yDir, 2)));
    }

    public void collision(){
        //playerCollision();
        wallCollision();

    }
    void wallCollision(){

        // Loops through the all houses(blocks)
        for(int i = 0; i < MapGeneration.houses.size();i++){ // Loops through the column of blocks
            for(int j = 0; j < MapGeneration.houses.get(i).size(); j++){ // Loops through the row of blocks
                int xPosWall = MapGeneration.houses.get(i).get(j)[0]; //gets the x postion of the corner of each block
                int yPosWall = MapGeneration.houses.get(i).get(j)[1]; //gets the y postion of the corner of each block
                int wallSize = MapGeneration.boxSize; // gets the size of each block
                
                //Skal skrives om
                if((ballXPos >= xPosWall && ballXPos<= xPosWall+wallSize) 
                    && (ballYPos>= yPosWall && ballYPos <= yPosWall+wallSize)){  //Checks if the shot hits a block
                        
                        MapGeneration.houses.get(i).remove(j); //Removes the block which the shot hit
                        // explosion();
                        removeShotFlag = true;
                        removeShot();
                        break;
                    }
                   
            }
        }
        //if((ballXPos >= p.xPos && ballXPos<= p.xPos+p.size) && (ballYPos >= p.yPos && ballYPos<= p.yPos+p.size)){
    }
    void removeShot(){
            removeShotFlag = true;
    }

    public boolean getRemoveShot(){
        return removeShotFlag;
    }

    void playerCollision(){
        App.spiller.forEach((p) -> {

            if((ballXPos>= p.xPos && ballXPos<= p.xPos+p.size) 
                && (ballYPos>= p.yPos && ballYPos<= p.yPos+p.size)){
                if(p.id == shooterId){
                    App.score.get(p.id-1).counter--;
                } else{
                    App.score.get(shooterId-1).counter++;
                }
                //explosion();
                removeShot();
            }
            
        });
    }

    void explosion(){
        // Loops through the all houses(blocks)
        for(int i = 0; i < MapGeneration.houses.size();i++){ // Loops through the column of blocks
            for(int j = 0; j < MapGeneration.houses.get(i).size(); j++){ // Loops through the row of blocks
                int xPosWall = MapGeneration.houses.get(i).get(j)[0]; //gets the x postion of the corner of each block
                int yPosWall = MapGeneration.houses.get(i).get(j)[1]; //gets the y postion of the corner of each block
                int wallSize = MapGeneration.boxSize; // gets the size of each block
                for(int h = 0; h < 4; h++){
                    Double x = 0., y = 0.; // creates two empty variables x and y
                    Double xWallD = Double.valueOf(xPosWall) ,yWallD = Double.valueOf(yPosWall); //converts the position of the wall from int to double
                    switch(h){ // This switch is used to create values for x and y which each represents each corner of the block
                        case 0: // top left corner
                        x = xWallD; y = yWallD; break;
                        case 1:
                        x = xWallD+wallSize; y = yWallD; break; //top right corner
                        case 3:
                        x = xWallD; y = yWallD+wallSize; break; // bottom left corner
                        case 4:
                        x = xWallD+wallSize; y = yWallD+wallSize; break; // bottom right corner
                    }
                    double distance = Math.sqrt(Math.pow(ballXPos-x, 2)+Math.pow(ballYPos-y, 2)); //calculates the distance between the shot and each corner of the ball
                    if(distance <=explosion_R){
                        MapGeneration.houses.get(i).remove(j); //removes the shot if it
                    }
                }  
            }
        }
    }



}