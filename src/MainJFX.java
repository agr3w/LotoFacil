import database.Database;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import screen.user.LoginScreen;

public class MainJFX extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Criar o arquivo de texto se não existir
        Database.createFilesIfNotExists();

        // Carregar a tela de login
        LoginScreen loginScreen =  new LoginScreen(primaryStage);
        Scene scene = new Scene(loginScreen.getLayout(), 800, 600);
        primaryStage.setTitle("LotoFacil - Login"); // necessita ser dinamico em todas as paginas
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}