package screen.adm;

import java.time.LocalDate;
import database.ContestFileManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.Contest;
import utils.ContestManager;
import utils.NumberSelection;
import utils.UIComponents;

import java.util.ArrayList;
import java.util.List;

public class RegisterContestScreen {
    private VBox layout;
    private static final int MAX_NUM = 15;
    private List<Button> numberButtons;
    private NumberSelection numberSelection;

    @SuppressWarnings("unused")
    public RegisterContestScreen(Stage stage) {
        layout = new VBox(20); // Espaçamento entre os elementos
        stage.setTitle("LotoFacil - Registro de Novo Concurso");
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #DCE8E8;");

        numberButtons = new ArrayList<>();
        numberSelection = new NumberSelection(); // Inicializa a seleção de números

        // Título da tela
        Label titleLabel = UIComponents.createLabel("Registrar Novo Concurso",
                "-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Campos para registro
        Label lblNomeConcurso = UIComponents.createLabel("Nome do Concurso",
                "-fx-font-size: 16px; -fx-text-fill: #555555;");
        TextField txtNomeConcurso = UIComponents.createTextField("Digite o nome do concurso", null);
        txtNomeConcurso
                .setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-border-radius: 5; -fx-border-color: #007BFF;");

        // Botão de salvar
        Button btnSalvar = UIComponents.createButton("Salvar Concurso",
                "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;",
                e -> {
                    String nomeConcurso = txtNomeConcurso.getText();
                    LocalDate startDate = LocalDate.now();

                    // Verificação do preenchimento do campo nome
                    if (nomeConcurso.isEmpty()) {
                        UIComponents.showAlert("Erro", "Por favor, preencha o campo de nome do concurso.",
                                AlertType.WARNING);
                        return; // Interrompe o processo caso o nome esteja vazio
                    }

                    // Verificação se já existe um concurso com a mesma data
                    if (ContestFileManager.isStartDateTaken(startDate)) {
                        UIComponents.showAlert("Erro",
                                "Já existe um concurso com esta data de início.", AlertType.WARNING);
                        return; // Interrompe o processo caso a data já exista
                    }

                    // Gera o próximo código de concurso e cria um novo concurso
                    String nextCode = ContestFileManager.getNextContestCode();
                    Contest newContest = new Contest(nomeConcurso, startDate, startDate.plusDays(30), nextCode, true);

                    // Define números vencedores (aqui podemos colocar os números selecionados ou
                    // gerados aleatoriamente)
                    newContest.setWinningNumbers(ContestManager.generateRandomNumbers());

                    // Salva o concurso
                    ContestFileManager.saveContest(newContest);
                    UIComponents.showAlert("Sucesso", "Concurso registrado com sucesso!", null);

                    // Redireciona para a tela principal
                    ScreenNavigator.navigateToMainScreen(stage);
                });

        // Botão de voltar
        Button btnVoltar = UIComponents.createButton("Voltar",
                "-fx-background-color: #FF5733; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;",
                e -> {
                    ScreenNavigator.navigateToMainScreen(stage); // Exemplo simples: fecha a tela atual
                });

        // Adicionar botão para selecionar números manualmente
        Button btnSelectNumbers = new Button("Selecionar Números Manualmente");
        btnSelectNumbers.setStyle(
                "-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;");
        btnSelectNumbers.setOnAction(e -> {
            openManualNumberSelectionPopup();
        });

        // Organizar elementos em um GridPane para alinhamento
        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setStyle(
                "-fx-padding: 20; -fx-background-color: #FFFFFF; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba( 0,0,0,0.2), 10, 0, 0, 1);");
        grid.add(lblNomeConcurso, 0, 0);
        grid.add(txtNomeConcurso, 0, 1);
        grid.add(btnSelectNumbers, 0, 2);
        grid.add(btnSalvar, 0, 3);
        grid.add(btnVoltar, 0, 4);

        // Adicionar elementos ao layout
        layout.getChildren().addAll(titleLabel, grid);
    }

    // Método para abrir o pop-up de seleção manual de números
    @SuppressWarnings("unused")
    private void openManualNumberSelectionPopup() {
        Stage popupStage = new Stage();
        popupStage.setTitle("Seleção Manual de Números");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 1; i <= 25; i++) {
            final int number = i; // Variável final para usar na lambda
            Button btnNumber = new Button(String.format("%02d", i)); // Formata o número com dois dígitos
            btnNumber.setMinSize(50, 50);
            btnNumber.setStyle(
                    "-fx-background-color: #FFFFFF; -fx-border-color: #007BFF; -fx-border-radius: 5; -fx-font-size: 14px;");
            btnNumber.setOnAction(e -> handleNumberSelection(btnNumber, number));
            numberButtons.add(btnNumber);
            grid.add(btnNumber, (i - 1) % 5, (i - 1) / 5); // Organiza os botões em uma grid 5x5
        }

        Button btnConfirmSelection = new Button("Confirmar Seleção");
        btnConfirmSelection.setStyle(
                "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;");
        btnConfirmSelection.setOnAction(e -> {
            // Confirma a seleção e fecha o pop-up
            confirmNumberSelection();
            popupStage.close();
        });

        grid.add(btnConfirmSelection, 0, 5, 5, 1); // Coloca o botão de confirmação

        Scene scene = new Scene(grid, 300, 350);
        popupStage.setScene(scene);
        popupStage.show();
    }

    // Função para lidar com a seleção de números
    private void handleNumberSelection(Button btn, int number) {
        if (numberSelection.getSelectedNumbers().contains(number)) {
            numberSelection.removeNumber(number);
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #007BFF; -fx-border-radius: 5;");
        } else {
            if (numberSelection.getSelectedCount() >= MAX_NUM) {
                UIComponents.showAlert("Limite Atingido", "O número máximo de seleções é 15.", AlertType.WARNING);
                return;
            }
            numberSelection.addNumber(number);
            btn.setStyle("-fx-background-color: #00FF00; -fx-border-color: #007BFF; -fx-border-radius: 5;");
        }
        updateSelectedInfo();
    }

    // Função para confirmar a seleção dos números
    private void confirmNumberSelection() {
        List<Integer> selectedNumbers = numberSelection.getSelectedNumbers();
        System.out.println("Números selecionados: " + selectedNumbers); // Aqui você pode salvar ou usar os números como
                                                                        // desejar

        // Por exemplo, definir os números vencedores para o concurso:
        Contest newContest = new Contest("Concurso Manual", LocalDate.now(), LocalDate.now().plusDays(30), "001", true);
        newContest.setWinningNumbers(selectedNumbers);
        ContestFileManager.saveContest(newContest);

        UIComponents.showAlert("Seleção Confirmada", "Você selecionou os números: " + selectedNumbers, null);
    }

    // Atualiza as informações dos números selecionados
    private void updateSelectedInfo() {
        String selectedNumbers = numberSelection.getSelectedNumbers().toString();
        System.out.println("Números selecionados: " + selectedNumbers);
    }

    public VBox getLayout() {
        return layout;
    }
}
