/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.general.Graph;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class GraphShiftOperations extends JPanel {
    private Graph graph;
    
    GraphShiftOperations(boolean withBorder) {
        setLayout(new GridLayout(3, 3));
        add(new JButton(new ShiftAction("NW", -1, -1)));
        add(new JButton(new ShiftAction("N", 0, -1)));
        add(new JButton(new ShiftAction("NE", 1, -1)));
        add(new JButton(new ShiftAction("W", -1, 0)));
        add(new JLabel(""));
        add(new JButton(new ShiftAction("E", 1, 0)));
        add(new JButton(new ShiftAction("SW", -1, 1)));
        add(new JButton(new ShiftAction("S", 0, 1)));
        add(new JButton(new ShiftAction("SE", 1, 1)));
        if(withBorder)
            setBorder(BorderFactory.createTitledBorder("Graph shift operations"));
    }

    public GraphShiftOperations(Graph graph) {
        this(graph, true);
    }
    
    public GraphShiftOperations(Graph graph, boolean withBorder) {
        this(withBorder);
        this.graph = graph;
    }
    
    public void setGraph(Graph graph){
        this.graph = graph;
    }
    
    private class ShiftAction extends AbstractAction{
        
        int x = 0;
        int y = 0;
        
        public ShiftAction(String name, int x, int y){
            super(name);
            this.x = x;
            this.y = y;
        }

        public void actionPerformed(ActionEvent e) {
            graph.translate(0.1*x, 0.1*y);
        }
    }
    
}
