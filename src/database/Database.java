package database;

import java.io.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Database {
    private static final String[] FILE_NAMES = { "users.txt", "purchases.txt" };
    private static final String DIRECTORY_PATH = "c:\\tmp\\";

    // Método para verificar credenciais
    public static boolean checkCredentials(String cpf, String senha) {
        try {
            Path path = Paths.get(DIRECTORY_PATH + FILE_NAMES[0]);
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                String[] data = line.split(";");
                String userCpf = data[0].split(": ")[1];
                String userSenha = data[2].split(": ")[1];

                if (userCpf.equals(cpf) && userSenha.equals(senha)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Método para verificar se o usuário é ADM
    public static boolean isAdm(String cpf) {
        try {
            Path path = Paths.get(DIRECTORY_PATH + FILE_NAMES[0]);
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                String[] data = line.split(";");
                String userCpf = data[0].split(": ")[1];

                // Verifica se o CPF do usuário corresponde ao fornecido
                if (userCpf.equals(cpf)) {
                    boolean isAdm = data[5].split(": ")[1].equals("1"); // Verifica se o usuário é ADM
                    return isAdm;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // Retorna false se não encontrar o usuário ou se não for ADM
    }

    // Método para salvar um novo usuário no arquivo
    public static void saveUser(String nome, String email, String cpf, String Nascimento, String senha) {
        Path path = Paths.get(DIRECTORY_PATH + FILE_NAMES[0]);
        try {
            Files.createDirectories(path.getParent());
            BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            String admValue = "0";
            writer.write("CPF: " + cpf + ";" + "DataNascimento: " + Nascimento + ";" + "senha: " + senha + ";"
                    + "nome: " + nome + ";" + "email: " + email + ";" +
                    "ADM: " + admValue);
            writer.newLine(); // Adicionar nova linha para o próximo usuário
            writer.close();

            System.out.println("Usuário salvo: " + nome);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para criar os arquivos se não existirem
    public static void createFilesIfNotExists() {
        try {
            Files.createDirectories(Paths.get(DIRECTORY_PATH)); // Garante que o diretório exista

            for (String fileName : FILE_NAMES) {
                Path filePath = Paths.get(DIRECTORY_PATH + fileName);
                if (Files.notExists(filePath)) {
                    Files.createFile(filePath);
                    System.out.println("Arquivo criado: " + filePath);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao criar os arquivos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
