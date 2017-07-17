package risk.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import risk.Player;
import risk.event.Event;
import risk.event.PlayerListener;

public class InstructionPanel extends JPanel implements PlayerListener {

    private static final long serialVersionUID = 4084741508428077966L;
    private final JTextPane instructionPane;

    public InstructionPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createLoweredBevelBorder()));

        instructionPane = new JTextPane();
        instructionPane.setContentType("text/html");
        add(instructionPane, BorderLayout.CENTER);

        setPreferredSize(new Dimension(250, 100));
        Event.addPlayerListener(this);
    }

    @Override
    public void onInstruction(Player player, String instructions) {
        instructionPane.setText(String.format(
                "<html><body style=\"text-align:center;color:%s\"><h2 style=\"margin:2px\">%s</h2><p>%s</p></body></html>",
                player.getHexColor(),
                player.getName(),
                instructions
        ));
    }
}
