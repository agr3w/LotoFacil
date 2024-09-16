import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainScreen {
    private VBox layout;

    public MainScreen(Stage stage) {
        layout = new VBox();
        Button btnComprarBilhete = new Button("Comprar Bilhete");
        Button btnPerfil = new Button("Perfil");
        Button btnSair = new Button("Sair");

        // Configurar layout
        layout.getChildren().addAll(btnComprarBilhete, btnPerfil, btnSair);

        // Ação dos botões
        btnComprarBilhete.setOnAction(e -> {
            TicketPurchaseScreen purchaseScreen = new TicketPurchaseScreen(stage);
            stage.setScene(new Scene(purchaseScreen.getLayout(), 600, 400));
        });

        btnSair.setOnAction(e -> stage.close());
    }

    public VBox getLayout() {
        return layout;
    }
}
