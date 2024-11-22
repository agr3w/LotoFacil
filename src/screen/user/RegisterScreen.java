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
    Label lblDataError = new Label();

    public RegisterScreen(Stage stage) {
        layout = new VBox(10);
        stage.setTitle("LotoFacil - Registro de novo usuário");
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        // Título da tela
        Label lblCadastro = UIComponents.createLabel("Cadastro de Usuário",
                "-fx-font-size: 22px; -fx-text-fill: #800080; -fx-font-weight: bold;");

        // Nome Completo
        Label lblNome = UIComponents.createLabel("Nome Completo:", null);
        TextField txtNome = UIComponents.createTextField("Digite seu nome completo", "-fx-max-width: 280;");
        Label lblNomeError = new Label();
        lblNomeError.setStyle("-fx-text-fill: red;");
        lblNomeError.setVisible(false);
        lblNomeError.setManaged(false);

        // Data Nascimento
        Label lblDataNasc = UIComponents.createLabel("Data de Nascimento:", null);
        DatePicker datePickerNasc = ValidateDate.createDatePicker("Selecione sua data de nascimento",
                "-fx-min-width: 280", lblDataError);
        lblDataError.setStyle("-fx-text-fill: red;");
        lblDataError.setManaged(false);

        // Email
        Label lblEmail = UIComponents.createLabel("Email", null);
        TextField txtEmail = UIComponents.createTextField("Digite seu Email", "-fx-max-width: 280;");
        Label lblEmailError = new Label();
        lblEmailError.setStyle("-fx-text-fill: red;");
        lblEmailError.setVisible(false);
        lblEmailError.setManaged(false);

        // CPF
        Label lblCPF = UIComponents.createLabel("CPF", null);
        TextField txtCPF = UIComponents.createTextField("Digite seu CPF", "-fx-max-width: 280;");
        Label lblCPFError = new Label();
        lblCPFError.setStyle("-fx-text-fill: red;");
        lblCPFError.setVisible(false);
        lblCPFError.setManaged(false);

        txtCPF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtCPF.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 11) {
                txtCPF.setText(newValue.substring(0, 11));
            }
        });

        // Senha
        Label lblSenha = UIComponents.createLabel("Senha", null);
        PasswordField txtSenha = UIComponents.createPasswordField("Digite sua senha", "-fx-max-width: 280;");

        Label lblSenhaErrorFirst = new Label("Deve conter no mínimo 6 caracteres");
        lblSenhaErrorFirst.setStyle("-fx-text-fill: red;");

        Label lblSenhaErrorSecond = new Label("Deve conter no mínimo 1 número.");
        lblSenhaErrorSecond.setStyle("-fx-text-fill: red;");

        // Confirmar Senha
        Label lblConfSenha = UIComponents.createLabel("Confirmar senha", null);
        PasswordField txtConfSenha = UIComponents.createPasswordField("Confirme sua senha", "-fx-max-width: 280;");
        Label lblConfSenhaError = new Label();
        lblConfSenhaError.setStyle("-fx-text-fill: red;");
        lblConfSenhaError.setManaged(false);

        // Checkbox para termos de serviço
        CheckBox checkTermos = new CheckBox("Concordo com os Termos de Serviço");
        checkTermos.setStyle("-fx-font-size: 12px;");

        // Estilo do botão de termos de serviço
        Button btnTermos = UIComponents.createButton("Termos de Serviço",
                "-fx-background-color: #ffff; -fx-text-fill: black; -fx-border-radius: 5; -fx-padding: 10 20; -fx-cursor: hand;",
                e -> showTermsDialog());

        // Estilo do botão de retorno para login
        Button btnRetornoLogin = UIComponents.createButton("Retornar",
                "-fx-background-color: #FF0000; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10 20; -fx-cursor: hand;",
                e -> ScreenNavigator.navigateToLoginScreen(stage));

        // Botão de Registrar
        Button btnRegistrar = UIComponents.createButton("Registrar",
                "-fx-background-color: #800080; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10 20; -fx-cursor: hand;",
                e -> {
                    boolean valid = validateFields(txtNome, datePickerNasc, txtEmail, txtCPF, txtSenha, txtConfSenha,
                            checkTermos,
                            lblNomeError, lblDataError, lblEmailError, lblCPFError, lblSenhaErrorFirst,
                            lblSenhaErrorSecond, lblConfSenhaError);
                    if (valid) {
                        if (txtSenha.getText().equals(txtConfSenha.getText())) {
                            if (ValidateDate.isOfLegalAge(datePickerNasc.getValue())) {
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

        HBox buttonGroup = new HBox(10, btnRetornoLogin, btnTermos, btnRegistrar);
        buttonGroup.setAlignment(Pos.CENTER);
        buttonGroup.setPadding(new Insets(10, 0, 0, 0));

        layout.getChildren().addAll(
                lblCadastro, lblNome, txtNome, lblNomeError, lblDataNasc, datePickerNasc, lblDataError, lblEmail,
                txtEmail,
                lblEmailError, lblCPF, txtCPF, lblCPFError, lblSenha, txtSenha, lblSenhaErrorFirst, lblSenhaErrorSecond,
                lblConfSenha,
                txtConfSenha, lblConfSenhaError, checkTermos, buttonGroup);

        // Adicionando o ScrollPane
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true); 
        scrollPane.setFitToHeight(true); 
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane, 800, 600); // Define o tamanho da cena
        stage.setScene(scene);
    }

    private boolean validateFields(TextField txtNome, DatePicker datePickerNasc, TextField txtEmail, TextField txtCPF,
            PasswordField txtSenha, PasswordField txtConfSenha, CheckBox checkTermos, Label lblNomeError,
            Label lblDataError,
            Label lblEmailError, Label lblCPFError, Label lblSenhaErrorFirst, Label lblSenhaErrorSecond,
            Label lblConfSenhaError) {

        boolean isValid = true;

        // Verificar e atualizar campos individualmente

        if (txtNome.getText().contentEquals(";")) {
            txtNome.setStyle("-fx-border-color: red; -fx-max-width: 280;");
            lblNomeError.setText("O nome não é válido.");
            lblNomeError.setVisible(true);
            lblNomeError.setManaged(true);
            isValid = false;
        }

        if (txtNome.getText().isEmpty()) {
            txtNome.setStyle("-fx-border-color: red; -fx-max-width: 280;");
            lblNomeError.setText("Nome é obrigatório.");
            lblNomeError.setVisible(true);
            lblNomeError.setManaged(true);
            isValid = false;
        } else {
            txtNome.setStyle("-fx-border-color: green; -fx-max-width: 280;");
            lblNomeError.setVisible(false);
            lblNomeError.setManaged(false);
        }

        if (datePickerNasc.getValue() == null) {
            datePickerNasc.setStyle("-fx-border-color: red; -fx-min-width: 280;");
            lblDataError.setText("Data de náscimento é obrigatório.");
            lblDataError.setVisible(true);
            lblDataError.setManaged(true);
            isValid = false;
        } else {
            datePickerNasc.setStyle("-fx-border-color: green; -fx-max-width: 280;");
            lblDataError.setVisible(false);
            lblDataError.setManaged(false);
        }

        if (txtEmail.getText().isEmpty() || !txtEmail.getText().contains("@")) {
            txtEmail.setStyle("-fx-border-color: red; -fx-max-width: 280;");
            lblEmailError.setText("Email inválido.");
            lblEmailError.setVisible(true);
            lblEmailError.setManaged(true);
            isValid = false;
        } else {
            txtEmail.setStyle("-fx-border-color: green; -fx-max-width: 280;");
            lblEmailError.setVisible(false);
            lblEmailError.setManaged(false);
        }

        if (txtCPF.getText().length() != 11) {
            txtCPF.setStyle("-fx-border-color: red; -fx-max-width: 280;");
            lblCPFError.setText("CPF deve conter 11 dígitos.");
            lblCPFError.setVisible(true);
            lblCPFError.setManaged(true);
            isValid = false;
        } else {
            txtCPF.setStyle("-fx-border-color: green; -fx-max-width: 280;");
            lblCPFError.setVisible(false);
            lblCPFError.setManaged(false);
        }

        if (!txtSenha.getText().equals(txtConfSenha.getText())) {
            txtConfSenha.setStyle("-fx-border-color: red; -fx-max-width: 280;");
            lblConfSenhaError.setText("Senhas não coincidem.");
            lblConfSenhaError.setVisible(true);
            lblConfSenhaError.setManaged(true);
            isValid = false;
        } else {
            txtConfSenha.setStyle("-fx-border-color: green; -fx-max-width: 280;");
            lblConfSenhaError.setVisible(false);
            lblConfSenhaError.setManaged(false);
        }

        if (!checkTermos.isSelected()) {
            isValid = false;
            UIComponents.showAlert("Informação de Registro", "Você deve concordar com os Termos de Serviço.", null);
        }

        if (txtSenha.getText().isEmpty()) {

        }

        setupPasswordValidation(txtSenha, lblSenhaErrorFirst, lblSenhaErrorSecond);

        return isValid;
    }

    // Método que será chamado para configurar as validações
    private void setupPasswordValidation(PasswordField txtSenha, Label lblSenhaErrorFirst, Label lblSenhaErrorSecond) {
        // Validação de comprimento
        validSenhaLength(txtSenha, lblSenhaErrorFirst);

        // Validação de presença de número
        validSenhaContainsNumber(txtSenha, lblSenhaErrorSecond);
    }

    // Método para verificar se a senha é válida
    private boolean validateFields(PasswordField txtSenha, Label lblSenhaErrorFirst, Label lblSenhaErrorSecond) {
        boolean isValid = true;

        // Verifica se ambos os critérios são atendidos
        if (txtSenha.getText().length() >= 6 && txtSenha.getText().matches(".*\\d.*")) {
            txtSenha.setStyle("-fx-border-color: green; -fx-max-width: 280;");
        } else {
            isValid = false; // Se algum critério não for atendido
        }

        return isValid;
    }

    // Método para validar o comprimento da senha
    private void validSenhaLength(PasswordField txtSenha, Label lblSenhaErrorFirst) {
        txtSenha.textProperty().addListener((observable, oldValue, newValue) -> {
            // Verifica se a senha tem pelo menos 6 caracteres
            if (newValue.length() < 6) {
                txtSenha.setStyle("-fx-border-color: red; -fx-max-width: 280;");
                lblSenhaErrorFirst.setText("Deve conter no mínimo 6 caracteres.");
                lblSenhaErrorFirst.setStyle("-fx-text-fill: red;"); // Cor da fonte do erro
            } else {
                lblSenhaErrorFirst.setStyle("-fx-text-fill: green;"); // Cor da fonte de sucesso
            }
        });
    }

    // Método para validar a presença de números na senha
    private void validSenhaContainsNumber(PasswordField txtSenha, Label lblSenhaErrorSecond) {
        txtSenha.textProperty().addListener((observable, oldValue, newValue) -> {
            // Verifica se a senha contém pelo menos um número
            if (!newValue.matches(".*\\d.*")) {
                txtSenha.setStyle("-fx-border-color: red; -fx-max-width: 280;");
                lblSenhaErrorSecond.setText("Deve conter no mínimo 1 número.");
                lblSenhaErrorSecond.setVisible(true);
                lblSenhaErrorSecond.setStyle("-fx-text-fill: red;"); // Cor da fonte do erro
            } else {
                lblSenhaErrorSecond.setVisible(true); // Mantém visível
                lblSenhaErrorSecond.setStyle("-fx-text-fill: green;"); // Cor da fonte de sucesso
            }
        });
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

    // Verifica se o texto contém pelo menos 3 caracteres úteis
    private boolean isValidText(String text) {
        return text.trim().length() >= 3; // Remove espaços e verifica comprimento
    }

    public VBox getLayout() {
        return layout;
    }
}
