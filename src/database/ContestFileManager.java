package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.Path;
import utils.Contest;

public class ContestFileManager {
    private static final String CONTEST_FILE_NAME = "contests.txt";
    private static final String DIRECTORY_PATH = "c:\\tmp\\";

    // No arquivo ContestFileManager.java
    public static boolean isStartDateTaken(LocalDate startDate) {
        Path path = Paths.get(DIRECTORY_PATH + CONTEST_FILE_NAME);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[1].contains("DataInicio: " + startDate)) { // Verifica a data de início
                    return true; // Retorna true se a data já existe
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // Retorna false se a data não foi encontrada
    }

    public static void saveContest(Contest contest) {
        // Verifica se a data de início já existe
        if (isStartDateTaken(contest.getStartDate())) {
            System.out.println("Erro: Já existe um concurso com esta data de início.");
            return; // Sai do método sem salvar o concurso
        }

        Path path = Paths.get(DIRECTORY_PATH + CONTEST_FILE_NAME);
        try {
            Files.createDirectories(path.getParent());
            BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            List<Integer> winningNumbers = contest.getWinningNumbers();

            writer.write("Nome: " + contest.getName() + ";");
            writer.write("DataInicio: " + contest.getStartDate() + ";");
            writer.write("DataFinal: " + contest.getEndDate() + ";");
            writer.write("Codigo: " + contest.getContestCode() + ";");
            writer.write("Status: " + (contest.isOpen() ? "Aberto" : "Fechado") + ";");
            writer.write("WinningNumbers: " + winningNumbers.toString() + ";");
            writer.write("TotalBets: " + contest.getNumberOfBets() + ";");
            writer.write("totalRevenue: " + "0" + ";");
            writer.write("totalPrizes: " + "0" + ";");
            writer.write("corporationShare: " + "0" + ";");
            writer.newLine(); // Adiciona nova linha para o próximo concurso
            writer.close();

            System.out.println("Concurso salvo: " + contest.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isContestOpen() {
        // Lógica para ler o arquivo de concursos e verificar se algum está aberto
        // Por exemplo:
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(DIRECTORY_PATH + CONTEST_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Supondo que a linha contém informações separadas por ";"
                String[] parts = line.split(";");
                boolean status = parts[4].contains("Aberto"); // Verifica o status
                if (status) {
                    return true; // Retorna true se houver um concurso aberto
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Retorna false se não houver concursos abertos
    }

    // Método para obter uma lista de concursos abertos
    public static List<Map<String, String>> getOpenContests() {
        List<Map<String, String>> openContests = new ArrayList<>();
        Path path = Paths.get(DIRECTORY_PATH + CONTEST_FILE_NAME);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 5 && parts[4].contains("Aberto")) {
                    Map<String, String> contest = new HashMap<>();
                    contest.put("name", parts[0].split(":")[1].trim());
                    contest.put("startDate", parts[1].split(":")[1].trim());
                    contest.put("endDate", parts[2].split(":")[1].trim());
                    contest.put("contestCode", parts[3].split(":")[1].trim());
                    contest.put("status", "Aberto");
                    contest.put("totalRevenue", parts[7].split(":")[1].trim());
                    contest.put("totalPrizes", parts[8].split(":")[1].trim());
                    contest.put("corporationShare", parts[9].split(":")[1].trim());
                    openContests.add(contest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return openContests;
    }

    // Método para obter uma lista de concursos abertos
    public static List<Map<String, String>> getAllContests() {
        List<Map<String, String>> AllContests = new ArrayList<>();
        Path path = Paths.get(DIRECTORY_PATH + CONTEST_FILE_NAME);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                Map<String, String> contest = new HashMap<>();
                contest.put("name", parts[0].split(":")[1].trim());
                contest.put("startDate", parts[1].split(":")[1].trim());
                contest.put("endDate", parts[2].split(":")[1].trim());
                contest.put("contestCode", parts[3].split(":")[1].trim());
                contest.put("status", parts[4].split(":")[1].trim());
                contest.put("totalRevenue", parts[7].split(":")[1].trim());
                contest.put("totalPrizes", parts[8].split(":")[1].trim());
                contest.put("corporationShare", parts[9].split(":")[1].trim());
                AllContests.add(contest);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return AllContests;
    }

    // Método para obter uma lista de códigos de concursos abertos
    public static List<String> getOpenContestCodes() {
        List<String> openContestCodes = new ArrayList<>();
        Path path = Paths.get(DIRECTORY_PATH + CONTEST_FILE_NAME);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 5 && parts[4].contains("Aberto")) {
                    // Extrai apenas o código do concurso
                    String contestCode = parts[3].split(":")[1].trim();
                    openContestCodes.add(contestCode);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return openContestCodes;
    }

    public static String getNextContestCode() {
        int maxCode = 0;
        Path path = Paths.get(DIRECTORY_PATH + CONTEST_FILE_NAME);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                for (String part : parts) {
                    if (part.startsWith("Codigo: ")) {
                        // Extrai o valor do código e converte para um número
                        int code = Integer.parseInt(part.replace("Codigo: ", "").trim());
                        if (code > maxCode) {
                            maxCode = code;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de concursos: " + e.getMessage());
            // Caso o arquivo não exista ou esteja vazio, o código será 0.
        }

        // Retorna o próximo código, incrementado em 1
        return String.valueOf(maxCode + 1);
    }

    public static boolean isNomeConcursoRepetido(String nomeConcurso) {
        // Verifica no arquivo de concursos se já existe um concurso com o nome fornecido
        Path path = Paths.get("c:\\tmp\\contests.txt");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Nome do Concurso: " + nomeConcurso)) {
                    return true; // Nome já existe
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de concursos: " + e.getMessage());
        }
        return false; // Nome não encontrado
    }
    
    public static boolean isContestCodeTaken(String contestCode) {
        // Verifica no arquivo de concursos se o código fornecido já foi utilizado
        Path path = Paths.get("c:\\tmp\\contests.txt");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Código do Concurso: " + contestCode)) {
                    return true; // Código já existe
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de concursos: " + e.getMessage());
        }
        return false; // Código não encontrado
    }
    

}
