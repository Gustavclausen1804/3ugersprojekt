import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import com.google.gson.*;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;


public class App extends Application {

    public static ArrayList<Player> spiller;
    public static ArrayList<Player> modstander;
    public static ArrayList<ToggleButton> toggleButtonList = new ArrayList<>();
    public static ArrayList<Slider> EnemyLevelList = new ArrayList<>();
    ToggleButton toggleSimpleAi = new ToggleButton("Enable simple AI shots");
    ToggleButton pauseButton = new ToggleButton("\u23f8");
    CustomButton exitButton = new CustomButton("EXIT");
    CustomButton backToMainButton = new CustomButton("Main");

    MapGeneration map;
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

    boolean lightBruteForce = false; //TODO: Skal have en boolean


    int winnerScore;
    static boolean gameEnded;
    Group winnerG = new Group();;

    static int turn = 1;
     
    boolean gamePaused;
    
    ArrayList<TextField> playerNameTextField = new ArrayList<>();

    static Image backGroundImage;
    static ArrayList<Image[]> playerImage = new ArrayList<Image[]>();
    static Image[] explosionImage = new Image[12];

    static int explosionRadius = 50;

    boolean enemyEnable = true;
    static int frameCount = 0;

    boolean pleaseForTheLoveOfGodOnlyRunOnce;

    public void start(Stage primaryStage) throws Exception {
        // start Screen forwards to gamestart
        primaryStage.setTitle("Gorillas But Better");
        primaryStage.setResizable(false); 
        Image icon = new Image("paul.png");
        primaryStage.getIcons().add(icon);
        if(!pleaseForTheLoveOfGodOnlyRunOnce){
            primaryStage.initStyle(StageStyle.UNDECORATED);
            pleaseForTheLoveOfGodOnlyRunOnce = true;
        }

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(hightGap);
        grid.setHgap(sideGap);
        // line 1
        CustomLabel Players = new CustomLabel("Select players: ");
        grid.add(Players, 0, 1);
        
        //Creates a slider, for the number of players playing
        Slider playerSlider = new Slider(minPlayers, maxPlayers, 1);
        playerSlider.setMajorTickUnit(1);
        playerSlider.setBlockIncrement(1);
        playerSlider.setMinorTickCount(0);
        playerSlider.setShowTickLabels(true);
        playerSlider.setSnapToTicks(true);
        playerSlider.setShowTickMarks(true);

        grid.add(playerSlider, 1, 1);

        //Creates the start Button, which upon pressing leads to the nameselect screen
        CustomButton startButtonMainScreen = new CustomButton("Play");
        startButtonMainScreen.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add( startButtonMainScreen, 1, 2);

        //Creates a button which upon pressing lead to the score board screen
        CustomButton ScoreBoardButton = new CustomButton("History");
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

        //Code describes what happens, when pressing on the start button
        startButtonMainScreen.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                playerAmount = (int) playerSlider.getValue();

                // Checks playamount input, if not in range give error msg
                
                try {
                    // sets range between score / players
                    xRange = width / (playerAmount + 1);
                    // goes name selection stage
                    playerNames(primaryStage);
                  } catch (Exception e1) {
                    e1.printStackTrace();
                }
                
            }
        });

        Scene scene = new Scene(grid, width, height);
        scene.getStylesheets().add("stylesheet.css");
        //Adds the background image and displays it
        Image backG = new Image("resources/background.png", App.width, App.height, false, false);
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
        GridPane nameGrid = new GridPane();
        nameGrid.setAlignment(Pos.CENTER);
        nameGrid.setVgap(hightGap);
        nameGrid.setHgap(sideGap);
        ArrayList<CustomLabel> playerLabel = new ArrayList<>();

        //Creates a label for the number of players selected in main screen 
        for (int i = 0; i < playerAmount; i++) {
            playerLabel.add(new CustomLabel("Player " + (i + 1) + " name: "));
            nameGrid.add(playerLabel.get(i), 0, i);
            playerNameTextField.add(new TextField("Player " + (i + 1)));
            nameGrid.add(playerNameTextField.get(i), 1, i);
            playerNameTextField.get(i).setPrefSize(130, 50);
            playerNameTextField.get(i).setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            //a Toggle button for if the player is an AI or not
            toggleButtonList.add(new ToggleButton("Toggle AI"));
            nameGrid.add(toggleButtonList.get(i), 2, i);
            // a slider for the level of the AI
            EnemyLevelList.add(new Slider(0, 3, 1));
            EnemyLevelList.get(i).setMajorTickUnit(1);
            EnemyLevelList.get(i).setBlockIncrement(1);
            EnemyLevelList.get(i).setMinorTickCount(0);
            EnemyLevelList.get(i).setShowTickLabels(true);
            EnemyLevelList.get(i).setSnapToTicks(true);
            EnemyLevelList.get(i).setShowTickMarks(true);
            //Slider hidden by default
            EnemyLevelList.get(i).setVisible(false);
            nameGrid.add(EnemyLevelList.get(i), 3, i);
            
        }
        //If the toggle button is pressed the slider is shown, pressed again it is hidden
        for(int i = 0; i < toggleButtonList.size(); i++){
            final int k = i;
            toggleButtonList.get(k).setOnAction(e -> {
                if(toggleButtonList.get(k).isSelected()) {
                    EnemyLevelList.get(k).setVisible(true);
                } else if(!toggleButtonList.get(k).isSelected()){
                    EnemyLevelList.get(k).setVisible(false);
                }
            });
        }
        
        
        CustomLabel scoreLabel = new CustomLabel("Score to Beat:");
        nameGrid.add(scoreLabel, 0, playerAmount);
        nameGrid.add(toggleSimpleAi, 2, playerAmount);

        //Adds a slider in which you put in the number of points a player need to reach in order to win
        Slider scoreSlider = new Slider(1, 10, 1);
        scoreSlider.setMajorTickUnit(1);
        scoreSlider.setBlockIncrement(1);
        scoreSlider.setMinorTickCount(0);
        scoreSlider.setShowTickLabels(true);
        scoreSlider.setSnapToTicks(true);
        scoreSlider.setShowTickMarks(true);
        nameGrid.add(scoreSlider, 1, playerAmount);

        
        //Creates a button which upon pressing starts the game
        CustomButton startButton = new CustomButton("Begin");
        startButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        nameGrid.add(startButton, 1, playerAmount + 1);

        startButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                // starts gameloop
                try {
                    winnerScore = (int) scoreSlider.getValue();
                    gamestart(stage);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        //Adds the background
        Image backG = new Image("resources/background.png", App.width, App.height, false, false);
        BackgroundImage bImg = new BackgroundImage(backG, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Background bGround = new Background(bImg);
        nameGrid.setBackground(bGround);

        Scene scene = new Scene(nameGrid, width, height);
        scene.getStylesheets().add("players.css");
        stage.setScene(scene);
        stage.show();
    }

    public void scoreBoardScreen(Stage stage) throws Exception {
        GridPane nameGrid = new GridPane();
        nameGrid.setAlignment(Pos.CENTER);
        nameGrid.setVgap(hightGap);
        nameGrid.setHgap(sideGap);

        // CustomLabel btn = new CustomLabel("Game History");
        // nameGrid.add(btn, 0, 0);

        JsonArray scoresArray = Score.readJsonArray(); // Read the scoreboard.json with our ReadJsonArray method.
        //Creates an arraylist which contains a label, which has the number for match
        ArrayList<CustomLabel> gameNumber = new ArrayList<>();
        //Creates an arraylist with an arralist which contains a label
        //The outer arrayList keeps track of the matches, and the inner arrayList keeps track of the label for each player in said match
        ArrayList<ArrayList<CustomLabel>> playerLabel = new ArrayList<>();

        // Create the scoreboard with the number of players on the match.
        for (int i = 0; i < scoresArray.size(); i++) {
            //Adds the label with the number of the game
            gameNumber.add(new CustomLabel("Game #" + (i + 1)));
            nameGrid.add(gameNumber.get(i), 0, i);
            //Adds a label, each label with the player name and their respective score
            playerLabel.add(new ArrayList<CustomLabel>());
            for(int j = 0; j < scoresArray.get(i).getAsJsonObject().entrySet().toArray().length;j++){
                playerLabel.get(i).add(new CustomLabel(scoresArray.get(i).getAsJsonObject().entrySet().toArray()[j].toString()));
                nameGrid.add(playerLabel.get(i).get(j), j+1, i);
            }

        }
        //Creates a button which goes back to main screen
        CustomButton backButton = new CustomButton("Back");
        backButton.setMaxSize(130, 50);
        nameGrid.add(backButton, 1, scoresArray.size());

        backButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                try {
                    start(stage);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        //Adds the background
        Image backG = new Image("resources/background.png", App.width, App.height, false, false);
        BackgroundImage bImg = new BackgroundImage(backG, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Background bGround = new Background(bImg);
        nameGrid.setBackground(bGround);

        Scene scene = new Scene(nameGrid, width, height);
        stage.setScene(scene);
        stage.show();

    }

    public void gamestart(Stage stage) throws Exception {
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //Creates the map class
        map = new MapGeneration();

        //creates a timeline which is used to create a method which keeps running
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc,stage)));
        tl.setCycleCount(Timeline.INDEFINITE);

        // Creates scores and players in arrayLists
        spiller = new ArrayList<Player>();
        modstander = new ArrayList<Player>();


        for (int i = 0; i < playerAmount; i++) {
            // creates a variable which contains a players name, the field is left empty a default name i selected
            String name = playerNameTextField.get(i).getText();
            if (name.length() == 0) {
                name = "Player " + i+1;
            }
            //If the toggle button was on an AI is created with the chosen name and level
            if (toggleButtonList.get(i).isSelected()) {
                spiller.add(new Enemy(i+1, name, (int)EnemyLevelList.get(i).getValue()));
            }
             //If the toggle button was off a player is created with the chosen name
            if (!toggleButtonList.get(i).isSelected()) {
                spiller.add(new Player(i+1, name));
            }
        }
        root.getChildren().add(canvas);
        spiller.forEach((p) -> {
            root.getChildren().add(p.playerRoot);
        });
        root.getChildren().add(winnerG);

        //Pause Button
        pauseButton.setLayoutX(0); pauseButton.setLayoutY(0); pauseButton.setMaxSize(28, 28); pauseButton.setMinSize(28, 28);
        //Exit Button
        exitButton.setMaxSize(200, 100);exitButton.setLayoutX(640-100);exitButton.setLayoutY(140+120); exitButton.setVisible(false);
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                    Platform.exit();
            }
        });
        //Back to main Button
        backToMainButton.setMaxSize(200, 100);backToMainButton.setLayoutX(640-100);backToMainButton.setLayoutY(140); backToMainButton.setVisible(false);
        backToMainButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                backToMain();
            }
        });
        winnerG.getChildren().addAll(pauseButton,exitButton,backToMainButton);
        
        

        loadSprites();

        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add("players.css");
        //adds detection if certin events are triggered
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey); //KeyPressed
        scene.setOnMouseMoved(this::handleMouseMove);   //mouseMoved
        scene.setOnMousePressed(this::handleMousePressed); //mousePressed


        stage.setScene(scene);
        stage.show();
        tl.play();

    }

    private void handleMouseMove(MouseEvent event) { // is triggered when the mousese is moved

        // gets the  Mouse' x & y coordinate
        if(!gamePaused){
        this.MouseX = event.getX();
        this.MouseY = event.getY();
        }
        
    }

    private void handleMousePressed(MouseEvent event) { // is triggered when the mousese is Pressed
        if(!gamePaused){
            
            spiller.forEach((p) -> {
                if (turn == p.id && !gameEnded) { // If it is a players turn and the game hasn't ended the following is run
                    if (!p.parameterChosen && !p.shootsFired) { // if the player hasn't chosen force and angle for their shot, 
                                                                // and a shot hasn't been created yet
                        p.shootingAngle = p.forceAndAngle[1];  // takes the value and saves it in an array
                        if(p.forceAndAngle[0] > 40){
                            p.forceAndAngle[0] = 40;
                        }
                        p.shootingForce = p.forceAndAngle[0];
                        p.parameterChosen = true;  
                        p.shootButton.setVisible(true);  // shows the button which shoots a bullet
                    }

                }
            });
        }
    }

    static double[] getForcesFromMouse(double[] PlayerPos, double[] mousePosition) {
        // player x & y coordinate
        double ForceVectorX = (mousePosition[0] - PlayerPos[0]); // gets the distance between mouse and the player in the first axis
        double ForceVectorY = (mousePosition[1] - PlayerPos[1]); // gets the distance between mouse and the player in the second axis

        double[] vector1 = new double[] { ForceVectorX, ForceVectorY }; // Is the direct vector between the player and the mouse
        double[] vector2 = new double[] { ForceVectorX, 0 }; // is the vector between the player and the mouse in first axis

        // the following lines calculate the angle between the player and the mouse in degrees
        // the calculation is done according to the formuler cos(v) = (a*b)/(|a|*|b|)
        double crossProduct = vector1[0] * vector2[0] + vector1[1] * vector2[1]; // calculates the crossproduct between the the two vector

        double vector1Length = Math.sqrt(Math.pow(vector1[0], 2) + Math.pow(vector1[1], 2));    // calculates the length of the first vector acording
                                                                                                // acording to pythagors' therom

        double cosToAngle = crossProduct / (vector1Length * vector2[0]); 

        double VectorAngle = Math.toDegrees(Math.acos(cosToAngle));

        if (ForceVectorY > 0) {  // if the angle is creater than 180 degreess we minus 360 with angle
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
                if (p.parameterChosen) { // if a player regrets their chosen parameters a keyevent cancels the values
                    p.parameterChosen = false;
                    p.shootButton.setVisible(false);
                }
            }
        });
    }



    private void run(GraphicsContext gc, Stage stage) { // the following is run every frame 
 
        map.drawMap(gc); // the background and the map is draw first

        spiller.forEach((playerList) -> { // Runs through all registered playerobjects
            playerList.move();
            playerList.draw(gc);

            if (playerList.playerShot != null) { // Shot is 'null' when non-excisting or removed.

                playerList.playerShot.updateShot(); // Update the shot's position.
                playerList.playerShot.drawShot(gc); // Draw the show on the screen.

                if (playerList.playerRemoveShot() == true) { // When the shot i removed by colission or hit.
                    turn++; // Next turn
                }
            }

            // Check if a Player Won
            if (playerList.PlayerScore.counter == winnerScore) {
               
                if (!gameEnded) { // this code is only run once
                    pauseButton.setVisible(false);
                    gameEnded = true;

                    //Updates the score to scoreboard file
                    Score.toJSONString();

                    //Creates a button which leads back to the main page
                    CustomButton backButton = new CustomButton("Back");
                    backButton.setVisible(true);
                    backButton.setMaxSize(200, 100);
                    backButton.setLayoutX(640-100);
                    backButton.setLayoutY(140);
                    backButton.setOnAction(new EventHandler<ActionEvent>() {
        
                        @Override
                        public void handle(ActionEvent e) {
                            backToMain();
                        }
                    });
                    winnerG.getChildren().add(backButton);
                }
                gc.setFont(Font.font("Verdana", 30));
                gc.fillText(playerList.name + " IS THE WINNER!!!!", 640, 100);
                
                
                
            }
            if(!gamePaused){
                if(playerList instanceof Enemy){
                    if(playerList.id == turn && !gameEnded && !playerList.shootsFired){
                        playerList.shoot();
                        playerList.shootsFired = true;
                    }
                }
            }


        });
        


        //Pause Button
        if(pauseButton.isSelected() && !gameEnded) {
            gc.setGlobalAlpha(0.7);
            pauseButton.setText("\u25b6");
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, width, height);
            gc.setGlobalAlpha(1);
            gamePaused = true;
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("Verdana", 40));
            gc.fillText("PAUSE", 640, 100);
            backToMainButton.setVisible(true);
            exitButton.setVisible(true);
            

        } else if(!pauseButton.isSelected()){
            pauseButton.setText("\u23f8");
            gamePaused = false;
            backToMainButton.setVisible(false);
            exitButton.setVisible(false);
        }
        

        if (turn > spiller.size()) { // when every player has had their turn the first player has their turn again
            turn = 1;
        }
        
        //keeps track of the number of frames, used for animation
        frameCount++;
    }
    

    

    

    void loadSprites() {
        shotExplosionBilleder();
        LoadPlayerPictures();

        // Load background
        backGroundImage = new Image("/resources/background.png", App.width, App.height, false, false);
    }

    void shotExplosionBilleder() {
        // Load explosion images into array.
        Image explosionSprite = new Image("/resources/explosionSprite.png", explosionRadius * 12 * 2,
                explosionRadius * 2, false, false);
        double width = explosionSprite.getWidth() / 12;
        PixelReader reader = explosionSprite.getPixelReader();

        for (int i = 0; i < explosionImage.length; i++) {
            explosionImage[i] = new WritableImage(reader, (int) width * i, 0, (int) width,
                    (int) explosionSprite.getHeight());
        }
    }

    void LoadPlayerPictures() {
        // Load player images into arrayList which contains an array.
        for (int j = 0; j < 4; j++) {
            playerImage.add(new Image[4]);
            Image playerSprite = new Image("/resources/player" + j + ".png", 30 * 4, 30, false, false);
            double width = playerSprite.getWidth() / 4;
            PixelReader reader = playerSprite.getPixelReader();
            for (int i = 0; i < playerImage.get(j).length; i++) {
                playerImage.get(j)[i] = new WritableImage(reader, (int) width * i, 0, (int) width,
                        (int) playerSprite.getHeight());
            }
        }
    }

    void backToMain(){
        try {
            URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
            File file = new File(location.getPath());
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();  
            desktop.open(file);
            TimeUnit.SECONDS.sleep(2);
            Platform.exit();   
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
