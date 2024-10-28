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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import database.TicketPricing;

public class TicketPurchaseScreen {
    private VBox layout;
    private List<Button> numberButtons;
    private List<Integer> selectedNumbers;
    private Label lblSelecionados;
    private Label lblValor;

    private static final int MAX_NUM = 20;
    private static final int MIN_NUM = 15;

    // Construtor principal
    public TicketPurchaseScreen(Stage stage, String cpfUsuarioLogado) {
        layout = new VBox(20);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #DCE8E8; -fx-alignment: center;");

        initializeUI(stage); // Inicializa a interface do usuário
        new HashMap<>(); // Para armazenar as compras
    }

    private void initializeUI(Stage stage) {
        // Título
        Label lblTitulo = UIComponents.createLabel("COMPRA DE BILHETE", "-fx-font-size: 20px; -fx-font-weight: bold;");

        // Grid para os números
        GridPane grid = createNumberGrid();

        // Labels para informações
        lblSelecionados = new Label("Selecionados: 00");
        lblValor = new Label("Valor: (valor a se pagar)");

        // Botões de Surpresinha, Limpar, Voltar e Confirmar
        HBox buttonBox = createButtonBox(stage);

        // Layout da página
        VBox infoBox = new VBox(10, lblSelecionados, lblValor);
        infoBox.setAlignment(Pos.CENTER);

        // Adiciona todos os elementos ao layout
        layout.getChildren().addAll(lblTitulo, grid, infoBox, buttonBox);
    }

    private GridPane createNumberGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        numberButtons = new ArrayList<>();
        selectedNumbers = new ArrayList<>();

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
        if (selectedNumbers.contains(number)) {
            selectedNumbers.remove((Integer) number);
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: black;");
        } else {
            if (selectedNumbers.size() < MAX_NUM) {
                selectedNumbers.add(number);
                btn.setStyle("-fx-background-color: #00FF00; -fx-border-color: black;");
            }
        }
        updateSelectedInfo();
    }

    private void updateSelectedInfo() {
        lblSelecionados.setText("Selecionados: " + String.format("%02d", selectedNumbers.size()));
        double valor = TicketPricing.calculatePrice(selectedNumbers.size());
        lblValor.setText("Valor: R$ " + String.format("%.2f", valor));
    }

    private void generateRandomNumbers() {
        clearSelection();
        Random random = new Random();
        while (selectedNumbers.size() < MIN_NUM) {
            int randomNum = random.nextInt(25) + 1;
            if (!selectedNumbers.contains(randomNum)) {
                selectedNumbers.add(randomNum);
                numberButtons.get(randomNum - 1).setStyle("-fx-background-color: #00FF00; -fx-border-color: black;");
            }
        }
        updateSelectedInfo();
    }

    private void clearSelection() {
        selectedNumbers.clear();
        for (Button btn : numberButtons) {
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: black;");
        }
        updateSelectedInfo();
    }

    private void confirmTicketPurchase() {
        if (selectedNumbers.size() >= MIN_NUM && selectedNumbers.size() <= MAX_NUM) {
            Stage stage = (Stage) layout.getScene().getWindow();
                    ScreenNavigator.navigateToTicketSummaryScreen(stage, selectedNumbers);
        } else {
            UIComponents.showAlert("Erro", "Número de seleções inválido.", AlertType.WARNING);
        }
    }
}
