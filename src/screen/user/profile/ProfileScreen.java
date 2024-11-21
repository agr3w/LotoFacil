package screen.user.profile;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.Contest;
import utils.ContestManager;
import utils.UIComponents;
import utils.UserSession;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.Database;
import database.PurchaseFileManager;

@SuppressWarnings("unused")
public class ProfileScreen {
    private BorderPane layout;
    private VBox menu;
    private VBox mainContent;
    private boolean isEditable = false; // Controle de edição
    private TextField txtNome;
    private TextField txtEmail;
    private TextField txtCelular;
    private Button btnEditar;

    private String loggedInUser = UserSession.getLoggedInUserCpf();

    public ProfileScreen(Stage stage) {
        layout = new BorderPane();

        // Criar menu à esquerda
        menu = new VBox(10);
        menu.setStyle("""
                    -fx-padding: 20;
                    -fx-background-color: #E3E7EA;
                    -fx-min-width: 220;
                    -fx-alignment: top-center;
                    -fx-spacing: 15;
                """);
        stage.setTitle("LotoFacil - Perfil de Usuário");
        menu.setStyle("-fx-padding: 10; -fx-background-color: #F0F0F0; -fx-min-width: 200;");

        // Adicionar itens ao menu
        Label lblPerfil = createClickableLabel("Perfil", e -> showPerfilContent());
        Label lblHistoricoCompras = createClickableLabel("Histórico de Compras", e -> showHistoricoContent());
        Label lblConfiguracoes = createClickableLabel("Configurações", e -> showConfiguracoesContent());
        Button btnVoltar = UIComponents.createButton("Voltar", "-fx-background-color: #D9534F; -fx-text-fill: white; -fx-cursor: hand",
                e -> ScreenNavigator.navigateToMainScreen(stage));

        lblPerfil.setStyle("-fx-font-size: 16; -fx-text-fill: #333; -fx-font-weight: bold; -fx-cursor: hand;");
        lblHistoricoCompras
                .setStyle("-fx-font-size: 16; -fx-text-fill: #333; -fx-font-weight: bold; -fx-cursor: hand;");
        lblConfiguracoes.setStyle("-fx-font-size: 16; -fx-text-fill: #333; -fx-font-weight: bold;-fx-cursor: hand;");

        menu.getChildren().addAll(lblPerfil, lblHistoricoCompras, lblConfiguracoes, btnVoltar);

        // Conteúdo principal
        mainContent = new VBox(15);
        mainContent.setStyle("-fx-padding: 20; -fx-background-color: #DCE8E8; -fx-alignment: center;");
        showPerfilContent(); // Exibe o conteúdo padrão

        // Configurando o layout principal
        layout.setLeft(menu);
        layout.setCenter(mainContent);
    }

    private Label createClickableLabel(String text, javafx.event.EventHandler<? super MouseEvent> eventHandler) {
        Label label = new Label(text);
        label.setStyle("-fx-cursor: hand; -fx-font-size: 16px; -fx-text-fill: #800080;");
        label.setOnMouseClicked(eventHandler);
        return label;
    }

    private void loadUserData() {
        String cpf = UserSession.getLoggedInUserCpf(); // Obtenha o CPF do usuário logado
        Path path = Paths.get("c:\\tmp\\users.txt");
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] data = line.split(";");
                String userCpf = data[0].split(": ")[1];
                if (userCpf.equals(cpf)) {
                    txtNome.setText(data[3].split(": ")[1]);
                    txtEmail.setText(data[4].split(": ")[1]);
                    txtCelular.setText(data[5].split(": ")[1]); // Presumindo que o celular esteja na posição 5
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUserData() {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String celular = txtCelular.getText();
        String cpf = UserSession.getLoggedInUserCpf(); // Obtenha o CPF do usuário logado
        Path path = Paths.get("c:\\tmp\\users.txt");

        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String userCpf = line.split(";")[0].split(": ")[1]; // Presumindo que o CPF esteja na primeira parte da
                                                                    // linha
                if (userCpf.equals(cpf)) {
                    // Atualiza apenas os campos que mudaram
                    String[] data = line.split(";");
                    data[3] = "nome: " + nome; // Atualiza o nome
                    data[4] = "email: " + email; // Atualiza o email
                    data[6] = "celular: " + celular; // Atualiza o celular (ajuste a posição se necessário)

                    // Recria a linha com os dados atualizados
                    lines.set(i, String.join(";", data));
                    break; // Sai do loop após encontrar e substituir
                }
            }
            // Escreve de volta no arquivo
            Files.write(path, lines);
            System.out.println("Informações salvas: " + nome + ", " + email + ", " + celular);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPerfilContent() {
        mainContent.getChildren().clear();

        // Título estilizado
        Label lblTitle = new Label("Perfil do Usuário");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4A4A4A; -fx-padding: 0 0 20 0;");

        // Campos para editar as informações do usuário
        txtNome = new TextField();
        txtEmail = new TextField();
        txtCelular = new TextField();

        // Estiliza os campos de texto
        String textFieldStyle = "-fx-font-size: 14px; -fx-padding: 10; -fx-background-radius: 5; " +
                "-fx-border-color: #DCDCDC; -fx-border-radius: 5;";
        txtNome.setStyle(textFieldStyle);
        txtEmail.setStyle(textFieldStyle);
        txtCelular.setStyle(textFieldStyle);

        // Botão para editar as informações
        btnEditar = new Button("Editar");
        btnEditar.setStyle("-fx-background-color: #800080; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;");
        btnEditar.setOnMouseEntered(e -> btnEditar.setStyle("-fx-background-color: #9932CC; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5;"));
        btnEditar.setOnMouseExited(e -> btnEditar.setStyle("-fx-background-color: #800080; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5;"));
        btnEditar.setOnAction(e -> toggleEdit());

        loadUserData(); // Carrega os dados do usuário no início

        // Layout principal
        VBox profileBox = new VBox(15);
        profileBox.setStyle("-fx-padding: 20; -fx-background-color: #F8F8FF; -fx-background-radius: 10;");
        profileBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Layout para os campos de entrada
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setStyle("-fx-alignment: center;");
        gridPane.add(new Label("Nome:"), 0, 0);
        gridPane.add(txtNome, 1, 0);
        gridPane.add(new Label("Email:"), 0, 1);
        gridPane.add(txtEmail, 1, 1);
        gridPane.add(new Label("Celular:"), 0, 2);
        gridPane.add(txtCelular, 1, 2);

        // Estilizar os rótulos
        for (int i = 0; i < gridPane.getChildren().size(); i++) {
            if (gridPane.getChildren().get(i) instanceof Label) {
                ((Label) gridPane.getChildren().get(i)).setStyle("-fx-font-size: 14px; -fx-text-fill: #4A4A4A;");
            }
        }

        // Botões alinhados no centro
        HBox buttonBox = new HBox(10, btnEditar);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Adicionar elementos ao layout principal
        profileBox.getChildren().addAll(lblTitle, gridPane, buttonBox);

        mainContent.getChildren().add(profileBox);
        setFieldsEditable(false); // Começa como somente leitura
    }

    private void toggleEdit() {
        isEditable = !isEditable;
        setFieldsEditable(isEditable);
        btnEditar.setText(isEditable ? "Salvar" : "Editar");

        if (!isEditable) {
            saveUserData(); // Salva os dados se não estiver em modo de edição
        }
    }

    private void setFieldsEditable(boolean editable) {
        txtNome.setEditable(editable);
        txtEmail.setEditable(editable);
        txtCelular.setEditable(editable);

        String readonlyStyle = "-fx-background-color: #E0E0E0; -fx-font-size: 14px; -fx-padding: 10; " +
                "-fx-background-radius: 5; -fx-border-color: #DCDCDC; -fx-border-radius: 5;";
        String editableStyle = "-fx-background-color: #FFFFFF; -fx-font-size: 14px; -fx-padding: 10; " +
                "-fx-background-radius: 5; -fx-border-color: #DCDCDC; -fx-border-radius: 5;";

        txtNome.setStyle(editable ? editableStyle : readonlyStyle);
        txtEmail.setStyle(editable ? editableStyle : readonlyStyle);
        txtCelular.setStyle(editable ? editableStyle : readonlyStyle);
    }

    private void showHistoricoContent() {
        // Limpa o conteúdo atual do mainContent
        mainContent.getChildren().clear();

        // Título da seção
        Label lblTitle = UIComponents.createLabel("Histórico de Compras",
                "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4A4A4A; -fx-padding: 0 0 20 0;");

        // Carrega o histórico de compras filtrado pelo CPF do usuário logado
        List<PurchaseFileManager> historicoCompras = PurchaseFileManager.loadUserTickets();

        // Layout para exibir os cartões
        VBox historicoContainer = new VBox(15); // Maior espaçamento entre os cartões
        historicoContainer.setStyle("-fx-padding: 20;");

        // Itera sobre cada compra no histórico e cria um cartão para exibir os detalhes
        for (PurchaseFileManager compra : historicoCompras) {
            VBox card = new VBox(10); // Espaçamento maior entre as linhas do cartão
            card.setStyle(
                    "-fx-border-color: #800080; -fx-border-radius: 10; -fx-border-width: 2; -fx-padding: 15; " +
                            "-fx-background-color: #F8F8FF; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 4, 0.5, 0, 2);");

            // Busca os detalhes do concurso pelo código
            Map<String, String> contestDetails = ContestManager.getContestByCode(compra.getContestCode());

            // Labels para mostrar as informações da compra
            Label lblCodigo = UIComponents.createLabel("Código da Compra: " + compra.getCodigoCompra(),
                    "-fx-font-size: 14px; -fx-font-weight: bold;");
            Label lblNomeAposta = UIComponents.createLabel("Nome do Concurso: " + compra.getNomeAposta(),
                    "-fx-font-size: 14px;");
            Label lblValor = UIComponents.createLabel("Valor Pago: R$ " + compra.getValueFromFile(),
                    "-fx-font-size: 14px; -fx-text-fill: #228B22;");
            Label lblValoresApostados = UIComponents.createLabel(
                    "Valores Apostados: " + compra.getSelectedNumbersFromFile(),
                    "-fx-font-size: 14px;");
            Label lblDataCompra = UIComponents.createLabel("Data da Compra: " + compra.getDataCompra(),
                    "-fx-font-size: 14px;");
            Label lblFormaPagamento = UIComponents.createLabel("Forma de Pagamento: " + compra.getFormaPagamento(),
                    "-fx-font-size: 14px;");

            // Verifica se o concurso foi finalizado e adiciona o prêmio ao cartão
            if (contestDetails != null && "Finalizado".equals(contestDetails.get("status"))) {
                List<Integer> selectedNumbers = ContestManager
                        .parseSelectedNumbers(compra.getSelectedNumbersFromFile());
                List<Integer> winningNumbers = ContestManager.parseWinningNumbers(contestDetails.get("winningNumbers"));
                int correctCount = ContestManager.countCorrectNumbers(selectedNumbers, winningNumbers);

                double totalPrizePool = Double.parseDouble(contestDetails.get("totalPrizes"));
                double prize = ContestManager.calculatePrize(correctCount, totalPrizePool);

                Label lblStatusConcurso = UIComponents.createLabel("Status do Concurso: Finalizado",
                        "-fx-font-size: 14px; -fx-text-fill: #FF4500;");
                Label lblPremio = UIComponents.createLabel("Prêmio: R$ " + String.format("%.2f", prize),
                        "-fx-font-size: 14px; -fx-text-fill: #FFD700;");

                Label lblValoresVencedores = UIComponents.createLabel("Valores vencedores: " + winningNumbers,
                        "-fx-font-size: 14px;");

                card.getChildren().addAll(lblStatusConcurso, lblPremio, lblValoresVencedores);
            } else {
                Label lblStatusConcurso = UIComponents.createLabel("Status do Concurso: Não Finalizado",
                        "-fx-font-size: 14px; -fx-text-fill: #4A4A4A;");
                card.getChildren().add(lblStatusConcurso);
            }

            // Adiciona as labels ao cartão
            card.getChildren().addAll(lblCodigo, lblNomeAposta, lblValor, lblValoresApostados, lblDataCompra,
                    lblFormaPagamento);

            // Adiciona o cartão ao container
            historicoContainer.getChildren().add(card);
        }

        // Se o histórico estiver vazio, mostra uma mensagem informativa
        if (historicoCompras.isEmpty()) {
            Label lblNoCompras = UIComponents.createLabel("Nenhum histórico de compras encontrado.",
                    "-fx-font-size: 16px; -fx-text-fill: #800080; -fx-padding: 20; -fx-alignment: center;");
            mainContent.getChildren().addAll(lblTitle, lblNoCompras);
        } else {
            // Se houver compras, adiciona o título e os cartões ao conteúdo principal
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(historicoContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            mainContent.getChildren().addAll(lblTitle, scrollPane);
        }
    }

    private void showConfiguracoesContent() {
        mainContent.getChildren().clear();

        Label lblTitle = UIComponents.createLabel("Configurações",
                "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4A4A4A; -fx-padding: 0 0 20 0;");

        // Exibir opções de configurações
        Label lblConfiguracoes = UIComponents.createLabel("Aqui você pode modificar suas configurações.",
                "-fx-font-size: 14px; -fx-padding: 0 0 20 0; -fx-text-fill: #4A4A4A;");

        // Botão de deletar perfil na "Danger Zone"
        Button btnDeleteProfile = UIComponents.createButton("Deletar Perfil",
                "-fx-background-color: #FF6347; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; " +
                        "-fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;",
                e -> {
                    showPasswordPrompt();
                });

        // Adiciona espaçamento e alinhamento ao botão
        VBox buttonContainer = new VBox(btnDeleteProfile);
        buttonContainer.setStyle("-fx-alignment: center; -fx-padding: 20;");

        mainContent.getChildren().addAll(lblTitle, lblConfiguracoes, buttonContainer);
    }

    // Método para solicitar a senha do usuário
    private void showPasswordPrompt() {
        Stage passwordStage = new Stage();
        passwordStage.setTitle("Confirmação de Senha");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #DCE8E8;");

        Label lblPrompt = UIComponents.createLabel("Por favor, insira sua senha para confirmar:",
                "-fx-font-size: 16px;");
        TextField txtPassword = new TextField();
        txtPassword.setPromptText("Senha");
        txtPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                verifyPassword(txtPassword.getText(), passwordStage);
            }
        });

        Button btnConfirm = UIComponents.createButton("Confirmar", null, e -> {
            verifyPassword(txtPassword.getText(), passwordStage);
        });

        layout.getChildren().addAll(lblPrompt, txtPassword, btnConfirm);
        Scene scene = new Scene(layout, 400, 200);
        passwordStage.setScene(scene);
        passwordStage.show();
    }

    // Método para verificar a senha do usuário
    private void verifyPassword(String password, Stage passwordStage) {
        if (Database.checkCredentials(loggedInUser, password)) {
            deleteUserProfile(); // Método para deletar o perfil

            passwordStage.close();
            closeProfileScreen(); // Fecha a tela de perfil antes de navegar de volta
            // Navega de volta para a tela de login
            ScreenNavigator.navigateToLoginScreen(passwordStage);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Senha incorreta. Tente novamente.");
            alert.showAndWait();
        }
    }

    // Método para deletar o perfil do usuário
    private void deleteUserProfile() {
        boolean success = Database.deleteUserProfile(loggedInUser);
        if (success) {
            System.out.println("Perfil deletado com sucesso!");
        } else {
            System.out.println("Erro ao deletar o perfil.");
        }
    }

    // Método para fechar a tela de perfil
    private void closeProfileScreen() {
        // Aqui você pode ter uma lógica específica para fechar a tela de perfil.
        // Por exemplo, se o stage da tela de perfil for acessível:
        Stage currentStage = (Stage) layout.getScene().getWindow();
        currentStage.close();
    }

    public BorderPane getLayout() {
        return layout;
    }
}
