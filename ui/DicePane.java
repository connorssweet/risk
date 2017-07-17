package risk.ui;

import java.awt.Color;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import risk.Dice;
import risk.event.Event;
import risk.event.Mode;
import risk.event.ModeListener;

public class DicePane extends JLayeredPane implements ModeListener {

    private static final long serialVersionUID = -1684733345381690827L;
    private static final int NUMBER_OF_IMAGES = 7;
    private final ImageIcon[] diceImages = new ImageIcon[NUMBER_OF_IMAGES];
    private final JLabel dice[] = new JLabel[NUMBER_OF_IMAGES];

    public DicePane() {
        setBounds(0, 450, 225, 380);
        loadDiceImages();
        add(createAttackerPanel());
        add(createDefenderPanel());
        setVisible(true);
        Event.addModeListener(this);
    }

    private JPanel createAttackerPanel() {
        final JPanel panel = new JPanel();
        panel.setBounds(0, 0, 210, 95);
        final TitledBorder titledBorder = BorderFactory.createTitledBorder("Attacker");
        titledBorder.setTitleColor(Color.WHITE);
        panel.setBorder(titledBorder);

        final int width = dice[0].getWidth();
        final int height = dice[0].getHeight();
        for (int i = 0; i < 3; i++) {
            dice[i].setBounds(10 + (i * width), 20, width, height);
            add(dice[i]);
        }

        panel.setOpaque(false);
        panel.setVisible(true);
        return panel;
    }

    private JPanel createDefenderPanel() {
        final JPanel panel = new JPanel();
        panel.setBounds(0, 100, 210, 95);

        final TitledBorder titledBorder = BorderFactory.createTitledBorder("Defender");
        titledBorder.setTitleColor(Color.WHITE);
        panel.setBorder(titledBorder);

        final int width = dice[0].getWidth();
        final int height = dice[0].getHeight();
        final int x = 10 + width / 2;

        dice[4].setBounds(x, 120, width, height);
        dice[5].setBounds(x + width, 120, width, height);
        add(dice[4]);
        add(dice[5]);
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Load the dice images
     */
    private void loadDiceImages() {
        for (int i = 0; i < NUMBER_OF_IMAGES; i++) {
            final String path = String.format("../asset/d%d.png", i);
            try {
                diceImages[i] = new ImageIcon(ImageIO.read(getClass().getResourceAsStream(path)));
                dice[i] = new JLabel();
                dice[i].setIcon(diceImages[0]);
                dice[i].setOpaque(false);
                dice[i].setBounds(10, 20, 64, 64);
            } catch (IOException e) {
                throw new RuntimeException("Error reading dice image", e);
            }
        }
    }

    @Override
    public void onMode(int mode, Object object) {
        if (mode != Mode.DICE_ROLL_MODE) {
            return;
        }

        final Dice[] attackDefend = (Dice[]) object;
        final Dice attackerDice = attackDefend[0];
        final Dice defenderDice = attackDefend[1];

        for (int i = 0; i < dice.length; i++) {
            dice[i].setVisible(false);
        }

        for (int i = 0; i < attackerDice.getSize(); i++) {
            dice[i].setIcon(diceImages[attackerDice.get(i)]);
            dice[i].setVisible(true);
        }

        for (int i = 0; i < defenderDice.getSize(); i++) {
            dice[i + 4].setIcon(diceImages[defenderDice.get(i)]);
            dice[i + 4].setVisible(true);
        }
    }
}
