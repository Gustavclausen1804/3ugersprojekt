import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;

class Player{
    int xPos, yPos;

   static final int size = 30;
   int id;
    Player(int yPos, int id){
        this.xPos = 0;
        this.yPos = yPos;
        this.id = id;
    }

    public void amountP(){
        
    }

    public void draw(GraphicsContext gc){
        gc.setFill(Color.WHITE);
        gc.fillRect( xPos, yPos,size,size);
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