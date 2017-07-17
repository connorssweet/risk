package risk.state;

import java.util.ArrayList;

import risk.Player;
import risk.ui.RiskFrame;

/**
 * This state represents the player order of the game. Subsequent game states
 * will reference the turn.
 */
public class Turn implements GameState {

    private final RiskFrame frame;
    private final ArrayList<Player> players;
    private Player currentPlayer;
    private int turnCount;

    public Turn(RiskFrame frame) {
        frame.setTurn(this);

        this.frame = frame;

        players = frame.getPlayers();
    }

    @Override
    public GameState play() throws InterruptedException {
        currentPlayer = getNext();

        return new Placement(frame);
    }

    public int getCount() {
        return turnCount;
    }

    public Player getPlayer() {
        return currentPlayer;
    }

    private Player getNext() {
        turnCount++;

        if (currentPlayer == null) {
            return players.get(0);
        }

        int currentIndex = players.indexOf(currentPlayer);

        int nextIndex = currentIndex + 1;
        if (nextIndex > players.size() - 1) {
            nextIndex = 0;
        }

        return players.get(nextIndex);
    }
}
