package risk.state;

import java.util.ArrayList;
import java.util.List;

import risk.Dice;
import risk.Map;
import risk.Player;
import risk.Territory;
import risk.event.Event;
import risk.event.Mode;
import risk.event.TurnListener;
import risk.ui.RiskFrame;

/**
 * This is the attack state where players decide to invade other territories.
 */
public class Attack implements GameState, TurnListener {

    private static final int MAX_ATTACKER_DICE = 3;
    private static final int MAX_DEFENDER_DICE = 2;
    private final RiskFrame frame;
    private final Map map;
    private final Player player;
    private final ArrayList<Player> players;
    private volatile boolean keepAttacking;

    public Attack(RiskFrame frame) {
        this.frame = frame;
        this.map = frame.getMap();
        this.player = frame.getTurn().getPlayer();
        this.players = frame.getPlayers();
        keepAttacking = true;
    }

    @Override
    public GameState play() throws InterruptedException {
        Event.addTurnListener(this);
        Event.fireBeginAttackEvent("Attack");

        while (keepAttacking && players.size() > 1) {
            final List<Territory> validFrom = map.getPlayerAttackSourceCandidates(player);
            if (validFrom.isEmpty()) {
                Event.fireLogEvent(String.format(
                        "<li style=\"color:%s\">%s does not have enough units to attack another territory</li>",
                        player.getHexColor(),
                        player.getName()
                ));
                break;
            }

            Event.fireModeEvent(Mode.HIGHLIGHT_OWNED_TERRITORY_SELECTION_MODE, player);
            Event.fireInstructionEvent(player, "Please select a territory to attack from or click 'End Attack'");
            final Territory from = player.selectTerritory(validFrom);
            if (from == null) {
                continue;
            }

            Event.fireModeEvent(Mode.HIGHLIGHT_ADJACENT_TERRITORY_SELECTION_MODE);
            Event.fireInstructionEvent(player, "Please select a territory to attack.");
            final Territory to = player.selectTerritory(map.getAdjacentTerritoriesNotOwnedByPlayer(from));
            if (to == null) {
                continue;
            }
            attack(from, to);
            frame.repaint();
        }

        Event.fireModeEvent(Mode.DISABLE_HIGHLIGHT_MODE);
        Event.fireEndAttackEvent();
        Event.removeTurnListener(this);

        return new Fortify(frame);
    }

    private void attack(Territory from, Territory to) {
        final Dice[] dice = new Dice[]{
            new Dice(getMaxAttackerDice(from)),
            new Dice(getMaxDefenderDice(to))
        };

        // Determine how many units were lost and adjust the territories
        final int[] loss = dice[0].compareDice(dice[1]);
        from.setUnitCount(from.getUnitCount() - loss[0]);
        to.setUnitCount(to.getUnitCount() - loss[1]);

        // Add the wins/losses for each player
        from.getPlayer().setUnitsLost(from.getPlayer().getUnitsLost() + loss[0]);
        from.getPlayer().setUnitsDefeated(from.getPlayer().getUnitsDefeated() + loss[1]);
        to.getPlayer().setUnitsLost(to.getPlayer().getUnitsLost() + loss[1]);
        to.getPlayer().setUnitsDefeated(to.getPlayer().getUnitsDefeated() + loss[0]);

        Event.fireModeEvent(Mode.DICE_ROLL_MODE, dice);

        Event.fireLogEvent(String.format(
                "<li style=\"color:%s\">%s lost %d units attacking %s from %s.  <span style=\"color:%s\">%s lost %d units.</span></li>",
                player.getHexColor(),
                player.getName(),
                loss[0],
                to.getName(),
                from.getName(),
                to.getPlayer().getHexColor(),
                to.getPlayer().getName(),
                loss[1]
        ));

        if (to.getUnitCount() <= 0) {
            final Player lostTerritoryPlayer = to.getPlayer();
            to.setPlayer(from.getPlayer());
            frame.repaint();

            final int movingUnits = player.moveUnits(frame, from, to, 1);
            from.setUnitCount(from.getUnitCount() - movingUnits);
            to.setUnitCount(movingUnits);

            checkPlayerEliminated(lostTerritoryPlayer);
        }
    }

    private void checkPlayerEliminated(Player eliminatedPlayer) {
        if (map.getPlayerTerritories(eliminatedPlayer).size() == 0) {
            Event.fireLogEvent(String.format(
                    "<li style=\"color:%s\">%s eliminated <span style=\"color:%s\">%s</span></li>",player.getHexColor(),player.getName(),eliminatedPlayer.getHexColor(), eliminatedPlayer.getName(), 0));

            players.remove(eliminatedPlayer);
        }
    }

    /**
     * Retrieve the maximum number of dice the attacker can use. The attacker
     * will roll 1, 2 or 3 dice. The attacker must have at least one more army
     * in the territory than the number of dice rolled. The more dice rolled,
     * the greater the odds of winning. Yet the number of armies that can be
     * lost is greater.
     *
     * @param territory The territory attacking from.
     * @return The maximum number of attacking dice.
     */
    private int getMaxAttackerDice(Territory territory) {
        return Math.min(territory.getUnitCount() - 1, MAX_ATTACKER_DICE);
    }

    /**
     * Retrieve the maximum number of dice the defender can use. The defender
     * will roll either 1 or 2 dice. Rolling 2 dice requires a territory with at
     * least 2 units on the territory under attack. The more dice the defender
     * rolls, the greater the odds of winning, but more armies can be lost.
     *
     * @param territory The territory defending from attack.
     * @param attackerDice
     * @return The maximum number of defending dice.
     */
    private int getMaxDefenderDice(Territory territory) {
        return Math.min(territory.getUnitCount(), MAX_DEFENDER_DICE);
    }

    @Override
    public void onBeginTurn(String name) {
        keepAttacking = true;
    }

    @Override
    public void onEndTurn() {
        keepAttacking = false;
    }
}
