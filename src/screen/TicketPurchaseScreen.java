package screen;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class TicketPurchaseScreen {
    private String loggedInUser;
    private VBox layout;
    private List<Button> numberButtons;
    private List<Integer> selectedNumbers;
    private Label lblSelecionados;
    private Label lblValor;
    private static final int MAX_NUM = 20;
    private static final int MIN_NUM = 15;
    private HashMap<String, List<Integer>> ticketPurchases;

    public TicketPurchaseScreen(Stage stage) {
        
        this.loggedInUser = "X";
        layout = new VBox(20);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #DCE8E8; -fx-alignment: center;");

        Label lblTitulo = new Label("COMPRA DE BILHETE");
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Grid para os números
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        
        numberButtons = new ArrayList<>();
        selectedNumbers = new ArrayList<>();
        
        for (int i = 1; i <= 25; i++) {
            final int number = i; // Copy the loop variable to a final variable
            Button btnNumber = new Button(String.format("%02d", i));
            btnNumber.setMinSize(40, 40);
            btnNumber.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: black;");
            btnNumber.setOnAction(e -> handleNumberSelection(btnNumber, number)); // Use 'number' here
            numberButtons.add(btnNumber);
            grid.add(btnNumber, (i - 1) % 5, (i - 1) / 5); // Add button to the grid
        }
        

        // Labels para informações
        lblSelecionados = new Label("Selecionados: 00");
        lblValor = new Label("Valor: (valor a se pagar)");

        // Botões de Surpresinha, Limpar, Voltar e Confirmar
        Button btnSurpresinha = new Button("Surpresinha");
        btnSurpresinha.setOnAction(e -> generateRandomNumbers());

        Button btnLimpar = new Button("Limpar");
        btnLimpar.setOnAction(e -> clearSelection());

        Button btnVoltar = new Button("Voltar");
        btnVoltar.setOnAction(e -> stage.setScene(new Scene(new MainScreen(stage).getLayout(), 600, 400)));

        Button btnConfirmar = new Button("Confirmar");
        btnConfirmar.setOnAction(e -> confirmTicketPurchase());

        // Estilos dos botões
        btnSurpresinha.setStyle("-fx-background-color: #800080; -fx-text-fill: white;");
        btnLimpar.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white;");
        btnVoltar.setStyle("-fx-background-color: #4169E1; -fx-text-fill: white;");
        btnConfirmar.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white;");

        HBox buttonBox = new HBox(10, btnSurpresinha, btnLimpar, btnVoltar, btnConfirmar);
        buttonBox.setAlignment(Pos.CENTER);

        // Layout da página
        VBox infoBox = new VBox(10, lblSelecionados, lblValor);
        infoBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(lblTitulo, grid, infoBox, buttonBox);
        ticketPurchases = new HashMap<>(); // Para armazenar as compras
    }

    public VBox getLayout() {
        return layout;
    }

    // Função para lidar com a seleção de números
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

    private double calculatePrice(int count) {
        switch (count) {
            case 15:
                return 3.00;
            case 16:
                return 48.00;
            case 17:
                return 408.00;
            case 18:
                return 2448.00;
            case 19:
                return 11628.00;
            case 20:
                return 46512.00;
            default:
                return 0.00;
        }
    }
    

    // Atualiza as informações de números selecionados e valor
    private void updateSelectedInfo() {
        lblSelecionados.setText("Selecionados: " + String.format("%02d", selectedNumbers.size()));
        // Atualiza o valor baseado no número de selecionados
        double valor = calculatePrice(selectedNumbers.size());
        lblValor.setText("Valor: R$ " + String.format("%.2f", valor));
    }

    // Gera números aleatórios (Surpresinha)
    private void generateRandomNumbers() {
        clearSelection();
        Random random = new Random();
        while (selectedNumbers.size() < MIN_NUM) {
            int randomNum = random.nextInt(25) + 1; // Números de 1 a 25
            if (!selectedNumbers.contains(randomNum)) {
                selectedNumbers.add(randomNum);
                numberButtons.get(randomNum - 1).setStyle("-fx-background-color: #00FF00; -fx-border-color: black;");
            }
        }
        updateSelectedInfo();
    }

    // Limpa a seleção de números
    private void clearSelection() {
        selectedNumbers.clear();
        for (Button btn : numberButtons) {
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: black;");
        }
        updateSelectedInfo();
    }

    // Confirma a compra e armazena
    private void confirmTicketPurchase() {
        if (selectedNumbers.size() >= MIN_NUM && selectedNumbers.size() <= MAX_NUM) {
            ticketPurchases.put(loggedInUser, new ArrayList<>(selectedNumbers));

            // Store ticket in a .txt file
            try (FileWriter writer = new FileWriter("purchases.txt", true)) {
                writer.write("Usuario: " + loggedInUser + "\n");
                writer.write("Numeros selecionados: " + selectedNumbers + "\n");
                writer.write("Valor: R$ " + String.format("%.2f", calculatePrice(selectedNumbers.size())) + "\n\n");
                showAlert("Compra Confirmada", "Seu bilhete foi registrado e salvo com sucesso!", AlertType.INFORMATION);
            } catch (IOException e) {
                showAlert("Erro", "Não foi possível salvar a compra: " + e.getMessage(), AlertType.ERROR);
            }

            clearSelection(); // Clear the selection after purchase
        } else {
            showAlert("Erro", "Número de seleções inválido.", AlertType.WARNING);
        }
    }

    // Function to show alerts
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
