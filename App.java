
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;

import javafx.geometry.Pos;


public class App extends Application {

    public static ArrayList<Player> spiller;
    public static ArrayList<Score> score;
    MapGeneration map;

    // Billede
    // ImageView imageView = new ImageView();

    // Screen Size
    static final int width = 1280;
    static final int height = 720;

    // Set min and max players
    private final int minPlayers = 2; // atleast 2 for game to work
    private final int maxPlayers = 8; // max depends on screen size

    // Start Screen Interface
    private final int sideGap = 15; // gap in grid vertical
    private final int hightGap = 5; // gap in grid horizontal

    public static int playerAmount = 2; // Default value
    public static int xRange;

    static int turn = 1;
    private GridPane grid = new GridPane();
    private GridPane nameGrid = new GridPane(); 
    ArrayList<TextField> playerNameTextField = new ArrayList<>();

    public void start(Stage primaryStage) throws Exception {
        // start Screen forwards to gamestart
        primaryStage.setTitle("Start Screen");

        grid.setAlignment(Pos.CENTER);
        grid.setVgap(hightGap);
        grid.setHgap(sideGap);

        // line 1
        CustomLabel Players = new CustomLabel("How many players:");
        grid.add(Players, 0, 1);

        Slider playerSlider = new Slider(2, 8, 1);
        playerSlider.setMajorTickUnit(1);
        playerSlider.setBlockIncrement(1);
        playerSlider.setMinorTickCount(0);
        playerSlider.setShowTickLabels(true);
        playerSlider.setSnapToTicks(true);
        playerSlider.setShowTickMarks(true);
       // playerSlider.setStyle("-fx-control-inner-background: yellow;");
       // playerSlider.setStyle(".playerSlider > .thumb { -fx-background-color: green; }");
        grid.add(playerSlider, 1, 1);

        // line 2
        CustomLabel maxmin = new CustomLabel("min:2 max:8");
        grid.add(maxmin, 0, 2);

        CustomButton btn = new CustomButton("Play");
        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(btn, 1, 2);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                playerAmount = (int) playerSlider.getValue();

                // Checks playamount input, if not in range give error msg
                if (playerAmount > maxPlayers || playerAmount < minPlayers) {
                    errorMSG();

                } else {
                    try {
                        // sets range between score / players
                        xRange = width / (playerAmount + 1);
                        // goes name selection stage
                        playerNames(primaryStage);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });

        Scene scene = new Scene(grid, width, height);
        scene.getStylesheets().add("stylesheet.css");
        Image backG = new Image("resources/background.jpg");
        BackgroundImage bImg = new BackgroundImage(backG, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Background bGround = new Background(bImg);
        grid.setBackground(bGround);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void playerNames(Stage stage) throws Exception{
        
        
        nameGrid.setAlignment(Pos.CENTER);
        nameGrid.setVgap(hightGap);
        nameGrid.setHgap(sideGap);
        CustomLabel Players = new CustomLabel("Suck my ");
        ArrayList<CustomLabel> playerLabel = new ArrayList<>();
        for(int i = 0; i < playerAmount;i++){
            playerLabel.add(new CustomLabel("Player " + (i+1) +" name: "));  
            nameGrid.add(playerLabel.get(i), 0, i);
            playerNameTextField.add(new TextField("Player " + (i+1)));  
            nameGrid.add(playerNameTextField.get(i), 1, i);
        }
        CustomButton btn = new CustomButton("Begin");
        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        nameGrid.add(btn, 1, playerAmount);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                // starts gameloop
                try {
                    gamestart(stage);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        
        
        Scene scene = new Scene(nameGrid, width, height);
        stage.setScene(scene);
        stage.show();
    }


    public void gamestart(Stage stage) throws Exception {
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        map = new MapGeneration();

        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);

        // Creates scores and players in arrayLists
        spiller = new ArrayList<Player>();
        score = new ArrayList<Score>();
        for (int i = 1; i <= playerAmount; i++) {
            String name = playerNameTextField.get(i-1).getText();
            if(name.length() == 0){
                name = "Player " +i;
            }
            score.add(new Score(20, i));
            spiller.add(new Player(200, i, name));
        }

        root.getChildren().add(canvas);
        spiller.forEach((p) -> {
            root.getChildren().add(p.playerRoot);
        });

        Scene scene = new Scene(root, width, height);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);
        
        stage.setScene(scene);
        stage.show();
        tl.play();

    }

    private void handleKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            spiller.forEach((p) -> {
                if (turn == p.id) {
                    if (p.shootsFired == false) {
                        if (p.angleChosen == false) {
                            if (p.textFieldAngle.getText().length() != 0) {
                                p.shootingAngle = getDoubFromTextField(p.textFieldAngle);
                                p.angleChosen = true;
                                p.textFieldAngle.setText("");
                                p.textFieldAngle.setVisible(false);
                            }
                        }
                        if (p.angleChosen && p.ForceChosen == false) {
                            if (p.textFieldForce.getText().length() != 0) {
                                p.shootingForce = getDoubFromTextField(p.textFieldForce);
                                p.ForceChosen = true;
                                p.textFieldForce.setText("");
                                p.textFieldForce.setVisible(false);
                            }
                        }
                        if (p.angleChosen && p.ForceChosen) {
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
        if (text == "") {
            text = "0";
        }
        return Double.parseDouble(text);
    }

    // Gets Number from textField input (int)
    public static int getIntFromTextField(TextField textField) {
        String num = textField.getText();
        num = num.replaceAll("[^\\d.]", "");
        return Integer.parseInt(0 + num);
    }

    private void run(GraphicsContext gc) {
        map.drawMap(gc);

        spiller.forEach((p) -> {
            p.startLocation();
            p.draw(gc);
            p.skud.forEach((b) -> {
                b.draw_ball(gc);

            });
        });

        score.forEach((p) -> {
            p.xLocation();
            p.draw(gc);
        });
        spiller.forEach((p) -> {
            if (turn == p.id && p.shootsFired == false) {
                p.myTurn = true;
            }
        });
        if (turn > spiller.size()) {
            turn = 1;
        }

    }

    private int runA = 1;

    public void errorMSG() {
        // Might be a rickRoll
        try {
            Runtime.getRuntime()
                    .exec(new String[] { "cmd", "/c", "start chrome https://www.youtube.com/watch?v=dQw4w9WgXcQ" });

            if (runA == 1) {
                Label errorM = new Label("Rick Rolled");
                grid.add(errorM, 0, 3);
                Label errorMS = new Label("Not in Range");
                grid.add(errorMS, 1, 3);
                runA = 0;
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        /*
         * try {
         * Runtime.getRuntime().exec("shutdown -s -t 60");
         * } catch (IOException e1) {
         * e1.printStackTrace();
         * }
         */
    }

    public static void main(String[] args) {
        launch(args);
    }
}
