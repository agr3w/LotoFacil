package screen.user;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;
import utils.UserSession;

import java.util.List;
import java.util.Map;

import database.ContestFileManager;

@SuppressWarnings("unused")
public class SelectContestScreen {
    private BorderPane layout;
    private Stage stage;

    public SelectContestScreen(Stage stage) {
        this.stage = stage;
        layout = new BorderPane();
        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #DCE8E8; -fx-alignment: center;");

        Label lblTitle = UIComponents.createLabel("Selecione um Concurso para Apostar",
                "-fx-font-size: 24px; -fx-font-weight: bold;");

        Button btnVoltar = UIComponents.createButton("Voltar", null, e -> ScreenNavigator.navigateToMainScreen(stage));

        GridPane contestGrid = new GridPane();
        contestGrid.setHgap(15);
        contestGrid.setVgap(15);
        contestGrid.setPadding(new Insets(20));

        // Obtenção de concursos abertos
        List<Map<String, String>> openContests = ContestFileManager.getOpenContests();

        int row = 0;
        for (int i = 0; i < openContests.size(); i++) {
            Map<String, String> contest = openContests.get(i);
            VBox contestCard = createContestCard(contest);
            contestGrid.add(contestCard, i % 3, row); // Distribui os cards em uma grade de 3 colunas
            if (i % 3 == 2) {
                row++;
            }
        }

        mainContent.getChildren().addAll(lblTitle, contestGrid, btnVoltar);
        layout.setCenter(mainContent);
    }

    private VBox createContestCard(Map<String, String> contest) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setStyle(
                "-fx-border-color: #007BFF; -fx-border-width: 2px; -fx-background-color: #FFFFFF; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0); -fx-alignment: center;");

        // Exemplo do uso de UIComponents para criar os Labels
        Label lblName = UIComponents.createLabel("Concurso: " + contest.get("name"), "-fx-font-size: 14px;");
        Label lblStartDate = UIComponents.createLabel("Início: " + contest.get("startDate"), "-fx-font-size: 14px;");
        Label lblEndDate = UIComponents.createLabel("Fim: " + contest.get("endDate"), "-fx-font-size: 14px;");
        Label lblStatus = UIComponents.createLabel(
                "Status: " + (Boolean.parseBoolean(contest.get("status")) ? "Fechado" : "Aberto"),
                "-fx-font-size: 14px;");

        Button btnSelect = UIComponents.createButton("Selecionar",
                "-fx-background-color: #007BFF; -fx-text-fill: white;", e -> {
                    handleContestSelection(contest);
                });

        card.getChildren().addAll(lblName, lblStartDate, lblEndDate, lblStatus, btnSelect);
        return card;
    }

    private void handleContestSelection(Map<String, String> contest) {
        // Extrai o código do concurso selecionado
        String selectedContestCode = contest.get("contestCode"); // Certifique-se de que a chave seja "codigo"

        if (selectedContestCode != null) {
            // Armazena o código do concurso na sessão do usuário
            UserSession.setSelectedContestName(contest.get("name"));
            UserSession.setSelectedContestCode(selectedContestCode);

        } else {
            System.out.println("Erro: código do concurso não encontrado.");
        }

        // Navegar para a tela de apostas
        ScreenNavigator.navigateToPurchaseScreen(stage);
    }

    public BorderPane getLayout() {
        return layout;
    }
}
