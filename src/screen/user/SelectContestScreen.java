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

public class SelectContestScreen {
    private BorderPane layout;
    private Stage stage;

    public SelectContestScreen(Stage stage) {
        this.stage = stage;
        layout = new BorderPane();
        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #DCE8E8; -fx-alignment: center;");

        Label lblTitle = new Label("Selecione um Concurso para Apostar");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

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

        mainContent.getChildren().addAll(lblTitle, contestGrid);
        layout.setCenter(mainContent);
    }

    @SuppressWarnings("unused")
    private VBox createContestCard(Map<String, String> contest) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setStyle(
                "-fx-border-color: #007BFF; -fx-border-width: 2px; -fx-background-color: #FFFFFF; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0); -fx-alignment: center;");

        Label lblName = new Label("Concurso: " + contest.get("name"));
        Label lblStartDate = new Label("Início: " + contest.get("startDate"));
        Label lblEndDate = new Label("Fim: " + contest.get("endDate"));
        Label lblStatus = new Label("Status: " + (Boolean.parseBoolean(contest.get("status")) ? "Fechado" : "Aberto"));

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