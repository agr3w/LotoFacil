package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {
    private static final String USER_FILE_NAME = "users.txt";
    private static final String DIRECTORY_PATH = "c:\\tmp\\";
    private static final String FILE_PATH = "c:\\tmp\\purchases.txt";



     /**
     * Verifica se o usuário realizou alguma aposta.
     *
     * @param cpf CPF do usuário.
     * @return true se o usuário fez apostas; caso contrário, false.
     */
    public static boolean hasUserMadeBets(String cpf) {
        Path path = Paths.get(FILE_PATH);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("CPF: ") && line.split(": ")[1].equals(cpf)) {
                    return true; // CPF encontrado
                }
                // Pula as próximas linhas do registro até o próximo CPF
                while ((line = reader.readLine()) != null && !line.isEmpty());
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Nenhuma aposta encontrada
    }

    /**
     * Retorna a lista de concursos nos quais o usuário apostou.
     *
     * @param cpf CPF do usuário.
     * @return Lista de códigos de concursos.
     */
    public static List<String> getUserContests(String cpf) {
        List<String> contests = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("CPF: ") && line.split(": ")[1].equals(cpf)) {
                    String contestCode = reader.readLine().split(": ")[1]; // Lê a linha do código do concurso
                    if (!contests.contains(contestCode)) {
                        contests.add(contestCode);
                    }
                }
                // Pula as próximas linhas do registro até o próximo CPF
                while ((line = reader.readLine()) != null && !line.isEmpty());
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            e.printStackTrace();
        }
        return contests;
    }

    /**
     * Calcula o valor total ganho pelo usuário.
     *
     * @param cpf CPF do usuário.
     * @return Valor total ganho.
     */
    public static double getUserTotalWinnings(String cpf) {
        double totalWinnings = 0.0;
        Path path = Paths.get(FILE_PATH);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("CPF: ") && line.split(": ")[1].equals(cpf)) {
                    reader.readLine(); // Pula código do concurso
                    reader.readLine(); // Pula data da compra
                    reader.readLine(); // Pula números selecionados
                    String valueLine = reader.readLine(); // Lê linha do valor
                    totalWinnings += Double.parseDouble(valueLine.split(": ")[1]);
                }
                // Pula as próximas linhas do registro até o próximo CPF
                while ((line = reader.readLine()) != null && !line.isEmpty());
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            e.printStackTrace();
        }
        return totalWinnings;
    }


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

    public static void saveUserData(String cpf, String nome, String email, String dataNascimento, String senha,
            String telefone, String adm) {
        Path path = Paths.get(DIRECTORY_PATH + USER_FILE_NAME);
        List<String> allUsers = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("CPF: " + cpf)) {
                    // Atualiza as informações do usuário
                    line = "CPF: " + cpf + "; DataNascimento: " + dataNascimento + "; senha: " + senha +
                            "; nome: " + nome + "; email: " + email + "; ADM: " + adm + ";" + "Telefone: " + telefone
                            + ";"; // Atualizando apenas os campos relevantes
                }
                allUsers.add(line); // Armazena todas as linhas
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sobrescreve o arquivo com os dados atualizados
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (String user : allUsers) {
                writer.write(user);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para buscar todos os usuários no arquivo
    public static List<Map<String, String>> getAllUsers() {
        List<Map<String, String>> users = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(DIRECTORY_PATH + USER_FILE_NAME))) {
            String line;

            // Lê linha por linha do arquivo
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Ignora linhas vazias
                }

                Map<String, String> userData = new HashMap<>();
                String[] parts = line.split(";");

                // Para cada campo na linha (dividido por ponto e vírgula)
                for (String part : parts) {
                    // Remover espaços extras e dividir por ": " para pegar a chave e o valor
                    String[] keyValue = part.trim().split(": ");
                    if (keyValue.length == 2) {
                        userData.put(keyValue[0].trim(), keyValue[1].trim());
                    }
                }

                // Adiciona os dados do usuário na lista
                users.add(userData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users; // Retorna a lista de usuários
    }

    public static void updateUserPhoneAndAdm(String cpf, String telefone, String adm) {
        Path path = Paths.get(DIRECTORY_PATH + USER_FILE_NAME);
        List<String> allUsers = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Verifica se a linha corresponde ao CPF do usuário que está sendo modificado
                if (line.contains("CPF: " + cpf)) {
                    // Atualiza apenas o telefone e o ADM
                    line = updateLineWithNewPhoneAndAdm(line, telefone, adm);
                }
                // Adiciona a linha ao arquivo, seja ela modificada ou não
                allUsers.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sobrescreve o arquivo com as alterações apenas no usuário modificado
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (String user : allUsers) {
                writer.write(user); // Escreve cada linha de volta ao arquivo
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para atualizar a linha com o novo telefone e ADM
    private static String updateLineWithNewPhoneAndAdm(String line, String telefone, String adm) {
        String[] parts = line.split(";");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].contains("Telefone:")) {
                parts[i] = "Telefone: " + telefone; // Atualiza o telefone
            }
            if (parts[i].contains("ADM:")) {
                parts[i] = "ADM: " + adm; // Atualiza o ADM
            }
        }
        return String.join(";", parts); // Junta as partes de volta em uma linha
    }

    public static void saveNewUser(String cpf, String nome, String email, String dataNascimento, String senha,
            String telefone, String adm) {
        Path path = Paths.get(DIRECTORY_PATH + USER_FILE_NAME);
        List<String> allUsers = new ArrayList<>();

        // Tentar abrir o arquivo para adicionar o novo usuário
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                allUsers.add(line); // Adiciona todas as linhas já existentes
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cria a linha com os dados do novo usuário
        String newUser = "CPF: " + cpf + "; DataNascimento: " + dataNascimento + "; senha: " + senha +
                "; nome: " + nome + "; email: " + email + "; ADM: " + adm + ";" + "Telefone: " + telefone + ";";

        // Adiciona o novo usuário à lista de usuários
        allUsers.add(newUser);

        // Sobrescreve o arquivo com a nova lista de usuários
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (String user : allUsers) {
                writer.write(user); // Escreve a linha de cada usuário
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteUserProfile(String cpf) {
        Path path = Paths.get(DIRECTORY_PATH + USER_FILE_NAME);
        List<String> allUsers = new ArrayList<>();
        boolean userFound = false;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("CPF: " + cpf)) {
                    allUsers.add(line); // Adiciona o usuário apenas se o CPF não for o do usuário a ser excluído
                } else {
                    userFound = true; // Marca que o usuário foi encontrado
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Em caso de erro, retornamos falso
        }

        if (userFound) {
            // Sobrescreve o arquivo com a lista de usuários atualizada
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (String user : allUsers) {
                    writer.write(user);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false; // Em caso de erro ao reescrever, retornamos falso
            }
            return true; // Retorna true se a exclusão foi bem-sucedida
        }

        return false; // Caso o CPF não tenha sido encontrado
    }

}
