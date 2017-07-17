package risk.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import risk.event.Event;

public class EndTurnPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = -9171484482307050783L;
    private final JButton endTurnButton;

    public EndTurnPanel() {
        setLayout(new BorderLayout());

        endTurnButton = new JButton();
        endTurnButton.addActionListener(this);
        add(endTurnButton, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Event.fireEndAttackEvent();
    }

    public void setTurnName(String name) {
        endTurnButton.setText("End " + name);
    }
}
