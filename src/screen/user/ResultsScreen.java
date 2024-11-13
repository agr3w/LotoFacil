package screen.user;

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
import utils.ContestManager;

import java.util.List;
import java.util.Map;

public class ResultsScreen {
        private VBox layout;
        private ScrollPane scrollPane;
        private VBox purchaseList;
        double ticketValue;

        @SuppressWarnings("unused")
        public ResultsScreen(Stage stage) {
                layout = new VBox(10);
                layout.setPadding(new Insets(20));
                layout.setAlignment(Pos.CENTER);

                // Título da tela de resultados
                Label lblTitle = UIComponents.createLabel(
                                "Resultados do Concurso",
                                "-fx-font-size: 24px; -fx-text-fill: #800080; -fx-font-weight: bold;");

                // Caixa para listar as compras com barra de rolagem
                scrollPane = new ScrollPane();
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle("-fx-background: #f5f5f5; -fx-border-color: #d0d0d0;");

                // Botão de voltar
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
                // Aqui você pode buscar os resultados do concurso
                Map<String, String> contest = ContestManager.getContestByCode(ticket.getContestCode());
                if (contest != null) {
                    // Verifica se o concurso está finalizado
                    if ("Finalizado".equals(contest.get("status"))) {
                        String winningNumbersString = contest.get("winningNumbers");
                        List<Integer> winningNumbers = ContestManager.parseWinningNumbers(winningNumbersString);
                        List<Integer> selectedNumbers = ContestManager.parseSelectedNumbers(ticket.getSelectedNumbersFromFile());
                        int correctCount = ContestManager.countCorrectNumbers(selectedNumbers, winningNumbers);
                        
                        try {                           
                            ticketValue = Double.parseDouble(ticket.getValueFromFile());
                        } catch (NumberFormatException e) {
                            // Trate a exceção conforme necessário, por exemplo, logando ou exibindo uma mensagem
                            System.err.println("Erro ao converter o valor do ticket: " + e.getMessage());
                        }
                        double totalPrizeValue = Double.parseDouble(contest.get("totalPrizes"));

                        double prize = ContestManager.calculatePrize(correctCount, totalPrizeValue); 
                        String resultMessage = correctCount >= 11 ? "Você ganhou! Números acertados: " + correctCount
                                : "Você perdeu! Números acertados: " + correctCount;
    
                        // Criação do box para exibir os resultados
                        VBox purchaseBox = createPurchaseBox(ticket, winningNumbers, resultMessage, prize);
                        purchaseList.getChildren().add(purchaseBox);
                    } else {
                        // Exibe uma mensagem informando que o concurso não está finalizado
                        VBox purchaseBox = createPurchaseBox(ticket, null, "O concurso ainda não foi finalizado.", 0);
                        purchaseList.getChildren().add(purchaseBox);
                    }
                }
            }
        } else {
            Label noPurchaseLabel = UIComponents.createLabel("Nenhuma compra encontrada para o usuário atual.",
                    "-fx-font-size: 14px; -fx-text-fill: #333;");
            purchaseList.getChildren().add(noPurchaseLabel);
        }
    
        // Atualiza o conteúdo do scrollPane com a nova lista de compras
        scrollPane.setContent(purchaseList);
    }

        

        private VBox createPurchaseBox(PurchaseFileManager ticket, List<Integer> winningNumbers, String resultMessage,
                        double prize) {
                VBox purchaseBox = new VBox(5);
                purchaseBox.setPadding(new Insets(10));
                purchaseBox.setStyle(
                                "-fx-background-color: #f5f5f5; -fx-border-color: #d0d0d0; -fx-border-radius: 5; -fx-background-radius: 5;");

                // Exibindo o CPF
                Label userLabel = UIComponents.createLabel("CPF: " + ticket.getCpfFromFile(),
                                "-fx-font-size: 14px; -fx-text-fill: #333;");
                // Exibindo os números selecionados
                Label numbersLabel = UIComponents.createLabel(
                                "Números Selecionados: " + ticket.getSelectedNumbersFromFile(),
                                "-fx-font-size: 12px; -fx-text-fill: #555;");
                // Exibindo o valor da compra
                Label valueLabel = UIComponents.createLabel("Valor: " + ticket.getValueFromFile(),
                                "-fx-font-size: 12px; -fx-text-fill: #555;");
                // Exibindo o resultado
                Label resultLabel = UIComponents.createLabel(resultMessage,
                                "-fx-font-size: 12px; -fx-text-fill: #333;");
                // Exibindo o valor ganho
                Label prizeLabel = UIComponents.createLabel("Valor Ganho: R$ " + String.format("%.2f", prize),
                                "-fx-font-size: 12px; -fx-text-fill: #333;");

                if (winningNumbers != null) {
                        Label winningNumbersLabel = UIComponents.createLabel(
                                        "Números Vencedores: " + winningNumbers.toString(),
                                        "-fx-font-size: 12px; -fx-text-fill: #333;");
                        purchaseBox.getChildren().add(winningNumbersLabel);
                }

                purchaseBox.getChildren().addAll(userLabel, numbersLabel, valueLabel, resultLabel, prizeLabel);
                return purchaseBox;
        }

        public VBox getLayout() {
                // Carrega o histórico sempre que a tela é requisitada
                loadPurchaseHistory();
                return layout;
        }
}