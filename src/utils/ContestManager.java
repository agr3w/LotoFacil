package utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import database.PurchaseFileManager;

public class ContestManager {
    private static final String CONTEST_FILE_NAME = "contests.txt"; // Caminho do arquivo onde os concursos estão
                                                                    // armazenados
    private static final String DIRECTORY_PATH = "c:\\tmp\\";

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
                if (parts.length >= 7) { // Alterado para 7 para incluir TotalBets
                    Map<String, String> contest = new HashMap<>();
                    contest.put("name", parts[0].split(":")[1].trim());
                    contest.put("startDate", parts[1].split(":")[1].trim());
                    contest.put("endDate", parts[2].split(":")[1].trim());
                    contest.put("contestCode", parts[3].split(":")[1].trim());
                    contest.put("status", parts[4].split(":")[1].trim());
                    contest.put("winningNumbers", parts[5].split(":")[1].trim()); // Nova linha para winningNumbers
                    contest.put("TotalBets", parts[6].split(":")[1].trim()); // Inicializa o total de apostas
                    contests.add(contest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contests;
    }

    // Atualizar o método de escrita para incluir TotalBets e winningNumbers
    private static void writeContestsToFile(List<Map<String, String>> contests) {
        Path path = Paths.get(DIRECTORY_PATH + CONTEST_FILE_NAME);

        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Map<String, String> contest : contests) {
                bw.write("Nome: " + contest.get("name") + ";");
                bw.write("DataInicio: " + contest.get("startDate") + ";");
                bw.write("DataFinal: " + contest.get("endDate") + ";");
                bw.write("Codigo: " + contest.get("contestCode") + ";");
                bw.write("Status: " + contest.get("status") + ";");
                bw.write("WinningNumbers: " + contest.get("winningNumbers") + ";");
                bw.write("TotalBets: " + contest.get("TotalBets") + ";"); // Inclui TotalBets
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> generateRandomNumbers() {
        List<Integer> numbers = new ArrayList<>();
        Random random = new Random();

        // Gerando 20 números únicos entre 1 e 25
        while (numbers.size() < 20) {
            int number = random.nextInt(25) + 1; // Gera números de 1 a 25
            if (!numbers.contains(number)) {
                numbers.add(number);
            }
        }

        Collections.shuffle(numbers);

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

        newContest.put("TotalBets", "0"); // Inicializa TotalBets com 0

        contests.add(newContest);
        writeContestsToFile(contests); // Salva as alterações no arquivo
    }

    public static void placeBet(String contestCode) {
        List<Map<String, String>> contests = readContestsFromFile();

        for (Map<String, String> contest : contests) {
            if (contest.get("contestCode").equals(contestCode)) {
                int currentBets = Integer.parseInt(contest.get("TotalBets"));
                currentBets++;
                contest.put("TotalBets", String.valueOf(currentBets)); // Atualiza a contagem de apostas
                writeContestsToFile(contests); // Salva as alterações no arquivo
                return;
            }
        }
        System.out.println("Concurso não encontrado: " + contestCode);
    }

    // Atualizar o método finalizeContest para incluir a distribuição de prêmios
    public static void finalizeContest(String contestCode) {
        try {
            List<Map<String, String>> contests = readContestsFromFile();
            boolean found = false;

            for (Map<String, String> contest : contests) {
                if (contest.get("contestCode").equals(contestCode)) {
                    found = true;
                    if (contest.get("status").equals("Finalizado")) {
                        System.out.println("Este concurso já está finalizado.");
                        return;
                    }
                    contest.put("status", "Finalizado");
                    writeContestsToFile(contests);

                    System.out.println("Concurso finalizado: " + contestCode);
                    distributePrizes(contestCode);
                    return;
                }
            }

            if (!found) {
                System.out.println("Concurso não encontrado: " + contestCode);
            }
        } catch (Exception e) {
            System.out.println("Erro ao finalizar concurso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Integer> parseWinningNumbers(String winningNumbersString) {
        String[] parts = winningNumbersString.replaceAll("[\\[\\]]", "").split(", ");
        List<Integer> winningNumbers = new ArrayList<>();
        for (String num : parts) {
            winningNumbers.add(Integer.parseInt(num));
        }
        return winningNumbers;
    }

    public static List<Integer> parseSelectedNumbers(String selectedNumbersString) {
        String[] parts = selectedNumbersString.replaceAll("[\\[\\]]", "").split(", ");
        List<Integer> selectedNumbers = new ArrayList<>();
        for (String num : parts) {
            selectedNumbers.add(Integer.parseInt(num));
        }
        return selectedNumbers;
    }

    public static int countCorrectNumbers(List<Integer> selectedNumbers, List<Integer> winningNumbers) {
        int count = 0;

        // As duas listas devem ter o mesmo tamanho para verificar as posições
        // correspondentes
        int size = Math.min(selectedNumbers.size(), winningNumbers.size());

        // Itera sobre os números até o tamanho mínimo das duas listas
        for (int i = 0; i < size; i++) {
            // Compara os números na mesma posição
            if (selectedNumbers.get(i).equals(winningNumbers.get(i))) {
                count++; // Conta o número correto
            }
        }

        return count;
    }

    // Método para obter os detalhes do concurso pelo código
    public static Map<String, String> getContestByCode(String contestCode) {
        List<Map<String, String>> contests = readContestsFromFile();

        for (Map<String, String> contest : contests) {
            if (contest.get("contestCode").equals(contestCode)) {
                return contest; // Retorna o concurso correspondente
            }
        }
        return null; // Retorna null se o concurso não for encontrado
    }

    public static double calculateTotalBets(String contestCode) {
        double totalBets = 0.0;
        Path path = Paths.get(DIRECTORY_PATH + "purchases.txt");

        // Verificar se o arquivo existe
        if (!Files.exists(path)) {
            System.out.println("Arquivo de compras não encontrado.");
            return totalBets;
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                // Procurar por linhas que contenham o código do concurso
                if (line.trim().equals("Código do Concurso: " + contestCode)) {
                    // Procurar pelo valor da aposta nas linhas anteriores
                    String[] fileContent = Files.readAllLines(path).toArray(new String[0]);
                    for (int i = 0; i < fileContent.length; i++) {
                        if (fileContent[i].trim().equals("Código do Concurso: " + contestCode)) {
                            // Procurar pelo valor pago nas linhas anteriores
                            for (int j = Math.max(0, i - 7); j < i; j++) {
                                if (fileContent[j].startsWith("Valor Pago: ")) {
                                    String valorStr = fileContent[j].split(": ")[1].trim();
                                    totalBets += Double.parseDouble(valorStr);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de compras: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Erro ao converter valor: " + e.getMessage());
            e.printStackTrace();
        }

        return totalBets;
    }

    // Método para distribuir os prêmios com base no total de apostas
    public static void distributePrizes(String contestCode) {
        try {
            List<Map<String, String>> contests = readContestsFromFile();
            double totalBets = calculateTotalBets(contestCode);

            if (totalBets == 0.0) {
                System.out.println("Não há apostas registradas para este concurso.");
                return;
            }

            for (Map<String, String> contest : contests) {
                if (contest.get("contestCode").equals(contestCode)) {
                    // Distribuição dos prêmios
                    double firstPrize = totalBets * 0.35;
                    double secondPrize = totalBets * 0.25;
                    double thirdPrize = totalBets * 0.20;
                    double fourthPrize = totalBets * 0.15;
                    double fifthPrize = totalBets * 0.05;

                    // Exibir os prêmios
                    System.out.println("\n=== Distribuição de Prêmios ===");
                    System.out.println("Total de Apostas: R$ " + String.format("%.2f", totalBets));
                    System.out.println("Prêmio do Primeiro Lugar: R$ " + String.format("%.2f", firstPrize));
                    System.out.println("Prêmio do Segundo Lugar: R$ " + String.format("%.2f", secondPrize));
                    System.out.println("Prêmio do Terceiro Lugar: R$ " + String.format("%.2f", thirdPrize));
                    System.out.println("Prêmio do Quarto Lugar: R$ " + String.format("%.2f", fourthPrize));
                    System.out.println("Prêmio do Quinto Lugar: R$ " + String.format("%.2f", fifthPrize));
                    System.out.println("==============================\n");

                    return;
                }
            }
            System.out.println("Concurso não encontrado: " + contestCode);
        } catch (Exception e) {
            System.out.println("Erro ao distribuir prêmios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para determinar os vencedores
    public static void determineWinners(String contestCode) {
        Map<String, String> contest = getContestByCode(contestCode);
        if (contest == null) {
            System.out.println("Concurso não encontrado: " + contestCode);
            return;
        }

        List<Integer> winningNumbers = parseWinningNumbers(contest.get("winningNumbers"));
        List<PurchaseFileManager> tickets = PurchaseFileManager.loadUserTickets();

        Map<Integer, List<String>> winners = new HashMap<>();
        for (int i = 11; i <= 15; i++) {
            winners.put(i, new ArrayList<>());
        }

        for (PurchaseFileManager ticket : tickets) {
            if (ticket.getContestCode().equals(contestCode)) {
                List<Integer> selectedNumbers = parseSelectedNumbers(ticket.getSelectedNumbersFromFile());
                int correctCount = countCorrectNumbers(selectedNumbers, winningNumbers);
                if (correctCount >= 11) {
                    winners.get(correctCount).add(ticket.getCpfFromFile());
                }
            }
        }

        // Exibir vencedores
        for (int i = 15; i >= 11; i--) {
            System.out.println("Vencedores com " + i + " acertos: " + winners.get(i).size());
            for (String cpf : winners.get(i)) {
                System.out.println("CPF: " + cpf);
            }
            System.out.println();
        }
    }

}