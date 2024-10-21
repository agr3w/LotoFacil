package database;

public class TicketPricing {
    public static double calculatePrice(int count) {
        switch (count) {
            case 15:
                return 3.00;
            case 16:
                return 48.00;
            case 17:
                return 408.00;
            case 18:
                return 2448.00;
            case 19:
                return 11628.00;
            case 20:
                return 46512.00;
            default:
                return 0.00;
        }
    }
}
