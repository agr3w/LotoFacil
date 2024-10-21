package database;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileManager {
    private static final String FILE_NAME = "purchases.txt";

    public static void savePurchase(String user, List<Integer> selectedNumbers) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write("Usuario: " + user + "\n");
            writer.write("Numeros selecionados: " + selectedNumbers + "\n");
            writer.write("Valor: R$ " + String.format("%.2f", TicketPricing.calculatePrice(selectedNumbers.size())) + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
