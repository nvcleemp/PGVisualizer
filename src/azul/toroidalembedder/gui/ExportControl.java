/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.io.IOManager;
import azul.toroidalembedder.graph.general.Graph;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class ExportControl extends JPanel{
    
    private ExportControl(){
        //
    }

    public static JPanel getPanel(TorusView torusView, final Graph graph) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JButton(new ExportBitmapAction(torusView)));
        panel.add(new JButton(new AbstractAction("Save graph") {
            public void actionPerformed(ActionEvent e) {
                System.out.println(IOManager.writePG(graph));
            }
        }));
        panel.setBorder(BorderFactory.createTitledBorder("Export"));
        return panel;
    }
    
    public static JPanel getPanel(final TorusView torusView, final GraphModel graphModel) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JButton(new ExportBitmapAction(torusView)));
        panel.add(new JButton(new CommitGraphAction(graphModel)));
        panel.add(new JButton(new RevertGraphAction(graphModel)));
        panel.add(new JButton(new SaveGraphListAction(graphModel)));
        panel.setBorder(BorderFactory.createTitledBorder("Export"));
        return panel;
    }
    
    public static JMenu getMenu(final TorusView torusView, final GraphModel graphModel) {
        JMenu panel = new JMenu("Export");
        panel.add(new JMenuItem(new ExportBitmapAction(torusView)));
        panel.add(new JMenuItem(new CommitGraphAction(graphModel)));
        panel.add(new JMenuItem(new RevertGraphAction(graphModel)));
        panel.add(new JMenuItem(new SaveGraphListAction(graphModel)));
        return panel;
    }
}
