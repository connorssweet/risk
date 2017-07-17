package risk.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import risk.Player;
import risk.Territory;
import risk.event.Event;
import risk.event.Mode;
import risk.event.ModeListener;

/**
 * The panel responsible for drawing each Risk territory on the map.
 */
public class MapPanel extends JPanel implements ModeListener {

    private static final long serialVersionUID = -3403820652198091577L;
    private static final String BACKGROUND_IMAGE = "../asset/background.png";
    private static final Color SELECTED_TERRITORY_COLOR = Color.WHITE;
    private static final Color NEUTRAL_TERRITORY_COLOR = Color.LIGHT_GRAY;

    private final BufferedImage background;
    private TerritoryShape[] territories;
    private Territory hoverTerritory;
    private int selectionMode = Mode.DISABLE_HIGHLIGHT_MODE;
    private Player selectedPlayer;

    public MapPanel() {
        try {
            final InputStream inputStream = getClass().getResourceAsStream(BACKGROUND_IMAGE);
            background = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error reading map from " + BACKGROUND_IMAGE, e);
        }

		// setting a null layout allows positioning unit counts at
        // absolute x,y coordinates
        setLayout(null);

        addMouseMotionListener(new HoverListener());
        addMouseListener(new TerritoryClickedListener());
        setBounds(0, 0, background.getWidth(), background.getHeight());
        Event.addModeListener(this);
    }

    /**
     * Set the territories this panel is responsible for drawing.
     *
     * @param territories The known territories.
     */
    public void setTerritories(TerritoryShape[] territories) {
        this.territories = territories;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);

        for (TerritoryShape shape : territories) {
            g.setColor(getTerritoryColor(shape.getTerritory()));
            g.fillPolygon(shape);

            applyTerritorySelection(g, shape);

            g.setColor(Color.BLACK);
            g.drawPolygon(shape);
        }
    }

    /**
     * Apply the territory selection, based on the selection mode, to the
     * current territory, if applicable.
     *
     * @param g The graphics object.
     * @param shape The object being painted.
     */
    private void applyTerritorySelection(Graphics g, TerritoryShape shape) {
        switch (selectionMode) {

            case Mode.HIGHLIGHT_OWNED_TERRITORY_SELECTION_MODE:
                if (shape.getTerritory().getPlayer().equals(selectedPlayer)
                        && shape.getTerritory().equals(hoverTerritory)) {
                    paintSelectedTerritory(g);
                }
                break;

            case Mode.HIGHLIGHT_ADJACENT_TERRITORY_SELECTION_MODE:
                if (hoverTerritory != null) {
                    for (Territory adjacentTerritory : hoverTerritory.getAdjacentTerritories()) {
                        if (shape.getTerritory().equals(adjacentTerritory)) {
                            paintAdjacentTerritory(g, adjacentTerritory);
                            break;
                        }
                    }
                }

                break;

            case Mode.HIGHLIGHT_UNOCCUPIED_TERRITORY_SELECTION_MODE:
                if (shape.getTerritory().getPlayer() == null
                        && shape.getTerritory().equals(hoverTerritory)) {
                    paintSelectedTerritory(g);
                }
                break;
        }
    }

    /**
     * Paint the selected territory using the defined color.
     *
     * @param g The graphics object.
     */
    private void paintSelectedTerritory(Graphics g) {
        g.setColor(SELECTED_TERRITORY_COLOR);
        paintTerritory(g, hoverTerritory);
    }

    /**
     * Paint the adjacent territory using the darkened player color.
     *
     * @param g The graphics object.
     * @param adjacentTerritory The territory adjacent to the selected
     * territory.
     */
    private void paintAdjacentTerritory(Graphics g, Territory adjacentTerritory) {
        Color color = getTerritoryColor(adjacentTerritory)
                .darker().darker();
        g.setColor(color);
        paintTerritory(g, adjacentTerritory);
    }

    /**
     * Paint the territory by filling each of the polygons that make the
     * territory.
     *
     * @param g The graphics object.
     * @param territory The territory to be painted.
     */
    private static void paintTerritory(Graphics g, Territory territory) {
        for (Polygon polygon : territory.getShapes()) {
            g.fillPolygon(polygon);
        }
    }

    /**
     * Retrieve the color of the given territory
     *
     * @param territory the territory
     * @return The color of the given territory, or a neutral color if the
     * territory is neutral.
     */
    private static Color getTerritoryColor(Territory territory) {
        final Player player = territory.getPlayer();
        if (player == null) {
            return NEUTRAL_TERRITORY_COLOR;
        }

        return player.getColor();
    }

    /**
     * A listener for changing the color of a territory when the mouse is
     * hovering over it by marking the territory as the hoverTerritory.
     */
    private class HoverListener implements MouseMotionListener {

        private Territory lastHoverTerritory;

        @Override
        public void mouseMoved(MouseEvent e) {
            hoverTerritory = null;
            for (TerritoryShape shape : territories) {
                if (shape.contains(e.getX(), e.getY())) {
                    hoverTerritory = shape.getTerritory();
                    break;
                }
            }

            if (hoverTerritory != null) {
                Event.fireTerritoryUpdatedEvent(hoverTerritory);
            }

            /**
             * Repaint the map when the hover moves to another territory
             */
            if (lastHoverTerritory != hoverTerritory) {
                lastHoverTerritory = hoverTerritory;
                repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }
    }

    /**
     * s	* This class handles mouse click interaction on the map panel.
     */
    private class TerritoryClickedListener implements MouseListener {

        @Override
        public void mousePressed(MouseEvent e) {
            Territory clickedTerritory = null;
            for (TerritoryShape shape : territories) {
                if (shape.contains(e.getX(), e.getY())) {
                    clickedTerritory = shape.getTerritory();
                    break;
                }
            }

            if (clickedTerritory != null) {
                Event.fireTerritorySelectedEvent(clickedTerritory);
                repaint();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }

    @Override
    public void onMode(int mode, Object object) {
        switch (mode) {
            case Mode.DISABLE_HIGHLIGHT_MODE:
            case Mode.HIGHLIGHT_ADJACENT_TERRITORY_SELECTION_MODE:
            case Mode.HIGHLIGHT_UNOCCUPIED_TERRITORY_SELECTION_MODE:
                selectionMode = mode;
                break;

            case Mode.HIGHLIGHT_OWNED_TERRITORY_SELECTION_MODE:
                selectionMode = mode;
                selectedPlayer = (Player) object;
        }

    }
}
