package screen.user.profile;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import utils.UserSession;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ProfileScreen {
    private BorderPane layout;
    private VBox menu;
    private VBox mainContent;
    private boolean isEditable = false; // Controle de edição
    private TextField txtNome;
    private TextField txtEmail;
    private TextField txtCelular;
    private Button btnEditar;

    public ProfileScreen(Stage stage) {
        layout = new BorderPane();

        // Criar menu à esquerda
        menu = new VBox(10);
        menu.setStyle("-fx-padding: 10; -fx-background-color: #F0F0F0; -fx-min-width: 200;");

        // Adicionar itens ao menu
        Label lblPerfil = createClickableLabel("Perfil", e -> showPerfilContent());
        Label lblHistoricoCompras = createClickableLabel("Histórico de Compras", e -> showHistoricoContent());
        Label lblConfiguracoes = createClickableLabel("Configurações", e -> showConfiguracoesContent());

        menu.getChildren().addAll(lblPerfil, lblHistoricoCompras, lblConfiguracoes);

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
                String userCpf = line.split(";")[0].split(": ")[1]; // Presumindo que o CPF esteja na primeira parte da linha
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
        mainContent.getChildren().clear();
        Label lblTitle = new Label("Histórico de Compras");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Exibir o histórico de compras (substituir pelo conteúdo real)
        Label lblHistorico = new Label("Aqui você pode ver seu histórico de compras.");
        mainContent.getChildren().addAll(lblTitle, lblHistorico);
    }

    private void showConfiguracoesContent() {
        mainContent.getChildren().clear();
        Label lblTitle = new Label("Configurações");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Exibir opções de configurações (substituir pelo conteúdo real)
        Label lblConfiguracoes = new Label("Aqui você pode modificar suas configurações.");
        mainContent.getChildren().addAll(lblTitle, lblConfiguracoes);
    }

    public BorderPane getLayout() {
        return layout;
    }
}
