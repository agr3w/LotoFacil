package utils;

import database.Database;

public class UserSession {
    private static String loggedInUserCpf;

    public static boolean isAdminLoggedIn() {
        if (Database.isAdm()) {
            return true;
        }
        return false;
    }

    public static void setLoggedInUserCpf(String cpf) {
        loggedInUserCpf = cpf;
    }

    public static String getLoggedInUserCpf() {
        return loggedInUserCpf;
    }
}
