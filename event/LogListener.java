package risk.event;

import java.util.EventListener;

/**
 * Listen for events related to logging.
 * @author Connor
 */
public interface LogListener extends EventListener {

    /**
     * Log the given message.
     * @param message the information to log.
     */
    void onLog(String message);
}
