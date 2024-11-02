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
    private String selectedNumbers;
    private String value;
    private String cpf;
    private String dataCompra;

    public PurchaseFileManager(String cpf, String dataCompra,  String selectedNumbers, String value) {
        this.cpf = cpf;
        this.dataCompra = dataCompra;
        this.selectedNumbers = selectedNumbers;
        this.value = value;
    }

    public String getSelectedNumbersFromFile() {
        return selectedNumbers;
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

    private static final String FILE_NAME = "purchases.txt";
    private static final String filePath = "c:\\tmp\\" + FILE_NAME;

    // Salva os valores do ticket no arquivo
    public static boolean saveBetToFile(String loggedInUser, List<Integer> numbers) {
        String selectedContestCode = UserSession.getSelectedContestCode(); // Obter o código do concurso da sessão
        Path path = Paths.get(filePath);
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(loggedInUser + "\n");
                writer.write(ValidateDate.todayLocalDate().toString() + "\n");
                writer.write(numbers.toString() + "\n");
                writer.write(TicketPricing.calculatePrice(numbers.size()) + "\n");
                writer.write("Código do Concurso: " + selectedContestCode + "\n\n");

            }
            return true;
        } catch (IOException e) {
            System.out.println("Erro ao salvar os valores: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Carrega as informações do arquivo
    public static List<PurchaseFileManager> loadUserTickets() {
        List<PurchaseFileManager> tickets = new ArrayList<>();
        String loggedInCpf = UserSession.getLoggedInUserCpf();

        Path path = Paths.get(filePath);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(loggedInCpf)) {
                    String cpf = line;
                    String dataCompra = reader.readLine();
                    String selectedNumbers = reader.readLine();
                    String value = reader.readLine();

                    if (cpf != null && selectedNumbers != null && value != null) {
                        tickets.add(new PurchaseFileManager(cpf, dataCompra,selectedNumbers, value));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler os valores: " + e.getMessage());
        }

        return tickets;
    }
}
