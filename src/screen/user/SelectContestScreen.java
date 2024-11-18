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
        stage.setTitle("LotoFacil - Seleção de Concurso");
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #DCE8E8; -fx-alignment: center;");

        Label lblTitle = UIComponents.createLabel("Selecione um Concurso para Apostar",
                "-fx-font-size: 24px; -fx-font-weight: bold;");

        Button btnVoltar = UIComponents.createButton("Voltar",
                "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8px ; -fx-border-radius: 5; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 2, 2); -fx-cursor: hand;",
                e -> ScreenNavigator.navigateToMainScreen(stage));

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
        VBox card = new VBox(10); // Espaçamento entre os elementos do card
        card.setPadding(new Insets(15));
        card.setStyle(
                "-fx-border-color: linear-gradient(to right, #020024, #800080); " +
                        "-fx-border-width: 2px; " +
                        "-fx-background-color: linear-gradient(to bottom, #f9f9f9, #ffffff); " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 10, 0, 5, 5); " +
                        "-fx-alignment: center;");

        // Labels estilizados
        Label lblName = UIComponents.createLabel(
                "Concurso: " + contest.get("name"),
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        Label lblStartDate = UIComponents.createLabel(
                "Início: " + contest.get("startDate"),
                "-fx-font-size: 14px; -fx-text-fill: #555555;");
        Label lblEndDate = UIComponents.createLabel(
                "Fim: " + contest.get("endDate"),
                "-fx-font-size: 14px; -fx-text-fill: #555555;");
        Label lblStatus = UIComponents.createLabel(
                "Status: " + (Boolean.parseBoolean(contest.get("status")) ? "Fechado" : "Aberto"),
                "-fx-font-size: 14px; -fx-text-fill: " +
                        (Boolean.parseBoolean(contest.get("status")) ? "#FF0000;" : "#008000;")
        );

        // Botão "Selecionar"
        Button btnSelect = UIComponents.createButton(
                "Selecionar",
                "-fx-background-color: #9370DB;" +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8 15; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 2, 2);",
                e -> handleContestSelection(contest));

        card.getChildren().addAll(lblName, lblStartDate, lblEndDate, lblStatus, btnSelect);
        return card;
    }

    private void handleContestSelection(Map<String, String> contest) {
        // Extrai o código do concurso selecionado
        String selectedContestCode = contest.get("contestCode");

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
