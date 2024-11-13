package screen.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;

@SuppressWarnings("unused")
public class TermsScreen {
    private VBox layout;

    public TermsScreen(Stage stage) {
        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label lblTermos = UIComponents.createLabel("Termos de Serviço",
                "-fx-font-size: 20px; -fx-text-fill: #800080; -fx-font-weight: bold;");

        TextArea txtTermos = new TextArea();
        txtTermos.setText("Aqui estão os termos de serviço...");
        txtTermos.setWrapText(true);
        txtTermos.setEditable(false);

        Button btnContinuar = UIComponents.createButton("Continuar",
                "-fx-background-color: #800080; -fx-text-fill: white;",
                e -> ScreenNavigator.navigateToRegisterScreen(stage));

        layout.getChildren().addAll(lblTermos, txtTermos, btnContinuar);
    }

    public VBox getLayout() {
        return layout;
    }
}
