package utils;

import java.time.LocalDate;
import java.time.Period;

public class ValidateDate {

    public static boolean isOfLegalAge(LocalDate pickedDate) {
        LocalDate today = LocalDate.now();
        Period age = Period.between(pickedDate, today);
        return age.getYears() >= 18;
    }
}