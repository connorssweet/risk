package risk.state;

/**
 * A representation of the current game state or phase. The valid states are:
 * [Claim Territory] -> [Initial Placement] -> [Turn] -> [Placement] -> [Attack]
 * -> [Fortify] -> [Turn] -> [Winner]
 */
public interface GameState {

    /**
     * Play the current game state.
     * @return The next game state.
     * @throws InterruptedException If the game was interrupted.
     */
    GameState play() throws InterruptedException;
}
