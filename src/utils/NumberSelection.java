package utils;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class NumberSelection {
    private static final int MAX_NUM = 20; // Número máximo de seleções
    private static final int MIN_NUM = 15; // Número mínimo de seleções
    
    private Set<Integer> selectedNumbers; // Usando Set para evitar duplicatas
    
    public NumberSelection() {
        selectedNumbers = new HashSet<>();
    }

    public List<Integer> getSelectedNumbers() {
        return selectedNumbers.stream().sorted().collect(Collectors.toList()); // Retorna uma lista ordenada
    }

    public int getSelectedCount() {
        return selectedNumbers.size();
    }

    public void addNumber(int number) {
        if (selectedNumbers.size() < MAX_NUM) {
            selectedNumbers.add(number); // O Set já evita duplicatas
        } else {
            // Aqui você pode adicionar um alerta ou mensagem se o limite for atingido
            System.out.println("Limite máximo de seleções atingido: " + MAX_NUM);
        }
    }

    public void removeNumber(int number) {
        selectedNumbers.remove(number); // O Set não precisa de casting
    }

    public void clearSelection() {
        selectedNumbers.clear();
    }

    public void generateRandomNumbers() {
        clearSelection();
        Random random = new Random();
        while (selectedNumbers.size() < MIN_NUM) {
            int randomNum = random.nextInt(25) + 1; // Gera números de 1 a 25
            selectedNumbers.add(randomNum); // O Set evita duplicatas automaticamente
        }
    }
}