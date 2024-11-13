package screen.adm;

import java.time.LocalDate;
import database.ContestFileManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import screen.sizes.ScreenNavigator;
import utils.Contest;
import utils.ContestManager;
import utils.UIComponents;

public class RegisterContestScreen {
    private VBox layout;

    @SuppressWarnings("unused")
    public RegisterContestScreen(Stage stage) {
        layout = new VBox(20); // Espaçamento entre os elementos
        layout.setStyle(
                "-fx-padding: 20; -fx-alignment: center; -fx-align-itens: center; -fx-background-color: #DCE8E8;");

        // Título da tela
        Label titleLabel = UIComponents.createLabel("Registrar Novo Concurso",
                "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Campos para registro
        TextField txtNomeConcurso = new TextField();
        txtNomeConcurso.setPromptText("Digite o nome do concurso");

        // Botão de salvar
        Button btnSalvar = UIComponents.createButton("Salvar Concurso",
                "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 5;",
                e -> {
                    String nomeConcurso = txtNomeConcurso.getText();
                    LocalDate startDate = LocalDate.now();

                    // Verificação do preenchimento do campo nome
                    if (nomeConcurso.isEmpty()) {
                        UIComponents.showAlert("Erro", "Por favor, preencha o campo de nome do concurso.", AlertType.WARNING);
                        return; // Interrompe o processo caso o nome esteja vazio
                    }

                    // Verificação de formato e conversão da data
                    // Verificação se já existe um concurso com a mesma data
                    if (ContestFileManager.isStartDateTaken(startDate)) {
                        UIComponents.showAlert("Erro",
                                "Já existe um concurso com esta data de início.", AlertType.WARNING);
                        return; // Interrompe o processo caso a data já exista
                    }

                    // Gera o próximo código de concurso e cria um novo concurso
                    String nextCode = ContestFileManager.getNextContestCode();
                    Contest newContest = new Contest(nomeConcurso, startDate, startDate.plusDays(30), nextCode, true);

                    // Define números vencedores
                    newContest.setWinningNumbers(ContestManager.generateRandomNumbers());

                    // Salva o concurso
                    ContestFileManager.saveContest(newContest);
                    UIComponents.showAlert("Sucesso", "Concurso registrado com sucesso!", null);

                    // Redireciona para a tela principal
                    ScreenNavigator.navigateToMainScreen(stage);
                });

        // Botão de voltar
        Button btnVoltar = UIComponents.createButton("Voltar",
                "-fx-background-color: #FF5733; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 5;",
                e -> {
                    // Aqui você pode adicionar a lógica para retornar à tela anterior
                    ScreenNavigator.navigateToMainScreen(stage); // Exemplo simples: fecha a tela atual
                });

        // Organizar elementos em um GridPane para alinhamento
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(txtNomeConcurso, 0, 0);
        grid.add(btnSalvar, 0, 2);
        grid.add(btnVoltar, 0, 3);

        // Adicionar elementos ao layout
        layout.getChildren().addAll(titleLabel, grid);
    }

    public VBox getLayout() {
        return layout;
    }
}
