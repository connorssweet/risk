package risk.state;

import risk.Player;
import risk.ui.RiskFrame;
import risk.ui.WinnerStatsDialog;

/**
 * This game state shows the winner of the game because the game is over.
 * @author Connor
 */
public class Winner implements GameState {

    private final RiskFrame frame;
    
    /**
     * Sets frame of Winner state
     * @param frame 
     */
    public Winner(RiskFrame frame) {
        this.frame = frame;
    }

    /**
     * Instantiates WinnerStatsDialog
     * @return null 
     */
    @Override
    public GameState play() {
        final Player winner = frame.getPlayers().get(0);

        final WinnerStatsDialog dialog = new WinnerStatsDialog(frame, winner.getName() + " is the Winner!", winner, frame.getTurn().getCount());
        dialog.setVisible(true);

        return null;
    }
}
