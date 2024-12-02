package database;

import java.io.IOException;
import java.util.List;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import utils.UserSession;
import utils.ValidateDate;

public class PurchaseFileManager {
    private String cpf;
    private String dataCompra;
    private String selectedNumbers;
    private String value;
    private String codigoCompra;
    private String formaPagamento;
    private String nomeAposta;
    private String contestCode;

    public PurchaseFileManager(String cpf, String dataCompra, String selectedNumbers, String value, String codigoCompra,
            String formaPagamento, String nomeAposta, String contestCode) {
        this.cpf = cpf;
        this.dataCompra = dataCompra;
        this.selectedNumbers = selectedNumbers;
        this.value = value;
        this.codigoCompra = codigoCompra;
        this.formaPagamento = formaPagamento;
        this.nomeAposta = nomeAposta;
        this.contestCode = contestCode;
    }

    public String getSelectedNumbersFromFile() {
        return selectedNumbers;
    }

    public String getContestCode() {
        return contestCode;
    }

    public String getValueFromFile() {
        return value;
    }

    public String getCpfFromFile() {
        return cpf;
    }

    public String getDataCompra() {
        return dataCompra;
    }

    public String getCodigoCompra() {
        return codigoCompra;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public String getNomeAposta() {
        return nomeAposta;
    }

    private static final String FILE_NAME = "purchases.txt";
    private static final String filePath = "c:\\tmp\\" + FILE_NAME;

    // Salva os valores do ticket no arquivo
    public static boolean saveBetToFile(String loggedInUser, List<Integer> numbers, String formaPagamento,
            String nomeAposta, String selectedContestCode) {
        // Verifica se a combinação de números já foi comprada
        if (isDuplicateTicket(loggedInUser, numbers)) {
            System.out.println("Erro: A combinação de números já foi comprada.");
            return false; // Impede a compra de um ticket duplicado
        }
        String codigoCompra = generateUniquePurchaseCode(); // Gerar um código de compra único
        Path path = Paths.get(filePath);

        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                writer.write("CPF: " + loggedInUser + "\n");
                writer.write("Código do Concurso: " + selectedContestCode + "\n");
                writer.write("Data da Compra: " + ValidateDate.todayLocalDate().toString() + "\n");
                writer.write("Números Selecionados: " + numbers.toString() + "\n");
                writer.write("Valor Pago: " + TicketPricing.calculatePrice(numbers.size()) + "\n");
                writer.write("Código da Compra: " + codigoCompra + "\n");
                writer.write("Forma de Pagamento: " + formaPagamento + "\n");
                writer.write("Nome da Aposta: " + nomeAposta + "\n\n");
            }
            return true;
        } catch (IOException e) {
            System.out.println("Erro ao salvar os valores: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Método para carregar as informações do arquivo
    public static List<PurchaseFileManager> loadUserTickets() {
        List<PurchaseFileManager> tickets = new ArrayList<>();
        String loggedInCpf = UserSession.getLoggedInUserCpf();
        Path path = Paths.get(filePath);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("CPF: " + loggedInCpf)) {
                    String cpf = line.split(": ")[1];
                    String codeContest = reader.readLine().split(": ")[1];
                    String dataCompra = reader.readLine().split(": ")[1];
                    String selectedNumbers = reader.readLine().split(": ")[1];
                    String value = reader.readLine().split(": ")[1];
                    String codigoCompra = reader.readLine().split(": ")[1];
                    String formaPagamento = reader.readLine().split(": ")[1];
                    String nomeAposta = reader.readLine().split(": ")[1];
                    reader.readLine();

                    tickets.add(
                            new PurchaseFileManager(cpf, dataCompra, selectedNumbers, value, codigoCompra,
                                    formaPagamento, nomeAposta, codeContest));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler os valores: " + e.getMessage());
        }

        return tickets;
    }

    // Método auxiliar para gerar um código de compra único (simples)
    private static String generateUniquePurchaseCode() {
        return "COMPRA-" + System.currentTimeMillis();
    }

    // Método para verificar se a combinação de números já foi comprada pelo usuário
    public static boolean isDuplicateTicket(String loggedInUser, List<Integer> selectedNumbers) {
        List<PurchaseFileManager> userTickets = loadUserTickets();

        // Verifica se já existe um ticket com a mesma combinação de números
        for (PurchaseFileManager ticket : userTickets) {
            if (ticket.getCpfFromFile().equals(loggedInUser)) {
                // Compara as combinações de números (convertidos para listas de inteiros)
                List<Integer> ticketNumbers = convertStringToListOfNumbers(ticket.getSelectedNumbersFromFile());
                if (ticketNumbers.equals(selectedNumbers)) {
                    return true; // Encontrou um ticket com a mesma combinação
                }
            }
        }
        return false; // Não encontrou duplicação
    }

    // Método para converter uma string de números selecionados para uma lista de
    // inteiros
    private static List<Integer> convertStringToListOfNumbers(String selectedNumbers) {
        String numbersString = selectedNumbers.replaceAll("[\\[\\] ]", ""); // Remove colchetes e espaços
        List<Integer> numbers = new ArrayList<>();
        for (String num : numbersString.split(",")) {
            numbers.add(Integer.parseInt(num));
        }
        return numbers;
    }
}
