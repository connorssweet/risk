package risk;

/**
 * This class represents a continent on the Risk map.
 * @author Connor
 */
public class Continent {

    private final String name;
    private final int bonusUnits;
    private Territory[] territories;

    /**
     * Constructor assigns passed name and amount of bonus units from the read
     * file to the continent.
     * @param name
     * @param bonusUnits
     */
    public Continent(String name, int bonusUnits) {
        this.name = name;
        this.bonusUnits = bonusUnits;
    }

    /**
     * Retrieve the continent name.
     * @return the continent name.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve the bonus army value for controlling all territories on the
     * continent.
     * @return the bonus army value.
     */
    public int getBonusUnits() {
        return bonusUnits;
    }

    /**
     * Set the territories in the continent.
     * @param territories the territories in the continent.
     */
    public void setTerritories(Territory[] territories) {
        this.territories = territories;
    }

    /**
     * Determine whether the continent is controlled by the player.
     * @param player the player.
     * @return True if the player controls the continent, otherwise False.
     */
    public boolean isControlledByPlayer(Player player) {
        if (territories == null) {
            return false;
        }

        for (Territory territory : territories) {
            if (!territory.getPlayer().equals(player)) {
                return false;
            }
        }

        return true;
    }
}
