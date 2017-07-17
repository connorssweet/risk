package risk.state;

import java.util.ArrayList;
import java.util.List;
import risk.Map;
import risk.Player;
import risk.Territory;
import risk.event.Event;
import risk.event.Mode;
import risk.ui.RiskFrame;

/**
 * This state represents the initial placement of units.
 */
public class InitialPlacement implements GameState {

    private final RiskFrame frame;
    private final ArrayList<Player> players;
    private final Map map;

    public InitialPlacement(RiskFrame frame) {
        this.frame = frame;
        this.players = frame.getPlayers();
        this.map = frame.getMap();
    }

    @Override
    public GameState play() throws InterruptedException {
        final int initialUnitCount = getInitialUnits(players.size());
        final int maximumUnitCount = initialUnitCount * players.size();
        int totalCount = map.getTotalUnitCount();

        while (totalCount < maximumUnitCount) {
            for (Player player : players) {
                Event.fireModeEvent(Mode.HIGHLIGHT_OWNED_TERRITORY_SELECTION_MODE, player);

                Event.fireInstructionEvent(player, "Please reinforce a territory");
                final List<Territory> playerTerritories = map.getPlayerTerritories(player);
                final Territory selected = player.selectTerritory(playerTerritories);
                selected.setUnitCount(selected.getUnitCount() + 1);
                frame.repaint();

                writePlacement(player, selected);
                totalCount++;
            }
        }

        Event.fireModeEvent(Mode.DISABLE_HIGHLIGHT_MODE);

        return new Turn(frame);
    }

    /**
     * Retrieve the number of units based on the number of players.
     * @param playerCount The number of players.
     * @return the number of initial, starting units.
     */
    private int getInitialUnits(int playerCount) {
        switch (playerCount) {
            case 2:
                return 40;
            case 3:
                return 35;
            case 4:
                return 30;
            case 5:
                return 25;
            case 6:
            default:
                return 20;
        }
    }

    private void writePlacement(Player player, Territory territory) {
        Event.fireLogEvent(String.format("<li style=\"color:%s\">%s reinforced <b>%s</b> to %d units</li>",
                player.getHexColor(),
                player.getName(),
                territory.getName(),
                territory.getUnitCount()
        ));
    }
}
