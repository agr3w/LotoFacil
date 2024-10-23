package screen.sizes;

public class ScreenSizeManager {
   
    public static ScreenSize getLoginScreenSize() {
        return new ScreenSize(400, 300); //login
    }

    public static ScreenSize getRegisterScreenSize() {
        return new ScreenSize(500, 550); // registro
    }

    public static ScreenSize getTermsScreenSize() {
        return new ScreenSize(500, 550); //termos
    }

    public static ScreenSize getMainScreenSize() {
        return new ScreenSize(800, 600); //main
    }

    public static ScreenSize getPurchaseScreenSize() {
        return new ScreenSize(600, 400); //compra
    }

    public static ScreenSize getRegisterContestScreenSize() {
        return new ScreenSize(600, 400); //compra
    }

    // Outros métodos podem ser adicionados para diferentes telas no futuro
}

