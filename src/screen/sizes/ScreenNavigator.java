package screen.sizes;

import javafx.stage.Stage;

import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import screen.adm.ContestStatusScreen;
import screen.adm.RegisterContestScreen;
import screen.adm.UserManagementScreen;
import screen.user.LoginScreen;
import screen.user.MainScreen;
import screen.user.PaymentScreen;
import screen.user.RegisterScreen;
import screen.user.ResultsScreen;
import screen.user.RulesScreen;
import screen.user.SelectContestScreen;
import screen.user.TicketPurchaseScreen;
import screen.user.TicketSummaryScreen;
import screen.user.profile.ProfileScreen;
import utils.UserSession;

public class ScreenNavigator {

    // User
    public static void navigateToMainScreen(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getComumScreenSize();
        MainScreen mainScreen = new MainScreen(stage);
        stage.setScene(new Scene(mainScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    public static void navigateToRegisterScreen(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getComumScreenSize();
        RegisterScreen registerScreen = new RegisterScreen(stage);

        ScrollPane scrollPane = new ScrollPane(registerScreen.getLayout());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Define a nova cena com o ScrollPane
        stage.setScene(new Scene(scrollPane, screenSize.getWidth(), screenSize.getHeight()));
    }

    public static void navigateToLoginScreen(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getComumScreenSize();
        LoginScreen loginScreen = new LoginScreen(stage);
        stage.setScene(new Scene(loginScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
        stage.show();
    }

    public static void navigateToPurchaseScreen(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getComumScreenSize();
        TicketPurchaseScreen ticketPurchaseScreen = new TicketPurchaseScreen(stage, UserSession.getLoggedInUserCpf());
        stage.setScene(new Scene(ticketPurchaseScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    public static <ArrayList> void navigateToTicketSummaryScreen(Stage stage, List<Integer> selectedNumbers) {
        ScreenSize screenSize = ScreenSizeManager.getComumScreenSize();
        TicketSummaryScreen TicketSummaryScreen = new TicketSummaryScreen(stage, selectedNumbers,
                UserSession.getLoggedInUserCpf());
        stage.setScene(new Scene(TicketSummaryScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    public static void navigateToResultsScreen(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getComumScreenSize();
        ResultsScreen historyScreen = new ResultsScreen(stage);
        stage.setScene(new Scene(historyScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    public static void navigateToPaymentScreen(Stage stage, List<Integer> selectedNumbers) {
        ScreenSize screenSize = ScreenSizeManager.getComumScreenSize();
        PaymentScreen paymentScreen = new PaymentScreen(stage, UserSession.getLoggedInUserCpf(), selectedNumbers);
        stage.setScene(new Scene(paymentScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    public static void navigateToRulesScreen(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getComumScreenSize();
        RulesScreen rulesScreen = new RulesScreen(stage);
        stage.setScene(new Scene(rulesScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    public static void navigateToSelectContestScreen(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getComumScreenSize();
        SelectContestScreen selectContestScreen = new SelectContestScreen(stage);
        stage.setScene(new Scene(selectContestScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    public static void navigateToProfileScreen(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getComumScreenSize();
        ProfileScreen profileScreen = new ProfileScreen(stage);
        stage.setScene(new Scene(profileScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    // ADM
    public static void navigateToRegisterContestScreenSize(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getComumScreenSize();
        RegisterContestScreen registerContestScreen = new RegisterContestScreen(stage);
        stage.setScene(new Scene(registerContestScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    public static void navigateToContestStatusScreen(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getComumScreenSize();
        ContestStatusScreen contestStatusScreen = new ContestStatusScreen(stage);
        stage.setScene(new Scene(contestStatusScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    public static void navigateToUserManagementScreen(Stage stage) {
        ScreenSize screenSize = ScreenSizeManager.getComumScreenSize();
        UserManagementScreen userManagementScreen = new UserManagementScreen(stage);
        stage.setScene(new Scene(userManagementScreen.getLayout(), screenSize.getWidth(), screenSize.getHeight()));
    }

    // Adicione mais métodos para outras telas conforme necessário
}
