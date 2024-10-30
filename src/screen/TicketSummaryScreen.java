package screen;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;
import database.PurchaseFileManager;

import java.util.List;

public class TicketSummaryScreen {
    private VBox layout;
    private List<Integer> selectedNumbers;
    private String loggedInUser;

    public TicketSummaryScreen(Stage stage, List<Integer> selectedNumbers, String loggedInUser) {
        this.selectedNumbers = selectedNumbers;
        this.loggedInUser = loggedInUser;

        initializeUI(stage);
    }

    private void initializeUI(Stage stage) {
        layout = new VBox(20);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #DCE8E8; -fx-alignment: center;");

        Label lblTitle = UIComponents.createLabel("Resumo da Aposta", "-fx-font-size: 20px; -fx-font-weight: bold;");
 
        Label lblNumbers = new Label("Números Selecionados: " + selectedNumbers.toString());

        Button btnConfirmar = UIComponents.createButton("Confirmar", null, e -> ScreenNavigator.navigateToPaymentScreen(stage, selectedNumbers));

        Button btnVoltar = UIComponents.createButton("Voltar", null, e -> ScreenNavigator.navigateToPurchaseScreen(stage));

        layout.getChildren().addAll(lblTitle, lblNumbers, btnConfirmar, btnVoltar);
        layout.setAlignment(Pos.CENTER);
    }

    // Método para salvar a aposta após o pagamento
    public void saveBetToFile() {
        boolean success = PurchaseFileManager.saveBetToFile(loggedInUser, selectedNumbers);
        if (success) {
            System.out.println("Aposta salva com sucesso!");
        } else {
            System.out.println("Erro ao salvar a aposta.");
        }
    }

    public VBox getLayout() {
        return layout;
    }
}
