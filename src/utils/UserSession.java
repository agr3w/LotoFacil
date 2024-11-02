package utils;

import java.util.Map;

import database.Database;

public class UserSession {
    private static String loggedInUserCpf;
    private static Map<String, String> selectedContest;
    private static String selectedContestCode;

    public static boolean isAdminLoggedIn() {
        if (Database.isAdm(loggedInUserCpf)) {
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

    // Método para armazenar o concurso selecionado
    public static void setSelectedContest(Map<String, String> contest) {
        selectedContest = contest;
    }

    // Método para obter o concurso selecionado
    public static Map<String, String> getSelectedContest() {
        return selectedContest;
    }

    // Método para armazenar o código do concurso selecionado
    public static void setSelectedContestCode(String contestCode) {
        selectedContestCode = contestCode;
    }

    // Método para obter o código do concurso selecionado
    public static String getSelectedContestCode() {
        return selectedContestCode;
    }
}
