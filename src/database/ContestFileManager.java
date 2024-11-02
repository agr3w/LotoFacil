package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import utils.Contest;

public class ContestFileManager {
    private static final String CONTEST_FILE_NAME = "contests.txt";
    private static final String DIRECTORY_PATH = "c:\\tmp\\";

    // Método para criar e salvar um concurso
    public static void saveContest(Contest contest) {
        Path path = Paths.get(DIRECTORY_PATH + CONTEST_FILE_NAME);
        try {
            Files.createDirectories(path.getParent());
            BufferedWriter writer = Files.newBufferedWriter(path);

            writer.write("Nome: " + contest.getName() + ";");
            writer.write("DataInicio: " + contest.getStartDate() + ";");
            writer.write("DataFinal: " + contest.getEndDate() + ";");
            writer.write("Codigo: " + contest.getContestCode() + ";");
            writer.write("Status: " + (contest.isOpen() ? "Aberto" : "Fechado") + ";");
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

}
