package screen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;
import utils.UserSession;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class PurchaseHistoryScreen {
    private VBox layout;
    private ScrollPane scrollPane;
    private VBox purchaseList;

    public PurchaseHistoryScreen(Stage stage) {
        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Título da tela de histórico de compras
        Label lblTitle = UIComponents.createLabel(
                "Histórico de Compras", 
                "-fx-font-size: 24px; -fx-text-fill: #800080; -fx-font-weight: bold;"
        );

        // Caixa para listar as compras com barra de rolagem
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f5f5f5; -fx-border-color: #d0d0d0;");

        // Botão de voltar
        Button btnVoltar = UIComponents.createButton(
                "Voltar", 
                "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;",
                e -> ScreenNavigator.navigateToMainScreen(stage)
        );

        layout.getChildren().addAll(lblTitle, scrollPane, btnVoltar);
    }

    public void loadPurchaseHistory() {
        // Limpa a lista de compras antes de recarregar
        purchaseList = new VBox(10);
        purchaseList.setAlignment(Pos.CENTER_LEFT);
        purchaseList.setPadding(new Insets(10));

        String loggedInCpf = UserSession.getLoggedInUserCpf();

        try (BufferedReader reader = new BufferedReader(new FileReader("purchases.txt"))) {
            String line;
            boolean purchaseFound = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Usuario: " + loggedInCpf)) {
                    purchaseFound = true;

                    // Criação de uma caixa individual para cada compra
                    VBox purchaseBox = new VBox(5);
                    purchaseBox.setPadding(new Insets(10));
                    purchaseBox.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #d0d0d0; -fx-border-radius: 5; -fx-background-radius: 5;");

                    // Exibindo o CPF
                    Label userLabel = UIComponents.createLabel(
                            line, 
                            "-fx-font-size: 14px; -fx-text-fill: #333;"
                    );
                    purchaseBox.getChildren().add(userLabel);

                    // Exibindo os números selecionados
                    line = reader.readLine();
                    if (line != null) {
                        Label numbersLabel = UIComponents.createLabel(
                                line, 
                                "-fx-font-size: 12px; -fx-text-fill: #555;"
                        );
                        purchaseBox.getChildren().add(numbersLabel);
                    }

                    // Exibindo o valor da compra
                    line = reader.readLine();
                    if (line != null) {
                        Label valueLabel = UIComponents.createLabel(
                                line, 
                                "-fx-font-size: 12px; -fx-text-fill: #555;"
                        );
                        purchaseBox.getChildren().add(valueLabel);
                    }

                    purchaseList.getChildren().add(purchaseBox);
                }
            }

            // Exibe mensagem caso o usuário não tenha compras registradas
            if (!purchaseFound) {
                Label noPurchasesLabel = UIComponents.createLabel(
                        "Nenhum histórico de compras encontrado.",
                        "-fx-text-fill: #555; -fx-font-size: 14px;"
                );
                purchaseList.getChildren().add(noPurchasesLabel);
            }
        } catch (IOException e) {
            Label errorLabel = UIComponents.createLabel(
                    "Erro ao carregar o histórico de compras.",
                    "-fx-text-fill: red; -fx-font-size: 14px;"
            );
            purchaseList.getChildren().add(errorLabel);
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
