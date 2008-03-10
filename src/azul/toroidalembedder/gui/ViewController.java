package azul.toroidalembedder.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ViewController extends JPanel {

    private int xView;
    private int yView;
    private TorusView torusView;

    public ViewController(TorusView torusView) {
        super();
        this.torusView = torusView;
        xView = torusView.getMaxX();
        yView = torusView.getMaxY();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Horizontal", JLabel.LEFT), gbc);
        gbc.gridy = 1;
        add(new JLabel("Vertical", JLabel.LEFT), gbc);
        gbc.gridy = 0;
        gbc.gridx = 1;
        add(new JButton(new XAction(1)), gbc);
        gbc.gridx = 2;
        add(new JButton(new XAction(-1)), gbc);
        gbc.gridy = 1;
        gbc.gridx = 1;
        add(new JButton(new YAction(1)), gbc);
        gbc.gridx = 2;
        add(new JButton(new YAction(-1)), gbc);
        setBorder(BorderFactory.createTitledBorder("View"));
    }

    private class XAction extends AbstractAction {

        private int increment;

        public XAction(int increment) {
            super(increment > 0 ? "+" : "-");
            this.increment = increment;
        }

        public void actionPerformed(ActionEvent e) {
            if (xView + increment >= 0) {
                xView += increment;
                torusView.setView(-xView, -yView, xView, yView);
            }
        }
    }

    private class YAction extends AbstractAction {

        private int increment;

        public YAction(int increment) {
            super(increment > 0 ? "+" : "-");
            this.increment = increment;
        }

        public void actionPerformed(ActionEvent e) {
            if (yView + increment >= 0) {
                yView += increment;
                torusView.setView(-xView, -yView, xView, yView);
            }
        }
    }
}
