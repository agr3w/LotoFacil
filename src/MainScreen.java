import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainScreen {
    private BorderPane layout;

    public MainScreen(Stage stage) {
        layout = new BorderPane();

        // Barra superior (NAV) com fundo mais escuro
        HBox topBar = new HBox(10);
        topBar.setStyle("-fx-background-color: #2F2F2F; -fx-padding: 10; -fx-alignment: center;");
        
        // Botão de configurações à esquerda
        MenuButton configButton = new MenuButton();
        configButton.setStyle("-fx-background-radius: 50%; -fx-background-color: #800080;");
        configButton.setText("⚙"); // Ícone de configuração, pode substituir por imagem
        MenuItem perfilItem = new MenuItem("Perfil");
        configButton.getItems().addAll(perfilItem);

        // Ícone de perfil à direita
        Button profileIcon = new Button("🌟");
        profileIcon.setStyle("-fx-background-radius: 50%; -fx-background-color: #800080;");

        // Ajustar posicionamento dos ícones na barra
        Pane spacerLeft = new Pane(); // Espaço à esquerda para separar os ícones
        Pane spacerRight = new Pane(); // Espaço à direita para separar o ícone do botão Sair
        spacerLeft.setMinWidth(250);  // Definir largura mínima do espaçador esquerdo
        spacerRight.setMinWidth(150); // Definir largura mínima do espaçador direito

        topBar.getChildren().addAll(configButton, spacerLeft, profileIcon, spacerRight);
        
        // Conteúdo principal
        VBox mainContent = new VBox(15); // Espaçamento entre os elementos
        mainContent.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #DCE8E8;"); // Ajuste do fundo mais claro

        // Botão principal "Comprar Bilhete"
        Button btnComprarBilhete = new Button("COMPRAR BILHETE");
        btnComprarBilhete.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        btnComprarBilhete.setMinWidth(200);
        
        // Ação do botão Comprar Bilhete
        btnComprarBilhete.setOnAction(e -> {
            TicketPurchaseScreen purchaseScreen = new TicketPurchaseScreen(stage);
            stage.setScene(new Scene(purchaseScreen.getLayout(), 600, 400));
        });

        // Botões secundários
        Button btnResultados = new Button("Resultados");
        Button btnHistorico = new Button("Histórico");
        Button btnRegras = new Button("Regras");

         // Botão de Sair à direita, separado
         Button btnSair = new Button("Sair");
         btnSair.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
         btnSair.setOnAction(e -> stage.close());

        // Estilos dos botões secundários
        btnResultados.setStyle("-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 14px;");
        btnHistorico.setStyle("-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 14px;");
        btnRegras.setStyle("-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 14px;");
        
        btnResultados.setMinWidth(150);
        btnHistorico.setMinWidth(150);
        btnRegras.setMinWidth(150);

        // Adicionar Tooltips aos botões
        btnComprarBilhete.setTooltip(new Tooltip("Compre bilhetes para o próximo sorteio."));
        btnResultados.setTooltip(new Tooltip("Veja os resultados anteriores."));
        btnHistorico.setTooltip(new Tooltip("Acompanhe seu histórico de apostas."));
        btnRegras.setTooltip(new Tooltip("Leia as regras do jogo."));
        
        // Adiciona os botões ao layout principal
        mainContent.getChildren().addAll(btnComprarBilhete, btnResultados, btnHistorico, btnRegras, btnSair);
        
        // Colocar a barra superior no topo e o conteúdo principal no centro
        layout.setTop(topBar);
        layout.setCenter(mainContent);
    }

    public BorderPane getLayout() {
        return layout;
    }
}
