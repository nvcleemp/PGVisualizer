package be.ugent.caagt.pg.visualizer.gui;

import be.ugent.caagt.pg.visualizer.gui.toggler.ClipViewToggler;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractSpinnerModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
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
        JSpinner spinner = new JSpinner(new AbstractSpinnerModel() {
            public Object getValue() {
                return Integer.valueOf(xView*2+1);
            }

            public void setValue(Object value) {
                if(value instanceof Integer){
                    int x = ((Integer)value).intValue();
                    if(x < 1)
                        xView = 0;
                    else
                        xView = (x-1)/2;
                    fireStateChanged();
                }
            }

            public Object getNextValue() {
                return Integer.valueOf(xView*2+3);
            }

            public Object getPreviousValue() {
                if(xView==0)
                    return null;
                else
                    return Integer.valueOf(xView*2-1);
            }
        });
        spinner.addChangeListener(new ViewChanged());
        add(spinner, gbc);
        gbc.gridy = 1;
        gbc.gridx = 1;
        JSpinner spinner2 = new JSpinner(new AbstractSpinnerModel() {
            public Object getValue() {
                return Integer.valueOf(yView*2+1);
            }

            public void setValue(Object value) {
                if(value instanceof Integer){
                    int y = ((Integer)value).intValue();
                    if(y < 1)
                        yView = 0;
                    else
                        yView = (y-1)/2;
                    fireStateChanged();
                }
            }

            public Object getNextValue() {
                return Integer.valueOf(yView*2+3);
            }

            public Object getPreviousValue() {
                if(yView==0)
                    return null;
                else
                    return Integer.valueOf(yView*2-1);
            }
        });
        spinner2.addChangeListener(new ViewChanged());
        add(spinner2, gbc);
        final JSlider slider = new JSlider(0, 100, torusView.getVertexSize());
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ViewController.this.torusView.setVertexSize(slider.getValue());
            }
        });
        gbc.gridy++;
        gbc.gridx = 1;
        add(slider, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        //add(new JButton(new ColorAction()), gbc);
        final JCheckBox clipView = new ClipViewToggler(torusView).getJCheckBox(); //new JCheckBox("Clip view (may slow performance down)", torusView.isViewClipped());
        clipView.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                ViewController.this.torusView.setViewClipped(clipView.isSelected());
            }
        });
        add(clipView, gbc);
        setName("View");
        //setBorder(BorderFactory.createTitledBorder("View"));
    }

    private class ViewChanged implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            torusView.setView(-xView, -yView, xView, yView);
        }
    }
}
