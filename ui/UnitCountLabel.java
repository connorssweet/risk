package risk.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import risk.Territory;

/**
 * This is the unit count badge/label that sits within a territory.
 */
public class UnitCountLabel extends JLabel {

    private static final long serialVersionUID = -2584077253940490484L;
    private final Territory territory;

    public UnitCountLabel(Territory territory) {
        this.territory = territory;
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setBounds(0, 0, 30, 30);
        setFont(new Font("Serif", Font.BOLD, 20));
    }

    /**
     * @see https://stackoverflow.com/questions/6090537/drawing-glassy-buttons
     */
    @Override
    protected void paintComponent(Graphics g) {
        setText("" + territory.getUnitCount());
        final Graphics2D g2 = (Graphics2D) g;
        final Composite originalComposite = g2.getComposite();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));

        final Color c1 = new Color(0, 0, 0, 0);
        final Color c2 = new Color(0, 0, 0, 20);
        final GradientPaint gradient = new GradientPaint(10, 8, c1, 10, 40, c2, true);

        g2.setColor(Color.WHITE);

        g.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
        g.fillOval(0, 0, getWidth() - 1, getHeight() - 1);

        g2.setPaint(gradient);
        g.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
        g.fillOval(0, 0, getWidth() - 1, getHeight() - 1);

        g2.setComposite(originalComposite);

        super.paintComponent(g);
    }
}
