package screen.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;

public class RulesScreen {
    private BorderPane layout;

    public RulesScreen(Stage stage) {
        // Criação do layout principal
        layout = new BorderPane();
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #f5f7fa, #c3cfe2);");

        // Conteúdo central
        VBox centralContent = new VBox(15);
        stage.setTitle("LotoFacil - Regras");
        centralContent.setPadding(new Insets(20, 40, 20, 40)); // Adicionando espaçamento interno
        centralContent.setAlignment(Pos.CENTER);
        centralContent.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        // Título
        Label lblTitle = UIComponents.createLabel("Regras da Lotofácil",
                "-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #4B0082; -fx-underline: true;");

        // Regras
        Label lblRules = UIComponents.createLabel(
                """
                        - Escolha entre 15 e 20 números, de 1 a 25.
                        - Ganha quem acertar 11, 12, 13, 14 ou 15 números.
                        - Os sorteios são realizados às segundas, quartas e sextas-feiras.
                        - Você pode optar por jogar com a *Surpresinha* (números escolhidos automaticamente).
                        - Os valores das apostas variam de acordo com a quantidade de números escolhidos.
                        - Prêmios devem ser resgatados até 90 dias após o sorteio.
                        """,
                "-fx-font-size: 16px; -fx-text-fill: #555555; -fx-line-spacing: 15; -fx-padding: 24px 0 0 0");

        // Botão para voltar ao menu principal
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0)); // Espaçamento superior
        @SuppressWarnings("unused")
        Button btnBack = UIComponents.createButton("Voltar ao Menu",
                "-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;",
                e -> ScreenNavigator.navigateToMainScreen(stage));
        buttonBox.getChildren().add(btnBack);

        // Adicionando elementos ao conteúdo central
        centralContent.getChildren().addAll(lblTitle, lblRules, buttonBox);

        // Adicionando ao layout principal com espaçamento
        StackPane centeredBox = new StackPane(centralContent);
        centeredBox.setPadding(new Insets(40, 20, 40, 20));
        layout.setCenter(centeredBox);
    }

    public BorderPane getLayout() {
        return layout;
    }
}
