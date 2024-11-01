package screen;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.UIComponents;

import java.util.List;
import database.PurchaseFileManager;
import database.TicketPricing;

@SuppressWarnings("unused")
public class PaymentScreen {
    private VBox layout;
    private String loggedInUser;
    private List<Integer> selectedNumbers;
    private String selectedPaymentMethod = "Boleto"; // Método padrão

    public PaymentScreen(Stage stage, String loggedInUser, List<Integer> selectedNumbers) {
        this.loggedInUser = loggedInUser;
        this.selectedNumbers = selectedNumbers;

        initializeUI(stage);
    }

    private void initializeUI(Stage stage) {
        layout = new VBox(20);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #DCE8E8; -fx-alignment: center;");

        Label lblTitle = new Label("Tela de Pagamento");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Grupo de botões para seleção do método de pagamento
        ToggleGroup paymentGroup = new ToggleGroup();

        RadioButton rbBoleto = new RadioButton("Boleto");
        rbBoleto.setToggleGroup(paymentGroup);
        rbBoleto.setSelected(true); // Boleto como opção padrão

        RadioButton rbCartao = new RadioButton("Cartão");
        rbCartao.setToggleGroup(paymentGroup);

        RadioButton rbPIX = new RadioButton("PIX");
        rbPIX.setToggleGroup(paymentGroup);

        paymentGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            selectedPaymentMethod = ((RadioButton) newValue).getText();
        });

        // Exibir valor total
        Label lblTotal = new Label("Valor Total: R$ " + TicketPricing.calculatePrice(selectedNumbers.size()));
        lblTotal.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Botão Confirmar Pagamento
        Button btnConfirmarPagamento = new Button("Confirmar Pagamento");
        btnConfirmarPagamento.setOnAction(e -> {
            if (confirmarPagamento()) {
                PurchaseFileManager.saveBetToFile(loggedInUser, selectedNumbers);
                System.out.println("Pagamento Confirmado com " + selectedPaymentMethod + "!");
                showConfirmationAlert("Pagamento Confirmado", "Seu pagamento foi realizado com " + selectedPaymentMethod);
            } else {
                UIComponents.showAlert("Erro no Pagamento", "Por favor, selecione um método de pagamento válido.", AlertType.ERROR);
            }
        });

        // Botão Voltar
        Button btnVoltar = new Button("Voltar");
        btnVoltar.setOnAction(e -> stage.setScene(new Scene(new TicketPurchaseScreen(stage, loggedInUser).getLayout(), 600, 400)));

        layout.getChildren().addAll(lblTitle, lblTotal, rbBoleto, rbCartao, rbPIX, btnConfirmarPagamento, btnVoltar);
        layout.setAlignment(Pos.CENTER);
    }

    private boolean confirmarPagamento() {
        return selectedPaymentMethod != null && !selectedPaymentMethod.isEmpty();
    }

    private void showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getLayout() {
        return layout;
    }
}
