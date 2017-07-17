package risk.event;

import java.util.EventListener;

import risk.Player;

/**
 * Listen for events related to the player.
 * @author Connor
 */
public interface PlayerListener extends EventListener {

    /**
     * Provide the user with instructions for the current task.
     * @param player The player.
     * @param instruction the instructions to show the player.
     */
    void onInstruction(Player player, String instruction);
}
