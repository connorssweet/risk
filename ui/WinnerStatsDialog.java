package risk.ui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import risk.Player;

/**
 * This dialog is used for displaying the winner's statistics at the end of the
 * game.
 */
public class WinnerStatsDialog extends JDialog {

    private static final long serialVersionUID = 9066903745961796498L;

    public WinnerStatsDialog(JFrame parent, String title, Player winner, int turns) {
        super(parent, title, true);

        // space between the two vertical controls
        add(Box.createRigidArea(new Dimension(0, 10)));

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(400, 120));

        final JLabel turnsLabel = new JLabel(
                String.format("You won in %d turns", turns));
        turnsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        add(turnsLabel);

        // space between the two vertical controls
        add(Box.createRigidArea(new Dimension(0, 10)));

        final JLabel unitsDefeatedLabel = new JLabel(
                String.format("You defeated %d units",
                        winner.getUnitsDefeated()));
        unitsDefeatedLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        add(unitsDefeatedLabel);

        // space between the two vertical controls
        add(Box.createRigidArea(new Dimension(0, 10)));

        final JLabel unitsLostLabel = new JLabel(
                String.format("You lost %d units",
                        winner.getUnitsLost()));
        unitsLostLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        add(unitsLostLabel);

        pack();
        setLocationRelativeTo(parent);
    }
}
