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

    public static boolean isAdm(String cpf) {
        try {
            Path path = Paths.get(DIRECTORY_PATH + FILE_NAMES[0]);
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                String[] data = line.split(";");
                if (data.length > 5) { // Verifica se há campos suficientes
                    String userCpf = data[0].split(": ")[1];
                    if (userCpf.equals(cpf)) {
                        boolean isAdm = data[5].split(": ")[1].equals("1"); // Verifica se o usuário é ADM
                        return isAdm;
                    }
                } else {
                    System.out.println("Linha inválida: " + line); // Para debug
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
                    "ADM: " + admValue + ";" + "Telefone: ");
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

    // Método para deletar o perfil do usuário com base no CPF
    public static boolean deleteUserProfile(String cpf) {
        Path path = Paths.get(DIRECTORY_PATH + FILE_NAMES[0]);
        List<String> lines;
        try {
            // Lê todas as linhas do arquivo
            lines = Files.readAllLines(path);

            // Filtra as linhas, mantendo apenas as que não correspondem ao CPF do usuário
            List<String> updatedLines = lines.stream()
                    .filter(line -> !line.contains("CPF: " + cpf + ";")) // Filtra a linha do usuário
                    .toList(); // Usa toList para coletar os resultados

            // Reescreve o arquivo sem a linha do usuário deletado
            Files.write(path, updatedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Perfil deletado com sucesso para CPF: " + cpf);

            return true; // Retorna verdadeiro se a exclusão foi bem-sucedida
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // Retorna falso em caso de erro
    }

}