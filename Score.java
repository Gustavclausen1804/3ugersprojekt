import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


class Score {
    int xPos, yPos = 20,counter = 0,id;

    static final int size = 30;
   
    Score(int id){
        this.id = id;
        this.xPos = App.xRange*id;   
    }

    public void draw(GraphicsContext gc){
        gc.setFill(Color.GREEN);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font ("Verdana", 20));
        App.spiller.forEach((p) -> {
            if(id == p.id ){
                gc.fillText(p.name+": "+ counter,xPos, yPos);
            }
        });
        
        
    }


}