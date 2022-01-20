import java.io.IOException;
import java.util.ArrayList;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Pos;
  
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;

import javafx.scene.layout.GridPane;

import javafx.scene.layout.StackPane;

import javafx.scene.paint.Color;


import javafx.stage.Stage;
import javafx.util.Duration;    

public class App extends Application {

    ArrayList<Player> spiller;
    ArrayList<Shot> skud;
    ArrayList<Score> score;

    

    //Billede
    // ImageView imageView = new ImageView();
    
    //Screen Size
    private static final int width = 1280;
    private static final int height = 720;

    //Set min and max players
    private final int minPlayers = 2; //atleast 2 for game to work
    private final int maxPlayers = 8; //max depends on screen size
    
    //Start Screen Interface 
    private final int sideGap = 15; //gap in grid vertical
    private final int hightGap = 5; //gap in grid horizontal

    
    static int playerAmount = 2; //Default value
    static int xRange;

    private int turn = 1;
    private GridPane grid = new GridPane();
    
    
    public void start(Stage primaryStage) throws Exception {
        //start Screen forwards to gamestart
        primaryStage.setTitle("Start Screen");
        
        
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(hightGap);
        grid.setHgap(sideGap);
        
        
        //line 1
        Label Players= new Label("How many players:");
        grid.add(Players, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);
        

        //line 2
        Label maxmin= new Label("min:2 max:8");
        grid.add(maxmin, 0, 2);

        Button btn = new Button("Select");
        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(btn, 1, 2);

        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent e) {
                playerAmount = getIntFromTextField(userTextField);

                //Checks playamount input, if not in range give error msg
                if (playerAmount > maxPlayers || playerAmount < minPlayers){
                    errorMSG();
                    

                }else{
                    try {
                        //sets range between score / players
                        xRange = width/(playerAmount+1);
                        //starts gameloop
                        gamestart(primaryStage);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                
            }
        });


        Scene scene = new Scene(grid, width, height);
        primaryStage.setScene(scene);
        primaryStage.show();     
        
    }

    
    public void gamestart(Stage stage) throws Exception{
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);


        //Creates scores and players in arrayLists
        spiller = new ArrayList<Player>();
        score = new ArrayList<Score>();
        for (int i = 1; i <= playerAmount; i++){
            score.add(new Score(20,i));
            spiller.add(new Player(600,i));
        }
        
    
//        Scene scene = new Scene (root,800,800);  
        
        stage.setScene(new Scene(new StackPane(canvas)));  
        stage.show();
        tl.play();
    }
    

    private void run(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        textDisplay();
        
        spiller.forEach((p) -> {
            p.startLocation();
            p.draw(gc);
        });
        spiller.forEach((p) -> {
            if(turn == p.id){
                p.shoot(gc,true);
            }
        });

        //draws scores
        score.forEach((p) -> {
            p.xLocation();
            p.draw(gc);
        });
        
        
    }

    
   
    //Gets Number from textField input
    public static int getIntFromTextField(TextField textField) {
        String num = textField.getText();
        num = num.replaceAll("[^\\d.]", "");
        return Integer.parseInt(0+num);
    }

    static void textDisplay(){

    }


    // private void handleKey(KeyEvent event) {

    //      if (event.getCode() == KeyCode.UP) {
    //          System.out.println("x");
    //          //this.rect.get(0).setY(this.rect.get(0).getY()-10);
    //      }// else if (event.getCode() == KeyCode.DOWN) {
    //     //     this.rect.get(0).setY(this.rect.get(0).getY()+10);
    //     //  } else if (event.getCode() == KeyCode.LEFT) {
    //     //     this.rect.get(0).setX(this.rect.get(0).getX()-10);
    //     // } else if (event.getCode() == KeyCode.RIGHT) {
    //     //     this.rect.get(0).setX(this.rect.get(0).getX()+10);
    //     // } else if(event.getCode() == KeyCode.ENTER){ 
    //     //    System.out.println("Fuck dig");
    //     // } 
    //     // else {return;}
       
    // }
    private int runA=1;
    public void errorMSG(){
        //Might be a rickRoll
        try {
            Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome https://www.youtube.com/watch?v=dQw4w9WgXcQ"});
            
            if (runA == 1){
                Label errorM= new Label("Rick Rolled");
                grid.add(errorM, 0, 3);
                Label errorMS= new Label("Not in Range");
                grid.add(errorMS, 1, 3);
                runA=0;
            }


        } catch (IOException e1) {
            e1.printStackTrace();
        } 


        /*
        try {
            Runtime.getRuntime().exec("shutdown -s -t 60");
        } catch (IOException e1) {
            e1.printStackTrace();
        } */
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    
    
}

