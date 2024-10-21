package screen;

import database.Database;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import utils.UIComponents;
import utils.UserSession;

public class LoginScreen {
    private VBox layout;
    private TextField txtCPF;
    private PasswordField txtSenha;

    public LoginScreen(Stage stage) {
        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Logo da LotoFácil
        Label lblLogo = new Label("LOTOFÁCIL");
        lblLogo.setStyle("-fx-font-size: 32px; -fx-text-fill: #800080; -fx-font-weight: bold;");

        txtCPF = UIComponents.createTextField("CPF");
        txtSenha = UIComponents.createPasswordField("Sua senha");

        Button btnEntrar = UIComponents.createButton("Entrar", "-fx-background-color: #007BFF; -fx-text-fill: white;", e -> {
            if (Database.checkCredentials(txtCPF.getText(), txtSenha.getText())) {
                UserSession.setLoggedInUserCpf(txtCPF.getText()); // Configura o CPF do usuário logado
                MainScreen mainScreen = new MainScreen(stage);
                stage.setScene(new Scene(mainScreen.getLayout(), 800, 600));
            } else {
                showAlert("Erro de Login", "CPF ou Senha inválidos.");
            }
        });

        Button btnRegistrar = UIComponents.createButton("Registrar", "-fx-background-color: #007BFF; -fx-text-fill: white;", e -> 
            stage.setScene(new Scene(new RegisterScreen(stage).getLayout(), 400, 500))
        );

        Hyperlink forgotPassword = new Hyperlink("Esqueci minha senha");

        layout.getChildren().addAll(lblLogo, txtCPF, txtSenha, btnEntrar, btnRegistrar, forgotPassword);
    }

    public VBox getLayout() {
        return layout;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
