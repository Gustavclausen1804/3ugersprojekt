import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;




class Player extends App{
    int xPos = 0, yPos = 0;
    ArrayList<Shot> skud = new ArrayList<Shot>();
    boolean myTurn,angleChosen,ForceChosen;
    
    Group playerRoot = new Group();
    //Creates two text fields where the player writes the angle and force of their shot
    TextField textFieldAngle= new TextField();
    TextField textFieldForce= new TextField();
    
    String name;
   final int size = 30;
   int id;
   boolean shootsFired;
   double shootingForce, shootingAngle;

   

    Player( int id, String name){
        this.id = id;
        this.name = name;
        
        //Gives the textfield a position for where to create them, hides them and add them to the group 
        textFieldAngle.setVisible(false);
        textFieldAngle.relocate(525, 380);
        textFieldForce.setVisible(false);
        textFieldForce.relocate(525, 380);
        playerRoot.getChildren().addAll(textFieldAngle,textFieldForce);
        shotExplosionBilleder();

        startLocation();
        
    }

    public void draw(GraphicsContext gc){
        
        //Draws the players
        
        gc.setFill(Color.BLACK);
        gc.fillRect( xPos, yPos,size,size);
        textDisplay(gc);
        //.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);
    }

    public void startLocation() {
        this.xPos = App.xRange*id;
    }

    public void move(){
        //if(frameCount % 20 == 0){
            for(int i = 0; i < MapGeneration.houses.size();i++){ // Loops through the column of blocks
                for(int j = 0; j < MapGeneration.houses.get(i).size();j++){
                    if(j==0){
                        //Checks if the player is within on block or not
                        if(xPos >= MapGeneration.houses.get(i).get(j)[0] && xPos+size <= MapGeneration.houses.get(i).get(j)[0]+MapGeneration.boxSize){ 
                            //yPos = MapGeneration.houses.get(i).get(j)[1]-size;
                        }
                        if(i > 0 && i < MapGeneration.houses.size()-1){
                            // if(xPos >= MapGeneration.houses.get(i).get(j)[0] && xPos+size >= MapGeneration.houses.get(i).get(j)[0]+MapGeneration.boxSize){ 
                            //if(xPos+size <= MapGeneration.houses.get(i+1).get(j)[0]+MapGeneration.boxSize){
                            if(xPos > MapGeneration.houses.get(i).get(j)[0] && xPos+size> MapGeneration.houses.get(i).get(j)[0]+ MapGeneration.boxSize){ 
                                // if( MapGeneration.houses.get(i).get(j)[1] >= MapGeneration.houses.get(i+1).get(j)[1]){
                                // if(yPos+size <){
                                    System.out.println(MapGeneration.houses.get(i+1).get(j)[1]);
                                   this.yPos = MapGeneration.houses.get(i).get(j)[1]-size;
                                // }
                                // } else{yPos = MapGeneration.houses.get(i+1).get(j)[1]-size;}
                            }
                            //  else if(xPos >= MapGeneration.houses.get(i-1).get(j)[0] && xPos+size <= MapGeneration.houses.get(i).get(j)[0]+MapGeneration.boxSize){
                            //     if( MapGeneration.houses.get(i).get(j)[1] >= MapGeneration.houses.get(i-1).get(j)[1]){
                            //         yPos = MapGeneration.houses.get(i).get(j)[1]-size;
                            //     } else{yPos = MapGeneration.houses.get(i-1).get(j)[1]-size;}
                            // }
                        }
                    }   
                }
            }
        // }
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
        if(myTurn){ // if it is the players turn, text is shown asking for the angle of their shot and then the force
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font ("Verdana", 15));
            if(angleChosen == false){
                gc.fillText(name+": angle ",400,400);   
                textFieldAngle.setVisible(true);

                // Draws the arrow which show the angle of the shot
                gc.setStroke(Color.BLUE);
                gc.setLineWidth(5);
                
                String text = textFieldAngle.getText();
                text = text.replaceAll("[^\\d.]", "");
                if (text == "" || text.length() == 0) {
                    text = "0.";
                }
                Double angle = Math.toRadians(Double.parseDouble(text));
                
                Double arrowLength = 20.;
                gc.strokeLine(xPos+size/2,yPos+size/2, (xPos+(size/2))+Math.cos(angle)*arrowLength,(yPos+(size/2))+Math.sin(angle)*arrowLength*(-1));
            }
            if(angleChosen && ForceChosen == false){
                gc.fillText(name+": Force ",400,400);   
                textFieldForce.setVisible(true);
            }

            
            
        }
    }
    
}