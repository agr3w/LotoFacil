package screen.user;

import database.Database;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
        layout = new VBox(10);
        stage.setTitle("LotoFacil - Login");
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Logo da LotoFácil
        Label lblLogo = UIComponents.createLabel("LOTOFACIL",
                "-fx-font-size: 32px; -fx-text-fill: #800080; -fx-font-weight: bold;");

        // campos de login
        Label lblLogiCpf = UIComponents.createLabel("CPF", null);
        txtCPF = UIComponents.createTextField("CPF", "-fx-max-width: 250px;");
        Label lblLoginSenha = UIComponents.createLabel("Senha", null);
        txtSenha = UIComponents.createPasswordField("Sua senha", "-fx-max-width: 250px;");

        // botão de entrar
        Button btnEntrar = UIComponents.createButton("Entrar", "-fx-background-color: #007BFF; -fx-text-fill: white;",
                e -> {
                    if (Database.checkCredentials(txtCPF.getText(), txtSenha.getText())) {
                        UserSession.setLoggedInUserCpf(txtCPF.getText()); // Configura o CPF do usuário logado
                        ScreenNavigator.navigateToMainScreen(stage);
                    } else {
                        UIComponents.showAlert("Erro de Login", "CPF ou Senha inválidos.", null);
                    }
                });

        // botão de registro
        Button btnRegistrar = UIComponents.createButton("Registrar",
                "-fx-background-color: #007BFF; -fx-text-fill: white;",
                e -> ScreenNavigator.navigateToRegisterScreen(stage));

        Hyperlink forgotPassword = new Hyperlink("Esqueci minha senha");

        layout.getChildren().addAll(lblLogo, lblLogiCpf, txtCPF, lblLoginSenha, txtSenha, btnEntrar, btnRegistrar,
                forgotPassword);
    }

    public VBox getLayout() {
        return layout;
    }

}
