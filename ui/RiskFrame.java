package risk.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import risk.ComputerPlayer;
import risk.GameStartException;
import risk.HumanPlayer;
import risk.Map;
import risk.MapBuilder;
import risk.Player;
import risk.state.Turn;

public class RiskFrame extends JFrame {

    private static final long serialVersionUID = -1457552393647621798L;
    private final Map map;
    private final ArrayList<Player> players;
    private final MapPanel mapPanel;
    private final StatusPanel statusPanel;
    private final MapBuilder mapBuilder;
    private final DicePane battlePane;
    private Turn turn;

    public RiskFrame() {
        map = new Map();
        players = new ArrayList<>();
        mapPanel = new MapPanel();
        statusPanel = new StatusPanel();
        mapBuilder = new MapBuilder(map, mapPanel, statusPanel);
        battlePane = new DicePane();

        try {
            mapBuilder.build();
        } catch (GameStartException e) {
            System.out.println("Error starting game: " + e.getMessage());
        }

        final JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(mapPanel.getPreferredSize());
        layeredPane.add(mapPanel, JLayeredPane.DEFAULT_LAYER, 0);
        layeredPane.add(battlePane, JLayeredPane.DEFAULT_LAYER + 1, 0);

        setBounds(0, 0, 1280, 960);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(layeredPane, BorderLayout.PAGE_START);
        add(statusPanel, BorderLayout.LINE_START);
        pack();
        setVisible(true);

        setupPlayers();
        final SetupDialog setupDialog = new SetupDialog(this, players);
        setupDialog.setVisible(true);
    }

    public Map getMap() {
        return map;
    }

    public MapBuilder getMapBuilder() {
        return mapBuilder;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public void repaint() {
        mapPanel.repaint();
    }

    private void setupPlayers() {
        players.add(new HumanPlayer("Red", Color.decode("#e42426")));
        players.add(new ComputerPlayer("Blue", Color.decode("#2072b2")));
        players.add(new ComputerPlayer("Yellow", Color.decode("#f1e70d")));
        players.add(new ComputerPlayer("Green", Color.decode("#0a905d")));
        players.add(new ComputerPlayer("Orange", Color.decode("#f28f20")));
        players.add(new ComputerPlayer("Purple", Color.decode("#6e398d")));
    }
}
