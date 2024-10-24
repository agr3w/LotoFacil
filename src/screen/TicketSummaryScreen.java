package screen;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import database.FileManager;

import java.util.List;

public class TicketSummaryScreen {
    private VBox layout;
    private List<Integer> selectedNumbers;
    private String loggedInUser;

    public TicketSummaryScreen(Stage stage, List<Integer> selectedNumbers, String loggedInUser) {
        this.selectedNumbers = selectedNumbers;
        this.loggedInUser = loggedInUser;

        initializeUI(stage);
    }

    private void initializeUI(Stage stage) {
        layout = new VBox(20);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #DCE8E8; -fx-alignment: center;");

        Label lblTitle = new Label("Resumo da Aposta");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label lblNumbers = new Label("Números Selecionados: " + selectedNumbers.toString());

        Button btnConfirmar = new Button("Confirmar");
        btnConfirmar.setOnAction(e -> confirmPayment(stage));

        Button btnVoltar = new Button("Voltar");
        btnVoltar.setOnAction(e -> stage.setScene(new Scene(new TicketPurchaseScreen(stage, loggedInUser).getLayout(), 600, 400)));

        layout.getChildren().addAll(lblTitle, lblNumbers, btnConfirmar, btnVoltar);
        layout.setAlignment(Pos.CENTER);
    }

    private void confirmPayment(Stage stage) {
        // Redirecionar para a tela de pagamento
        PaymentScreen paymentScreen = new PaymentScreen(stage, loggedInUser, selectedNumbers, this);
        stage.setScene(new Scene(paymentScreen.getLayout(), 600, 400));
    }

    // Método para salvar a aposta após o pagamento
    public void saveBetToFile() {
        boolean success = FileManager.saveBetToFile(loggedInUser, selectedNumbers);
        if (success) {
            System.out.println("Aposta salva com sucesso!");
        } else {
            System.out.println("Erro ao salvar a aposta.");
        }
    }

    public VBox getLayout() {
        return layout;
    }
}
