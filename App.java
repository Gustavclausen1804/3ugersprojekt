
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import com.google.gson.*;

import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
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

    public static ArrayList<Player> spiller;
    public static ArrayList<Player> modstander;

    MapGeneration map;

    // Billede
    // ImageView imageView = new ImageView();

    // Screen Size
    static final int width = 1280;
    static final int height = 720;

    // Mouse x & y coordinate
    public static double MouseX;
    public static double MouseY;

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

    static Image[] explosionImage = new Image[11];
    static int explosionRadius = 50;

    static int frameCount = 0;

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
        // playerSlider.setStyle(".playerSlider > .thumb { -fx-background-color: green;
        // }");

        grid.add(playerSlider, 1, 1);

        // line 2
        CustomLabel maxmin = new CustomLabel("min:2 max:8");
        grid.add(maxmin, 0, 2);

        CustomButton btn = new CustomButton("Play");
        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(btn, 1, 2);

        CustomButton ScoreBoardButton = new CustomButton("View Scoreboard");
        ScoreBoardButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(ScoreBoardButton, 1, 3);
        ScoreBoardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    scoreBoardScreen(primaryStage);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
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

    public void playerNames(Stage stage) throws Exception {

        nameGrid.setAlignment(Pos.CENTER);
        nameGrid.setVgap(hightGap);
        nameGrid.setHgap(sideGap);
        ArrayList<CustomLabel> playerLabel = new ArrayList<>();
        for (int i = 0; i < playerAmount; i++) {
            playerLabel.add(new CustomLabel("Player " + (i + 1) + " name: "));
            nameGrid.add(playerLabel.get(i), 0, i);
            playerNameTextField.add(new TextField("Player " + (i + 1)));
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

    public void scoreBoardScreen(Stage stage) throws Exception {

        nameGrid.setAlignment(Pos.CENTER);
        nameGrid.setVgap(hightGap);
        nameGrid.setHgap(sideGap);

        // CustomLabel btn = new CustomLabel("Game History");
        // nameGrid.add(btn, 0, 0);

        JsonArray scoresArray = Score.readJsonArray(); // Read the scoreboard.json with our ReadJsonArray method.
        ArrayList<CustomLabel> gameNumber = new ArrayList<>();
        ArrayList<CustomLabel> player1Label = new ArrayList<>();
        ArrayList<CustomLabel> player2Label = new ArrayList<>();

        // Create the scoreboard with two players only. If we need more players. We can
        // perhaps use ForEach.
        for (int i = 0; i < scoresArray.size(); i++) { // evt. køre den omvendt så det seneste spil bliver vist først.
            gameNumber.add(new CustomLabel("Game #" + (i + 1)));
            nameGrid.add(gameNumber.get(i), 0, i);
            System.out.println(scoresArray.get(i).getAsJsonObject().entrySet().toArray()[1].toString());
            player1Label.add(new CustomLabel(scoresArray.get(i).getAsJsonObject().entrySet().toArray()[0].toString()));
            // System.out.println(scoresArray.get(i).toString());
            nameGrid.add(player1Label.get(i), 1, i);
            player2Label.add(new CustomLabel(scoresArray.get(i).getAsJsonObject().entrySet().toArray()[1].toString()));
            nameGrid.add(player2Label.get(i), 2, i);
        }
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
        modstander = new ArrayList<Player>();

        for (int i = 1; i <= playerAmount; i++) {
            String name = playerNameTextField.get(i - 1).getText();
            if (name.length() == 0) {
                name = "Player " + i;
            }

            spiller.add(new Player(i, name));
        }

        root.getChildren().add(canvas);
        spiller.forEach((p) -> {
            root.getChildren().add(p.playerRoot);
        });

        shotExplosionBilleder();

        Scene scene = new Scene(root, width, height);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);
        scene.setOnMouseMoved(this::handleMouseMove);
        scene.setOnMousePressed(this::handleMousePressed);
        stage.setScene(scene);
        stage.show();
        tl.play();

    }

    private void handleMouseMove(MouseEvent event) {

        // Mouse x & y coordinate
        this.MouseX = event.getX();
        this.MouseY = event.getY();
    }

    private void handleMousePressed(MouseEvent event) {

        spiller.forEach((p) -> {
            if (turn == p.id) {
                if (!p.parameterChosen) {
                    p.shootingAngle = p.forceAndAngle[1];
                    p.shootingForce = p.forceAndAngle[0];
                    p.parameterChosen = true;
                    p.btn.setVisible(true);
                }

            }
        });
    }

    static double[] getForcesFromMouse(double[] PlayerPos, double[] mousePosition) {
        // player x & y coordinate
        double ForceVectorX = (mousePosition[0] - PlayerPos[0]); // To invert, so it is easier to set angle for humans
        double ForceVectorY = (mousePosition[1] - PlayerPos[1]); // To invert, so it is easier to set angle for humans

        double[] vector1 = new double[] { ForceVectorX, ForceVectorY }; // Is the vector from the playpos to the mouse
        double[] vector2 = new double[] { ForceVectorX, 0 };

        double crossProduct = vector1[0] * vector2[0] + vector1[1] * vector2[1];

        double vector1Length = Math.sqrt(Math.pow(vector1[0], 2) + Math.pow(vector1[1], 2));

        double cosToAngle = crossProduct / (vector1Length * vector2[0]);

        double VectorAngle = Math.toDegrees(Math.acos(cosToAngle));

        if (ForceVectorY > 0) {
            VectorAngle = 360 - VectorAngle;
        }

        double ForceVectorLength = (Math.sqrt(Math.pow(ForceVectorX, 2) + Math.pow(ForceVectorY, 2))) / 10; // Vector
                                                                                                            // length
                                                                                                            // reduced
                                                                                                            // with
                                                                                                            // factor

        return new double[] { ForceVectorLength, VectorAngle };
    }

    // Input for each player.
    private void handleKey(KeyEvent event) {
        spiller.forEach((p) -> {
            if (turn == p.id) {
                if (p.parameterChosen) {
                    p.parameterChosen = false;
                    p.btn.setVisible(false);
                }
            }
        });
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

        spiller.forEach((playerList) -> { // Runs through all registered playerobjects
            playerList.move();
            playerList.draw(gc);

            if (playerList.playerShot != null) { // Shot is 'null' when non-excisting or removed.

                playerList.playerShot.updateShot(); // Update the shot's position.
                playerList.playerShot.drawShot(gc); // Draw the show on the screen.

                if (playerList.removeShot() == true) { // When the shot i removed by colission or hit.
                    turn++; // Next turn
                }
            }
            // if (turn == playerList.id && playerList.playerShot == null){ //
            // if (playerList.hitlerDidNothingWrong){ //Will return true when angle and
            // force has been set, otherwise false.
            // playerList.
            // playerList.hitlerDidNothingWrong = false;
            // }
            // }
        });

        if (turn > spiller.size()) {
            turn = 1;
            System.out.println("turn " + turn);
        }
        frameCount++;
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

    void shotExplosionBilleder() {
        // Load explosion images into array.
        for (int i = 0; i < explosionImage.length; i++) {
            explosionImage[i] = new Image("/resources/explosion" + i + ".png", explosionRadius * 2, explosionRadius * 2,
                    false, false);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
