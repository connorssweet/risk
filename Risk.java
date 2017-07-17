package risk;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import javax.swing.SwingUtilities;
import risk.state.ClaimTerritory;
import risk.state.GameState;
import risk.state.Winner;
import risk.ui.RiskFrame;

/**
 * This is the start of the Risk program that sets up the GUI on the Swing on
 * the AWT event dispatch thread and our main game loop on the normal, main
 * thread
 * @author Connor
 */
public class Risk implements Runnable {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    private RiskFrame frame = null;

    public static void main(String[] args) throws InterruptedException {
        Risk risk = new Risk();
        SwingUtilities.invokeLater(risk);
        countDownLatch.await();
        risk.gameLoop(risk.frame);
    }

    /**
     * Create and run the GUI in the AWT event dispatching thread.
     */
    public void run() {
        // create and display the GUI
        frame = new RiskFrame();
        countDownLatch.countDown();
    }

    /**
     * Run the game loop in the normal main thread
     * @param frame The frame object
     * @throws InterruptedException If the thread is interrupted
     */
    private void gameLoop(RiskFrame frame) throws InterruptedException {
        final ArrayList<Player> players = frame.getPlayers();
        GameState state = new ClaimTerritory(frame);

        while (players.size() > 1) {
            state = state.play();
        }

        state = new Winner(frame);
        state.play();
        System.exit(0);
    }
}