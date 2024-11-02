package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.nio.file.*;
import java.util.*;

public class ContestManager {
    private static final String CONTEST_FILE_NAME = "contests.txt"; // Caminho do arquivo onde os concursos estão armazenados
    private static final String DIRECTORY_PATH = "c:\\tmp\\";

    // Método para finalizar um concurso
    public static void finalizeContest(String contestName) {
        List<Map<String, String>> contests = readContestsFromFile();
        
        for (Map<String, String> contest : contests) {
            if (contest.get("name").equals(contestName)) {
                contest.put("status", "Finalizado"); // Atualiza o status para "Finalizado"
                break;
            }
        }
        
        writeContestsToFile(contests); // Salva as alterações no arquivo
    }

    // Método para editar o nome de um concurso
    public static void editContestName(String oldName, String newName) {
        List<Map<String, String>> contests = readContestsFromFile();
        
        for (Map<String, String> contest : contests) {
            if (contest.get("name").equals(oldName)) {
                contest.put("name", newName); // Atualiza o nome do concurso
                break;
            }
        }
        
        writeContestsToFile(contests); // Salva as alterações no arquivo
    }

    // Método para excluir um concurso
    public static void deleteContest(String contestName) {
        List<Map<String, String>> contests = readContestsFromFile();
        contests.removeIf(contest -> contest.get("name").equals(contestName)); // Remove o concurso da lista
        
        writeContestsToFile(contests); // Salva as alterações no arquivo
    }

    // Método para ler os concursos do arquivo
    private static List<Map<String, String>> readContestsFromFile() {
        List<Map<String, String>> contests = new ArrayList<>();
        Path path = Paths.get(DIRECTORY_PATH + CONTEST_FILE_NAME);

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 5) {
                    Map<String, String> contest = new HashMap<>();
                    contest.put("name", parts[0].split(":")[1].trim());
                    contest.put("startDate", parts[1].split(":")[1].trim());
                    contest.put("endDate", parts[2].split(":")[1].trim());
                    contest.put("contestCode", parts[3].split(":")[1].trim());
                    contest.put("status", parts[4].split(":")[1].trim());
                    contests.add(contest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contests;
    }

    // Método para escrever os concursos no arquivo
    private static void writeContestsToFile(List<Map<String, String>> contests) {
        Path path = Paths.get(DIRECTORY_PATH + CONTEST_FILE_NAME);
        
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Map<String, String> contest : contests) {
                // Escreva cada concurso no formato desejado (aqui em formato CSV)
                bw.write("Nome: " + contest.get("name") + ";");
                bw.write("DataInicio: " + contest.get("startDate") + ";");
                bw.write("DataFinal: " + contest.get("endDate") + ";");
                bw.write("Codigo: " + contest.get("contestCode") + ";");
                bw.write("Status: " + contest.get("status") + ";");
                bw.newLine(); // Adiciona nova linha para o próximo concurso
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}