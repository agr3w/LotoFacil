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
                    contest.put("totalRevenue", parts[7].split(":")[1].trim()); // Inicializa o total de apostas
                    contest.put("totalPrizes", parts[8].split(":")[1].trim()); // Inicializa o total de apostas
                    contest.put("corporationShare", parts[9].split(":")[1].trim()); // Inicializa o total de apostas
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
                bw.write("totalRevenue: " + contest.get("totalRevenue") + ";"); // Inclui TotalBets
                bw.write("totalPrizes: " + contest.get("totalPrizes") + ";"); // Inclui TotalBets
                bw.write("corporationShare: " + contest.get("corporationShare") + ";"); // Inclui TotalBets
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

                    // Passo 1: Calcular receita total e separar porcentagens
                    double totalRevenue = calculateTotalBets(contestCode);
                    double prizePool = totalRevenue * 0.6; // 60% para prêmios
                    double corporationShare = totalRevenue * 0.4; // 40% para a corporação

                    // Atualiza os valores no mapa
                    contest.put("totalRevenue", String.valueOf(totalRevenue));
                    contest.put("totalPrizes", String.valueOf(prizePool));
                    contest.put("corporationShare", String.valueOf(corporationShare));

                    // Salva as alterações no arquivo
                    writeContestsToFile(contests);

                    System.out.println("Concurso finalizado: " + contestCode);
                    System.out.println("Receita total: " + totalRevenue);
                    System.out.println("Total de prêmios distribuídos: " + prizePool);
                    System.out.println("Parte da corporação: " + corporationShare);
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
            boolean isCorrectContest = false;

            while ((line = br.readLine()) != null) {
                // Verificar se a linha contém o código do concurso
                if (line.trim().equals("Código do Concurso: " + contestCode)) {
                    isCorrectContest = true; // Ativa o sinalizador quando o concurso é encontrado
                } else if (isCorrectContest && line.startsWith("Valor Pago: ")) {
                    // Se for a linha com o valor da aposta, somar ao total
                    String valorStr = line.split(": ")[1].trim();
                    totalBets += Double.parseDouble(valorStr);
                    isCorrectContest = false; // Resetar o sinalizador para procurar o próximo concurso
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

    public static double calculateTotalPrizes(String contestCode) {
        double totalPrizes = 0.0;
        Map<String, String> contest = getContestByCode(contestCode);

        if (contest == null) {
            System.out.println("Concurso não encontrado: " + contestCode);
            return totalPrizes;
        }

        List<Integer> winningNumbers = parseWinningNumbers(contest.get("winningNumbers"));
        List<PurchaseFileManager> tickets = PurchaseFileManager.loadUserTickets(); // Carrega todos os bilhetes

        for (PurchaseFileManager ticket : tickets) {
            if (ticket.getContestCode().equals(contestCode)) {
                List<Integer> selectedNumbers = parseSelectedNumbers(ticket.getSelectedNumbersFromFile());
                int correctCount = countCorrectNumbers(selectedNumbers, winningNumbers);
                double ticketValue = 0.0;

                // Converte o valor do ticket de String para Double
                try {
                    ticketValue = Double.parseDouble(ticket.getValueFromFile());
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao converter o valor do ticket: " + e.getMessage());
                }

                // Calcula o prêmio com base na contagem de acertos
                double prize = calculatePrize(correctCount, ticketValue);
                totalPrizes += prize; // Adiciona o prêmio ao total
            }
        }
        return totalPrizes; // Retorna o total de prêmios
    }

    public static double calculatePrize(int correctCount, double totalPrizePool) {
        double prize = 0;
        double totalPrizeDistribuation = totalPrizePool * 0.4335; //Apenas o valor de premios que sera distribuido

        if (correctCount == 11) {
            prize = totalPrizeDistribuation * 0.10; // Exemplo: 10% do total para 11 acertos
        } else if (correctCount == 12) {
            prize = totalPrizeDistribuation * 0.20; // Exemplo: 20% do total para 12 acertos
        } else if (correctCount == 13) {
            prize = totalPrizeDistribuation * 0.30; // Exemplo: 30% do total para 13 acertos
        } else if (correctCount == 14) {
            prize = totalPrizeDistribuation * 0.32; // 32% do total para 14 acertos
        } else if (correctCount == 15) {
            prize = totalPrizeDistribuation * 0.68; // 68% do total para 15 acertos
        }

        return prize;
    }

}