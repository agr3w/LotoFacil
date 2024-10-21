package database;

import java.io.*;
import java.util.Scanner;

public class Database {
    private static final String FILE_NAME = "usuarios.txt";

    // Método para verificar credenciais
    public static boolean checkCredentials(String cpf, String senha) {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                return false;
            }

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Remover os prefixos e dividir a linha corretamente
                String[] data = line.split(";");
                String userCpf = data[0].split(": ")[1]; // Extrair o CPF
                String userSenha = data[1].split(": ")[1]; // Extrair a senha
                
                // Verificar credenciais
                if (userCpf.equals(cpf) && userSenha.equals(senha)) {
                    scanner.close();
                    return true; // Credenciais corretas
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false; // Credenciais incorretas
    }

    // Método para salvar um novo usuário no arquivo
    public static void saveUser(String nome, String email, String cpf, String senha) {
        try {
            FileWriter writer = new FileWriter(FILE_NAME, true); // Modo de append
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            // Salvar as informações no formato "CPF: cpf;senha: senha;nome: nome;email: email"
            bufferedWriter.write("CPF: " + cpf + ";" + "senha: " + senha + ";" + "nome: " + nome + ";" + "email: " + email);
            bufferedWriter.newLine(); // Adicionar nova linha para o próximo usuário
            bufferedWriter.close();

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
