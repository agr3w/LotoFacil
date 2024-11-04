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

    public Contest(String name, LocalDate startDate, LocalDate endDate, String contestCode, boolean isOpen) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contestCode = contestCode;
        this.isOpen = isOpen;
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
}
