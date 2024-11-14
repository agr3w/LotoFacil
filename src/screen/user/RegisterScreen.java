package screen.user;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import database.Database;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;
import utils.ValidateDate;

@SuppressWarnings("unused")
public class RegisterScreen {
    private VBox layout;

    public RegisterScreen(Stage stage) {
        layout = new VBox(10); // Menor espaçamento entre os campos
        stage.setTitle("LotoFacil - Registro de novo usuário");
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER); // Alinhamento do conteúdo no topo para otimizar o uso do espaço

        // Título da tela
        Label lblCadastro = UIComponents.createLabel("Cadastro de Usuário",
                "-fx-font-size: 22px; -fx-text-fill: #800080; -fx-font-weight: bold;");
        
        // Nome Completo
        Label lblNome = UIComponents.createLabel("Nome Completo:", null);
        TextField txtNome = UIComponents.createTextField("Digite seu nome completo", "-fx-max-width: 280;");
        Label lblError = new Label();
        lblError.setVisible(false);
        // Data Nascimento
        Label lblDataNasc = UIComponents.createLabel("Data de Nascimento:", null);
        DatePicker datePickerNasc = ValidateDate.createDatePicker("Selecione sua data de nascimento",
                "-fx-min-width: 280", lblError); // Passa o Label de erro

        // Email
        Label lblEmail = UIComponents.createLabel("Email", null);
        TextField txtEmail = UIComponents.createTextField("Digite seu Email", "-fx-max-width: 280;");

        // CPF
        Label lblCPF = UIComponents.createLabel("CPF", null);
        TextField txtCPF = UIComponents.createTextField("Digite seu CPF", "-fx-max-width: 280;");
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
        PasswordField txtSenha = UIComponents.createPasswordField("Digite sua senha", "-fx-max-width: 280;");

        // Confirmar Senha
        Label lblConfSenha = UIComponents.createLabel("Confirmar senha", null);
        PasswordField txtConfSenha = UIComponents.createPasswordField("Confirme sua senha", "-fx-max-width: 280;");

        // Checkbox para termos de serviço
        CheckBox checkTermos = new CheckBox("Concordo com os Termos de Serviço");
        checkTermos.setStyle("-fx-font-size: 12px;");

        // Botão de Termos de Serviço
        Button btnTermos = UIComponents.createButton("Termos de Serviço",
                "-fx-background-color: #ffff; -fx-text-fill: black;", e -> {
                    showTermsDialog();
                });

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
                                    UIComponents.showAlert("Informação de Registro", "CPF ou e-mail já registrados.",
                                            null);
                                }
                            } else {
                                UIComponents.showAlert("Informação de Registro", "Usuário deve ser maior de idade.",
                                        null);
                            }
                        } else {
                            UIComponents.showAlert("Informação de Registro", "Senhas não coincidem", null);
                        }
                    }
                });

        // Agrupamento dos botões de forma mais compacta
        HBox buttonGroup = new HBox(10, btnRetornoLogin, btnTermos, btnRegistrar);
        buttonGroup.setAlignment(Pos.CENTER); // Alinha os botões ao centro
        buttonGroup.setPadding(new Insets(10, 0, 0, 0)); // Espaçamento superior para os botões

        // Adiciona todos os componentes ao layout
        layout.getChildren().addAll(
                lblCadastro, lblNome, txtNome, lblDataNasc, datePickerNasc, lblError, lblEmail,
                txtEmail, lblCPF, txtCPF, lblSenha, txtSenha, lblConfSenha, txtConfSenha,
                checkTermos, buttonGroup
        );
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

    private void showTermsDialog() {
        Dialog<Void> termsDialog = new Dialog<>();
        termsDialog.setTitle("Termos de Serviço");

        // Estilo personalizado para o cabeçalho e conteúdo
        Label lblHeader = UIComponents.createLabel("Termos de Serviço",
                "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #800080;");

        TextArea txtTermos = new TextArea("Aqui estão os termos de serviço detalhados...");
        txtTermos.setWrapText(true);
        txtTermos.setEditable(false);
        txtTermos.setMaxHeight(300);
        txtTermos.setMaxWidth(400);

        ButtonType btnClose = new ButtonType("Fechar", ButtonBar.ButtonData.OK_DONE);
        termsDialog.getDialogPane().getButtonTypes().add(btnClose);

        VBox content = new VBox(10, lblHeader, txtTermos);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.CENTER);
        termsDialog.getDialogPane().setContent(content);

        termsDialog.showAndWait();
    }

    public VBox getLayout() {
        return layout;
    }
}
