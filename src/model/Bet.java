package model;

import java.util.ArrayList;
import java.util.List;

public class Bet {
    private List<Integer> selectedNumbers;

    public Bet() {
        this.selectedNumbers = new ArrayList<>();
    }

    public void addNumber(int number) {
        if (!selectedNumbers.contains(number)) {
            selectedNumbers.add(number);
        }
    }

    public List<Integer> getSelectedNumbers() {
        return selectedNumbers;
    }

    public void clearNumbers() {
        selectedNumbers.clear();
    }
}
