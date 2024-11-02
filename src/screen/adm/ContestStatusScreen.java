package screen.adm;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;

import java.util.List;
import java.util.Map;

import database.ContestFileManager;

public class ContestStatusScreen {
    private VBox layout;
    private TableView<Map<String, String>> table; // Alterado para Map<String, String>

    @SuppressWarnings("unused")
    public ContestStatusScreen(Stage stage) {
        layout = new VBox(15);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #DCE8E8;");

        Label titleLabel = new Label("Status dos Concursos");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Inicializar tabela
        table = new TableView<>();
        table.setPrefWidth(600);
        setupTableColumns();

        // Carregar dados reais
        loadContestData();

        // Botão de voltar
        Button btnVoltar = UIComponents.createButton("Voltar", "-fx-background-color: #FF0000; -fx-text-fill: white;",
                e -> ScreenNavigator.navigateToMainScreen(stage));

        layout.getChildren().addAll(titleLabel, table, btnVoltar);
    }

    @SuppressWarnings("unchecked")
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
        statusColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper("Aberto")); // Todos os concursos são abertos
        statusColumn.setPrefWidth(150);

        table.getColumns().addAll(nameColumn, startDateColumn, endDateColumn, statusColumn);
    }

    private void loadContestData() {
        List<Map<String, String>> contests = ContestFileManager.getAllContests(); // Chama o método que você criou
        table.getItems().clear(); // Limpar itens antes de carregar novos dados
        table.getItems().addAll(contests);
    }

    public VBox getLayout() {
        return layout;
    }
}
