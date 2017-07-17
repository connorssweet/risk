package risk;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import risk.event.Event;
import risk.event.TerritoryListener;
import risk.event.TurnListener;

/**
 * The TerritorySelector is used to select a territory on the map.
 * @author Connor
 */
public class TerritorySelector implements TerritoryListener, TurnListener {

    private final CountDownLatch latch;
    private Territory selected;
    private volatile boolean keepSelecting;

    /**
     * Constructor assigns values of latch and keepSelecting
     */
    public TerritorySelector() {
        latch = new CountDownLatch(1);
        keepSelecting = true;
    }

    /**
     * This is used to select a territory as a player
     * @param player The player claiming the territory
     * @param territoryList The valid list of territories to choose from
     * @return The selected territory.
     * @throws InterruptedException If the game was interrupted
     */
    public Territory selectTerritory(Player player, List<Territory> territoryList) throws InterruptedException {
        // subscribe the events that we want a callback from
        Event.addTerritoryListener(this);
        Event.addTurnListener(this);

        // ensure that the selected territory is in the list
        while (keepSelecting && (selected == null || !territoryList.contains(selected))) {

            // Wait for the player to select a territory
            latch.await();
        }

        // unsubscribe from the events because our territory is selected
        Event.removeTurnListener(this);
        Event.removeTerritoryListener(this);

        return selected;
    }

    /**
     * When a territory is selected, it is assigned to selected and sets off the 
     * count down latch
     * @param territory 
     */
    @Override
    public void onSelected(Territory territory) {
        selected = territory;
        latch.countDown();
    }

    @Override
    public void onUpdated(Territory territory) {
    }

    @Override
    public void onBeginTurn(String name) {
    }

    /**
     * User is no longer allowed to select territories at the end of turn and 
     * count down latch is set off
     */
    @Override
    public void onEndTurn() {
        // stop waiting because the user is finished afterall
        keepSelecting = false;
        latch.countDown();
    }
}
