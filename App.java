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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

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

    public void start(Stage primaryStage) throws Exception {
        // start Screen forwards to gamestart
        primaryStage.setTitle("Start Screen");

        grid.setAlignment(Pos.CENTER);
        grid.setVgap(hightGap);
        grid.setHgap(sideGap);

        // line 1
        Label Players = new Label("How many players:");
        grid.add(Players, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        // line 2
        Label maxmin = new Label("min:2 max:8");
        grid.add(maxmin, 0, 2);

        Button btn = new Button("Select");
        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(btn, 1, 2);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                playerAmount = getIntFromTextField(userTextField);

                // Checks playamount input, if not in range give error msg
                if (playerAmount > maxPlayers || playerAmount < minPlayers) {
                    errorMSG();

                } else {
                    try {
                        // sets range between score / players
                        xRange = width / (playerAmount + 1);
                        // starts gameloop
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
            score.add(new Score(20, i));
            spiller.add(new Player(200, i));
        }

        root.getChildren().add(map.tilePane);
        root.getChildren().add(canvas);
        // root.getChildren().add(map.mapRoot);

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
