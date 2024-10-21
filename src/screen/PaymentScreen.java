package screen;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class PaymentScreen {
    private VBox layout;
    private String loggedInUser;
    private List<Integer> selectedNumbers;
    private TicketSummaryScreen summaryScreen;

    public PaymentScreen(Stage stage, String loggedInUser, List<Integer> selectedNumbers, TicketSummaryScreen summaryScreen) {
        this.loggedInUser = loggedInUser;
        this.selectedNumbers = selectedNumbers;
        this.summaryScreen = summaryScreen;

        initializeUI(stage);
    }

    private void initializeUI(Stage stage) {
        layout = new VBox(20);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #DCE8E8; -fx-alignment: center;");

        Label lblTitle = new Label("Tela de Pagamento");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Aqui você pode adicionar campos de pagamento, como valor total, etc.

        Button btnConfirmarPagamento = new Button("Confirmar Pagamento");
        btnConfirmarPagamento.setOnAction(e -> {
            summaryScreen.saveBetToFile(); // Salvar a aposta após pagamento
            System.out.println("Pagamento Confirmado!");
            // Aqui você pode redirecionar para uma tela de confirmação de compra ou de sucesso
        });

        Button btnVoltar = new Button("Voltar");
        btnVoltar.setOnAction(e -> stage.setScene(new Scene(new TicketPurchaseScreen(stage, loggedInUser).getLayout(), 600, 400)));

        layout.getChildren().addAll(lblTitle, btnConfirmarPagamento, btnVoltar);
        layout.setAlignment(Pos.CENTER);
    }

    public VBox getLayout() {
        return layout;
    }
}
