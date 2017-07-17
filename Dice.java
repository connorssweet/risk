package risk;

import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * This class represents the dice used in during the attacking phase.
 * @author Connor
 */
public class Dice {

    private final ArrayList<Integer> dice = new ArrayList<>();

    /**
     * Constructor adds dice equal to the value of the parameter to the dice
     * ArrayList.
     * @param numberOfDice
     */
    public Dice(int numberOfDice) {
        for (int i = 0; i < numberOfDice; i++) {
            dice.add(rollDie());
        }
        sortDice();
    }
    
    /**
     * Accesses the Integer value at a given position in the dice ArrayList.
     * @param index
     * @return the Integer value of dice at the index parameter.
     */
    public Integer get(int index) {
        return dice.get(index);
    }

    /**
     * Accesses the getSize of the dice ArrayList.
     * @return Size of dice.
     */
    public int getSize() {
        return dice.size();
    }

    /**
     * Roll a random die between 1 and 6 inclusive.
     * @return A random number between 1 and 6 inclusive.
     */
    private int rollDie() {
        final SecureRandom random = new SecureRandom();
        return random.nextInt(6 - 1) + 1;
    }

    /**
     * Selection sort the dice from highest to lowest.
     */
    private void sortDice() {
        for (int index = 0; index < dice.size(); index++) {
            for (int subIndex = index; subIndex < dice.size(); subIndex++) {
                if (dice.get(subIndex).compareTo((dice.get(index))) > 0) {
                    final Integer temp = dice.get(index);
                    dice.set(index, dice.get(subIndex));
                    dice.set(subIndex, temp);
                }
            }
        }
    }
    
    /**
     * Compares the values of the dice rolled and increments loss of troops in 
     * fighting armies.
     * @param defender
     * @return amount of troops lost on each side.
     */
    public int[] compareDice(Dice defender) {
        final int[] loss = new int[]{0, 0}; // attackers, defenders
        //Sets number of dice to the size of the team with the least units
        final int numberOfDice = defender.getSize() <= getSize()
                ? defender.getSize()
                : getSize();
        //Determines which team loses units when comparing dice
        for (int roll = 0; roll < numberOfDice; roll++) {
            if (defender.get(roll).compareTo(get(roll)) >= 0) {
                loss[0]++;
            } else {
                loss[1]++;
            }
        }
        return loss;
    }
}
