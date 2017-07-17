package risk;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This class retrieves information about Territories on the map. 
 * @author Connor
 */
public class Map {

    private Territory[] territories;
    private Continent[] continents;

    /**
     * Retrieve all the continents.
     * @return all the continents.
     */
    public Continent[] getContinents() {
        return continents;
    }

    /**
     * Set all the continents.
     * @param continents all the continents.
     */
    public void setContinents(Continent[] continents) {
        this.continents = continents;
    }

    /**
     * Retrieve all the territories.
     * @return all the territories.
     */
    public Territory[] getTerritories() {
        return territories;
    }

    /**
     * Set all the territories.
     * @param territories all the territories.
     */
    public void setTerritories(Territory[] territories) {
        this.territories = territories;
    }

    /**
     * Retrieve the adjacent territories to the given territory that the player
     * does not own.
     * @param territory the territory owned by the player.
     * @return A list of adjacent territories not owned by the player.
     */
    public List<Territory> getAdjacentTerritoriesNotOwnedByPlayer(Territory territory) {
        final ArrayList<Territory> adjacentUnownedTerritories = new ArrayList<>();

        for (Territory adjacent : territory.getAdjacentTerritories()) {
            if (!adjacent.getPlayer().equals(territory.getPlayer())) {
                adjacentUnownedTerritories.add(adjacent);
            }
        }

        return adjacentUnownedTerritories;
    }

    /**
     * Retrieve the territories that are directly or indirectly connected to the
     * given territory. through other territories the player occupies.
     * @param territory the source territory.
     * @return The territories directly or indirectly connected to the given
     * territory.
     */
    public List<Territory> getConnectedTerritories(Territory territory) {
        final ArrayList<Territory> connectedTerritories = new ArrayList<>();
        final Stack<Territory> stack = new Stack<>();
        stack.push(territory);

        while (!stack.isEmpty()) {
            final Territory connectedTerritory = stack.pop();

            for (Territory adjacent : connectedTerritory.getAdjacentTerritories()) {
                if (adjacent.getPlayer().equals(territory.getPlayer())
                        && !connectedTerritories.contains(adjacent)) {
                    connectedTerritories.add(adjacent);
                    stack.push(adjacent);
                }
            }
        }

        return connectedTerritories;
    }

    /**
     * Retrieve all the player territories.
     * @param player the player.
     * @return A list of all territories owned by the player.
     */
    public List<Territory> getPlayerTerritories(Player player) {
        final ArrayList<Territory> playerTerritories = new ArrayList<>();

        if (territories != null) {
            for (Territory territory : territories) {
                if (territory.getPlayer().equals(player)) {
                    playerTerritories.add(territory);
                }
            }
        }

        return playerTerritories;
    }

    /**
     * Retrieve the player territories that have more than one unit.
     * @return A list of territories that have more than one unit.
     */
    public List<Territory> getPlayerTerritoriesWithMultipleUnits(Player player) {
        final ArrayList<Territory> multipleUnitTerritories = new ArrayList<>();

        for (Territory territory : getPlayerTerritories(player)) {
            if (territory.getUnitCount() > 1) {
                multipleUnitTerritories.add(territory);
            }
        }

        return multipleUnitTerritories;
    }

    /**
     * Retrieve the player territories that have multiple units and at least one
     * adjacent territory owned by another player.
     * @param player The owner for the territories.
     * @return A list of candidates that can be used to attack from.
     */
    public List<Territory> getPlayerAttackSourceCandidates(Player player) {
        final ArrayList<Territory> attackSource = new ArrayList<>();

        for (Territory territory : getPlayerTerritoriesWithMultipleUnits(player)) {
            for (Territory adjacent : territory.getAdjacentTerritories()) {
                if (!adjacent.getPlayer().equals(player)) {
                    attackSource.add(territory);
                    break;
                }
            }
        }

        return attackSource;
    }

    /**
     * Retrieve the total number of units on the map.
     * @return the total number of units on the map.
     */
    public int getTotalUnitCount() {
        int count = 0;

        if (territories != null) {
            for (Territory territory : territories) {
                count += territory.getUnitCount();
            }
        }

        return count;
    }
}
