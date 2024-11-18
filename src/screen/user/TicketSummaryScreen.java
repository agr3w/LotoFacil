package screen.user;

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
        layout = new VBox(20); // Espaçamento entre os elementos
        stage.setTitle("LotoFacil - Resumo de Compra");
        layout.setStyle("-fx-padding: 20; -fx-background-color: #F0F4F8; -fx-alignment: center;");

        // Título
        Label lblTitle = UIComponents.createLabel("Resumo da Aposta",
                "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #007BFF;");

        // Informações do concurso
        Label lblContestName = UIComponents.createLabel("Nome do Concurso: " + contestName,
                "-fx-font-size: 16px; -fx-text-fill: #333;");
        Label lblNumbers = UIComponents.createLabel("Números Selecionados: " + selectedNumbers.toString(),
                "-fx-font-size: 16px; -fx-text-fill: #333;");

        // Informações de valor
        double price = TicketPricing.calculatePrice(selectedNumbers.size());
        Label lblValor = UIComponents.createLabel("Valor a se pagar: R$ " + String.format("%.2f", price),
                "-fx-font-size: 16px; -fx-text-fill: #333;");

        // Botão de confirmar
        Button btnConfirmar = UIComponents.createButton("Confirmar",
                "-fx-background-color: #32CD32; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5;",
                e -> ScreenNavigator.navigateToPaymentScreen(stage, selectedNumbers));

        // Botão de voltar
        Button btnVoltar = UIComponents.createButton("Voltar",
                "-fx-background-color: #4169E1; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5;",
                e -> ScreenNavigator.navigateToPurchaseScreen(stage));

        // Efeito de hover nos botões
        addButtonHoverEffect(btnConfirmar, "#32CD32", "#98FB98");
        addButtonHoverEffect(btnVoltar, "#4169E1", "#6495ED");

        // Organizando os elementos no layout
        layout.getChildren().addAll(lblTitle, lblContestName, lblNumbers, lblValor, btnConfirmar, btnVoltar);
        layout.setAlignment(Pos.CENTER);
    }

    public VBox getLayout() {
        return layout;
    }

    @SuppressWarnings("unused")
    private void addButtonHoverEffect(Button button, String normalColor, String hoverColor) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + hoverColor
                + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + normalColor
                + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;"));
    }
}
