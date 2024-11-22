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
import utils.FieldValidator;
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
            PasswordField txtSenha, PasswordField txtConfSenha, CheckBox checkTermos,
            Label lblNomeError, Label lblDataError, Label lblEmailError, Label lblCPFError,
            Label lblSenhaErrorFirst, Label lblSenhaErrorSecond, Label lblConfSenhaError) {

        boolean isValid = true;

        // Validação de todos os campos e retorno do resultado
        isValid &= FieldValidator.validateName(txtNome, lblNomeError); // Valida o nome
        isValid &= FieldValidator.validateDate(datePickerNasc, lblDataError); // Valida a data de nascimento
        isValid &= FieldValidator.validateEmail(txtEmail, lblEmailError); // Valida o email
        isValid &= FieldValidator.validateCPF(txtCPF, lblCPFError); // Valida o CPF
        isValid &= FieldValidator.validatePassword(txtSenha, txtConfSenha, lblSenhaErrorFirst, lblSenhaErrorSecond,
                lblConfSenhaError); // Valida senha
        isValid &= FieldValidator.validateTerms(checkTermos); // Valida os termos de serviço

        return isValid;
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
