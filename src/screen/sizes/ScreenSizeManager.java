package screen.sizes;

public class ScreenSizeManager {

    public static ScreenSize getComumScreenSize() {
        return new ScreenSize(800, 600);
    }

    public static ScreenSize getLoginScreenSize() {
        return new ScreenSize(800, 600); // login
    }

    public static ScreenSize getRegisterScreenSize() {
        return new ScreenSize(800, 600); // registro
    }

    public static ScreenSize getTermsScreenSize() {
        return new ScreenSize(800, 600); // termos
    }

    public static ScreenSize getMainScreenSize() {
        return new ScreenSize(800, 600); // main
    }

    public static ScreenSize getPurchaseScreenSize() {
        return new ScreenSize(800, 600); // compra
    }

    public static ScreenSize getRegisterContestScreenSize() {
        return new ScreenSize(800, 600); // A fazer
    }

    public static ScreenSize getPurchaseHistoryScreenSize() {
        return new ScreenSize(800, 600); // Historico
    }

    public static ScreenSize getTicketSummaryScreenSize() {
        return new ScreenSize(800, 600); // Ajuste o tamanho conforme necessário
    }

    public static ScreenSize getTicketPaymentScreenSize() {
        return new ScreenSize(800, 600); // Ajuste o tamanho conforme necessário
    }

    // Outros métodos podem ser adicionados para diferentes telas no futuro
}
