package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String USER_FILE_NAME = "users.txt";
    private static final String DIRECTORY_PATH = "c:\\tmp\\";
    
    // Carregar dados de um usuário específico
    public static List<String> loadUserData(String cpf) {
        List<String> userData = new ArrayList<>();
        Path path = Paths.get(DIRECTORY_PATH + USER_FILE_NAME);
        
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("CPF: " + cpf)) {
                    String[] parts = line.split(";");
                    for (String part : parts) {
                        userData.add(part.split(": ")[1].trim());
                    }
                    break; // Encerra a busca após encontrar o usuário
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return userData; // Retorna os dados do usuário
    }

    // Salvar as informações do usuário editadas
    public static void saveUserData(String cpf, String nome, String email, String dataNascimento, String senha) {
        Path path = Paths.get(DIRECTORY_PATH + USER_FILE_NAME);
        List<String> allUsers = new ArrayList<>();
        
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("CPF: " + cpf)) {
                    // Atualiza as informações do usuário
                    line = "CPF: " + cpf + "; DataNascimento: " + dataNascimento + "; senha: " + senha + 
                           "; nome: " + nome + "; email: " + email + "; ADM: 0" + ";"+ "Telefone: "; // Exemplo de ADM fixo
                }
                allUsers.add(line); // Armazena todas as linhas
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Escreve todas as informações de volta ao arquivo
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
            for (String user : allUsers) {
                writer.write(user);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
