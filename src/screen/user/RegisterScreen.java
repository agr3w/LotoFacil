package screen.user;

import java.time.format.DateTimeFormatter;
import database.Database;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;
import utils.ValidateDate;

@SuppressWarnings("unused")
public class RegisterScreen {
    private VBox layout;

    public RegisterScreen(Stage stage) {
        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Título da tela
        Label lblCadastro = UIComponents.createLabel("Cadastro de Usuário",
                "-fx-font-size: 22px; -fx-text-fill: #800080; -fx-font-weight: bold;");

        // Nome Completo
        Label lblNome = UIComponents.createLabel("Nome Completo:", null);
        TextField txtNome = UIComponents.createTextField("Digite seu nome completo", "-fx-max-width: 300;");

        // Data Nascimento
        Label lblDataNasc = UIComponents.createLabel("Data de Nascimento:", null);
        DatePicker datePickerNasc = UIComponents.createDatePicker("Selecione sua data de nascimento",
                "-fx-min-width: 300");

        // Email
        Label lblEmail = UIComponents.createLabel("Email", null);
        TextField txtEmail = UIComponents.createTextField("Digite seu Email", "-fx-max-width: 300;");

        // CPF
        Label lblCPF = UIComponents.createLabel("CPF", null);
        TextField txtCPF = UIComponents.createTextField("Digite seu CPF", "-fx-max-width: 300;");
        txtCPF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtCPF.setText(newValue.replaceAll("[^\\d]", "")); // Permite apenas números
            }
            if (newValue.length() > 11) {
                txtCPF.setText(newValue.substring(0, 11)); // Limita a 11 dígitos
            }
        });

        // Senha
        Label lblSenha = UIComponents.createLabel("Senha", null);
        PasswordField txtSenha = UIComponents.createPasswordField("Digite sua senha", "-fx-max-width: 300;");

        // Confirmar Senha
        Label lblConfSenha = UIComponents.createLabel("Confirmar senha", null);
        PasswordField txtConfSenha = UIComponents.createPasswordField("Confirme sua senha", "-fx-max-width: 300;");

        // Checkbox para termos de serviço
        CheckBox checkTermos = new CheckBox("Concordo com os Termos de Serviço");

        // Botão de Termos de Serviço
        Button btnTermos = UIComponents.createButton("Termos de Serviço",
                "-fx-background-color: #ffff; -fx-text-fill: black;",
                e -> ScreenNavigator.navigateToTermsScreen(stage));

        // Botão de retorno para login
        Button btnRetornoLogin = UIComponents.createButton("Retornar",
                "-fx-background-color: #FF0000; -fx-text-fill: white;",
                e -> ScreenNavigator.navigateToLoginScreen(stage));

        // Botão de Registrar
        Button btnRegistrar = UIComponents.createButton("Registrar",
                "-fx-background-color: #800080; -fx-text-fill: white;", e -> {
                    if (validateFields(txtNome, datePickerNasc, txtEmail, txtCPF, txtSenha, txtConfSenha,
                            checkTermos)) {
                        if (txtSenha.getText().equals(txtConfSenha.getText())) {
                            if (ValidateDate.isOfLegalAge(datePickerNasc.getValue())) {
                                // Tenta salvar o usuário e verifica o retorno
                                boolean userSaved = Database.saveUser(
                                        txtNome.getText(),
                                        txtEmail.getText(),
                                        txtCPF.getText(),
                                        datePickerNasc.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                                        txtSenha.getText());

                                if (userSaved) {
                                    UIComponents.showAlert("Registro Concluído", "Usuário registrado com sucesso.",
                                            null);
                                    ScreenNavigator.navigateToLoginScreen(stage);
                                } else {
                                    UIComponents.showAlert("Informação de Registro", "CPF ou e-mail já registrados.", null);
                                }
                            } else {
                                UIComponents.showAlert("Informação de Registro", "Usuário deve ser maior de idade.", null);
                            }
                        } else {
                            UIComponents.showAlert("Informação de Registro", "Senhas não coincidem", null);
                        }
                    }
                });

        // Adiciona todos os componentes ao layout
        layout.getChildren().addAll(lblCadastro, lblNome, txtNome, lblDataNasc, datePickerNasc, lblEmail, txtEmail,
                lblCPF, txtCPF,
                lblSenha, txtSenha, lblConfSenha, txtConfSenha, checkTermos, btnRetornoLogin, btnTermos, btnRegistrar);
    }

    // Validação dos campos de entrada, incluindo CPF e Email
    private boolean validateFields(TextField txtNome, DatePicker datePickerNasc, TextField txtEmail, TextField txtCPF,
            PasswordField txtSenha, PasswordField txtConfSenha, CheckBox checkTermos) {

        // Verifica se todos os campos obrigatórios foram preenchidos
        if (txtNome.getText().isEmpty() || datePickerNasc.getValue() == null || txtEmail.getText().isEmpty()
                || txtCPF.getText().isEmpty() || txtSenha.getText().isEmpty() || txtConfSenha.getText().isEmpty()) {
            UIComponents.showAlert("Informação de Registro", "Todos os campos devem ser preenchidos.", null);
            return false;
        }

        // Verificação de concordância com os Termos de Serviço
        if (!checkTermos.isSelected()) {
            UIComponents.showAlert("Informação de Registro", "Você deve concordar com os Termos de Serviço.", null);
            return false;
        }

        // Verificação de quantidade de dígitos do CPF
        if (txtCPF.getText().length() != 11) {
            UIComponents.showAlert("Informação de Registro", "O CPF deve conter exatamente 11 dígitos.", null);
            return false;
        }

        // Validação do formato do Email
        if (!txtEmail.getText().contains("@")) {
            UIComponents.showAlert("Informação de Registro", "O e-mail deve conter o caractere '@'.", null);
            return false;
        }

        return true;
    }

    public VBox getLayout() {
        return layout;
    }
}
