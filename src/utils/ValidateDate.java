package utils;

import java.time.LocalDate;
import java.time.Period;

public class ValidateDate {

    public static LocalDate todayLocalDate() {
        LocalDate today = LocalDate.now();
        return today;
    }

    public static boolean isOfLegalAge(LocalDate pickedDate) {
        Period age = Period.between(pickedDate, todayLocalDate());
        return age.getYears() >= 18;
    }
}