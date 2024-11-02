package database;

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
}
