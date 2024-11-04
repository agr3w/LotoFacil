package screen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import database.PurchaseFileManager;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.util.List;

public class ResultsScreen {
    private VBox layout;
    private ScrollPane scrollPane;
    private VBox purchaseList;


    public ResultsScreen(Stage stage) {
        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Título da tela de histórico de compras
        Label lblTitle = UIComponents.createLabel(
                "Histórico de Compras",
                "-fx-font-size: 24px; -fx-text-fill: #800080; -fx-font-weight: bold;");

        // Caixa para listar as compras com barra de rolagem
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f5f5f5; -fx-border-color: #d0d0d0;");

        // Botão de voltar
        @SuppressWarnings("unused")
        Button btnVoltar = UIComponents.createButton(
                "Voltar",
                "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;",
                e -> ScreenNavigator.navigateToMainScreen(stage));

        layout.getChildren().addAll(lblTitle, scrollPane, btnVoltar);
    }

    public void loadPurchaseHistory() {
        // Limpa a lista de compras antes de recarregar
        purchaseList = new VBox(10);
        purchaseList.setAlignment(Pos.CENTER_LEFT);
        purchaseList.setPadding(new Insets(10));

        // Carrega os tickets do usuário logado
        List<PurchaseFileManager> tickets = PurchaseFileManager.loadUserTickets();

        if (!tickets.isEmpty()) {
            for (PurchaseFileManager ticket : tickets) {
                VBox purchaseBox = new VBox(5);
                purchaseBox.setPadding(new Insets(10));
                purchaseBox.setStyle(
                        "-fx-background-color: #f5f5f5; -fx-border-color: #d0d0d0; -fx-border-radius: 5; -fx-background-radius: 5;");

                // Exibindo o CPF
                Label userLabel = UIComponents.createLabel(
                    "CPF: " + ticket.getCpfFromFile(),
                    "-fx-font-size: 14px; -fx-text-fill: #333;"
                );

                // Exibindo os números selecionados
                Label numbersLabel = UIComponents.createLabel(
                    "Números Selecionados: " + ticket.getSelectedNumbersFromFile(),
                    "-fx-font-size: 12px; -fx-text-fill: #555;"
                );

                // Exibindo o valor da compra
                Label valueLabel = UIComponents.createLabel(
                    "Valor: " + ticket.getValueFromFile(),
                    "-fx-font-size: 12px; -fx-text-fill: #555;"
                );
                purchaseBox.getChildren().addAll(userLabel ,numbersLabel ,valueLabel);

                purchaseList.getChildren().add(purchaseBox);
            }
        } else {
            Label noPurchaseLabel = UIComponents.createLabel(
                "Nenhuma compra encontrada para o usuário atual.",
                "-fx-font-size: 14px; -fx-text-fill: #333;"
            );
            purchaseList.getChildren().add(noPurchaseLabel);
        }

        // Atualiza o conteúdo do scrollPane com a nova lista de compras
        scrollPane.setContent(purchaseList);
    }

    public VBox getLayout() {
        // Carrega o histórico sempre que a tela é requisitada
        loadPurchaseHistory();
        return layout;
    }
}
