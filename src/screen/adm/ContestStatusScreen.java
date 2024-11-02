package screen.adm;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.Contest;
import utils.UIComponents;

public class ContestStatusScreen {
    private VBox layout;

    @SuppressWarnings({ "unused", "unchecked" })
    public ContestStatusScreen(Stage stage) {
        layout = new VBox(15); // Espaçamento entre os elementos
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #DCE8E8;");

        // Título da tela
        Label titleLabel = new Label("Status dos Concursos");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Tabela para exibir o status dos concursos
        TableView<Contest> table = new TableView<>();
        table.setPrefWidth(600); // Definir largura preferida da tabela

        // Coluna para o nome do concurso
        TableColumn<Contest, String> nameColumn = new TableColumn<>("Nome do Concurso");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(200);

        // Coluna para a data do concurso
        TableColumn<Contest, String> dateColumn = new TableColumn<>("Data");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setPrefWidth(150);

        // Coluna para o status do concurso
        TableColumn<Contest, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setPrefWidth(150);

        // Adicionar colunas à tabela
        table.getColumns().addAll(nameColumn, dateColumn, statusColumn);

        // Adicionar dados à tabela (simulado, adicione lógica para buscar dados reais)
        // table.getItems().addAll(
        //         new Contest("Concurso 1", "01/11/2024", "Aberto"),
        //         new Contest("Concurso 2", "15/11/2024", "Fechado"),
        //         new Contest("Concurso 3", "25/11/2024", "Em andamento")
        // );

        // Botão de voltar
        Button btnVoltar = UIComponents.createButton("Voltar", "-fx-background-color: #FF0000; -fx-text-fill: white;",
                e -> ScreenNavigator.navigateToMainScreen(stage)); // Fechar a tela ou navegar de volta à tela anterior

        // Adicionar elementos ao layout
        layout.getChildren().addAll(titleLabel, table, btnVoltar);
    }

    public VBox getLayout() {
        return layout;
    }
}
