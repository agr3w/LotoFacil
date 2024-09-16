import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterScreen {
    private VBox layout;

    public RegisterScreen(Stage stage) {
        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label lblCadastro = new Label("Cadastro de Usuário");
        lblCadastro.setStyle("-fx-font-size: 20px; -fx-text-fill: #800080; -fx-font-weight: bold;");

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome completo");

        TextField txtDataNasc = new TextField();
        txtDataNasc.setPromptText("Data de Nasc.");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");

        TextField txtCPF = new TextField();
        txtCPF.setPromptText("CPF");

        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("Senha");

        PasswordField txtConfSenha = new PasswordField();
        txtConfSenha.setPromptText("Conf Senha");

        CheckBox checkTermos = new CheckBox("Concordo com os Termos de Serviço");

        Button btnRegistrar = new Button("Registrar");
        btnRegistrar.setStyle("-fx-background-color: #800080; -fx-text-fill: white;");
        btnRegistrar.setOnAction(e -> {
            if (validateFields(txtNome, txtDataNasc, txtEmail, txtCPF, txtSenha, txtConfSenha, checkTermos)) {
                if (txtSenha.getText().equals(txtConfSenha.getText())) {
                    Database.saveUser(txtNome.getText(), txtEmail.getText(), txtCPF.getText(), txtSenha.getText());
                    showAlert("Registro Concluído", "Usuário registrado com sucesso.");
                    stage.setScene(new Scene(new LoginScreen(stage).getLayout(), 400, 300));
                } else {
                    showAlert("Erro de Registro", "Senhas não coincidem.");
                }
            }
        });

        Button btnTermos = new Button("Termos de Serviço");
        btnTermos.setOnAction(e -> stage.setScene(new Scene(new TermsScreen(stage).getLayout(), 400, 500)));

        layout.getChildren().addAll(lblCadastro, txtNome, txtDataNasc, txtEmail, txtCPF, txtSenha, txtConfSenha, checkTermos, btnRegistrar, btnTermos);
    }

    public VBox getLayout() {
        return layout;
    }

    private boolean validateFields(TextField txtNome, TextField txtDataNasc, TextField txtEmail, TextField txtCPF,
                                   PasswordField txtSenha, PasswordField txtConfSenha, CheckBox checkTermos) {
        if (txtNome.getText().isEmpty() || txtDataNasc.getText().isEmpty() || txtEmail.getText().isEmpty()
                || txtCPF.getText().isEmpty() || txtSenha.getText().isEmpty() || txtConfSenha.getText().isEmpty()) {
            showAlert("Erro de Registro", "Todos os campos devem ser preenchidos.");
            return false;
        }
        if (!checkTermos.isSelected()) {
            showAlert("Erro de Registro", "Você deve concordar com os Termos de Serviço.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
