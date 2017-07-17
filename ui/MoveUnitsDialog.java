package risk.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JSlider;

/**
 * This dialog determines how many units the player wants to move from one
 * territory to another.
 */
public class MoveUnitsDialog extends JDialog implements ActionListener {

    private final JSlider slider;
    private final JButton moveButton;

    public MoveUnitsDialog(JFrame parent, String title, int minUnits, int maxUnits) {
        super(parent, title, true);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(400, 120));

        slider = new JSlider(minUnits, maxUnits, maxUnits);
        slider.setAlignmentX(Component.CENTER_ALIGNMENT);
        slider.setAlignmentY(Component.TOP_ALIGNMENT);
        slider.setBounds(0, 0, 400, 100);
        slider.setMajorTickSpacing(maxUnits-minUnits);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        add(slider);

        // space between the two vertical controls
        add(Box.createRigidArea(new Dimension(0, 10)));

        moveButton = new JButton("Move Units");
        moveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        moveButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        moveButton.addActionListener(this);
        add(moveButton);

        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Retrieve the number of units that the user wants to move.
     *
     * @return the number of units to move.
     */
    public int getUnits() {
        return slider.getValue();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }
}
