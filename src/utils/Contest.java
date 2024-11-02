package utils;

import java.time.LocalDate;

public class Contest {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String contestCode;
    private boolean isOpen;

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
}
