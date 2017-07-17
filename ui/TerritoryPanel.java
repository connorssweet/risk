package risk.ui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import risk.Territory;
import risk.event.Event;
import risk.event.TerritoryListener;

public class TerritoryPanel extends JPanel implements TerritoryListener {

    private static final long serialVersionUID = 3314300539435216394L;
    private final JLabel territoryNameLabel;

    public TerritoryPanel() {
        territoryNameLabel = new JLabel();
        territoryNameLabel.setFont(new Font("Serif", Font.ITALIC, 24));
        add(territoryNameLabel);

        setPreferredSize(new Dimension(250, 100));
        Event.addTerritoryListener(this);
    }

    @Override
    public void onSelected(Territory territory) {
    }

    @Override
    public void onUpdated(Territory territory) {
        final String name = territory != null
                ? territory.getName()
                : "";

        territoryNameLabel.setText(name);
    }
}
