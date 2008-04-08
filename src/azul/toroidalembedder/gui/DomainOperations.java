/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.Graph;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;

/**
 *
 * @author nvcleemp
 */
public class DomainOperations extends JPanel implements GraphModelListener {
    private Graph graph;
    private GraphModel model = null;

    private DomainOperations() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Angle", JLabel.LEFT), gbc);
        gbc.gridy = 1;
        add(new JLabel("Horizontal side", JLabel.LEFT), gbc);
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.weightx = 1;
        add(new JButton(new AngleAction(0.1)), gbc);
        gbc.gridx = 2;
        add(new JButton(new AngleAction(-0.1)), gbc);
        gbc.gridy = 1;
        gbc.gridx = 1;
        add(new JButton(new SideAction(0.1)), gbc);
        gbc.gridx = 2;
        add(new JButton(new SideAction(-0.1)), gbc);
        setBorder(BorderFactory.createTitledBorder("Domain operations"));
    }
    
    public DomainOperations(Graph graph) {
        this();
        this.graph = graph;
    }
    
    public DomainOperations(GraphModel model) {
        this(model.getSelectedGraph());
        this.model = model;
        model.addGraphModelListener(this);
    }
    
    public void setGraph(Graph graph){
        this.graph = graph;
    }
    
    private class AngleAction extends AbstractAction{
        
        double increment;
        
        public AngleAction(double increment){
            super(increment > 0 ? "+" : "-");
            this.increment = increment;
        }
        
        public void actionPerformed(ActionEvent e) {
            graph.getFundamentalDomain().addToAngle(increment);
        }
    }
    
    private class SideAction extends AbstractAction{
        
        double increment;
        
        public SideAction(double increment){
            super(increment > 0 ? "+" : "-");
            this.increment = increment;
        }
        
        public void actionPerformed(ActionEvent e) {
            graph.getFundamentalDomain().addToHorizontalSide(increment);
        }
    }

    public void selectedGraphChanged() {
        if(model!=null)
            setGraph(model.getSelectedGraph());
    }

    public void intervalAdded(ListDataEvent e) {
        //
    }

    public void intervalRemoved(ListDataEvent e) {
        //
    }

    public void contentsChanged(ListDataEvent e) {
        //
    }
    
}
