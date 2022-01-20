
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;


public class App extends Application {

    public static ArrayList<Player> spiller;
    public static ArrayList<Score> score;
    
    
   

   

    //Billede
    // ImageView imageView = new ImageView();
    
    //Screen Size
    static final int width = 1280;
    static final int height = 720;

    //Set min and max players
    private final int minPlayers = 2; //atleast 2 for game to work
    private final int maxPlayers = 8; //max depends on screen size
    
    //Start Screen Interface 
    private final int sideGap = 15; //gap in grid vertical
    private final int hightGap = 5; //gap in grid horizontal

    
    public static int playerAmount = 2; //Default value
    public static int xRange;

    static int turn = 1;
    private GridPane grid = new GridPane();

   
    
    public void start(Stage stage) throws Exception {
        //start Screen forwards to gamestart
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);


        //Creates scores and players in arrayLists
        spiller = new ArrayList<Player>();
        score = new ArrayList<Score>();

        score.add(new Score(100,1));
        spiller.add(new Player(0,1));
        
        score.add(new Score(App.width-100,2));
        spiller.add(new Player(App.width-30,2));
        
        
        root.getChildren().add(canvas);
        //root.getChildren().add(map.mapRoot);
        
        spiller.forEach((p) -> {
            root.getChildren().add(p.playerRoot);
         });
         
        Scene scene = new Scene(root, width,height);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);
        stage.setScene(scene);  
        stage.show();
        tl.play();   
    }

    private void handleKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
           spiller.forEach((p) -> {
                if(turn == p.id ){
                    if(p.shootsFired == false){
                        if(p.angleChosen == false){
                            if(p.textFieldAngle.getText().length() != 0){
                                p.shootingAngle=getDoubFromTextField(p.textFieldAngle);
                                p.angleChosen = true;
                                p.textFieldAngle.setText("");
                                p.textFieldAngle.setVisible(false);
                            }
                        }
                        if(p.angleChosen && p.ForceChosen == false){
                            if(p.textFieldForce.getText().length() != 0){
                                p.shootingForce=getDoubFromTextField(p.textFieldForce);
                                p.ForceChosen = true;
                                p.textFieldForce.setText("");
                                p.textFieldForce.setVisible(false);
                            }   
                        }
                        if(p.angleChosen && p.ForceChosen){
                            p.shoot();
                        }

                    }                    

                    
               
                }
             });
        }
    }

    public static Double getDoubFromTextField(TextField textField) {
        String text = textField.getText();
        text = text.replaceAll("[^\\d.]", "");
        if(text == ""){
            text = "0";
        }
        return Double.parseDouble(text);
    }
    //Gets Number from textField input (int)
    public static int getIntFromTextField(TextField textField) {
        String num = textField.getText();
        num = num.replaceAll("[^\\d.]", "");
        return Integer.parseInt(0+num);
    }

    private void run(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);
        
        spiller.forEach((p) -> {
            p.draw(gc);
            p.skud.forEach((b) -> {
                b.draw_ball(gc);
               
            });
        });

        score.forEach((p) -> {
            p.draw(gc);
        });
        spiller.forEach((p) -> {
            if(turn == p.id && p.shootsFired == false){
                p.myTurn = true;
            }
        });
        if(turn > spiller.size()){
            turn = 1;
        }
        
    }


    

    
    public static void main(String[] args) {
        launch(args);
    }
}

