package risk.event;

import javax.swing.event.EventListenerList;
import risk.Player;
import risk.Territory;

/**
 * Controls the events used in Risk.
 */
public class Event {

    private static final EventListenerList LISTENER_LIST = new EventListenerList();

    public static void addLogListener(LogListener listener) {
        LISTENER_LIST.add(LogListener.class, listener);
    }

    public static void addModeListener(ModeListener listener) {
        LISTENER_LIST.add(ModeListener.class, listener);
    }

    public static void addPlayerListener(PlayerListener listener) {
        LISTENER_LIST.add(PlayerListener.class, listener);
    }

    public static void addTerritoryListener(TerritoryListener listener) {
        LISTENER_LIST.add(TerritoryListener.class, listener);
    }

    public static void addTurnListener(TurnListener listener) {
        LISTENER_LIST.add(TurnListener.class, listener);
    }

    public static void removeLogListener(LogListener listener) {
        LISTENER_LIST.remove(LogListener.class, listener);
    }

    public static void removeModeListener(ModeListener listener) {
        LISTENER_LIST.remove(ModeListener.class, listener);
    }

    public static void removePlayerListener(PlayerListener listener) {
        LISTENER_LIST.remove(PlayerListener.class, listener);
    }

    public static void removeTerritoryListener(TerritoryListener listener) {
        LISTENER_LIST.remove(TerritoryListener.class, listener);
    }

    public static void removeTurnListener(TurnListener listener) {
        LISTENER_LIST.add(TurnListener.class, listener);
    }

    public static void fireBeginAttackEvent(String name) {
        final Object[] listeners = LISTENER_LIST.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == TurnListener.class) {
                ((TurnListener) listeners[i + 1]).onBeginTurn(name);
            }
        }
    }

    public static void fireEndAttackEvent() {
        final Object[] listeners = LISTENER_LIST.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == TurnListener.class) {
                ((TurnListener) listeners[i + 1]).onEndTurn();
            }
        }
    }

    public static void fireInstructionEvent(Player player, String instructions) {
        final Object[] listeners = LISTENER_LIST.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == PlayerListener.class) {
                ((PlayerListener) listeners[i + 1]).onInstruction(player, instructions);
            }
        }
    }

    public static void fireLogEvent(String message) {
        final Object[] listeners = LISTENER_LIST.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == LogListener.class) {
                ((LogListener) listeners[i + 1]).onLog(message);
            }
        }
    }

    public static void fireModeEvent(int mode) {
        fireModeEvent(mode, null);
    }

    public static void fireModeEvent(int mode, Object object) {
        final Object[] listeners = LISTENER_LIST.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == ModeListener.class) {
                ((ModeListener) listeners[i + 1]).onMode(mode, object);
            }
        }
    }

    public static void fireTerritorySelectedEvent(Territory territory) {
        final Object[] listeners = LISTENER_LIST.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == TerritoryListener.class) {
                ((TerritoryListener) listeners[i + 1]).onSelected(territory);
            }
        }
    }

    public static void fireTerritoryUpdatedEvent(Territory territory) {
        final Object[] listeners = LISTENER_LIST.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == TerritoryListener.class) {
                ((TerritoryListener) listeners[i + 1]).onUpdated(territory);
            }
        }
    }
}
