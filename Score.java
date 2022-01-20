import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


class Score {
    int xPos, yPos,counter,id;

    static final int size = 30;
   
    Score(int xPos, int id){
        this.xPos = xPos;
        this.yPos = 20;
        this.counter = 0;
        this.id = id;
    }

    public void draw(GraphicsContext gc){
        gc.setFill(Color.GREEN);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font ("Verdana", 20));
        gc.fillText("Player"+ id +": "+ counter,xPos, yPos);
    }



}