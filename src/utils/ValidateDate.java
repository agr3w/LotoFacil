package utils;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidateDate {

    public static LocalDate todayLocalDate() {
        return LocalDate.now();
    }

    public static boolean isOfLegalAge(LocalDate pickedDate) {
        return Period.between(pickedDate, todayLocalDate()).getYears() >= 18;
    }

    @SuppressWarnings("unused")
    public static DatePicker createDatePicker(String text, String style, Label errorLabel) {
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText(text);
        datePicker.setStyle(style);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Configurar o conversor para autocompletar e validar o formato
        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? date.format(formatter) : "";
            }

            @Override
            public LocalDate fromString(String text) {
                try {
                    // Verifica se o texto está vazio
                    if (text.isEmpty()) {
                        errorLabel.setText(""); // Limpa o erro ao apagar a data
                        errorLabel.setVisible(false); // Esconde o erro
                        return null; // Permite limpar o campo
                    }

                    // Tenta fazer o parse da data
                    LocalDate parsedDate = LocalDate.parse(text, formatter);
                    LocalDate today = LocalDate.now();

                    // Restringir datas futuras
                    if (parsedDate.isAfter(today)) {
                        showError(errorLabel, "Insira uma data anterior ao ano atual.");
                        return null;
                    }

                    // Limpar erro se data for válida
                    errorLabel.setText("");
                    errorLabel.setVisible(false);
                    return parsedDate;
                } catch (DateTimeParseException e) {
                    showError(errorLabel, "Data inválida! Use o formato DD/MM/AAAA.");
                    return null;
                }
            }
        });

        // Listener para completar "/" automaticamente e garantir limite de 4 dígitos
        datePicker.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            // Limita o número de caracteres a 10 (dd/MM/yyyy)
            if (newValue.length() > 10) {
                datePicker.getEditor().setText(oldValue); // Reverte para o valor anterior
                return;
            }

            // Permite apenas números e barra ("/")
            if (!newValue.matches("[0-9/]*")) {
                datePicker.getEditor().setText(newValue.replaceAll("[^0-9/]", "")); // Remove caracteres não permitidos
                return;
            }

            if (newValue.length() < oldValue.length()) {
                return;
            }

            // Adiciona as barras automaticamente
            if (newValue.length() == 2 || newValue.length() == 5) {
                // Evita adicionar uma barra adicional
                if (!newValue.endsWith("/")) {
                    datePicker.getEditor().setText(newValue + "/");
                }
            }

        });

        return datePicker;
    }

    // Função auxiliar para mostrar o erro
    private static void showError(Label errorLabel, String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        errorLabel.setVisible(true); // Torna o erro visível
    }

}
