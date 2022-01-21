// Gustav Clausen s214940
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

public class CustomLabel extends Label {
    private final static String FONT_PATH = "/resources/kenvector_future.ttf";

    public CustomLabel(String text) {

        setPrefWidth(130);
        setPrefHeight(50);
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("/resources/yellow_button_pressed.png", 130, 50, false, false),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        setBackground(new Background(backgroundImage));
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10));
        setText(text);
        setLabelFont();
    }

    private void setLabelFont() {
        setFont(Font.loadFont(getClass().getResourceAsStream(FONT_PATH), 9));
    }

}
