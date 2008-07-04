/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.visualizer.gui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class ToolFactory {
    
    public static JPanel createEmbeddedTool(JPanel panel){
        panel.setBorder(BorderFactory.createTitledBorder(panel.getName()));
        return panel;
    }

}
