package screen.user;

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
import screen.sizes.ScreenNavigator;
import utils.ContestManager;
import utils.UIComponents;
import utils.UserSession;

import java.util.List;
import database.PurchaseFileManager;
import database.TicketPricing;

@SuppressWarnings("unused")
public class PaymentScreen {
    private VBox layout;
    private String loggedInUser;
    private List<Integer> selectedNumbers;
    private String selectedPaymentMethod = "Boleto"; // Método padrão
    private String contestCode;
    private String contestName;

    public PaymentScreen(Stage stage, String loggedInUser, List<Integer> selectedNumbers) {
        this.loggedInUser = loggedInUser;
        this.selectedNumbers = selectedNumbers;
        this.contestName = UserSession.getSelectedContestName();
        this.contestCode = UserSession.getSelectedContestCode();
        initializeUI(stage);
    }

    private void initializeUI(Stage stage) {
        layout = new VBox(20);
        stage.setTitle("LotoFacil - Pagamento");
        layout.setStyle("-fx-padding: 20; -fx-background-color: #DCE8E8; -fx-alignment: center;");

        // Título
        Label lblTitle = UIComponents.createLabel("Página de Pagamento", 
                "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #007BFF;");

        // Grupo de botões para seleção do método de pagamento
        ToggleGroup paymentGroup = new ToggleGroup();

        RadioButton rbBoleto = new RadioButton("Boleto");
        rbBoleto.setToggleGroup(paymentGroup);
        rbBoleto.setSelected(true); // Boleto como opção padrão
        styleRadioButton(rbBoleto);

        RadioButton rbCartao = new RadioButton("Cartão");
        rbCartao.setToggleGroup(paymentGroup);
        styleRadioButton(rbCartao);

        RadioButton rbPIX = new RadioButton("PIX");
        rbPIX.setToggleGroup(paymentGroup);
        styleRadioButton(rbPIX);

        paymentGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            selectedPaymentMethod = ((RadioButton) newValue).getText();
        });

        // Exibir valor total
        Label lblTotal = UIComponents.createLabel(
                "Valor Total: R$ " + TicketPricing.calculatePrice(selectedNumbers.size()),
                "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Botão Confirmar Pagamento
        Button btnConfirmarPagamento = UIComponents.createButton("Confirmar Pagamento", 
                "-fx-background-color: #32CD32; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5;", 
                e -> {
                    if (confirmarPagamento()) {
                        PurchaseFileManager.saveBetToFile(loggedInUser, selectedNumbers, selectedPaymentMethod, contestName, contestCode);
                        ContestManager.placeBet(contestCode);
                        System.out.println("Pagamento Confirmado com " + selectedPaymentMethod + "!");
                        UIComponents.showAlert("Pagamento Confirmado",
                                "Seu pagamento foi realizado com " + selectedPaymentMethod, null);
                        ScreenNavigator.navigateToMainScreen(stage);
                    } else {
                        UIComponents.showAlert("Erro no Pagamento", "Por favor, selecione um método de pagamento válido.",
                                AlertType.ERROR);
                    }
                });

        // Botão Voltar
        Button btnVoltar = UIComponents.createButton("Voltar", 
                "-fx-background-color: #4169E1; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5;", 
                e -> ScreenNavigator.navigateToTicketSummaryScreen(stage, selectedNumbers));

        // Efeito de hover nos botões
        addButtonHoverEffect(btnConfirmarPagamento, "#32CD32", "#98FB98");
        addButtonHoverEffect(btnVoltar, "#4169E1", "#6495ED");

        // Organizando os elementos no layout
        layout.getChildren().addAll(lblTitle, lblTotal, rbBoleto, rbCartao, rbPIX, btnConfirmarPagamento, btnVoltar);
        layout.setAlignment(Pos.CENTER);
    }

    public VBox getLayout() {
        return layout;
    }

    private boolean confirmarPagamento() {
        return selectedPaymentMethod != null && !selectedPaymentMethod.isEmpty();
    }

    private void styleRadioButton(RadioButton radioButton) {
        radioButton.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");
    }

    private void addButtonHoverEffect(Button button, String normalColor, String hoverColor) {
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;"));
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: " + normalColor + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;"));
    }
}
