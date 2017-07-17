package risk.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import risk.ComputerPlayer;
import risk.HumanPlayer;
import risk.Player;

public class SetupDialog extends JDialog {

    private static final long serialVersionUID = 4648665767771450336L;
    private static final String[] playerTypes = {"Human", "Computer"};
    private final ArrayList<Player> players;

    public SetupDialog(JFrame parent, ArrayList<Player> players) {
        super(parent, "Welcome to Risk!", true);
        this.players = players;

        addWindowListener(new CloseWindowListener());

        setLayout(new GridBagLayout());

        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridwidth = 3;
        c.insets = new Insets(5, 10, 0, 10);
        add(new JLabel("Welcome to Connor Sweet's implementation of Risk. Please configure 2-6 players below"), c);
        c.gridy++;
        add(new JLabel("and click Start to begin playing."), c);
        c.gridy++;
        c.insets = new Insets(5, 10, 0, 10);
        c.gridwidth = 1;

        final JLabel playerName = new JLabel("Player Name");
        playerName.setPreferredSize(new Dimension(300, playerName.getHeight()));
        add(playerName, c);

        final JLabel playerType = new JLabel("Player Type");
        add(playerType, c);
        add(new JLabel(""), c);

        for (int i = 0; i < players.size(); i++) {
            c.gridy++;
            createPlayerRow(c, players.get(i));
        }

        c.gridy++;
        c.gridwidth = 3;
        c.insets = new Insets(5, 10, 10, 10);
        final JButton start = new JButton("Start");
        start.addActionListener(new StartGameListener());
        add(start, c);

        pack();
        setLocationRelativeTo(parent);
    }

    private void createPlayerRow(GridBagConstraints c, Player player) {
        final JTextField playerName = new JTextField(player.getName());
        playerName.getDocument().addDocumentListener(new NameChangeListener(playerName, player));
        add(playerName, c);

        final JComboBox<String> playerType = new JComboBox<>(playerTypes);
        playerType.setSelectedItem(player instanceof HumanPlayer ? "Human" : "Computer");
        playerType.addItemListener(new PlayerTypeListener(playerType, player));
        add(playerType, c);

        if (c.gridy > 4) {
            final JButton removeButton = new JButton("Remove");
            final Component[] components = new Component[]{playerName, playerType, removeButton};
            removeButton.addActionListener(new DeleteRowListener(components, player));
            add(removeButton, c);
        }
    }

    private int findPlayer(Player player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(player)) {
                return i;
            }
        }
        return -1;
    }

    private class NameChangeListener implements DocumentListener {

        private final JTextField field;
        private final Player player;

        public NameChangeListener(JTextField field, Player player) {
            this.field = field;
            this.player = player;
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateName();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateName();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateName();
        }

        private void updateName() {
            player.setName(field.getText());
        }
    }

    private class PlayerTypeListener implements ItemListener {

        private final JComboBox<String> playerType;
        private final Player player;

        public PlayerTypeListener(JComboBox<String> playerType, Player player) {
            this.playerType = playerType;
            this.player = player;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            int index = findPlayer(player);
            if (index >= 0) {
                final Player replacePlayer = playerType.getSelectedItem().equals("Human")
                        ? new HumanPlayer(player.getName(), player.getColor())
                        : new ComputerPlayer(player.getName(), player.getColor());
                players.set(index, replacePlayer);
            }
        }
    }

    private class DeleteRowListener implements ActionListener {

        private final Component[] components;
        private final Player player;

        public DeleteRowListener(Component[] components, Player player) {
            this.components = components;
            this.player = player;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            players.remove(player);
            for (Component component : components) {
                remove(component);
            }
            pack();
            revalidate();
            repaint();
        }
    }

    private class StartGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }

    private class CloseWindowListener implements WindowListener {

        @Override
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowOpened(WindowEvent e) {
        }
    }
}
