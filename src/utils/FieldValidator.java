package utils;

import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class FieldValidator {

    // Adiciona os ouvintes para os campos
    @SuppressWarnings("unused")
    public static void addValidationListeners(TextField txtNome, Label lblNomeError,
            DatePicker datePickerNasc, Label lblDataError,
            TextField txtEmail, Label lblEmailError,
            TextField txtCPF, Label lblCPFError,
            PasswordField txtSenha, Label lblSenhaErrorFirst,
            PasswordField txtConfSenha, Label lblSenhaErrorSecond,
            Label lblConfSenhaError, CheckBox checkTermos) {

        // Ouvinte para o campo de nome
        txtNome.textProperty().addListener((observable, oldValue, newValue) -> {
            validateName(txtNome, lblNomeError);
        });

        // Ouvinte para a data de nascimento
        datePickerNasc.valueProperty().addListener((observable, oldValue, newValue) -> {
            validateDate(datePickerNasc, lblDataError);
        });

        // Ouvinte para o e-mail
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            validateEmail(txtEmail, lblEmailError);
        });

        // Ouvinte para o CPF
        txtCPF.textProperty().addListener((observable, oldValue, newValue) -> {
            validateCPF(txtCPF, lblCPFError);
        });

        // Ouvinte para a senha
        txtSenha.textProperty().addListener((observable, oldValue, newValue) -> {
            validatePassword(txtSenha, txtConfSenha, lblSenhaErrorFirst, lblSenhaErrorSecond, lblConfSenhaError);
        });

        // Ouvinte para a confirmação da senha
        txtConfSenha.textProperty().addListener((observable, oldValue, newValue) -> {
            validatePassword(txtSenha, txtConfSenha, lblSenhaErrorFirst, lblSenhaErrorSecond, lblConfSenhaError);
        });

        // Ouvinte para os termos de serviço
        checkTermos.selectedProperty().addListener((observable, oldValue, newValue) -> {
            validateTerms(checkTermos);
        });
    }

    // Verifica se a string contém múltiplos espaços consecutivos
    private static boolean hasMultipleSpaces(String text) {
        return text.contains("  ");
    }

    // Valida o nome
    public static boolean validateName(TextField txtNome, Label lblNomeError) {
        boolean isValid = true;
        String name = txtNome.getText().trim();

        // validar que o nome contém apenas letras e espaços, e não permite
        // múltiplos espaços consecutivos
        if (!name.matches("[a-zA-ZÀ-ÿ]+( [a-zA-ZÀ-ÿ]+)*")) {
            txtNome.setStyle("-fx-border-color: red; -fx-max-width: 280;");
            lblNomeError.setText("O nome não pode conter caracteres especiais ou múltiplos espaços.");
            lblNomeError.setVisible(true);
            lblNomeError.setManaged(true);
            isValid = false;
        } else {
            txtNome.setStyle("-fx-border-color: green; -fx-max-width: 280;");
            lblNomeError.setVisible(false);
            lblNomeError.setManaged(false);
        }
        return isValid;
    }

    // Valida a data de nascimento
    public static boolean validateDate(DatePicker datePickerNasc, Label lblDataError) {
        boolean isValid = true;
        if (datePickerNasc.getValue() == null) {
            datePickerNasc.setStyle("-fx-border-color: red; -fx-min-width: 280;");
            lblDataError.setText("Data de nascimento é obrigatória.");
            lblDataError.setVisible(true);
            lblDataError.setManaged(true);
            isValid = false;
        } else {
            datePickerNasc.setStyle("-fx-border-color: green; -fx-max-width: 280;");
            lblDataError.setVisible(false);
            lblDataError.setManaged(false);
        }
        return isValid;
    }

    // Valida o e-mail
    public static boolean validateEmail(TextField txtEmail, Label lblEmailError) {
        boolean isValid = true;
        if (txtEmail.getText().isEmpty() || !txtEmail.getText().contains("@")
                || hasMultipleSpaces(txtEmail.getText())) {
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
        return isValid;
    }

    // Valida o CPF
    public static boolean validateCPF(TextField txtCPF, Label lblCPFError) {
        boolean isValid = true;
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
        return isValid;
    }

    // Valida a senha e a confirmação
    public static boolean validatePassword(PasswordField txtSenha, PasswordField txtConfSenha,
            Label lblSenhaErrorFirst, Label lblSenhaErrorSecond,
            Label lblConfSenhaError) {
        boolean isValid = true;

        // Validação de comprimento da senha
        if (txtSenha.getText().length() < 6 || hasMultipleSpaces(txtSenha.getText()) || txtSenha.getText().contains(";")
                || txtSenha.getText().contains(":")) {
            txtSenha.setStyle("-fx-border-color: red; -fx-max-width: 280;");
            lblSenhaErrorFirst.setText("Deve conter no mínimo 6 caracteres válidos. ⛔");
            lblSenhaErrorFirst.setStyle("-fx-text-fill: red;");
            isValid = false;
        } else {
            lblSenhaErrorFirst.setText("Deve conter no mínimo 6 caracteres. ✅");
            lblSenhaErrorFirst.setStyle("-fx-text-fill: green;");
        }

        // Validação de número na senha
        if (!txtSenha.getText().matches(".*\\d.*")) {
            txtSenha.setStyle("-fx-border-color: red; -fx-max-width: 280;");
            lblSenhaErrorSecond.setText("Deve conter no mínimo 1 número. ⛔");
            lblSenhaErrorSecond.setStyle("-fx-text-fill: red;");
            isValid = false;
        } else {
            lblSenhaErrorSecond.setText("Deve conter no mínimo 1 número. ✅");
            lblSenhaErrorSecond.setStyle("-fx-text-fill: green;");
        }

        if (txtSenha.getText().matches(".*\\d.*") && txtSenha.getText().length() < 6) {
            txtSenha.setStyle("-fx-border-color: green; -fx-max-width: 280;");
        }

        // Validação se as senhas coincidem
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

        return isValid;
    }

    // Valida a aceitação dos termos
    public static boolean validateTerms(CheckBox checkTermos) {
        if (!checkTermos.isSelected()) {
            UIComponents.showAlert("Informação de Registro", "Você deve concordar com os Termos de Serviço.", null);
            return false;
        }
        return true;
    }

}
