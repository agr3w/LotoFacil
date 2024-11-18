package screen.user;

import database.Database;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;
import utils.UserSession;

public class LoginScreen {
    private VBox layout;
    private TextField txtCPF;
    private PasswordField txtSenha;

    @SuppressWarnings("unused")
    public LoginScreen(Stage stage) {
        layout = new VBox(15); // Espaçamento entre os elementos
        stage.setTitle("LotoFacil - Login");
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #F8F9FA;"); // Fundo claro

        // Logo da LotoFácil
        ImageView logoImg = new ImageView(
                new Image(getClass().getResource("/imgs/logo-lotofacil.png").toExternalForm()));
        logoImg.setFitWidth(70);
        logoImg.setPreserveRatio(true);

        Label lblLogo = UIComponents.createLabel("LOTOFÁCIL",
                "-fx-font-size: 34px; -fx-text-fill: #800080; -fx-font-weight: bold;");

        // Campos de login
        Label lblLogiCpf = UIComponents.createLabel("CPF",
                "-fx-font-size: 14px; -fx-text-fill: #555555;");
        txtCPF = UIComponents.createTextField("Digite seu CPF",
                "-fx-max-width: 300px; -fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 10;");

        Label lblLoginSenha = UIComponents.createLabel("Senha",
                "-fx-font-size: 14px; -fx-text-fill: #555555;");
        txtSenha = UIComponents.createPasswordField("Digite sua senha",
                "-fx-max-width: 300px; -fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 10;");

        // Botão de entrar
        Button btnEntrar = UIComponents.createButton("Entrar",
                "-fx-background-color: #007BFF; " +
                        "-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 8 20;" +
                        "-fx-cursor: hand; fx-width: 200;",
                e -> {
                    if (Database.checkCredentials(txtCPF.getText(), txtSenha.getText())) {
                        UserSession.setLoggedInUserCpf(txtCPF.getText());
                        ScreenNavigator.navigateToMainScreen(stage);
                    } else {
                        UIComponents.showAlert("Erro de Login", "CPF ou Senha inválidos.", null);
                    }
                });

        // Botão de registro
        Button btnRegistrar = UIComponents.createButton("Registrar",
                "-fx-background-color: #007BFF; " +
                        "-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 8;" +
                        "-fx-cursor: hand; fx-width: 200;",
                e -> ScreenNavigator.navigateToRegisterScreen(stage));

        // Adicionando elementos ao layout
        layout.getChildren().addAll(logoImg, lblLogo, lblLogiCpf, txtCPF, lblLoginSenha, txtSenha, btnEntrar,
                btnRegistrar);
    }

    public VBox getLayout() {
        return layout;
    }
}
