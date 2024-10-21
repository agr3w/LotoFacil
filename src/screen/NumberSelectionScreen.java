package screen;

import model.Bet;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class NumberSelectionScreen {
    private VBox layout;
    private Bet currentBet; // Aposta atual do usuário
    private Button btnConfirmar;
    private Label lblSelecionados; // Label para exibir números selecionados

    public NumberSelectionScreen(Stage stage) {
        currentBet = new Bet();
        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Adicione seus componentes para seleção de números
        lblSelecionados = new Label("Números Selecionados: Nenhum");
        layout.getChildren().add(lblSelecionados);

        // Exemplo de Checkbox para números
        for (int i = 1; i <= 20; i++) {
            CheckBox checkBox = new CheckBox(String.valueOf(i));
            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {
                    if (currentBet.getSelectedNumbers().size() < 20) {
                        currentBet.addNumber(i); // Adiciona número à aposta
                    } else {
                        checkBox.setSelected(false); // Desmarca se o limite for atingido
                        showAlert("Limite Atingido", "Você só pode selecionar até 20 números.");
                    }
                } else {
                    currentBet.getSelectedNumbers().remove(Integer.valueOf(i)); // Remove número da aposta
                }
                updateSelectedNumbers();
            });
            layout.getChildren().add(checkBox);
        }
    }

    private void updateSelectedNumbers() {
        lblSelecionados.setText("Números Selecionados: " + currentBet.getSelectedNumbers().toString());
    }

    public VBox getLayout() {
        return layout;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
