package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<Usuario> usuarios;

    public UserManager(String filePath) {
        usuarios = new ArrayList<>();
        loadUsersFromFile(filePath);
    }

    private void loadUsersFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // Supondo que os dados estejam separados por vírgula
                if (data.length == 2) {
                    String cpf = data[0].trim();
                    String nome = data[1].trim();
                    usuarios.add(new Usuario(cpf, nome));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String login(String cpf) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCpf().equals(cpf)) {
                UserSession.setLoggedInUserCpf(cpf); // Configura o CPF do usuário logado
                return usuario.getNome(); // Retorna o nome do usuário logado
            }
        }
        return null; // Retorna null se o CPF não for encontrado
    }
}
