package screen.sizes;

import javafx.stage.Stage;
import javafx.scene.Scene;
import screen.LoginScreen;
import screen.MainScreen;
import screen.RegisterScreen;
import screen.TermsScreen;
import screen.TicketPurchaseScreen;
import screen.sizes.ScreenSize;
import screen.sizes.ScreenSizeManager;
import utils.UserSession;

public class ScreenNavigator {

    public static void navigateToMainScreen(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getMainScreenSize();
        MainScreen mainScreen = new MainScreen(stage);
        stage.setScene(new Scene(mainScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    public static void navigateToTermsScreen(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getTermsScreenSize();
        TermsScreen termsScreen = new TermsScreen(stage);
        stage.setScene(new Scene(termsScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    public static void navigateToRegisterScreen(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getRegisterScreenSize();
        RegisterScreen registerScreen = new RegisterScreen(stage);
        stage.setScene(new Scene(registerScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    public static void navigateToLoginScreen(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getLoginScreenSize();
        LoginScreen loginScreen = new LoginScreen(stage);
        stage.setScene(new Scene(loginScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    public static void navigateToPurchaseScreen(Stage stage) { 
        ScreenSize screenSize = ScreenSizeManager.getPurchaseScreenSize();
        TicketPurchaseScreen ticketPurchaseScreen = new TicketPurchaseScreen(stage, UserSession.getLoggedInUserCpf());
        stage.setScene(new Scene(ticketPurchaseScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    } 

    public static void navigateToRegisterContestScreenSize(Stage stage) { //falta criar
        ScreenSize screenSize = ScreenSizeManager.getRegisterContestScreenSize();
        TicketPurchaseScreen ticketPurchaseScreen = new TicketPurchaseScreen(stage, UserSession.getLoggedInUserCpf());
        stage.setScene(new Scene(ticketPurchaseScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    // Adicione mais métodos para outras telas conforme necessário
}
