/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.visualizer.structures.jmol;

import java.awt.Color;
import java.text.MessageFormat;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jmol.api.JmolViewer;

/**
 *
 * @author nvcleemp
 */
public class JmolColorChangeListener implements ChangeListener{

    protected JmolViewer viewer;
    protected String component;
    private ColorSelectionModel colorModel;

    public JmolColorChangeListener(JmolViewer viewer, String component, ColorSelectionModel colorModel) {
        this.viewer = viewer;
        this.component = component;
        this.colorModel = colorModel;
    }

    protected static String colorToString(Color c){
        String red = MessageFormat.format("00{0}", Integer.toHexString(c.getRed()));
        String green = MessageFormat.format("00{0}", Integer.toHexString(c.getGreen()));
        String blue = MessageFormat.format("00{0}", Integer.toHexString(c.getBlue()));
        String colorString = MessageFormat.format("[x{0}{1}{2}]",
                red.substring(red.length()-2),
                green.substring(green.length()-2),
                blue.substring(blue.length()-2));
        return colorString;
    }

    public void stateChanged(ChangeEvent e) {
        viewer.evalString(MessageFormat.format("color {0} {1}", component, colorToString(colorModel.getSelectedColor())));
    }

}
