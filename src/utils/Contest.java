package utils;

import java.time.LocalDate;
import java.util.List;

public class Contest {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String contestCode;
    private boolean isOpen;
       private List<Integer> winningNumbers;
       private int numberOfBets;
       

    public Contest(String name, LocalDate startDate, LocalDate endDate, String contestCode, boolean isOpen) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contestCode = contestCode;
        this.isOpen = isOpen;
        this.numberOfBets = 0;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getContestCode() {
        return contestCode;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public List<Integer> getWinningNumbers() {
        return winningNumbers;
    }

    // Método para definir os números vencedores
    public void setWinningNumbers(List<Integer> winningNumbers) {
        this.winningNumbers = winningNumbers;
    }

    public void addBet() {
        this.numberOfBets++; // Incrementa a quantidade de apostas
    }

    public int getNumberOfBets() {
        return numberOfBets; // Retorna a quantidade de apostas
    }
}
