package database;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import utils.UserSession;

public class PurchaseFileManager {
    private String selectedNumbers;
    private String value;
    private String cpf;

    public PurchaseFileManager(String cpf, String selectedNumbers, String value) {
        this.cpf = cpf;
        this.selectedNumbers = selectedNumbers;
        this.value = value;
    }

    public String getSelectedNumbers() {
        return selectedNumbers;
    }

    public String getValue() {
        return value;
    }

    public String getCpf() {
        return cpf;
    }

    private static final String FILE_NAME = "purchases.txt";

    //Salva os valores do ticket no arquivo
    public static boolean saveBetToFile(String loggedInUser, List<Integer> numbers) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(loggedInUser + "\n");
            writer.write(numbers + "\n");
            writer.write(TicketPricing.calculatePrice(numbers.size()) + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Carrega as informacoes do arquivo
    public static List<PurchaseFileManager> loadUserTickets() {
        List<PurchaseFileManager> tickets = new ArrayList<>();
        String loggedInCpf = UserSession.getLoggedInUserCpf();

        try (BufferedReader reader = new BufferedReader(new FileReader("purchases.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(loggedInCpf)) {
                    String cpf = line;
                    String selectedNumbers = reader.readLine();
                    String value = reader.readLine();

                    if (cpf != null && selectedNumbers != null && value != null) {
                        tickets.add(new PurchaseFileManager(cpf, selectedNumbers, value));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler os valores!");
        }

        return tickets;
    }
}
