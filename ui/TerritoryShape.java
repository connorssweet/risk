package risk.ui;

import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import risk.Territory;

public class TerritoryShape extends Polygon {

    private static final long serialVersionUID = -7755064246825986208L;
    private final Territory territory;

    public TerritoryShape(Territory territory, InputStream inputStream) throws IOException {
        this.territory = territory;
        readShapeData(inputStream);
    }

    public Territory getTerritory() {
        return territory;
    }

    private void readShapeData(InputStream inputStream) throws IOException {
        String line;
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                while ((line = reader.readLine()) != null) {
                    final String[] coordinates = line.split(",");
                    final int x = Integer.parseInt(coordinates[0]);
                    final int y = Integer.parseInt(coordinates[1]);
                    addPoint(x, y);
                }
            }
        }
    }
}
