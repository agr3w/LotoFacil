package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class UIComponents {

    public static Label createLabel(String text, String style) {
        Label label = new Label(text);
        label.setStyle(style);
        return label;
    }

    public static TextField createTextField(String promptText, String style) {
        TextField textField = new TextField();
        textField.setStyle(style);
        textField.setPromptText(promptText);
        return textField;
    }

    public static PasswordField createPasswordField(String promptText, String style) {
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle(style);
        passwordField.setPromptText(promptText);
        return passwordField;
    }

    public static Button createButton(String text, String style,
            javafx.event.EventHandler<javafx.event.ActionEvent> eventHandler) {
        Button button = new Button(text);
        button.setStyle(style);
        button.setOnAction(eventHandler);
        return button;
    }

    // Exibe alertas
    public static Alert showAlert(String title, String message, AlertType type) {
        if (type == null) {
            type = AlertType.INFORMATION;
        }
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
        return alert;
    }

}
