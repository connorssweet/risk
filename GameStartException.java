package risk;

/**
 * This class handles exceptions occurring while building the UI.
 * @author Connor
 */
public class GameStartException extends Exception {

    /**
     * Constructor sends error message to superclass Exception.
     * @param message
     */
    public GameStartException(String message) {
        super(message);
    }

    /**
     * Constructor sends error message to superclass Exception.
     * @param message
     * @param throwable
     */
    public GameStartException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
