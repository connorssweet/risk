package risk.state;

import java.util.ArrayList;
import java.util.Arrays;

import risk.Map;
import risk.Player;
import risk.Territory;
import risk.event.Event;
import risk.event.Mode;
import risk.ui.RiskFrame;

/**
 * This state represents the initial claim territory phase.
 */
public class ClaimTerritory implements GameState {

    private final RiskFrame frame;
    private final Map map;
    private final ArrayList<Player> players;

    public ClaimTerritory(RiskFrame frame) {
        this.frame = frame;
        this.map = frame.getMap();
        this.players = frame.getPlayers();
    }

    @Override
    public GameState play() throws InterruptedException {
        final ArrayList<Territory> unclaimed = new ArrayList<Territory>();
        unclaimed.addAll(Arrays.asList(map.getTerritories()));

        Event.fireModeEvent(Mode.HIGHLIGHT_UNOCCUPIED_TERRITORY_SELECTION_MODE);

        while (!unclaimed.isEmpty()) {
            for (Player player : players) {
                
                Event.fireInstructionEvent(player, "Please claim a territory.");
                final Territory claimed = player.selectTerritory(unclaimed);
                claimed.setPlayer(player);
                claimed.setUnitCount(1);
                unclaimed.remove(claimed);

                writeClaimedTerritory(player, claimed);

                frame.getMapBuilder().addUnitCountLabels(claimed);
                frame.repaint();
                if(unclaimed.isEmpty()){
                    break;
                }
            }
        }

        Event.fireModeEvent(Mode.DISABLE_HIGHLIGHT_MODE);

        return new InitialPlacement(frame);
    }

    private void writeClaimedTerritory(Player player, Territory claimed) {
        Event.fireLogEvent(String.format("<li style=\"color:%s\">%s claimed <i>%s</i> in <b>%s</b></li>",
                player.getHexColor(),
                player.getName(),
                claimed.getName(),
                claimed.getContinent().getName()
        ));
    }
}
