package risk.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;

import risk.event.Event;
import risk.event.LogListener;

public class LogPanel extends JPanel implements LogListener {

    private static final long serialVersionUID = 8367118493044838780L;
    private JTextPane log;

    public LogPanel() {
        log = new JTextPane();
        log.setContentType("text/html");
        log.setEditable(false);
        log.setText("<html><body><ul style=\"margin:2 10px 2 10px;padding:0\"id=\"log\"></ul></body></html>");

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(350, 100));

        final JScrollPane scrollPane = new JScrollPane(log);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);
        Event.addLogListener(this);
    }

    @Override
    public void onLog(String message) {
        final HTMLDocument document = (HTMLDocument) log.getStyledDocument();
        final Element logElement = document.getElement("log");
        try {
            document.insertAfterStart(logElement, message);
        } catch (BadLocationException e) {
            System.err.println("Inserting log message at bad position: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error writing log message: " + e.getMessage());
        }
    }
}
