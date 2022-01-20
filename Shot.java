import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;




public class Shot{
    private double ballXVector;
    private double ballYVector;

    private double ballXPos;
    private double ballYPos;

    private double gravity_force = 9.82;

    private static final int BALL_R = 15;

    Shot(int ballXPos, int ballYPos, double angle, double force){
        this.ballXPos = ballXPos;
        this.ballYPos = ballYPos;
        this.ballXVector += Math.cos(Math.toRadians(angle))*force;
        this.ballYVector += -Math.sin(Math.toRadians(angle))*force;
    }

    public void draw_ball(GraphicsContext gc){
        move_ball();

        //Vertical
        gc.setFill(Color.RED);
        if (ballYVector >= 0) {
            gc.fillRect(ballXPos, ballYPos + BALL_R / 2, BALL_R, ballYVector * 10);
        } else {
            gc.fillRect(ballXPos, (ballYPos + BALL_R / 2) - Math.abs(ballYVector * 10), BALL_R, Math.abs(ballYVector * 10));
        }

        //Horisontal
        gc.setFill(Color.GREEN);
        if (ballXVector >= 0) {
            gc.fillRect(ballXPos, ballYPos, ballXVector * 10, BALL_R);
        } else {
            gc.fillRect(ballXPos-Math.abs(ballXVector * 10), ballYPos, Math.abs(ballXVector * 10), BALL_R);
        }

        //Ball
        gc.setFill(Color.WHITE);
        gc.fillOval(ballXPos,ballYPos,BALL_R,BALL_R);

        
    }

    public void move_ball(){
        ballXPos += ballXVector;
        ballYPos += ballYVector;
        ballYVector += (gravity_force / 100);



        //Vindmodstand X
        if (ballXVector >= 0) {
            ballXVector -= (ballXVector * ballXVector) / 500;
            } else {
            ballXVector += (ballXVector * ballXVector) / 500;
            }

        //Vindmodstand Y
        if (ballYVector >= 0) {
        ballYVector -= (ballYVector * ballYVector) / 500;
        } else {
        ballYVector += (ballYVector * ballYVector) / 500;
        }

        // makes sure the ball stays in the canvas
        if (ballYPos > 800 || ballYPos < 0) {
            ballYVector *= -1;
        }
        if (ballXPos > 800 || ballXPos < 0) {
            ballXVector *= -1;
        }
    }





}