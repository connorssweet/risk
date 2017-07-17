package risk;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;

/**
 * This is an abstract representation for a player.
 * @see HumanPlayer
 * @see ComputerPlayer
 * @author Connor
 */
public abstract class Player {

    private final Color color;
    private String name;
    private int unitsLost;
    private int unitsDefeated;

    /**
     * Constructor assigns name and color to Player instance.
     * @param name
     * @param color
     */
    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Access color of player.
     * @return color of the player.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Retrieve the HTML/hex representation for player's color.
     * @return the HTML/hex representation for player's color.
     */
    public String getHexColor() {
        return "#" + Integer.toHexString(getColor().getRGB()).substring(2);
    }

    /**
     * Retrieve the player's name.
     * @return the player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the player's name.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve the number of units lost.
     * @return the number of units lost.
     */
    public int getUnitsLost() {
        return unitsLost;
    }

    /**
     * Set the number of units lost.
     * @param unitsLost
     */
    public void setUnitsLost(int unitsLost) {
        this.unitsLost = unitsLost;
    }

    /**
     * Retrieve the number of units defeated.
     * @return the number of units defeated.
     */
    public int getUnitsDefeated() {
        return unitsDefeated;
    }

    /**
     * Set the number of units defeated.
     * @param unitsDefeated
     */
    public void setUnitsDefeated(int unitsDefeated) {
        this.unitsDefeated = unitsDefeated;
    }

    /**
     * Select a territory.
     * @param territoryList - the valid list of territories to select from.
     * @return The selected Territory.
     * @throws InterruptedException If the selection thread was interrupted.
     */
    public abstract Territory selectTerritory(List<Territory> territoryList) throws InterruptedException;

    /**
     * Move units from one location to another.
     * @param frame the main JFrame.
     * @param fromTerritory the territory to move units from.
     * @param toTerritory the territory to move units to.
     * @param minimumUnits the minimum number of units to move.
     * @return The number of units to move.
     */
    public abstract int moveUnits(
            JFrame frame,
            Territory fromTerritory,
            Territory toTerritory,
            int minimumUnits);
}
