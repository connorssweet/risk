package risk.event;

import java.util.EventListener;

import risk.Territory;

/**
 * Listen for events related to a territory.
 * @author Connor
 */
public interface TerritoryListener extends EventListener {

    /**
     * The given territory has been selected.
     * @param territory the selected territory.
     */
    void onSelected(Territory territory);

    /**
     * The given territory has been updated.
     * @param territory the updated territory.
     */
    void onUpdated(Territory territory);
}
