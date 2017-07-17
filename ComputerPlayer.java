package risk;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;

/**
 * This class represents a computer controlled player.
 * @author Connor
 */
public class ComputerPlayer extends Player {

    /**
     * Constructor sends name and color of instantiated computer player to 
     * Player superclass.
     * player class
     * @param name
     * @param color
     */
    public ComputerPlayer(String name, Color color) {
        super(name, color);
    }

    /**
     * Selects random unclaimed territory of the options the computer may choose
     * to attack.
     * @param unclaimed
     * @return random unclaimed territory the computer will attack
     * @throws InterruptedException
     */
    @Override
    public Territory selectTerritory(List<Territory> unclaimed) throws InterruptedException {
        final int random = new Random().nextInt(unclaimed.size());
        return unclaimed.get(random);
    }

    /**
     * Sets the maximum amount of computer units to move to the selected
     * territory.
     * @param frame
     * @param fromTerritory
     * @param toTerritory
     * @param minimumUnits
     * @return maximum amount of units the computer will move
     */
    @Override
    public int moveUnits(JFrame frame, Territory fromTerritory, Territory toTerritory, int minimumUnits) {
        final int maxMovableUnits = fromTerritory.getUnitCount() - 1;
        return maxMovableUnits;
    }
}
