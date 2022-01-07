import java.awt.Canvas;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Shot{
    private double ballXVector;
    private double ballYVector;

    private double ballXPos;
    private double ballYPos;

    private double gravity_force = 10;

    private int shooterId;

    private static final int BALL_R = 15;

    
    
    Shot(Double ballXPos, Double ballYPos, double angle, double force, int shooterId){
        
        this.ballXPos = ballXPos;
        this.ballYPos = ballYPos;
        this.ballXVector += Math.cos(Math.toRadians(angle))*force;
        this.ballYVector += -Math.sin(Math.toRadians(angle))*force;
        this.shooterId = shooterId;
    }

    public void draw_ball(GraphicsContext gc){
        move_ball();
        collision();

        // //Vertical
        // gc.setFill(Color.RED);
        // if (ballYVector >= 0) {
        //     gc.fillRect(ballXPos, ballYPos + BALL_R / 2, BALL_R, ballYVector * 10);
        // } else {
        //     gc.fillRect(ballXPos, (ballYPos + BALL_R / 2) - Math.abs(ballYVector * 10), BALL_R, Math.abs(ballYVector * 10));
        // }

        // //Horisontal
        // gc.setFill(Color.GREEN);
        // if (ballXVector >= 0) {
        //     gc.fillRect(ballXPos, ballYPos, ballXVector * 10, BALL_R);
        // } else {
        //     gc.fillRect(ballXPos-Math.abs(ballXVector * 10), ballYPos, Math.abs(ballXVector * 10), BALL_R);
        // }

        //Ball
        gc.setFill(Color.BLACK);
        gc.fillOval(ballXPos,ballYPos,BALL_R,BALL_R);

        
    }

    public void move_ball(){
        ballXPos += ballXVector;
        ballYPos += ballYVector;
        ballYVector += (gravity_force / 100);



        //Vindmodstand X
        // if (ballXVector >= 0) {
        //     ballXVector -= (ballXVector * ballXVector) / 500;
        //     } else {
        //     ballXVector += (ballXVector * ballXVector) / 500;
        //     }

        //Vindmodstand Y
        // if (ballYVector >= 0) {
        // ballYVector -= (ballYVector * ballYVector) / 500;
        // } else {
        // ballYVector += (ballYVector * ballYVector) / 500;
        // }

        // makes sure the ball stays in the canvas
        if (ballYPos > Player.height || ballYPos < 0) {
            ballYVector *= -1;
        }
        if (ballXPos > Player.width|| ballXPos < 0) {
            ballXVector *= -1;
        }
    }

    public void collision(){
       playerCollision();
       wallCollision();

    }
    void wallCollision(){
        if(ballYPos == App.height){
            // removeShot();
        }
        for(int i = 0; i < MapGeneration.houses.size();i++){
            for(int j = 0; j < MapGeneration.houses.get(i).size(); j++){
                int xPosWall = MapGeneration.houses.get(i).get(j)[0];
                int yPosWall = MapGeneration.houses.get(i).get(j)[0];
                int wallSize = MapGeneration.boxSize;
                if((ballXPos >= xPosWall && ballXPos <= xPosWall+wallSize) && (ballYPos >= yPosWall && ballYPos <= yPosWall+wallSize)){
                    MapGeneration.houses.get(i).remove(j);
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
                ballXVector = 0;
                ballYVector = 0;
                gravity_force = 0;
                App.spiller.get(shooterId-1).shootsFired=false;
                App.spiller.get(shooterId-1).ForceChosen=false;
                App.spiller.get(shooterId-1).angleChosen=false;
                
                App.turn++;
    }

    void playerCollision(){
        App.spiller.forEach((p) -> {
            for(int i = 0; i < 360; i+=45){
               Double angleRadian = Math.toRadians(i);
                if((ballXPos >= p.xPos && ballXPos<= p.xPos+p.size) && (ballYPos >= p.yPos && ballYPos<= p.yPos+p.size)){
                    if(p.id == shooterId){
                        App.score.get(p.id-1).counter--;
                    } else{
                        App.score.get(shooterId-1).counter++;
                    }
                
                    removeShot();
                }
            }
        });
    }





}