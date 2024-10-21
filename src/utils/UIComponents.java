package utils;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UIComponents {
    
    public static TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        return textField;
    }
    
    public static PasswordField createPasswordField(String promptText) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(promptText);
        return passwordField;
    }
    
    public static Button createButton(String text, String style, javafx.event.EventHandler<javafx.event.ActionEvent> eventHandler) {
        Button button = new Button(text);
        button.setStyle(style);
        button.setOnAction(eventHandler);
        return button;
    }
}
