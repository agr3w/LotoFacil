package screen;

import database.ContestFileManager;
import database.Database;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import utils.UserSession;

public class MainScreen {
        private BorderPane layout;
        private Label lblContestStatus;
        private Button btnComprarBilhete;

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
                mainContent.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #DCE8E8;");

                // Verificar se o usuário é um administrador
                if (UserSession.isAdminLoggedIn()) {
                        Button btnCadastrarConcurso = UIComponents.createButton("CADASTRAR CONCURSO",
                                        "-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-min-Width: 200;",
                                        e -> ScreenNavigator.navigateToRegisterContestScreenSize(stage));
                        // Button btnResultados = UIComponents.createButton("Resultados",
                        // "-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 14px;",
                        // e -> ScreenNavigator.navigateToResultsScreen(stage));
                        Button btnStatusConcurso = UIComponents.createButton("Status dos concursos",
                                        "-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 14px;",
                                        e -> ScreenNavigator.navigateToContestStatusScreen(stage));

                        btnCadastrarConcurso.setTooltip(new Tooltip("Cadastre um novo concurso."));
                        // btnResultados.setTooltip(new Tooltip("Veja os resultados anteriores."));
                        btnStatusConcurso.setTooltip(new Tooltip("Verifique o status dos concursos."));

                        // Adicionar botões adicionais para administradores
                        mainContent.getChildren().addAll(btnCadastrarConcurso, btnStatusConcurso);
                } else {
                        // Label para exibir o status do concurso
                        lblContestStatus = new Label();

                        // Botão "Comprar Bilhete"
                        btnComprarBilhete = UIComponents.createButton("COMPRAR BILHETE",
                                        "-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-min-Width: 200;",
                                        e -> {
                                                if (ContestFileManager.isContestOpen()) { // Verifica se há concurso
                                                                                          // aberto
                                                        ScreenNavigator.navigateToPurchaseScreen(stage);
                                                } else {
                                                        UIComponents.showAlert("Nenhum Concurso Aberto",
                                                                        "Não há concursos abertos no momento.", null);
                                                }
                                        });
                        // Botões comuns a todos os usuários
                        Button btnHistoricoCompras = UIComponents.createButton("Histórico de Compras",
                                        "-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 14px;",
                                        e -> ScreenNavigator.navigateToPurchaseHistoryScreen(stage));
                        // Button btnRegras = UIComponents.createButton("Regras",
                        // "-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 14px;",
                        // e -> ScreenNavigator.navigateToRulesScreen(stage));

                        // Adicionar Tooltips
                        btnComprarBilhete.setTooltip(new Tooltip("Compre bilhetes para o próximo sorteio."));
                        btnHistoricoCompras.setTooltip(new Tooltip("Veja o histórico de compras."));
                        // btnRegras.setTooltip(new Tooltip("Leia as regras do jogo."));

                        updateContestStatus();

                        // Adicionar botões ao layout principal para usuários comuns
                        mainContent.getChildren().addAll(lblContestStatus, btnComprarBilhete, btnHistoricoCompras);
                }

                // Botão de Sair
                Button btnSair = UIComponents.createButton("Sair",
                                "-fx-background-color: #FF0000; -fx-text-fill: white;",
                                e -> stage.close());

                mainContent.getChildren().add(btnSair);
                // Colocar a barra superior no topo e o conteúdo principal no centro
                // layout.setTop(topBar);
                layout.setCenter(mainContent);
        }

        // Função para atualizar o status do concurso
        private void updateContestStatus() {
                if (ContestFileManager.isContestOpen()) {
                        lblContestStatus.setText("Há um concurso aberto! Você pode apostar.");
                        btnComprarBilhete.setDisable(false); // Habilita o botão
                        btnComprarBilhete.setStyle(
                                        "-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-min-Width: 200;");
                } else {
                        lblContestStatus.setText("Não há concursos abertos no momento.");
                        btnComprarBilhete.setDisable(true); // Desabilita o botão
                        btnComprarBilhete.setStyle(
                                        "-fx-background-color: #A9A9A9; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-min-Width: 200;");
                }
        }

        public BorderPane getLayout() {
                return layout;
        }
}
