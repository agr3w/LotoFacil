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
        Label lblCadastro = UIComponents.createLabel("Registro de Usuário",
                "-fx-font-size: 22px; -fx-text-fill: #800080; -fx-font-weight: bold;");

        // Nome Completo
        Label lblNome = UIComponents.createLabel("Nome Completo:", null);
        TextField txtNome = UIComponents.createTextField("Digite seu nome completo",
                "-fx-max-width: 280; -fx-background-radius: 5; -fx-border-radius: 5;");
        Label lblNomeError = new Label();
        lblNomeError.setStyle("-fx-text-fill: red;");
        lblNomeError.setVisible(false);
        lblNomeError.setManaged(false);

        // Validação do nome
        txtNome.textProperty().addListener((observable, oldValue, newValue) -> {
            FieldValidator.validateName(txtNome, lblNomeError); // Chama a validação do nome
        });

        // Data Nascimento
        Label lblDataNasc = UIComponents.createLabel("Data de Nascimento:", null);
        DatePicker datePickerNasc = ValidateDate.createDatePicker("dd/MM/yyyy",
                "-fx-max-width: 280; -fx-background-radius: 5; -fx-border-radius: 5;", lblDataError);
        lblDataError.setStyle("-fx-text-fill: red;");
        lblDataError.setManaged(false);

        // Validação da data
        datePickerNasc.valueProperty().addListener((observable, oldValue, newValue) -> {
            FieldValidator.validateDate(datePickerNasc, lblDataError); // Chama a validação da data
        });

        // Email
        Label lblEmail = UIComponents.createLabel("Email", null);
        TextField txtEmail = UIComponents.createTextField("Digite seu Email",
                "-fx-max-width: 280; -fx-background-radius: 5; -fx-border-radius: 5;");
        Label lblEmailError = new Label();
        lblEmailError.setStyle("-fx-text-fill: red;");
        lblEmailError.setVisible(false);
        lblEmailError.setManaged(false);

        // Validação do email
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            FieldValidator.validateEmail(txtEmail, lblEmailError); // Chama a validação do email
        });

        // CPF
        Label lblCPF = UIComponents.createLabel("CPF", null);
        TextField txtCPF = UIComponents.createTextField("Digite seu CPF",
                "-fx-max-width: 280; -fx-background-radius: 5; -fx-border-radius: 5;");
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
            FieldValidator.validateCPF(txtCPF, lblCPFError); // Chama a validação do CPF
        });

        // Senha
        Label lblSenha = UIComponents.createLabel("Senha", null);
        PasswordField txtSenha = UIComponents.createPasswordField("Digite sua senha",
                "-fx-max-width: 280; -fx-background-radius: 5; -fx-border-radius: 5;");

        Label lblSenhaErrorFirst = new Label("Deve conter no mínimo 6 caracteres. ⛔");
        lblSenhaErrorFirst.setStyle("-fx-text-fill: red;");

        Label lblSenhaErrorSecond = new Label("Deve conter no mínimo 1 número. ⛔");
        lblSenhaErrorSecond.setStyle("-fx-text-fill: red;");

        // Confirmar Senha
        Label lblConfSenha = UIComponents.createLabel("Confirmar senha", null);
        PasswordField txtConfSenha = UIComponents.createPasswordField("Confirme sua senha",
                "-fx-max-width: 280; -fx-background-radius: 5; -fx-border-radius: 5;");
        Label lblConfSenhaError = new Label();
        lblConfSenhaError.setStyle("-fx-text-fill: red;");
        lblConfSenhaError.setManaged(false);

        // Validação da senha
        txtSenha.textProperty().addListener((observable, oldValue, newValue) -> {
            FieldValidator.validatePassword(txtSenha, txtConfSenha, lblSenhaErrorFirst, lblSenhaErrorSecond,
                    lblConfSenhaError); // Chama a validação da senha
        });

        // Ouvinte para a confirmação da senha
        txtConfSenha.textProperty().addListener((observable, oldValue, newValue) -> {
            FieldValidator.validatePassword(txtSenha, txtConfSenha, lblSenhaErrorFirst, lblSenhaErrorSecond,
                    lblConfSenhaError);
        });

        // Checkbox para termos de serviço
        CheckBox checkTermos = new CheckBox("Concordo com os Termos de Serviço");
        checkTermos.setStyle("-fx-font-size: 12px;");

        // Estilo do botão de termos de serviço
        Button btnTermos = UIComponents.createButton("Termos de Serviço",
                "-fx-background-color: #e6e6fa; -fx-text-fill: black; -fx-font-size: 12px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 8;" +
                        "-fx-cursor: hand; fx-width: 200;",
                e -> showTermsDialog());

        // Estilo do botão de retorno para login
        Button btnRetornoLogin = UIComponents.createButton("Retornar",
                "-fx-background-color: #D9534F; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 8;" +
                        "-fx-cursor: hand; fx-width: 200;",
                e -> ScreenNavigator.navigateToLoginScreen(stage));

        // Botão de Registrar
        Button btnRegistrar = UIComponents.createButton("Registrar",
                "-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 8;" +
                        "-fx-cursor: hand; fx-width: 200;",
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

        // Texto dos Termos de Serviço
        String termos = "1. Aceitação dos Termos\n" +
                "Ao utilizar este aplicativo, você concorda em cumprir e estar vinculado a estes termos de serviço.\n\n"
                +
                "2. Modificações dos Termos\n" +
                "Reservamo-nos o direito de modificar estes termos a qualquer momento. As alterações serão publicadas nesta página.\n\n"
                +
                "3. Uso do Aplicativo\n" +
                "Você concorda em usar o aplicativo apenas para fins legais e de acordo com todas as leis aplicáveis.\n\n"
                +
                "4. Responsabilidade\n" +
                "Não nos responsabilizamos por quaisquer danos diretos, indiretos, incidentais ou consequenciais resultantes do uso ou da incapacidade de usar o aplicativo.\n\n"
                +
                "5. Propriedade Intelectual\n" +
                "Todos os direitos de propriedade intelectual relacionados ao aplicativo são de nossa propriedade ou de nossos licenciadores.\n\n"
                +
                "6. Contato\n" +
                "Se você tiver alguma dúvida sobre estes termos, entre em contato conosco através do nosso suporte.\n\n"
                +
                "7. Lei Aplicável\n" +
                "Estes termos serão regidos e interpretados de acordo com as leis do seu país.\n";

        TextArea txtTermos = new TextArea(termos);
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
