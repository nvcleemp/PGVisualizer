/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.FundamentalDomainListener;
import azul.toroidalembedder.graph.general.Graph;
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
public class DomainOperations extends JPanel implements GraphModelListener, FundamentalDomainListener {
    private Graph graph;
    private GraphModel model = null;
    private JLabel hsLabel = new JLabel();
    private JLabel vsLabel = new JLabel();
    private JLabel angleLabel = new JLabel();

    private DomainOperations() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        add(new JLabel("Angle", JLabel.LEFT), gbc);
        gbc.gridy = 1;
        add(new JLabel("Horizontal side", JLabel.LEFT), gbc);
        gbc.gridy = 0;
        gbc.gridx = 1;
        add(new JButton(new AngleAction(0.1)), gbc);
        gbc.gridx = 2;
        add(new JButton(new AngleAction(-0.1)), gbc);
        gbc.gridy = 1;
        gbc.gridx = 1;
        add(new JButton(new SideAction(0.1)), gbc);
        gbc.gridx = 2;
        add(new JButton(new SideAction(-0.1)), gbc);
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Horizontal side:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(hsLabel, gbc);
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("Vertical side:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(vsLabel, gbc);
        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(new JLabel("angle:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(angleLabel, gbc);
        setName("Domain operations");
    }
    
    public DomainOperations(Graph graph) {
        this();
        setGraph(graph);
    }
    
    public DomainOperations(GraphModel model) {
        this(model.getSelectedGraph());
        this.model = model;
        model.addGraphModelListener(this);
    }
    
    public void setGraph(Graph graph){
        if(this.graph!=null)
            this.graph.getFundamentalDomain().removeFundamentalDomainListener(this);
        this.graph = graph;
        setLabels();
        graph.getFundamentalDomain().addFundamentalDomainListener(this);
    }
    
    private void setLabels(){
        hsLabel.setText(Double.toString(graph.getFundamentalDomain().getHorizontalSide()));
        vsLabel.setText(Double.toString(graph.getFundamentalDomain().getVerticalSide()));
        angleLabel.setText(Double.toString(graph.getFundamentalDomain().getAngle()));
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

    public void fundamentalDomainShapeChanged() {
        setLabels();
    }
}
