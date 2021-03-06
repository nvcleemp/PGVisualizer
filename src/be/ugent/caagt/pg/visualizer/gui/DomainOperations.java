/* DomainOperations.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008-2009 Universiteit Gent
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package be.ugent.caagt.pg.visualizer.gui;

import be.ugent.caagt.pg.graph.FundamentalDomainListener;
import be.ugent.caagt.pg.graph.Graph;
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
public class DomainOperations extends JPanel implements GraphListModelListener, FundamentalDomainListener {
    private Graph graph;
    private GraphListModel model = null;
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
    
    public DomainOperations(GraphListModel model) {
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
