package utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ContestManager {
    private static final String CONTEST_FILE_NAME = "contests.txt"; // Caminho do arquivo onde os concursos estão
                                                                    // armazenados
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
    public static void deleteContest(String contestCode) {
        List<Map<String, String>> contests = readContestsFromFile();
        contests.removeIf(contest -> contest.get("contestCode").equals(contestCode)); // Remove o concurso da lista

        writeContestsToFile(contests); // Salva as alterações no arquivo
    }

    // Atualizar o método de leitura para incluir winningNumbers
    private static List<Map<String, String>> readContestsFromFile() {
        List<Map<String, String>> contests = new ArrayList<>();
        Path path = Paths.get(DIRECTORY_PATH + CONTEST_FILE_NAME);

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 6) { // Alterado para 6
                    Map<String, String> contest = new HashMap<>();
                    contest.put("name", parts[0].split(":")[1].trim());
                    contest.put("startDate", parts[1].split(":")[1].trim());
                    contest.put("endDate", parts[2].split(":")[1].trim());
                    contest.put("contestCode", parts[3].split(":")[1].trim());
                    contest.put("status", parts[4].split(":")[1].trim());
                    contest.put("winningNumbers", parts[5].split(":")[1].trim()); // Nova linha para winningNumbers
                    contests.add(contest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contests;
    }

    // Atualizar o método de escrita para incluir winningNumbers
    private static void writeContestsToFile(List<Map<String, String>> contests) {
        Path path = Paths.get(DIRECTORY_PATH + CONTEST_FILE_NAME);

        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Map<String, String> contest : contests) {
                bw.write("Nome: " + contest.get("name") + ";");
                bw.write("DataInicio: " + contest.get("startDate") + ";");
                bw.write("DataFinal: " + contest.get("endDate") + ";");
                bw.write("Codigo: " + contest.get("contestCode") + ";");
                bw.write("Status: " + contest.get("status") + ";");
                bw.write("WinningNumbers: " + contest.get("winningNumbers") + ";"); // Nova linha para winningNumbers
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> generateRandomNumbers() {
        List<Integer> numbers = new ArrayList<>();
        Random random = new Random();

        // Gerando 20 números únicos entre 1 e 60
        while (numbers.size() < 20) {
            int number = random.nextInt(60) + 1; // Gera números de 1 a 60
            if (!numbers.contains(number)) {
                numbers.add(number);
            }
        }

        Collections.sort(numbers); // Opcional: ordena os números
        return numbers;
    }

    // Método para criar um novo concurso
    public static void createContest(String name, String startDate, String endDate, String contestCode) {
        List<Map<String, String>> contests = readContestsFromFile();
        Map<String, String> newContest = new HashMap<>();

        newContest.put("name", name);
        newContest.put("startDate", startDate);
        newContest.put("endDate", endDate);
        newContest.put("contestCode", contestCode);
        newContest.put("status", "Ativo");

        // Gera os 20 números aleatórios
        List<Integer> winningNumbers = generateRandomNumbers();
        newContest.put("winningNumbers", winningNumbers.toString()); // Armazena os números como string

        contests.add(newContest);
        writeContestsToFile(contests); // Salva as alterações no arquivo
    }

    

}