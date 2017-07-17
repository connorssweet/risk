package risk.state;

import java.util.List;

import risk.Continent;
import risk.Map;
import risk.Player;
import risk.Territory;
import risk.event.Event;
import risk.event.Mode;
import risk.ui.RiskFrame;

/**
 * This state represents the placement of units at the beginning of each
 * player's turn.
 */
public class Placement implements GameState {

    private final RiskFrame frame;
    private final Map map;
    private final Player player;

    public Placement(RiskFrame frame) {
        this.frame = frame;
        this.map = frame.getMap();
        this.player = frame.getTurn().getPlayer();
    }

    @Override
    public GameState play() throws InterruptedException {
        int unitsToPlace
                = getTerritoryOccupationUnitCount()
                + getContinentBonus();

        final List<Territory> playerTerritories = map.getPlayerTerritories(player);

        Event.fireModeEvent(Mode.HIGHLIGHT_OWNED_TERRITORY_SELECTION_MODE, player);

        for (int i = unitsToPlace; i > 0; --i) {
            Event.fireInstructionEvent(player, "You have " + i + " remaining units to place.");
            final Territory selected = player.selectTerritory(playerTerritories);
            selected.setUnitCount(selected.getUnitCount() + 1);
            writePlacement(player, selected);
            frame.repaint();
        }

        Event.fireModeEvent(Mode.DISABLE_HIGHLIGHT_MODE);

        return new Attack(frame);
    }

    /**
     * According to the risk rules:
     *
     * At the beginning of every turn (including your first), count the number
     * of territories you currently occupy, then divide the total by three
     * (ignore any fraction). The answer is the number of armies you receive.
     * Place the new armies on any territory you already occupy. Example: 11
     * territories = 3 armies 14 territories = 4 armies You will always receive
     * at least 3 armies on a turn, even if you occupy fewer than 9 territories.
     * @return the number of units related to the number of territories the
     * player occupies.
     */
    private int getTerritoryOccupationUnitCount() {
        final int territoryCount = map.getPlayerTerritories(player).size() / 3;
        return Math.max(territoryCount, 3);
    }

    /**
     * According to the risk rules:
     * In addition, at the beginning of your turn you will receive armies for
     * each continent you control. (To control a continent, you must occupy all
     * its territories at the start of your turn.) To find the exact number of
     * armies you�ll receive for each continent, look at the chart in the lower
     * left-hand corner of the game board.
     * @return the number of units related to the continents the player
     * controls.
     */
    private int getContinentBonus() {
        int bonusUnits = 0;
        for (Continent continent : map.getContinents()) {
            if (continent.isControlledByPlayer(player)) {
                bonusUnits += continent.getBonusUnits();
            }
        }

        return bonusUnits;
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
