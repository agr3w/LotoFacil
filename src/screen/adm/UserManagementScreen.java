package screen.adm;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.UIComponents;
import utils.ValidateDate;
import database.UserManager;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserManagementScreen {
    private VBox layout;
    private TableView<Map<String, String>> table;

    @SuppressWarnings("unused")
    public UserManagementScreen(Stage stage) {
        layout = new VBox(15);
        stage.setTitle("LotoFacil - Status de Usuários");
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
        List<Map<String, String>> users = UserManager.getAllUsers();
        table.getItems().clear();

        for (Map<String, String> user : users) {
            table.getItems().add(user);
        }
    }

    private void adicionarUsuario() {
        // Criar campos utilizando UIComponents
        TextField nomeField = UIComponents.createTextField("Nome", "");
        TextField cpfField = UIComponents.createTextField("CPF", "");
        TextField emailField = UIComponents.createTextField("Email", "");
        PasswordField senhaField = UIComponents.createPasswordField("Senha", "");
        TextField telefoneField = UIComponents.createTextField("Telefone", "");

        // Criar o ComboBox para selecionar ADM (1 para Sim, 0 para Não)
        ComboBox<String> admComboBox = new ComboBox<>();
        admComboBox.getItems().addAll("1", "0"); // "1" para Admin, "0" para não admin
        admComboBox.setValue("0"); // Valor padrão para ADM (não admin)

        // Criar o DatePicker para selecionar Data de Nascimento
        DatePicker datePicker = ValidateDate.createDatePicker("Selecione sua data de nascimento",
        "-fx-min-width: 300", null);

        // Criar o layout do formulário
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                nomeField,
                cpfField,
                emailField,
                senhaField,
                telefoneField,
                admComboBox,
                datePicker);

        // Criar o diálogo para adicionar novo usuário
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Adicionar Novo Usuário");
        alert.setHeaderText("Preencha os dados do novo usuário");
        alert.getDialogPane().setContent(vbox);

        // Exibir o alerta e esperar a confirmação
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Verificar se todos os campos foram preenchidos
            String nome = nomeField.getText();
            String cpf = cpfField.getText();
            String email = emailField.getText();
            String senha = senhaField.getText();
            String telefone = telefoneField.getText();
            String dataNascimento = datePicker.getValue() != null
                    ? datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    : "";

            if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty() || telefone.isEmpty()
                    || dataNascimento.isEmpty()) {
                UIComponents.showAlert("Erro", "Todos os campos devem ser preenchidos.", Alert.AlertType.ERROR);
                return; // Impede de continuar se algum campo estiver vazio
            }

            // Obter o valor de ADM selecionado
            String adm = admComboBox.getValue();

            // Chamar o método para salvar o novo usuário
            UserManager.saveNewUser(cpf, nome, email, dataNascimento, senha, telefone, adm);

            // Recarregar os dados para atualizar a tabela
            loadUserData();
        }
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
                String newPhone = newValues[0]; // Novo telefone
                String newAdm = newValues[1]; // Novo valor do ADM

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
            String cpf = selectedUser.get("CPF");
            UserManager.deleteUserProfile(cpf); // Exclui o usuário pelo CPF
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
