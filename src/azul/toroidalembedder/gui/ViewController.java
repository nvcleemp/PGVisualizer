package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.general.Vertex;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ViewController extends JPanel {

    private int xView;
    private int yView;
    private TorusView torusView;

    public ViewController(TorusView torusView) {
        this.torusView = torusView;
        xView = torusView.getMaxX();
        yView = torusView.getMaxY();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Horizontal views", JLabel.LEFT), gbc);
        gbc.gridy = 1;
        add(new JLabel("Vertical views", JLabel.LEFT), gbc);
        gbc.gridy = 2;
        add(new JLabel("Vertex size", JLabel.LEFT), gbc);
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.weightx = 1;
        add(new JButton(new XAction(1)), gbc);
        gbc.gridx = 2;
        add(new JButton(new XAction(-1)), gbc);
        gbc.gridy = 1;
        gbc.gridx = 1;
        add(new JButton(new YAction(1)), gbc);
        gbc.gridx = 2;
        add(new JButton(new YAction(-1)), gbc);
        final JSlider slider = new JSlider(0, 100, torusView.getVertexSize());
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ViewController.this.torusView.setVertexSize(slider.getValue());
            }
        });
        gbc.gridy++;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(slider, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3;
        //add(new JButton(new ColorAction()), gbc);
        final JCheckBox clipView = new JCheckBox("Clip view (may slow performance down)", torusView.isViewClipped());
        clipView.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                ViewController.this.torusView.setViewClipped(clipView.isSelected());
            }
        });
        add(clipView, gbc);
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
    
    private class ColorAction extends AbstractAction {
        
        public ColorAction() {
            super("Color selected");
        }

        public void actionPerformed(ActionEvent e) {
            if(torusView.getHighlight()==null)
                torusView.setHighlight(new DefaultVertexHightlighter());

            Color selected = JColorChooser.showDialog(torusView, "Select color", Color.WHITE);
            if(selected != null){
                VertexHighlighter theHighlighter = torusView.getHighlight();
                for (Vertex v : torusView.getGraphSelectionModel().getSelectedVertices()) {
                    theHighlighter.setColor(v, selected);
                }
                torusView.repaint();
            }
        }
    }
}
