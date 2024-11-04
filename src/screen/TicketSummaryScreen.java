package screen;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;
import utils.UserSession;
import database.TicketPricing;

import java.util.List;

public class TicketSummaryScreen {
    private VBox layout;
    private List<Integer> selectedNumbers;
    private String contestName;

    public TicketSummaryScreen(Stage stage, List<Integer> selectedNumbers, String loggedInUser) {
        this.selectedNumbers = selectedNumbers;
        this.contestName = UserSession.getSelectedContestName(); // Obtendo o nome do concurso da sessão

        initializeUI(stage);
    }

    @SuppressWarnings("unused")
    private void initializeUI(Stage stage) {
        layout = new VBox(20);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #DCE8E8; -fx-alignment: center;");

        Label lblTitle = UIComponents.createLabel("Resumo da Aposta", "-fx-font-size: 20px; -fx-font-weight: bold;");

        Label lblContestName = UIComponents.createLabel("Nome do Concurso" + contestName, null);
        Label lblNumbers = UIComponents.createLabel("Números Selecionados: " + selectedNumbers.toString(), null);
        Label lblValor = UIComponents
                .createLabel("Valor a se pagar: R$ " + TicketPricing.calculatePrice(selectedNumbers.size()), null);

        Button btnConfirmar = UIComponents.createButton("Confirmar", null, e -> {
            // Aqui você pode chamar um método para salvar a aposta
            ScreenNavigator.navigateToPaymentScreen(stage, selectedNumbers);
        });

        Button btnVoltar = UIComponents.createButton("Voltar", null,
                e -> ScreenNavigator.navigateToPurchaseScreen(stage));

        layout.getChildren().addAll(lblTitle, lblContestName, lblNumbers, lblValor, btnConfirmar, btnVoltar);
        layout.setAlignment(Pos.CENTER);
    }

    public VBox getLayout() {
        return layout;
    }
}
