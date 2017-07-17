package risk;

import risk.ui.TerritoryShape;

/**
 * This class represents a territory on the Risk map.
 * @author Connor
 */
public class Territory {

    private final String name;
    private final Continent continent;
    private final TerritoryShape[] shapes;
    private int unitCount;
    private Territory[] adjacentTerritories;
    private Player player;

    public Territory(String name, Continent continent, TerritoryShape[] shapes) {
        this.name = name;
        this.continent = continent;
        this.unitCount = 0;
        this.shapes = shapes;
    }

    /**
     * Retrieve the territory name.
     * @return the territory name.
     */
    public String getName() {
        return name;
    }

    public Continent getContinent() {
        return continent;
    }

    /**
     * Retrieve the unit count.
     * @return the unit count.
     */
    public int getUnitCount() {
        return unitCount;
    }

    /**
     * Set the unit count.
     * @param count the unit count.
     */
    public void setUnitCount(int count) {
        this.unitCount = count;
    }

    /**
     * Retrieve the territory shapes that visually define the territory.
     * @return the territory shapes that visually define the territory.
     */
    public TerritoryShape[] getShapes() {
        return shapes;
    }

    /**
     * Set the territories that are adjacent to this territory.
     * @param adjacentTerritories the territories that are adjacent to this
     * territory.
     */
    public void setAdjacentTerritories(Territory[] adjacentTerritories) {
        this.adjacentTerritories = adjacentTerritories;
    }

    /**
     * Retrieve territories that are adjacent to this territory.
     * @return territories that are adjacent to this territory.
     */
    public Territory[] getAdjacentTerritories() {
        return adjacentTerritories;
    }

    /**
     * Retrieve the player that occupies this territory.
     * @return the player that occupies this territory.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set the player that occupies this territory.
     * @param player the player that occupies this territory.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}
