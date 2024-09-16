import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TicketPurchaseScreen {
    private VBox layout;

    public TicketPurchaseScreen(Stage stage) {
        layout = new VBox();
        Label lblInfo = new Label("Selecione os números para o bilhete:");
        Button btnConfirmar = new Button("Confirmar Bilhete");

        layout.getChildren().addAll(lblInfo, btnConfirmar);

        // Ação do botão de confirmar
        btnConfirmar.setOnAction(e -> {
            // Lógica de confirmação do bilhete
            showAlert("Compra Realizada", "Bilhete comprado com sucesso.");
            stage.setScene(new Scene(new MainScreen(stage).getLayout(), 800, 600));
        });
    }

    public VBox getLayout() {
        return layout;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
