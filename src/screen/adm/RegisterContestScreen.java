package screen.adm;

import java.time.LocalDate;

import database.ContestFileManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Contest;
import utils.UIComponents;

public class RegisterContestScreen {
    private VBox layout;

    @SuppressWarnings("unused")
    public RegisterContestScreen(Stage stage) {
        layout = new VBox(15); // Espaçamento entre os elementos
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #DCE8E8;");

        // Título da tela
        Label titleLabel = UIComponents.createLabel("Registrar  Novo Concurso",
                "-fx-font-size: 24px; -fx-font-weight: bold;");

        Label nomeConcursoLabel = UIComponents.createLabel("Registrar  Novo Concurso", null);

        // Campo para nome do concurso
        TextField txtNomeConcurso = new TextField();
        txtNomeConcurso.setPromptText("Digite o nome do concurso");

        Label dataConcursoLabel = UIComponents.createLabel("Registrar  Novo Concurso", null);

        // Campo para data do concurso
        TextField txtDataConcurso = new TextField();
        txtDataConcurso.setPromptText("Digite a data do concurso (dd/MM/yyyy)");

        // Botão de salvar
        Button btnSalvar = UIComponents.createButton("Salvar Concurso",
                "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-size: 16px;",
                e -> {
                    String nomeConcurso = txtNomeConcurso.getText();
                    String dataConcurso = txtDataConcurso.getText();

                    // Exemplo de uso ao criar um concurso
                    Contest newContest = new Contest(nomeConcurso, LocalDate.now(), LocalDate.now().plusDays(30),
                            "1", true);
                    ContestFileManager.saveContest(newContest);

                    if (nomeConcurso.isEmpty() || dataConcurso.isEmpty()) {
                        UIComponents.showAlert("Erro", "Por favor, preencha todos os campos.", null);
                    } else {
                        // Implementar lógica de salvamento do concurso aqui
                        UIComponents.showAlert("Sucesso", "Concurso registrado com sucesso!", null);
                        // Pode adicionar um redirecionamento ou limpar campos após o sucesso
                    }
                });

        // Adicionar elementos ao layout
        layout.getChildren().addAll(titleLabel, nomeConcursoLabel, txtNomeConcurso, dataConcursoLabel, txtDataConcurso,
                btnSalvar);
    }

    public VBox getLayout() {
        return layout;
    }
}
