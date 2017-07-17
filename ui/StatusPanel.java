package risk.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import risk.event.Event;
import risk.event.TurnListener;

public class StatusPanel extends JPanel implements TurnListener {

    private static final long serialVersionUID = -6024282405473180167L;
    private final EndTurnPanel endTurnPanel;

    public StatusPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(new Dimension(1265, 100));

        add(new TerritoryPanel(), BorderLayout.WEST);
        add(new InstructionPanel(), BorderLayout.NORTH);
        add(new LogPanel(), BorderLayout.EAST);

        endTurnPanel = new EndTurnPanel();
        endTurnPanel.setVisible(false);
        add(endTurnPanel);

        Event.addTurnListener(this);
    }

    @Override
    public void onBeginTurn(String name) {
        endTurnPanel.setTurnName(name);
        endTurnPanel.setVisible(true);
    }

    @Override
    public void onEndTurn() {
        endTurnPanel.setVisible(false);
    }
}
