package risk.event;

import java.util.EventListener;

/**
 * Listen for game mode changing events.
 * @author Connor
 */
public interface ModeListener extends EventListener {

    /**
     * When the mode has been changes.
     * @param mode The game mode.
     * @param object An object related to the game mode.
     */
    void onMode(int mode, Object object);
}
