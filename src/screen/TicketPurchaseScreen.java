package screen;

import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
        layout = new VBox(20);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #DCE8E8; -fx-alignment: center;");
        numberSelection = new NumberSelection();

        initializeUI(stage);
    }

    private void initializeUI(Stage stage) {
        Label lblTitulo = UIComponents.createLabel("COMPRA DE BILHETE", "-fx-font-size: 20px; -fx-font-weight: bold;");
        GridPane grid = createNumberGrid();

        lblSelecionados = UIComponents.createLabel("Selecionados: 00", null);
        lblValor = UIComponents.createLabel("Valor: (valor a se pagar)", null);
        HBox buttonBox = createButtonBox(stage);

        VBox infoBox = new VBox(10, lblSelecionados, lblValor);
        infoBox.setAlignment(Pos.CENTER);
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
            btnNumber.setMinSize(40, 40);
            btnNumber.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: black;");
            btnNumber.setOnAction(e -> handleNumberSelection(btnNumber, number));
            numberButtons.add(btnNumber);
            grid.add(btnNumber, (i - 1) % 5, (i - 1) / 5);
        }

        return grid;
    }
    
    private HBox createButtonBox(Stage stage) {
        Button btnSurpresinha = UIComponents.createButton("Surpresinha",
                "-fx-background-color: #800080; -fx-text-fill: white;", e -> generateRandomNumbers());

        Button btnLimpar = UIComponents.createButton("Limpar", "-fx-background-color: #FF6347; -fx-text-fill: white;",
                e -> clearSelection());

        Button btnVoltar = UIComponents.createButton("Voltar", "-fx-background-color: #4169E1; -fx-text-fill: white;",
                e -> ScreenNavigator.navigateToMainScreen(stage));

        Button btnConfirmar = UIComponents.createButton("Confirmar",
                "-fx-background-color: #32CD32; -fx-text-fill: white;", e -> confirmTicketPurchase());

        return new HBox(10, btnSurpresinha, btnLimpar, btnVoltar, btnConfirmar);
    }

    public VBox getLayout() {
        return layout;
    }

    private void handleNumberSelection(Button btn, int number) {
        if (numberSelection.getSelectedNumbers().contains(number)) {
            numberSelection.removeNumber(number);
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: black;");
        } else {
            if (numberSelection.getSelectedCount() >= MAX_NUM) {
                UIComponents.showAlert("Limite Atingido", "O número máximo de seleções é 20.", AlertType.WARNING);
                return;
            }
            numberSelection.addNumber(number);
            btn.setStyle("-fx-background-color: #00FF00; -fx-border-color: black;");
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
                btn.setStyle("-fx-background-color: #00FF00; -fx-border-color: black;");
            } else {
                btn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: black;");
            }
        }
        updateSelectedInfo();
    }

    private void clearSelection() {
        numberSelection.clearSelection();
        for (Button btn : numberButtons) {
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: black;");
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
