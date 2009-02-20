/* GrowGraphAction.java
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

package be.ugent.caagt.pg.visualizer.gui.action;

import be.ugent.caagt.pg.graph.DefaultGraph;
import be.ugent.caagt.pg.graph.DefaultVertex;
import be.ugent.caagt.pg.graph.Face;
import be.ugent.caagt.pg.graph.FundamentalDomain;
import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.graph.Vertex;
import be.ugent.caagt.pg.visualizer.gui.GraphListModel;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author nvcleemp
 */
public class GrowGraphAction extends AbstractAction{
    
    private GraphListModel graphModel;
    private GrowDialog dialog = new GrowDialog();

    public GrowGraphAction(GraphListModel graphModel) {
        super("Grow graph");
        this.graphModel = graphModel;
    }

    public void actionPerformed(ActionEvent e) {
        dialog.reset();
        dialog.setVisible(true);
        if(dialog.wasOKPressed()){
            graphModel.putGraph(concat((Integer)dialog.x.getNumber(), (Integer)dialog.y.getNumber()), graphModel.getSelectionModel().getMinSelectionIndex());
        }
    }
    
    private class GrowDialog extends JDialog{
        
        private SpinnerNumberModel x = new SpinnerNumberModel(1, 1, 10, 1);
        private SpinnerNumberModel y = new SpinnerNumberModel(1, 1, 10, 1);
        private boolean ok;

        public GrowDialog() {
            super((Frame)null, "Grow", true);
            setLayout(new GridLayout(3, 2));
            reset();
            add(new JLabel("Horizontal copies"));
            add(new JSpinner(x));
            add(new JLabel("Vertical copies"));
            add(new JSpinner(y));
            add(new JButton(new AbstractAction("Cancel") {
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            }));
            add(new JButton(new AbstractAction("OK") {
                public void actionPerformed(ActionEvent e) {
                    ok = true;
                    setVisible(false);
                }
            }));
            pack();
        }
        
        public void reset(){
            x.setValue(1);
            y.setValue(1);
            ok = false;
        }
        
        public boolean wasOKPressed(){
            return ok;
        }
        
    }

    private DefaultGraph concat(int x, int y){
        Graph input = graphModel.getSelectedGraph();
        FundamentalDomain inputDomain = input.getFundamentalDomain();
        DefaultGraph result = new DefaultGraph(new FundamentalDomain(inputDomain.getAngle(), inputDomain.getHorizontalSide()*x, inputDomain.getVerticalSide()*y));
        DefaultVertex[] vertices = new DefaultVertex[x*y*input.getVertices().size()];
        int verticalOffset = -(y - 1);
        for (int yi = 0; yi < y; yi++) {
            int horizontalOffset = -(x - 1);
            for (int xi = 0; xi < x; xi++) {
                for (int i = 0; i < input.getVertices().size(); i++) {
                    int index = (yi * x + xi)*input.getVertices().size() + i;
                    double coordX = (input.getVertex(i).getRawX() + horizontalOffset)/x;
                    double coordY = (input.getVertex(i).getRawY() + verticalOffset)/y;
                    vertices[index] = new DefaultVertex(coordX, coordY);
                    result.addVertex(vertices[index]);
                }
                horizontalOffset +=2;
            }
            verticalOffset+=2;
        }
        for (Vertex vertex : input.getVertices()) {
            for (Edge edge : vertex.getEdges()) {
                if(vertex.getIndex() <= edge.getEnd().getIndex()){
                    for (int yi = 0; yi < y; yi++) {
                        int newTargetY = (edge.getTargetY()+yi)/y;
                        int internalY = (edge.getTargetY()+yi)%y;
                        if((edge.getTargetY()+yi)<0){
                            newTargetY = ((edge.getTargetY()+yi)- y + 1)/y;
                            internalY = (internalY + y)%y;
                        }
                        for (int xi = 0; xi < x; xi++) {
                            int newTargetX = (edge.getTargetX()+xi)/x;
                            int internalX = ((edge.getTargetX()+xi)%x + x)%x;
                            if((edge.getTargetX()+xi)<0){
                                newTargetX = ((edge.getTargetX()+xi)- x + 1)/x;
                                internalX = (internalX + x)%x;
                            }
                            if(!(internalX<0 || internalX >= x || internalY < 0 || internalY >= y))
                                result.addEdge((yi*x + xi)*input.getVertices().size() + vertex.getIndex(), (internalY*x + internalX)*input.getVertices().size() + edge.getEnd().getIndex(), newTargetX, newTargetY);
                        }
                    }
                }
            }
        }
        if(input instanceof DefaultGraph){
            DefaultGraph defaultGraph = (DefaultGraph)input;
            for (Face face : defaultGraph.getFaces()) {
                for (int yi = 0; yi < y; yi++) {
                    for (int xi = 0; xi < x; xi++) {
                        Face newFace = new Face();
                        int xFace = 0;
                        int yFace = 0;
                        for (Edge edge : face.getEdges()) {
                            int internalX = ((xi+xFace)%x + x)%x;
                            int internalY = ((yi+yFace)%y + y)%y;
                            newFace.add(vertices[(internalY*x + internalX)*input.getVertices().size() + edge.getStart().getIndex()]);
                            xFace += edge.getTargetX();
                            yFace += edge.getTargetY();
                        }
                        result.addFace(newFace);
                    }
                }
            }

        }
        return result;
    }

}
