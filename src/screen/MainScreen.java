package screen;

import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;


public class MainScreen {
    private BorderPane layout;
    @SuppressWarnings("unused")
    public MainScreen(Stage stage) {
        layout = new BorderPane();

        // // Barra superior (NAV) com fundo mais escuro
        // HBox topBar = new HBox(10);
        // topBar.setStyle("-fx-background-color: #EEEEEE; -fx-padding: 10;
        // -fx-alignment: center;");

        // // Botão de configurações à esquerda
        // MenuButton configButton = new MenuButton();
        // configButton.setStyle("-fx-background-radius: 50%; -fx-background-color:
        // #800080;");
        // configButton.setText("⚙"); // Ícone de configuração, pode substituir por
        // imagem
        // MenuItem perfilItem = new MenuItem("Perfil");
        // configButton.getItems().addAll(perfilItem);

        // // Ícone de perfil à direita
        // Button profileIcon = new Button("🌟");
        // profileIcon.setStyle("-fx-background-radius: 50%; -fx-background-color:
        // #800080;");

        // // Ajustar posicionamento dos ícones na barra
        // Pane spacerLeft = new Pane(); // Espaço à esquerda para separar os ícones
        // Pane spacerRight = new Pane(); // Espaço à direita para separar o ícone do
        // botão Sair
        // spacerLeft.setMinWidth(250); // Definir largura mínima do espaçador esquerdo
        // spacerRight.setMinWidth(150); // Definir largura mínima do espaçador direito

        // topBar.getChildren().addAll(configButton, spacerLeft, profileIcon,
        // spacerRight);

        // Conteúdo principal
        VBox mainContent = new VBox(15); // Espaçamento entre os elementos
        mainContent.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #DCE8E8;"); // Ajuste do
                                                                                                        // fundo mais
                                                                                                        // claro

        // Botão principal "Comprar Bilhete"
        Button btnComprarBilhete = UIComponents.createButton("COMPRAR BILHETE",
                "-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-min-Width: 200;",
                e -> ScreenNavigator.navigateToPurchaseScreen(stage));

        // Botão principal "Comprar Bilhete"
        Button btnCadastrarConcurso = UIComponents.createButton("CADASTRAR BILHETE",
                "-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-min-Width: 200;",
                e -> ScreenNavigator.navigateToRegisterContestScreenSize(stage));

        // Botões secundários
        Button btnResultados = new Button("Resultados");

        Button btnHistoricoCompras = UIComponents.createButton("Histórico de Compras",
                "-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 14px;",
                e -> ScreenNavigator.navigateToPurchaseHistoryScreen(stage));

        Button btnRegras = new Button("Regras");
        Button btnStatusConcurso = new Button("Status dos concursos");

        // Estilos dos botões secundários
        btnResultados.setStyle("-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 14px;");
        btnRegras.setStyle("-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 14px;");
        btnStatusConcurso.setStyle("-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 14px;");

        btnResultados.setMinWidth(150);
        btnRegras.setMinWidth(150);
        btnStatusConcurso.setMinWidth(150);

        // Adicionar Tooltips aos botões
        btnComprarBilhete.setTooltip(new Tooltip("Compre bilhetes para o próximo sorteio."));
        btnResultados.setTooltip(new Tooltip("Veja os resultados anteriores."));
        btnRegras.setTooltip(new Tooltip("Leia as regras do jogo."));
        btnStatusConcurso.setTooltip(new Tooltip("Veja o status dos concursos."));

        // Botão de Sair à direita, separado
        Button btnSair = UIComponents.createButton("Sair", "-fx-background-color: #FF0000; -fx-text-fill: white;",
                e -> stage.close());

        // Adiciona os botões ao layout principal
        mainContent.getChildren().addAll(btnComprarBilhete, btnCadastrarConcurso, btnResultados, btnStatusConcurso,
                btnHistoricoCompras, btnRegras, btnSair);

        // Colocar a barra superior no topo e o conteúdo principal no centro
        // layout.setTop(topBar);
        layout.setCenter(mainContent);
    }

    public BorderPane getLayout() {
        return layout;
    }
}
