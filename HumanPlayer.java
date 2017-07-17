package risk;

import java.awt.Color;
import java.util.List;
import javax.swing.JFrame;
import risk.ui.MoveUnitsDialog;

/**
 * This class represents a human controlled player.
 * @author Connor
 */
public class HumanPlayer extends Player {

    /**
     * Constructor assigns passed name and value to superclass Player.
     * @param name
     * @param color
     */
    public HumanPlayer(String name, Color color) {
        super(name, color);
    }

    /**
     * Instantiates TerritorySelector and returns territory selected by player.
     * @param territoryList
     * @return territory from TerritorySelector pertaining to the player.
     * @throws InterruptedException
     */
    @Override
    public Territory selectTerritory(List<Territory> territoryList) throws InterruptedException {
        final TerritorySelector selector = new TerritorySelector();
        return selector.selectTerritory(this, territoryList);
    }

    /**
     * Prompts player to decide how many units to move to the selected territory.
     * @param frame
     * @param fromTerritory
     * @param toTerritory
     * @param minUnits
     * @return number of units requested to be sent to selected territory.
     */
    @Override
    public int moveUnits(JFrame frame, Territory fromTerritory, Territory toTerritory, int minUnits) {
        //title of dialog
        final String title = String.format("Move from %s to %s", fromTerritory.getName(), toTerritory.getName());
        
        final int maxMovableUnits = fromTerritory.getUnitCount() - 1;
        
        //instantiate MoveUnitsDialog
        final MoveUnitsDialog dialog = new MoveUnitsDialog(frame, title, minUnits, maxMovableUnits);
        dialog.setVisible(true);

        return dialog.getUnits();
    }
}
