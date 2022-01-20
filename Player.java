import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

class Player extends App{
    int xPos, yPos;
     ArrayList<Shot> skud = new ArrayList<Shot>();
    boolean myTurn,angleChosen,ForceChosen;
    
    Group playerRoot = new Group();
    TextField textFieldAngle= new TextField();
    TextField textFieldForce= new TextField();
    
    
   static final int size = 30;
   int id;
   boolean shootsFired;
   double shootingForce, shootingAngle;
    Player(int xPos, int id){
        this.xPos = xPos;
        this.yPos = App.height-size;
        this.id = id;
        //laver 
        textFieldAngle.setVisible(false);
        textFieldAngle.relocate(525, 380);
        textFieldForce.setVisible(false);
        textFieldForce.relocate(525, 380);
        playerRoot.getChildren().addAll(textFieldAngle,textFieldForce);
        
    }

    public void draw(GraphicsContext gc){
        
        gc.setFill(Color.BLACK);
        gc.fillRect( xPos, yPos,size,size);
        textDisplay(gc);
    }


    public void shoot(){
        
        if(shootsFired == false){
            double sizeD = Math.sqrt(Math.pow(size/2,2)+Math.pow(size/2,2));
            double shootingAngleRadian = Math.toRadians(shootingAngle);
            skud.add(new Shot(xPos+(size/2)+(sizeD*Math.cos(shootingAngleRadian)),yPos+(size/2)+(sizeD*Math.sin(shootingAngleRadian)*(-1)),shootingAngle,shootingForce,id));
            myTurn = false;
            shootsFired = true;
            
            
        }
            
        

    }
    

     void textDisplay(GraphicsContext gc){
        if(myTurn){
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font ("Verdana", 15));
            if(angleChosen == false){
                gc.fillText("Player "+id+": angle ",400,400);   
                textFieldAngle.setVisible(true);
            }
            if(angleChosen && ForceChosen == false){
                gc.fillText("Player "+id+": Force ",400,400);   
                textFieldForce.setVisible(true);
            }

            
            
        }
    }
}