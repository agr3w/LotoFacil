import database.Database;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import screen.LoginScreen;

public class MainJFX extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Criar o arquivo de texto se não existir
        Database.createFileIfNotExists();

        // Carregar a tela de login
        LoginScreen loginScreen = new LoginScreen(primaryStage);
        Scene scene = new Scene(loginScreen.getLayout(), 400, 300);
        primaryStage.setTitle("LotoFacil - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}