package risk;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import risk.ui.MapPanel;
import risk.ui.StatusPanel;
import risk.ui.TerritoryShape;
import risk.ui.UnitCountLabel;

/**
 * This class builds and links Territories and sets up UI panels
 *
 * @author Connor
 */
public class MapBuilder {

    private static final String CONTINENT_DATA_FILENAME = "continent.dat";
    private static final String TERRITORY_DATA_FILENAME = "territory.dat";
    private static final String TERRITORY_PATH = "asset/territory/";
    private final Map map;
    private final MapPanel mapPanel;
    private final HashMap<String, Continent> continents;
    private final HashMap<String, Territory> territories;
    private final HashMap<String, String> adjacency;
    private final HashMap<String, Point> unitOffsets;

    public MapBuilder(Map map, MapPanel mapPanel, StatusPanel statusPanel) {
        this.map = map;
        this.mapPanel = mapPanel;
        continents = new HashMap<>();
        territories = new HashMap<>();
        adjacency = new HashMap<>();
        unitOffsets = new HashMap<>();
    }

    public void build() throws GameStartException {
        loadContinents();
        loadTerritories();
        linkAdjacentTerritories();
        linkContinentTerritories();

        map.setContinents(continents.values().toArray(new Continent[continents.size()]));
        map.setTerritories(territories.values().toArray(new Territory[territories.size()]));
        mapPanel.setTerritories(getTerritoryShapes());
    }

    private void loadContinents() throws GameStartException {
        String line;

        try (InputStream inputStream = getClass().getResourceAsStream(CONTINENT_DATA_FILENAME)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;
                    }
                    final String[] data = line.split(",");
                    final String name = data[0];
                    final int bonus = Integer.parseInt(data[1]);
                    continents.put(name, new Continent(name, bonus));
                }
            }
        } catch (IOException e) {
            throw new GameStartException("Error loading continent data", e);
        }
    }

    /**
     * Create all the territories from the information in territory.dat
     *
     * @throws GameStartException if the territory information could not be
     * loaded.
     */
    private void loadTerritories() throws GameStartException {
        String line;

        try (InputStream inputStream = getClass().getResourceAsStream(TERRITORY_DATA_FILENAME)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;
                    }
                    final String[] data = line.split(";");
                    final String name = data[0];
                    final Continent continent = continents.get(data[1]);
                    final String adjacentTerritories = data[2];
                    final String[] shapeData = data[3].split(",");
                    final String[] unitOffset = data[4].split(",");

                    final TerritoryShape[] shapes = new TerritoryShape[shapeData.length];
                    final Territory territory = new Territory(name, continent, shapes);
                    for (int i = 0; i < shapeData.length; i++) {
                        final String path = TERRITORY_PATH + shapeData[i].trim();
                        final InputStream territoryStream = getClass().getResourceAsStream(path);
                        if (territoryStream == null) {
                            throw new GameStartException("Map data not found: " + path);
                        }
                        shapes[i] = new TerritoryShape(territory, getClass().getResourceAsStream(path));
                    }
                    territories.put(name, territory);
                    adjacency.put(name, adjacentTerritories);
                    unitOffsets.put(name, new Point(Integer.parseInt(unitOffset[0]), Integer.parseInt(unitOffset[1])));
                }
            }
        } catch (IOException e) {
            throw new GameStartException("Error loading territory data", e);
        }
    }

    /**
     * Link all the adjacent territories once all the territories have been
     * created
     */
    private void linkAdjacentTerritories() throws GameStartException {
        for (Territory territory : territories.values()) {

            final String[] adjacentTerritoryNames = adjacency.get(territory.getName()).split(",");
            final Territory[] adjacentTerritories = new Territory[adjacentTerritoryNames.length];

            for (int i = 0; i < adjacentTerritoryNames.length; i++) {

                final String name = adjacentTerritoryNames[i].trim();
                final Territory adjacentTerritory = territories.get(name);
                if (adjacentTerritory == null) {
                    throw new GameStartException("Error linking unknown territory: " + name);
                }
                adjacentTerritories[i] = adjacentTerritory;
            }

            territory.setAdjacentTerritories(adjacentTerritories);
        }
    }

    /**
     * Link all the territories in each continent.
     */
    private void linkContinentTerritories() throws GameStartException {
        for (Continent continent : continents.values()) {
            final ArrayList<Territory> continentTerritories = new ArrayList<>();

            for (Territory territory : territories.values()) {
                if (territory.getContinent().equals(continent)) {
                    continentTerritories.add(territory);
                }
            }

            continent.setTerritories(continentTerritories.toArray(new Territory[continentTerritories.size()]));
        }
    }

    /**
     * Add the unit count labels for each territory
     */
    public void addUnitCountLabels(Territory territory) {
        final Rectangle territoryBounds = territory.getShapes()[0].getBounds();

        // The unit count label should center between all the territory shapes 
        if (territory.getShapes().length > 1) {
            for (TerritoryShape shape : territory.getShapes()) {
                Rectangle bounds = shape.getBounds();
                double minX = Math.min(bounds.getMinX(), territoryBounds.getMinX());
                double minY = Math.min(bounds.getMinY(), territoryBounds.getMinY());
                double maxX = Math.max(bounds.getMaxX(), territoryBounds.getMaxX());
                double maxY = Math.max(bounds.getMaxY(), territoryBounds.getMaxY());
                territoryBounds.setBounds((int) minX, (int) minY, (int) (maxX - minX), (int) (maxY - minY));
            }
        }

        final Point offset = unitOffsets.get(territory.getName());
        final UnitCountLabel unitCount = new UnitCountLabel(territory);

        int centerX = (int) territoryBounds.getCenterX() - (unitCount.getWidth() / 2) + offset.x;
        int centerY = (int) territoryBounds.getCenterY() - (unitCount.getHeight() / 2) + offset.y;

        unitCount.setLocation(centerX, centerY);

        mapPanel.add(unitCount);
    }

    private TerritoryShape[] getTerritoryShapes() {
        final ArrayList<TerritoryShape> territoryShapes = new ArrayList<>();
        for (Territory territory : territories.values()) {
            territoryShapes.addAll(Arrays.asList(territory.getShapes()));
        }
        return territoryShapes.toArray(new TerritoryShape[territoryShapes.size()]);
    }
}

//HELP
