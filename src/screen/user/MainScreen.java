package screen.user;

import database.ContestFileManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;
import utils.UserSession;

public class MainScreen {
        private BorderPane layout;
        private Label lblContestStatus;
        private Button btnComprarBilhete;
        private String userName = UserSession.getLoggedInUserName();

        @SuppressWarnings("unused")
        public MainScreen(Stage stage) {
                layout = new BorderPane();
                stage.setTitle("LotoFacil - Menu Principal");

                // Conteúdo principal
                VBox mainContent = new VBox(15); // Espaçamento entre os elementos
                mainContent.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #DCE8E8;");

                // Barra superior (NAV) com fundo mais escuro e estilização
                HBox topBar = new HBox();
                topBar.setStyle("-fx-background-color: #800080; -fx-padding: 10;");
                topBar.setAlignment(Pos.CENTER_LEFT);

                // Ícone de configurações à esquerda
                MenuButton configButton = new MenuButton("👤");
                configButton.setStyle(
                                "-fx-font-size: 16px; -fx-text-fill: white; -fx-background-color: transparent; "
                                                + "-fx-padding: 5; -fx-cursor: hand;");

                // MenuItem "Perfil"
                MenuItem perfilItem = new MenuItem("Perfil");
                perfilItem.setStyle(
                                "-fx-font-size: 14px; -fx-text-fill: #FFFFFF; -fx-background-color: #6A5ACD; -fx-cursor: hand; "
                                                + "-fx-padding: 10px; -fx-font-weight: bold; -fx-border-radius: 5;");
                perfilItem.setOnAction(e -> {
                        ScreenNavigator.navigateToProfileScreen(stage); // Navegar para a tela de perfil
                });

                // MenuItem "Trocar Usuário"
                MenuItem sairItem = new MenuItem("Trocar Usuário");
                sairItem.setStyle("-fx-font-size: 14px; -fx-text-fill: #FFFFFF; -fx-background-color: #FF4500; -fx-cursor: hand; "
                                + "-fx-padding: 10px; -fx-font-weight: bold; -fx-border-radius: 5;");
                sairItem.setOnAction(e -> {
                        ScreenNavigator.navigateToLoginScreen(stage); // Navegar para a tela de login
                });

                // Adiciona os itens ao MenuButton
                configButton.getItems().addAll(perfilItem, sairItem);

                // Espaço central para a mensagem de boas-vindas
                Label lblWelcome = UIComponents.createLabel(
                                "Bem-vindo, " + userName,
                                "-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;");

                // Adicionando espaçadores para organizar os elementos
                Region spacerLeft = new Region();
                Region spacerRight = new Region();
                spacerRight.setStyle("-fx-padding: 0 35px;");
                HBox.setHgrow(spacerLeft, Priority.ALWAYS);
                HBox.setHgrow(spacerRight, Priority.ALWAYS);

                // Adiciona os elementos à barra superior
                topBar.getChildren().addAll(configButton, spacerLeft, lblWelcome, spacerRight);

                // Verificar se o usuário é um administrador
                if (UserSession.isAdminLoggedIn()) {

                        Button btnCadastrarConcurso = UIComponents.createButton("CADASTRAR CONCURSO",
                                        "-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 10 20 10 20; -fx-border-radius: 5; -fx-background-radius: 5; -fx-pref-width: 200; -fx-cursor: hand;",
                                        e -> ScreenNavigator.navigateToRegisterContestScreenSize(stage));
                        Button btnStatusConcurso = UIComponents.createButton("Status dos concursos",
                                        "-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20 10 20; -fx-border-radius: 5; -fx-background-radius: 5; -fx-pref-width: 200; -fx-cursor: hand;",
                                        e -> ScreenNavigator.navigateToContestStatusScreen(stage));

                        Button btnStatusUsuarios = UIComponents.createButton("Status dos usuarios",
                                        "-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20 10 20; -fx-border-radius: 5; -fx-background-radius: 5; -fx-pref-width: 200; -fx-cursor: hand;",
                                        e -> ScreenNavigator.navigateToUserManagementScreen(stage));

                        btnCadastrarConcurso.setTooltip(new Tooltip("Cadastre um novo concurso."));
                        btnStatusConcurso.setTooltip(new Tooltip("Verifique o status dos concursos."));
                        btnStatusUsuarios.setTooltip(new Tooltip("Verifique o status dos usuários."));

                        // Adicionar botões adicionais para administradores
                        mainContent.getChildren().addAll(btnCadastrarConcurso, btnStatusConcurso, btnStatusUsuarios);
                } else {

                        lblContestStatus = new Label();
                        lblContestStatus.setStyle(
                                        "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-alignment: center;");

                        // Botão "Comprar Bilhete"
                        btnComprarBilhete = UIComponents.createButton("COMPRAR BILHETE",
                                        "-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; "
                                                        + "-fx-padding: 10 20 10 20; -fx-border-radius: 5; -fx-background-radius: 5; -fx-pref-width: 200; -fx-cursor: hand;",
                                        e -> {
                                                if (ContestFileManager.isContestOpen()) { // Verifica se há concurso
                                                                                          // aberto
                                                        ScreenNavigator.navigateToSelectContestScreen(stage);
                                                } else {
                                                        UIComponents.showAlert("Nenhum Concurso Aberto",
                                                                        "Não há concursos abertos no momento.", null);
                                                }
                                        });

                        // Botão "Resultados"
                        Button btnResultadosDeSorteios = UIComponents.createButton("RESULTADOS",
                                        "-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; "
                                                        + "-fx-padding: 10 20 10 20; -fx-border-radius: 5; -fx-background-radius: 5; -fx-pref-width: 200; -fx-cursor: hand;",
                                        e -> ScreenNavigator.navigateToResultsScreen(stage));

                        // Botão "Regras"
                        Button btnRegras = UIComponents.createButton("REGRAS",
                                        "-fx-background-color: #9370DB; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; "
                                                        + "-fx-padding: 10 20 10 20; -fx-border-radius: 5; -fx-background-radius: 5; -fx-pref-width: 200; -fx-cursor: hand;",
                                        e -> ScreenNavigator.navigateToRulesScreen(stage));

                        // Tooltips para orientação
                        btnComprarBilhete.setTooltip(new Tooltip("Compre bilhetes para o próximo sorteio."));
                        btnResultadosDeSorteios.setTooltip(new Tooltip("Veja os resultados anteriores."));

                        // Atualizar o status do concurso
                        updateContestStatus();

                        mainContent.getChildren().addAll(lblContestStatus, btnComprarBilhete,
                                        btnResultadosDeSorteios, btnRegras);
                }

                // Botão de Sair
                Button btnSair = UIComponents.createButton("Sair",
                                "-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 14px; "
                                                + "-fx-padding: 10 20 10 20; -fx-border-radius: 5; -fx-background-radius: 5; -fx-pref-width: 200; -fx-cursor: hand;",
                                e -> stage.close());

                mainContent.getChildren().add(btnSair);
                layout.setTop(topBar);
                layout.setCenter(mainContent);
        }

        // Metodo para atualizar o status do concurso
        private void updateContestStatus() {
                if (ContestFileManager.isContestOpen()) {
                        lblContestStatus.setText("Há concursos abertos! Você pode apostar.");
                        lblContestStatus.setStyle(
                                        "-fx-text-fill: #228B22; -fx-font-size: 16px; -fx-font-weight: bold;");
                        btnComprarBilhete.setDisable(false);
                } else {
                        lblContestStatus.setText("Não há concursos abertos no momento.");
                        lblContestStatus.setStyle(
                                        "-fx-text-fill: #FF4500; -fx-font-size: 16px; -fx-font-weight: bold;");
                        btnComprarBilhete.setDisable(true);
                        btnComprarBilhete.setStyle(
                                        "-fx-background-color: #A9A9A9; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; "
                                                        + "-fx-padding: 10 20 10 20; -fx-border-radius: 5; -fx-background-radius: 5; -fx-pref-width: 200;");
                }
        }

        public BorderPane getLayout() {
                return layout;
        }
}
