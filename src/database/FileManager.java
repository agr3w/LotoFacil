package database;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileManager {
    private static final String FILE_NAME = "purchases.txt";

    public static boolean saveBetToFile(String loggedInUser, List<Integer> numbers) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write("Usuario: " + loggedInUser + "\n");
            writer.write("Numeros selecionados: " + numbers + "\n");
            writer.write("Valor: R$ " + String.format("%.2f", TicketPricing.calculatePrice(numbers.size())) + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
