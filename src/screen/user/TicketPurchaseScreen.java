package screen.user;

import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;
import utils.NumberSelection;

import java.util.ArrayList;
import java.util.List;
import database.TicketPricing;

@SuppressWarnings("unused")
public class TicketPurchaseScreen {
    private VBox layout;
    private List<Button> numberButtons;
    private Label lblSelecionados;
    private Label lblValor;

    private NumberSelection numberSelection;

    private static final int MAX_NUM = 20;
    private static final int MIN_NUM = 15;

    public TicketPurchaseScreen(Stage stage, String cpfUsuarioLogado) {
        layout = new VBox(20); // Espaçamento maior
        stage.setTitle("LotoFacil - Compra de Bilhete");
        layout.setStyle("-fx-padding: 20; -fx-background-color: #F0F4F8; -fx-alignment: center;");
        numberSelection = new NumberSelection();

        initializeUI(stage);
    }

    private void initializeUI(Stage stage) {
        // Título
        Label lblTitulo = UIComponents.createLabel("COMPRA DE BILHETE",
                "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #007BFF;");

        // Grid de números
        GridPane grid = createNumberGrid();

        // Caixa de informações
        lblSelecionados = UIComponents.createLabel("Selecionados: 00", "-fx-font-size: 14px; -fx-text-fill: #333;");
        lblValor = UIComponents.createLabel("Valor: R$ 0,00", "-fx-font-size: 14px; -fx-text-fill: #333;");
        VBox infoBox = new VBox(10, lblSelecionados, lblValor);
        infoBox.setAlignment(Pos.CENTER);

        // Caixa de botões
        HBox buttonBox = createButtonBox(stage);
        buttonBox.setStyle("-fx-alignment: center;");

        // Layout final
        layout.getChildren().addAll(lblTitulo, grid, infoBox, buttonBox);
    }

    private GridPane createNumberGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        numberButtons = new ArrayList<>();

        for (int i = 1; i <= 25; i++) {
            final int number = i;
            Button btnNumber = new Button(String.format("%02d", i));
            btnNumber.setMinSize(50, 50);
            btnNumber.setStyle(
                    "-fx-background-color: #FFFFFF; -fx-border-color: #007BFF; -fx-border-radius: 5; -fx-font-size: 14px;");
            btnNumber.setOnAction(e -> handleNumberSelection(btnNumber, number));
            numberButtons.add(btnNumber);
            grid.add(btnNumber, (i - 1) % 5, (i - 1) / 5);
        }

        return grid;
    }

    private HBox createButtonBox(Stage stage) {
        Button btnSurpresinha = UIComponents.createButton("Surpresinha",
                "-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;",
                e -> generateRandomNumbers());

        Button btnLimpar = UIComponents.createButton("Limpar",
                "-fx-background-color: #FF6347; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;",
                e -> clearSelection());

        Button btnVoltar = UIComponents.createButton("Voltar",
                "-fx-background-color: #4169E1; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;",
                e -> ScreenNavigator.navigateToMainScreen(stage));

        Button btnConfirmar = UIComponents.createButton("Confirmar",
                "-fx-background-color: #32CD32; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;",
                e -> confirmTicketPurchase());

        // Efeito de hover nos botões
        addButtonHoverEffect(btnSurpresinha, "#9370DB", "#DDA0DD");
        addButtonHoverEffect(btnLimpar, "#FF6347", "#FF8C69");
        addButtonHoverEffect(btnVoltar, "#4169E1", "#6495ED");
        addButtonHoverEffect(btnConfirmar, "#32CD32", "#98FB98");

        return new HBox(15, btnSurpresinha, btnLimpar, btnVoltar, btnConfirmar);
    }

    private void addButtonHoverEffect(Button button, String normalColor, String hoverColor) {
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: " + hoverColor
                + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;"));
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: " + normalColor
                + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;"));
    }

    public VBox getLayout() {
        return layout;
    }

    private void handleNumberSelection(Button btn, int number) {
        if (numberSelection.getSelectedNumbers().contains(number)) {
            numberSelection.removeNumber(number);
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #007BFF; -fx-border-radius: 5;");
        } else {
            if (numberSelection.getSelectedCount() >= MAX_NUM) {
                UIComponents.showAlert("Limite Atingido", "O número máximo de seleções é 20.", AlertType.WARNING);
                return;
            }
            numberSelection.addNumber(number);
            btn.setStyle("-fx-background-color: #00FF00; -fx-border-color: #007BFF; -fx-border-radius: 5;");
        }
        updateSelectedInfo();
    }

    private void updateSelectedInfo() {
        lblSelecionados.setText("Selecionados: " + String.format("%02d", numberSelection.getSelectedCount()));
        double valor = TicketPricing.calculatePrice(numberSelection.getSelectedCount());
        lblValor.setText("Valor: R$ " + String.format("%.2f", valor));
    }

    private void generateRandomNumbers() {
        numberSelection.generateRandomNumbers();
        for (int i = 0; i < numberButtons.size(); i++) {
            Button btn = numberButtons.get(i);
            if (numberSelection.getSelectedNumbers().contains(i + 1)) {
                btn.setStyle("-fx-background-color: #00FF00; -fx-border-color: #007BFF; -fx-border-radius: 5;");
            } else {
                btn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #007BFF; -fx-border-radius: 5;");
            }
        }
        updateSelectedInfo();
    }

    private void clearSelection() {
        numberSelection.clearSelection();
        for (Button btn : numberButtons) {
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #007BFF; -fx-border-radius: 5;");
        }
        updateSelectedInfo();
    }

    private void confirmTicketPurchase() {
        if (numberSelection.getSelectedCount() >= MIN_NUM && numberSelection.getSelectedCount() <= MAX_NUM) {
            Stage stage = (Stage) layout.getScene().getWindow();
            ScreenNavigator.navigateToTicketSummaryScreen(stage, numberSelection.getSelectedNumbers());
        } else {
            UIComponents.showAlert("Erro", "Número de seleções inválido.", AlertType.WARNING);
        }
    }
}
