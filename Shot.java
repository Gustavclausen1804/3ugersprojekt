import java.awt.Canvas;
import java.util.Map;

import javax.swing.plaf.basic.BasicTabbedPaneUI.MouseHandler;

import javafx.event.EventType;
import javafx.css.Size;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Shot {
    public double xDir = 0;
    public double yDir = 0;

    public int microSteps = 10;

    public double ballXPos;
    public double ballYPos;

    public double gravityForce = 9.82;
    public double dragCoefficient = 0.47;
    public double massObject = 1;

    public static final int BALL_R = 15;
    public int explosion_R = App.explosionRadius;

    public int shooterId;

    public boolean show = true;
    boolean showBullet = true;
    public boolean move = true;

    int animationTimer = 0;
    int currentImage = 0;
    boolean explosionActive;
    boolean countOnce = false;
    boolean shotGone;

    boolean leftShooter = false;
    boolean ShotHitOpposite = false;

    boolean removeShotFlag = false;

    Shot(int x, int y, boolean move, boolean show, int id){
        this.ballXPos = x;
        this.ballYPos = y;
        this.move = move;
        this.show = show;
        this.shooterId = id; 
    }


    public void drawShot(GraphicsContext gc){
        if (this.show == true){
            collision();
            explosionAnimation(gc);
            if(!explosionActive){
                // DrawDir(gc);
                //Ball
                gc.setFill(Color.BLACK);
                // gc.fillRect(200, 200, 30, 30);
                gc.fillOval(ballXPos,ballYPos,BALL_R,BALL_R);
            }
        }
    }

    public void DrawDir(GraphicsContext gc) {
        // Vertical
        gc.setFill(Color.RED);
        if (yDir >= 0) {
            gc.fillRect(ballXPos, ballYPos + BALL_R / 2, BALL_R, yDir * 10);
        } else {
            gc.fillRect(ballXPos, (ballYPos + BALL_R / 2) - Math.abs(yDir * 10), BALL_R, Math.abs(yDir * 10));
        }

        // Horisontal
        gc.setFill(Color.GREEN);
        if (xDir >= 0) {
            gc.fillRect(ballXPos, ballYPos, xDir * 10, BALL_R);
        } else {
            gc.fillRect(ballXPos - Math.abs(xDir * 10), ballYPos, Math.abs(xDir * 10), BALL_R);
        }
    }

    public void applyForce(double angle, double force) {
        this.xDir += Math.cos(Math.toRadians(angle)) * force;
        this.yDir += -Math.sin(Math.toRadians(angle)) * force;
    }

    public void applyForce(String direction, double force) {
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

                xDir += Math.cos(Math.toRadians(angle)) * (force);
                yDir += Math.sin(Math.toRadians(angle)) * (force);
                break;

            default:
                break;
        }
    }

    public void updateShot(){
        int refinementFactor = 2;
        if (move == true){

                //Update the balls position according to its direction vector components
                this.ballXPos += xDir/refinementFactor;
                this.ballYPos += yDir/refinementFactor;

                //Make sure it is within the screen, if not, correct it.
                if (this.ballXPos > App.width-1){  //Account for the 0-offset
                    this.ballXPos = App.width-1;
                    this.xDir *= -1;
                }
                if (this.ballXPos < 0){
                    this.ballXPos = 0;
                    this.xDir *= -1;
                }
                // if (this.ballYPos > App.height-1){
                //     this.ballYPos = App.height-1;
                //     this.yDir *= -1;
                // }
                // if (this.ballYPos < 0){
                //     this.ballYPos = 0;
                //     this.yDir *= -1;
                // }
                //Wind resistiance
                if(getSpeed() > 0){
                    applyForce("follow", -((Math.pow(getSpeed(), 2)/500)/refinementFactor));
                }
                //Gravity
                applyForce("downConstant", ((massObject*gravityForce)/70)/refinementFactor);
            }
        }
    

    public void setMove(boolean state) {
        this.move = state;
    }

    public void setShow(boolean state) {
        this.show = state;
    }

    public double[] getPosition() { // Returns the coordiantes of the shot.
        double[] array = { ballXPos, ballYPos };
        return array;
    }

    public double[] GetDirection() { // Returns the components of the directional vector
        double[] array = { xDir, yDir };
        return array;
    }

    public double getAngle() { // Returns the angle of the directional vector
        // Calculate angle between components
        double angle = Math.toDegrees(Math.atan((yDir) / xDir));

        // Account for their placement, [-180:180]
        if (xDir >= 0 && yDir >= 0) {
            angle = angle;
        } else if (xDir >= 0 && yDir <= 0) {
            angle = angle;
        } else if (xDir <= 0 && yDir >= 0) {
            angle = 180 + angle;
        } else if (xDir <= 0 && yDir <= 0) {
            angle = angle - 180;
        }
        return angle;

    }

    public double getSpeed() { // Returns the length of the directional vector
        return Math.sqrt((Math.pow(xDir, 2) + Math.pow(yDir, 2)));
    }

    public void collision(){
        playerCollision(true);
        wallCollision(true);

    }

    public boolean wallCollision(boolean enableCollision){
        if(ballYPos >= App.height-2*BALL_R && enableCollision == true){
            explosion();
        }
        // Loops through the all houses(blocks)
        for (int i = 0; i < MapGeneration.houses.size(); i++) { // Loops through the column of blocks
            for (int j = 0; j < MapGeneration.houses.get(i).size(); j++) { // Loops through the row of blocks
                int xPosWall = MapGeneration.houses.get(i).get(j)[0]; // gets the x postion of the corner of each block
                int yPosWall = MapGeneration.houses.get(i).get(j)[1]; // gets the y postion of the corner of each block
                int wallSize = MapGeneration.boxSize; // gets the size of each block
                
                if((ballXPos >= xPosWall && ballXPos<= xPosWall+wallSize) && (ballYPos>= yPosWall && ballYPos <= yPosWall+wallSize)){  //Checks if the shot hits a block

                        if (enableCollision == true){
                            MapGeneration.houses.get(i).remove(j); //Removes the block which the shot hit
                            explosion();
                            break;
                        }

                        return true;

                    }
                   
            }
        }
        return false;
    }

    void removeShot() {
        App.spiller.get(shooterId-1).shootsFired=false;
        removeShotFlag = true;

    }

    public boolean getRemoveShot() {
        return removeShotFlag;
    }

    boolean playerCollision(boolean enableCollision){
        App.spiller.forEach((p) -> {
            if((ballXPos>= p.xPos && ballXPos<= p.xPos+p.size) && (ballYPos>= p.yPos && ballYPos<= p.yPos+p.size)){
                if(p.id == shooterId){
                    if (leftShooter == true){
                        if(countOnce == false){
                            if (enableCollision == true){
                                App.spiller.get(shooterId-1).playerScore.counter--;
                                explosion();
                            }
                            countOnce = true;
                            
                        }
                    }
                }
                else {
                    if(countOnce == false){
                        if (enableCollision == true){
                            App.spiller.get(shooterId-1).playerScore.counter++;
                            explosion();
                        }
                        countOnce = true;
                        ShotHitOpposite = true; //Due to the 'forEach' loop being a void, an object variable is nessecary for scope reasons.
                    }
                }
            } else {  //When the ball is not in contact with a player
                if(p.id == shooterId){  //The player which made the shot
                    leftShooter = true;
                }
            } 
        });
        return ShotHitOpposite;
    }

    void explosion() {
        // Loops through the all houses(blocks)
        explosionActive = true;
        this.move = false;
        for (int i = 0; i < MapGeneration.houses.size(); i++) { // Loops through the column of blocks
            for (int j = 0; j < MapGeneration.houses.get(i).size(); j++) { // Loops through the row of blocks
                int xPosWall = MapGeneration.houses.get(i).get(j)[0]; // gets the x postion of the corner of each block
                int yPosWall = MapGeneration.houses.get(i).get(j)[1]; // gets the y postion of the corner of each block
                int wallSize = MapGeneration.boxSize; // gets the size of each block
                for (int h = 0; h < 4; h++) {
                    double x = 0, y = 0; // creates two empty variables x and y
                    switch (h) { // This switch is used to create values for x and y which each represents each
                                 // corner of the block
                        case 0: // top left corner
                            x = xPosWall;
                            y = yPosWall;
                            break;
                        case 1:
                            x = xPosWall + wallSize;
                            y = yPosWall;
                            break; // top right corner
                        case 3:
                            x = xPosWall;
                            y = yPosWall + wallSize;
                            break; // bottom left corner
                        case 4:
                            x = xPosWall + wallSize;
                            y = yPosWall + wallSize;
                            break; // bottom right corner
                    }
                    double distance = Math.sqrt(Math.pow(ballXPos - x, 2) + Math.pow(ballYPos - y, 2)); // calculates
                                                                                                        // the distance
                                                                                                        // between the
                                                                                                        // shot and each
                                                                                                        // corner of the
                                                                                                        // ball
                    if (distance <= explosion_R) {
                        MapGeneration.houses.get(i).remove(j); // removes the shot if it
                    }
                }
            }
        }
    }

    void explosionAnimation(GraphicsContext gc) {
        if (explosionActive) {
            gc.drawImage(App.explosionImage[currentImage], this.ballXPos - (explosion_R / 2),
                    this.ballYPos - (explosion_R / 2));
            if (animationTimer % 10 == 0 && currentImage < App.explosionImage.length) {
                currentImage++;
            }
            if (currentImage == App.explosionImage.length - 1) {
                removeShot();
            }
            animationTimer++;
        }
    }
}