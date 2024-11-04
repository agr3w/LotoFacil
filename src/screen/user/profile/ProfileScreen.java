package screen.user.profile;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.Contest;
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
        menu.setStyle("-fx-padding: 10; -fx-background-color: #F0F0F0; -fx-min-width: 200;");

        // Adicionar itens ao menu
        Label lblPerfil = createClickableLabel("Perfil", e -> showPerfilContent());
        Label lblHistoricoCompras = createClickableLabel("Histórico de Compras", e -> showHistoricoContent());
        Label lblConfiguracoes = createClickableLabel("Configurações", e -> showConfiguracoesContent());
        Button btnVoltar = UIComponents.createButton("Voltar", "-fx-backgroundcolor: red",
                e -> ScreenNavigator.navigateToMainScreen(stage));

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

    private void showPerfilContent() {
        mainContent.getChildren().clear();
        Label lblTitle = new Label("Perfil do Usuário");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Campos para editar as informações do usuário
        txtNome = new TextField();
        txtEmail = new TextField();
        txtCelular = new TextField();

        // Botão para editar as informações
        btnEditar = new Button("Editar");
        btnEditar.setOnAction(e -> toggleEdit());

        loadUserData(); // Carrega os dados do usuário no início

        // Exibir informações do usuário
        HBox buttonBox = new HBox(10, btnEditar);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        mainContent.getChildren().addAll(lblTitle, new Label("Nome:"), txtNome,
                new Label("Email:"), txtEmail,
                new Label("Celular:"), txtCelular,
                buttonBox);
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
        txtNome.setStyle(editable ? "-fx-background-color: #FFFFFF;" : "-fx-background-color: #E0E0E0;");
        txtEmail.setStyle(editable ? "-fx-background-color: #FFFFFF;" : "-fx-background-color: #E0E0E0;");
        txtCelular.setStyle(editable ? "-fx-background-color: #FFFFFF;" : "-fx-background-color: #E0E0E0;");
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

    private void showHistoricoContent() {
        // Limpa o conteúdo atual do mainContent
        mainContent.getChildren().clear();

        // Título da seção
        Label lblTitle = UIComponents.createLabel("Histórico de Compras",
                "-fx-font-size: 24px; -fx-font-weight: bold;");

        // Carrega o histórico de compras filtrado pelo CPF do usuário logado
        List<PurchaseFileManager> historicoCompras = PurchaseFileManager.loadUserTickets();

        // Layout para exibir os cartões
        VBox historicoContainer = new VBox(10);
        historicoContainer.setStyle("-fx-padding: 20;");

        // Itera sobre cada compra no histórico e cria um cartão para exibir os detalhes
        for (PurchaseFileManager compra : historicoCompras) {
            VBox card = new VBox(5);
            card.setStyle(
                    "-fx-border-color: #800080; -fx-border-width: 2; -fx-padding: 10; -fx-background-color: #F5F5F5;");

            // Labels para mostrar as informações da compra
            Label lblCodigo = UIComponents.createLabel("Código da Compra: " + compra.getCodigoCompra(),
                    "-fx-font-size: 14px;");
            Label lblNomeAposta = UIComponents.createLabel("Nome da Aposta: " + compra.getNomeAposta(),
                    "-fx-font-size: 14px;");
            Label lblValor = UIComponents.createLabel("Valor Pago: R$ " + compra.getValueFromFile(),
                    "-fx-font-size: 14px;");
            Label lblValoresApostados = UIComponents
                    .createLabel("Valores Apostados: " + compra.getSelectedNumbersFromFile(), "-fx-font-size: 14px;");
            Label lblDataCompra = UIComponents.createLabel("Data da Compra: " + compra.getDataCompra(),
                    "-fx-font-size: 14px;");
            Label lblFormaPagamento = UIComponents.createLabel("Forma de Pagamento: " + compra.getFormaPagamento(),
                    "-fx-font-size: 14px;");

            // Adiciona as labels ao cartão
            card.getChildren().addAll(lblCodigo, lblNomeAposta, lblValor, lblValoresApostados, lblDataCompra,
                    lblFormaPagamento);

            // Adiciona o cartão ao container
            historicoContainer.getChildren().add(card);
        }

        // Se o histórico estiver vazio, mostra uma mensagem informativa
        if (historicoCompras.isEmpty()) {
            Label lblNoCompras = UIComponents.createLabel("Nenhum histórico de compras encontrado.",
                    "-fx-font-size: 16px; -fx-text-fill: #800080;");

            mainContent.getChildren().addAll(lblTitle, lblNoCompras);
        } else {
            // Se houver compras, adiciona o título e os cartões ao conteúdo principal
            mainContent.getChildren().addAll(lblTitle, historicoContainer);
        }
    }

    private void showConfiguracoesContent() {
        mainContent.getChildren().clear();
        Label lblTitle = UIComponents.createLabel("Configurações", "-fx-font-size: 24px; -fx-font-weight: bold;");

        // Exibir opções de configurações
        Label lblConfiguracoes = UIComponents.createLabel("Aqui você pode modificar suas configurações.",
                "-fx-font-size: 14px;");

        // Botão de deletar perfil na "Danger Zone"
        Button btnDeleteProfile = UIComponents.createButton("Deletar Perfil",
                "-fx-background-color: red; -fx-text-fill: white;", e -> {
                    showPasswordPrompt(); // Chama o método para pedir a senha
                });

        mainContent.getChildren().addAll(lblTitle, lblConfiguracoes, btnDeleteProfile);
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
