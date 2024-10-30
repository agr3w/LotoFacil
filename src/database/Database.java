package database;

import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Database {
    private static final String FILE_NAME = "usuarios.txt"; // ta criando usuarios com nomes iguais, ARRUMAR
    private static final String filePath = "c:\\tmp\\" + FILE_NAME;

    // Método para verificar credenciais
    public static boolean checkCredentials(String cpf, String senha) {
        try {
            Path path = Paths.get(filePath);
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

    // Método para salvar um novo usuário no arquivo
    public static void saveUser(String nome, String email, String cpf, String Nascimento,String senha) {
        Path path = Paths.get(filePath);
        try {
            Files.createDirectories(path.getParent());
            BufferedWriter writer = Files.newBufferedWriter(path);

            writer.write("CPF: " + cpf + ";" + "DataNascimento: " + Nascimento + ";" + "senha: " + senha + ";" + "nome: " + nome + ";" + "email: " + email);
            writer.newLine(); // Adicionar nova linha para o próximo usuário
            writer.close();

            System.out.println("Usuário salvo: " + nome);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para criar o arquivo, se não existir
    public static void createFileIfNotExists() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Arquivo de usuários criado.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
