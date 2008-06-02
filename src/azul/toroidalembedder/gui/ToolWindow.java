/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class ToolWindow extends JFrame {

    public ToolWindow(JPanel toolPanel, JFrame parentWindow) {
        super(toolPanel.getName());
        parentWindow.addWindowListener(new ParentWindowListener());
        add(toolPanel);
        setAlwaysOnTop(true);
        pack();
    }

    private class ParentWindowListener extends WindowAdapter{

        @Override
        public void windowClosed(WindowEvent e) {
            ToolWindow.this.setVisible(false);
            ToolWindow.this.dispose();
        }
        
    }

}
