package CustomException;

/**
 * Custom Exception class, zwraca wyjątek brak gotówki na zrealizwanie wypłaty
 *
 * @author Karol Kistela
 */
public class OutOfMoneyException extends Exception {
    public OutOfMoneyException(String message) {
        super(message);
    }
}
