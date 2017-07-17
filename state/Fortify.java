package risk.state;

import java.util.List;

import risk.Map;
import risk.Player;
import risk.Territory;
import risk.event.Event;
import risk.event.Mode;
import risk.ui.RiskFrame;

/**
 * This is the fortification phase that follows the attack state.
 */
public class Fortify implements GameState {

    private final RiskFrame frame;
    private final Map map;
    private final Player player;

    public Fortify(RiskFrame frame) {
        this.frame = frame;
        this.map = frame.getMap();
        this.player = frame.getTurn().getPlayer();
    }

    @Override
    public GameState play() throws InterruptedException {
        final List<Territory> validFrom = map.getPlayerTerritoriesWithMultipleUnits(player);
        if (validFrom.isEmpty()) {
            Event.fireLogEvent(String.format("<li style=\"color:%s\">%s does not have enough units to fortify another territory</li>",
                    player.getHexColor(),
                    player.getName()
            ));
            return frame.getTurn();
        }

        Event.fireModeEvent(Mode.HIGHLIGHT_OWNED_TERRITORY_SELECTION_MODE, player);
        Event.fireInstructionEvent(player, "You may fortify from one territory to another connected territory.");
        Event.fireBeginAttackEvent("Fortify");

        final Territory from = player.selectTerritory(validFrom);
        if (from != null) {
            Event.fireModeEvent(Mode.HIGHLIGHT_ADJACENT_TERRITORY_SELECTION_MODE);
            final Territory to = player.selectTerritory(map.getConnectedTerritories(from));
            if (to != null) {
                moveUnits(player, from, to);
                frame.repaint();
            }
        }

        Event.fireEndAttackEvent();
        Event.fireModeEvent(Mode.DISABLE_HIGHLIGHT_MODE);

        return frame.getTurn();
    }

    private void moveUnits(Player player, Territory from, Territory to) {
        int movingUnits = player.moveUnits(frame, from, to, 0);

        to.setUnitCount(to.getUnitCount() + movingUnits);
        from.setUnitCount(from.getUnitCount() - movingUnits);

        Event.fireLogEvent(String.format("<li style=\"color:%s\">%s moved %d units from %s to %s</li>",
                player.getHexColor(),
                player.getName(),
                movingUnits,
                from.getName(),
                to.getName()
        ));
    }
}
