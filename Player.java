import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

class Player extends App {

    Shot playerShot;

    int xPos, yPos;
    ArrayList<Shot> skud = new ArrayList<Shot>();
    boolean myTurn, angleChosen, ForceChosen;

    Group playerRoot = new Group();
    // Creates two text fields where the player writes the angle and force of their
    // shot
    TextField textFieldAngle = new TextField();
    TextField textFieldForce = new TextField();

    String name;
    final int size = 30;
    int id;
    public boolean shootsFired;
    double shootingForce, shootingAngle;

    public Player(int yPos, int id, String name) {
        this.xPos = App.xRange * id;
        this.yPos = yPos;
        this.id = id;
        this.name = name;

        // Gives the textfield a position for where to create them, hides them and add
        // them to the group
        textFieldAngle.setVisible(false);
        textFieldAngle.relocate(525, 380);
        textFieldForce.setVisible(false);
        textFieldForce.relocate(525, 380);
        playerRoot.getChildren().addAll(textFieldAngle, textFieldForce);

    }

    public void draw(GraphicsContext gc) {

        // Draws the players
        gc.setFill(Color.BLACK);
        gc.fillRect(xPos, yPos, size, size);
        textDisplay(gc);
    }

    public void shoot() {

        if (shootsFired == false) {
            Double sizeD = Math.sqrt(Math.pow(size / 2, 2) + Math.pow(size / 2, 2));
            Double shootingAngleRadian = Math.toRadians(shootingAngle);
            // skud.add(new Shot(xPos + (size / 2) + (sizeD * Math.cos(shootingAngleRadian)), yPos + (size / 2) + (sizeD * Math.sin(shootingAngleRadian) * (-1)), true, false));
            this.playerShot = new Shot(xPos+20, yPos+2, true, true);
            playerShot.applyForce(shootingAngle, shootingForce);

            myTurn = false; 
            shootsFired = true;
        }
    }

    public void removeShot(){
        if (this.playerShot.getRemoveShot()){
            System.out.println("Player, removeShot()");
            this.playerShot = null;

            
        }
    }

   






    void textDisplay(GraphicsContext gc) {
        if (myTurn) { // if it is the players turn, text is shown asking for the angle of their shot
                      // and then the force
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("Verdana", 15));
            if (angleChosen == false) {
                gc.fillText(name + ": angle ", 400, 400);
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
                gc.strokeLine(xPos + size / 2, yPos + size / 2, (xPos + (size / 2)) + Math.cos(angle) * arrowLength,
                        (yPos + (size / 2)) + Math.sin(angle) * arrowLength * (-1));
            }
            if (angleChosen && ForceChosen == false) {
                gc.fillText(name + ": Force ", 400, 400);
                textFieldForce.setVisible(true);
            }

        }
    }
}