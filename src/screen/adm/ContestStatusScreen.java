package screen.adm;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.ContestManager;
import utils.UIComponents;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import database.ContestFileManager;

public class ContestStatusScreen {
    private VBox layout;
    private TableView<Map<String, String>> table;

    @SuppressWarnings("unused")
    public ContestStatusScreen(Stage stage) {
        layout = new VBox(15);
        stage.setTitle("LotoFacil - Status de Concursos");
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #DCE8E8;");

        Label titleLabel = new Label("Status dos Concursos");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Inicializar tabela
        table = new TableView<>();
        table.setPrefWidth(800); // Aumentar a largura para acomodar novas colunas
        setupTableColumns();

        // Carregar dados reais
        loadContestData();

        // Organizando botões
        HBox buttonBox = new HBox(10);
        Button btnFinalizar = UIComponents.createButton("Finalizar Concurso",
                "-fx-background-color: #FFA500; -fx-text-fill: white;", e -> finalizarConcurso());
        Button btnEditar = UIComponents.createButton("Editar Nome",
                "-fx-background-color: #1E90FF; -fx-text-fill: white;", e -> editarConcurso());
        Button btnExcluir = UIComponents.createButton("Excluir Concurso",
                "-fx-background-color: #FF0000; -fx-text-fill: white;", e -> excluirConcurso());
        buttonBox.getChildren().addAll(btnFinalizar, btnEditar, btnExcluir);

        // Botão de voltar
        Button btnVoltar = UIComponents.createButton("Voltar", "-fx-background-color: #FF0000; -fx-text-fill: white;",
                e -> ScreenNavigator.navigateToMainScreen(stage));

        layout.getChildren().addAll(titleLabel, table, buttonBox, btnVoltar);
    }

    @SuppressWarnings({ "unchecked" })
    private void setupTableColumns() {
        TableColumn<Map<String, String>, String> nameColumn = new TableColumn<>("Nome do Concurso");
        nameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get("name")));
        nameColumn.setPrefWidth(200);

        TableColumn<Map<String, String>, String> startDateColumn = new TableColumn<>("Início");
        startDateColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get("startDate")));
        startDateColumn.setPrefWidth(150);

        TableColumn<Map<String, String>, String> endDateColumn = new TableColumn<>("Fim");
        endDateColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get("endDate")));
        endDateColumn.setPrefWidth(150);

        TableColumn<Map<String, String>, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get("status")));
        statusColumn.setPrefWidth(150);

        TableColumn<Map<String, String>, String> codeColumn = new TableColumn<>("Código");
        codeColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get("contestCode")));
        codeColumn.setPrefWidth(100);

        // Nova coluna para Arrecadação Total
        TableColumn<Map<String, String>, String> totalRevenueColumn = new TableColumn<>("Arrecadação Total");
        totalRevenueColumn
                .setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get("totalRevenue")));
        totalRevenueColumn.setPrefWidth(150);

        // Nova coluna para Total de Prêmios
        TableColumn<Map<String, String>, String> totalPrizesColumn = new TableColumn<>("Total Para Prêmios");
        totalPrizesColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get("totalPrizes")));
        totalPrizesColumn.setPrefWidth(150);

        TableColumn<Map<String, String>, String> totalPrizesDistribuation = new TableColumn<>("Total de Prêmio Distribuidos");
        totalPrizesDistribuation
                .setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get("totalPrizesDistribuation")));
                totalPrizesDistribuation.setPrefWidth(170);

        TableColumn<Map<String, String>, String> totalRemainingPrizes = new TableColumn<>("Total Sobra de Prêmio");
        totalRemainingPrizes
                .setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get("remainingPrizes")));
        totalRemainingPrizes.setPrefWidth(150);

        TableColumn<Map<String, String>, String> totalCoporationShareColumn = new TableColumn<>("Total da corporação");
        totalCoporationShareColumn
                .setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get("corporationShare")));
        totalCoporationShareColumn.setPrefWidth(150);

        table.getColumns().addAll(nameColumn, startDateColumn, endDateColumn, statusColumn, codeColumn,
                totalRevenueColumn, totalPrizesColumn, totalPrizesDistribuation, totalRemainingPrizes, totalCoporationShareColumn);
    }

    private void loadContestData() {
        List<Map<String, String>> contests = ContestFileManager.getAllContests();
        table.getItems().clear(); // Limpar itens antes de carregar novos dados

        for (Map<String, String> contest : contests) {
            // Calcular arrecadação total e total de prêmios
            double totalRevenue = Double.parseDouble(contest.get("totalRevenue"));
            double totalPrizes = Double.parseDouble(contest.get("totalPrizes"));
            double corporationShare = Double.parseDouble(contest.get("corporationShare"));
            double remainingPrizes = Double.parseDouble(contest.get("remainingPrizes"));
            double totalPrizesDistribuation =  totalPrizes - remainingPrizes;

            // Adicionar informações monetárias ao mapa do concurso
            contest.put("totalRevenue", String.format("R$ %.2f", totalRevenue));
            contest.put("totalPrizes", String.format("R$ %.2f", totalPrizes));
            contest.put("corporationShare", String.format("R$ %.2f", corporationShare));
            contest.put("remainingPrizes", String.format("R$ %.2f", remainingPrizes));
            contest.put("totalPrizesDistribuation", String.format("R$ %.2f", totalPrizesDistribuation));

        }

        table.getItems().addAll(contests);
    }

    private void finalizarConcurso() {
        Map<String, String> selectedContest = table.getSelectionModel().getSelectedItem();
        if (selectedContest != null) {
            String contestCode = selectedContest.get("contestCode");
            ContestManager.finalizeContest(contestCode); // Chama o método do ContestManager
            UIComponents.showAlert("Concurso Finalizado", "O concurso " + contestCode + " foi finalizado.",
                    AlertType.INFORMATION);
            loadContestData(); // Recarregar dados para atualizar a tabela
        } else {
            UIComponents.showAlert("Erro", "Selecione um concurso para finalizar.", AlertType.INFORMATION);
        }
    }

    private void editarConcurso() {
        Map<String, String> selectedContest = table.getSelectionModel().getSelectedItem();
        if (selectedContest != null) {
            String contestName = selectedContest.get("name");

            // Cria um diálogo para solicitar o novo nome do concurso
            TextInputDialog dialog = new TextInputDialog(contestName); // O nome atual como sugestão
            dialog.setTitle("Editar Concurso");
            dialog.setHeaderText("Renomear Concurso");
            dialog.setContentText("Digite o novo nome para o concurso:");

            // Mostra o diálogo e aguarda a entrada do usuário
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newName -> {
                ContestManager.editContestName(contestName, newName); // Chama o método do ContestManager
                UIComponents.showAlert("Concurso Editado",
                        "O concurso " + contestName + " foi renomeado para " + newName + ".", AlertType.INFORMATION);
                loadContestData(); // Recarregar dados para atualizar a tabela
            });
        } else {
            UIComponents.showAlert("Erro", "Selecione um concurso para editar.", AlertType.INFORMATION);
        }
    }

    private void excluirConcurso() {
        Map<String, String> selectedContest = table.getSelectionModel().getSelectedItem();
        if (selectedContest != null) {
            String contestCode = selectedContest.get("contestCode");
            ContestManager.deleteContest(contestCode); // Chama o método do ContestManager
            UIComponents.showAlert("Concurso Excluído", "O concurso " + contestCode + " foi excluído.",
                    AlertType.INFORMATION);
            loadContestData(); // Recarregar dados para atualizar a tabela
        } else {
            UIComponents.showAlert("Erro", "Selecione um concurso para excluir.", AlertType.INFORMATION);
        }
    }

    public VBox getLayout() {
        return layout;
    }
}