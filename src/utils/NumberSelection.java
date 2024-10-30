package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NumberSelection {
    private static final int MAX_NUM = 20;
    private static final int MIN_NUM = 15;
    
    private List<Integer> selectedNumbers;
    
    public NumberSelection() {
        selectedNumbers = new ArrayList<>();
    }

    public List<Integer> getSelectedNumbers() {
        return selectedNumbers;
    }

    public int getSelectedCount() {
        return selectedNumbers.size();
    }

    public void addNumber(int number) {
        if (!selectedNumbers.contains(number) && selectedNumbers.size() < MAX_NUM) {
            selectedNumbers.add(number);
        }
    }

    public void removeNumber(int number) {
        selectedNumbers.remove((Integer) number);
    }

    public void clearSelection() {
        selectedNumbers.clear();
    }

    public void generateRandomNumbers() {
        clearSelection();
        Random random = new Random();
        while (selectedNumbers.size() < MIN_NUM) {
            int randomNum = random.nextInt(25) + 1;
            if (!selectedNumbers.contains(randomNum)) {
                selectedNumbers.add(randomNum);
            }
        }
    }
}
