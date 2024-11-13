package screen.adm;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;
import database.Database;
import database.UserManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserManagementScreen {
    private VBox layout;
    private TableView<Map<String, String>> table;

    @SuppressWarnings("unused")
    public UserManagementScreen(Stage stage) {
        layout = new VBox(15);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #DCE8E8;");

        Label titleLabel = new Label("Gerenciamento de Usuários");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Inicializar tabela
        table = new TableView<>();
        table.setPrefWidth(800); // Aumentar a largura para acomodar novas colunas
        setupTableColumns();

        // Carregar dados dos usuários
        loadUserData();

        // Organizando botões
        HBox buttonBox = new HBox(10);
        Button btnAdicionar = UIComponents.createButton("Adicionar Usuário",
                "-fx-background-color: #28A745; -fx-text-fill: white;", e -> adicionarUsuario());
        Button btnEditar = UIComponents.createButton("Editar Usuário",
                "-fx-background-color: #1E90FF; -fx-text-fill: white;", e -> editarUser());
        Button btnExcluir = UIComponents.createButton("Excluir Usuário",
                "-fx-background-color: #FF0000; -fx-text-fill: white;", e -> excluirUsuario());
        buttonBox.getChildren().addAll(btnAdicionar, btnEditar, btnExcluir);

        // Botão de voltar
        Button btnVoltar = UIComponents.createButton("Voltar", "-fx-background-color: #FF0000; -fx-text-fill: white;",
                e -> ScreenNavigator.navigateToMainScreen(stage));

        layout.getChildren().addAll(titleLabel, table, buttonBox, btnVoltar);
    }

    @SuppressWarnings("unchecked")
    private void setupTableColumns() {
        TableColumn<Map<String, String>, String> emailColumn = new TableColumn<>("E-mail");
        emailColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get("email")));
        emailColumn.setPrefWidth(200);

        TableColumn<Map<String, String>, String> statusColumn = new TableColumn<>("Administrador");
        statusColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get("ADM")));
        statusColumn.setPrefWidth(100);

        // TableColumn<Map<String, String>, String> nameColumn = new
        // TableColumn<>("Nome");
        // nameColumn.setCellValueFactory(param -> new
        // ReadOnlyStringWrapper(param.getValue().get("nome")));
        // nameColumn.setPrefWidth(200);

        // TableColumn<Map<String, String>, String> cpfColumn = new
        // TableColumn<>("CPF");
        // cpfColumn.setCellValueFactory(param -> new
        // ReadOnlyStringWrapper(param.getValue().get("CPF")));
        // cpfColumn.setPrefWidth(150);

        TableColumn<Map<String, String>, String> phoneColumn = new TableColumn<>("Telefone");
        phoneColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get("Telefone")));
        phoneColumn.setPrefWidth(200);

        TableColumn<Map<String, String>, String> passWordColumn = new TableColumn<>("Senha");
        passWordColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get("senha")));
        passWordColumn.setPrefWidth(150);

        table.getColumns().addAll(phoneColumn, passWordColumn, emailColumn, statusColumn);
    }

    private void loadUserData() {
        List<Map<String, String>> users = UserManager.getAllUsers(); // Você precisaria de um método para pegar os
                                                                     // usuários
        table.getItems().clear(); // Limpar itens antes de carregar novos dados

        for (Map<String, String> user : users) {
            table.getItems().add(user);
        }
    }

    private void adicionarUsuario() {
        // Criar um diálogo ou formulário para adicionar um novo usuário
        // Exemplo: Diálogo de entrada para nome, email, CPF, etc.
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Adicionar Novo Usuário");
        dialog.setHeaderText("Preencha os dados do novo usuário");
        dialog.setContentText("Nome:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nome -> {
            // Adicione lógica para salvar novo usuário
            UserManager.saveUserData(nome, "", "", "", "", "", ""); // Substitua com os dados reais
            loadUserData(); // Recarregar dados para atualizar a tabela
        });
    }

    private void editarUser() {
        Map<String, String> selectedUser = table.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String telefone = selectedUser.get("Telefone");
            String adm = selectedUser.get("ADM");
    
            // Criar um diálogo para editar telefone e ADM
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Editar Usuário");
            dialog.setHeaderText("Edite as informações do usuário");
    
            // Criar os campos para telefone e ADM
            TextField telefoneField = new TextField(telefone);
            ComboBox<String> admComboBox = new ComboBox<>();
            admComboBox.getItems().addAll("1", "0"); // 1 - Verdadeiro (Admin), 0 - Falso (não Admin)
            admComboBox.setValue(adm); // Define o valor atual do ADM
    
            // Criar o layout do diálogo
            VBox vbox = new VBox(10);
            vbox.getChildren().addAll(new Label("Novo telefone:"), telefoneField,
                    new Label("Validação de Administrador:"), admComboBox);
    
            dialog.getDialogPane().setContent(vbox);
    
            // Botões do diálogo
            ButtonType saveButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
    
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    return telefoneField.getText() + ";" + admComboBox.getValue();
                }
                return null;
            });
    
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(data -> {
                String[] newValues = data.split(";");
                String newPhone = newValues[0];  // Novo telefone
                String newAdm = newValues[1];    // Novo valor do ADM
    
                if (newPhone != null && !newPhone.trim().isEmpty()) {
                    // Obtenha o CPF do usuário selecionado
                    String cpf = selectedUser.get("CPF");
    
                    // Atualize os dados do usuário (telefone e ADM)
                    UserManager.updateUserPhoneAndAdm(cpf, newPhone, newAdm); // Chama o novo método
    
                    // Recarregar os dados para atualizar a tabela
                    loadUserData();
                } else {
                    UIComponents.showAlert("Erro", "Telefone não pode ser vazio.", AlertType.INFORMATION);
                }
            });
        } else {
            UIComponents.showAlert("Erro", "Selecione um usuário para editar.", AlertType.INFORMATION);
        }
    }
    
    
    

    private void excluirUsuario() {
        Map<String, String> selectedUser = table.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String cpf = selectedUser.get("cpf");
            Database.deleteUserProfile(cpf); // Exclui o usuário pelo CPF
            UIComponents.showAlert("Usuário Excluído", "O usuário com CPF " + cpf + " foi excluído.",
                    AlertType.INFORMATION);
            loadUserData(); // Recarregar dados para atualizar a tabela
        } else {
            UIComponents.showAlert("Erro", "Selecione um usuário para excluir.", AlertType.INFORMATION);
        }
    }

    public VBox getLayout() {
        return layout;
    }
}
