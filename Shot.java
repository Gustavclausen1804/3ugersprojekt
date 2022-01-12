import java.awt.Canvas;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

public class Shot{
    private double xDir = 0;
    private double yDir = 0;

    private double ballXPos;
    private double ballYPos;

    private double gravityForce = 9.82;
    private double dragCoefficient = 0.47;
    private double massObject = 1;

    private static final int BALL_R = 15;
    private int explosion_R = App.explosionRadius;

    private int shooterId;


    int animationTimer = 0;
    int currentImage = 0;
    boolean explosionActive;
    

    Shot(int x, int y){
        this.ballXPos = x;
        this.ballYPos = y;
        // enemyShot(x,y);
    }
    
    Shot(Double ballXPos, Double ballYPos, double angle, double force, int shooterId){
        
        this.ballXPos = ballXPos;
        this.ballYPos = ballYPos;
        this.xDir += Math.cos(Math.toRadians(angle))*force;
        this.yDir += -Math.sin(Math.toRadians(angle))*force;
        this.shooterId = shooterId;
    }

    public void draw_ball(GraphicsContext gc){
        move_ball();
        DrawDir(gc);
        collision();
        explosionAnimation(gc);


        //Ball
        if(!explosionActive){
        gc.setFill(Color.BLACK);
        gc.fillOval(ballXPos,ballYPos,BALL_R,BALL_R);
        }
        
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

    public void move_ball(){

        //Update the balls position according to its direction vector components
        ballXPos += xDir;
        ballYPos += yDir;

        //Decide between bounce or force
        if (ballYPos > App.height || ballYPos < 0) {
            yDir *= -1;
        } else if (ballXPos > App.width || ballXPos < 0) {
            xDir *= -1;
        } else {
            //Wind resistiance
            if(getSpeed() > 0){
                applyForce("follow", -(Math.pow(getSpeed(), 2)/300));
            }
            //Gravity
            applyForce("downConstant", (massObject*gravityForce)/100);
        }
    }

    public double[] getPosition(){
        double[] array = {ballXPos,ballYPos};
        return array;  
    }

    public double[] GetDirection(){
        double[] array = {xDir,yDir};
        return array;  
    }

    public double getAngle(){
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
    public double getSpeed(){
        return Math.sqrt((Math.pow(xDir, 2)+Math.pow(yDir, 2)));
    }

    public void collision(){
       playerCollision();
       wallCollision();

    }
    void wallCollision(){
        if(ballYPos == App.height){
            // removeShot();
        }
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
                        explosionActive = true;
                        explosion();
                        xDir = 0;
                        yDir = 0;
                        gravityForce = 0;
                        //removeShot();
                        break;
                    }
                   
            }
        }
        //if((ballXPos >= p.xPos && ballXPos<= p.xPos+p.size) && (ballYPos >= p.yPos && ballYPos<= p.yPos+p.size)){
    }
    void removeShot(){
        //Player.skud.remove(this);  //Der skal findes en ordentlig måde at slette de her på
                //Dårlig hack for nu
            ballXPos = 0;
            ballYPos = 0;
            xDir = 0;
            yDir = 0;
            gravityForce = 0;

            
            App.spiller.get(shooterId-1).shootsFired=false;
            App.spiller.get(shooterId-1).ForceChosen=false;
            App.spiller.get(shooterId-1).angleChosen=false;
            App.turn++;

                
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

    void explosionAnimation(GraphicsContext gc) {
        if (explosionActive) {
                gc.drawImage(App.explosionImage[currentImage], this.ballXPos-(explosion_R/2), this.ballYPos-(explosion_R/2));
                if (animationTimer % 10 == 0 && currentImage < App.explosionImage.length) {
                    currentImage++;
                }
                if(currentImage == App.explosionImage.length-1){
                    explosionActive = false;
                    removeShot();
                }
            animationTimer++;
        }
    }

    // //Enemy shot
    // public void enemyShot(int x, int y){
    //     int iterationAngle = 150;  //Antal udførte kast
    //     int iterationForce = 20;  //Antal udførte kast

    //     double[] angleMinMax = {0,180};
    //     double[] forceMinMax = {0,20};

    //     double AngleStep = ((angleMinMax[1]-angleMinMax[0])/iterationAngle);
    //     double ForceStep = ((forceMinMax[1]-forceMinMax[0])/iterationForce);

    //     double[][] liste = new double[iterationAngle][];
        
    //     for (int iA = 0; iA < iterationAngle; iA++){
    //         for (int iF = 0; iF < iterationForce; iF++){

    //             //Reset shot
    //             this.ballXPos = x;
    //             this.ballYPos = y;
    //             this.xDir = 0;
    //             this.yDir = 0;

                
    //             double angleNext = angleMinMax[0]+AngleStep*iA;
    //             double forceNext = forceMinMax[0]+ForceStep*iF;

    //             System.out.println(xDir + "   " + yDir);


    //             this.xDir += Math.cos(Math.toRadians(angleNext))*(forceNext);
    //             this.yDir += -Math.sin(Math.toRadians(angleNext))*(forceNext);



    //             int iteration = 1000;   //  1000/30FPS = 30sek simulering
    //             double[] insert = {-1,-1,-1};  //Load standard value if no hit
    //             for (int i = 0; i < iteration; i++){
    //                 move_ball();
    //                 if ((ballXPos > 700) && (ballYPos > 700)){
    //                     insert[0] = angleMinMax[0]+AngleStep*i;
    //                     insert[1] = forceMinMax[0]+ForceStep*i;
    //                     insert[2] = i;
                        
    //                     //Leave iteration loop
    //                     //break
    //                 }
    //             }
                

    //             //Gem værdier
    //             liste[iA] = insert;
    //             if (insert[0] != -1 && insert[1] != -1){
    //                 System.out.println(insert[0] + "   " + insert[1]);
    //             }
    //         }
    //     }
    // }



}