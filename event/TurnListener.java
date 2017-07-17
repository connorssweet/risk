package risk.event;

import java.util.EventListener;

/**
 * Listen for events related to a game turn.
 * @author Connor
 */
public interface TurnListener extends EventListener {

    /**
     * A player has started the turn.
     */
    void onBeginTurn(String name);

    /**
     * A player has ended the turn.
     */
    void onEndTurn();
}
