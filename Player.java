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
    
    String name;
   final int size = 30;
   int id;
   boolean shootsFired;
   double shootingForce, shootingAngle;
    Player(int yPos, int id, String name){
        this.xPos = 0;
        this.yPos = yPos;
        this.id = id;
        this.name = name;
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
        //.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);
    }

    public void startLocation() {
        this.xPos = App.xRange*id;
    }

    public void shoot(){
        
        if(shootsFired == false){
            Double sizeD = Math.sqrt(Math.pow(size/2,2)+Math.pow(size/2,2));
            Double shootingAngleRadian = Math.toRadians(shootingAngle);
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
                gc.fillText(name+": angle ",400,400);   
                textFieldAngle.setVisible(true);
            }
            if(angleChosen && ForceChosen == false){
                gc.fillText(name+": Force ",400,400);   
                textFieldForce.setVisible(true);
            }

            
            
        }
    }
}