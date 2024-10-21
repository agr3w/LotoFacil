package utils;

public class UserSession {
    private static String loggedInUserCpf;

    public static void setLoggedInUserCpf(String cpf) {
        loggedInUserCpf = cpf;
    }

    public static String getLoggedInUserCpf() {
        return loggedInUserCpf;
    }
}
