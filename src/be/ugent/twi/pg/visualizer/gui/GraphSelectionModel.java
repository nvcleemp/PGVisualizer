/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.visualizer.gui;

import be.ugent.twi.pg.graph.Vertex;

/**
 *
 * @author nvcleemp
 */
public interface GraphSelectionModel {
    
    public void clearSelection();
    public void addVertex(Vertex v);
    public void removeVertex(Vertex v);
    public void toggleVertex(Vertex v);
    public boolean isSelected(Vertex v);
    public void addGraphSelectionListener(GraphSelectionListener l);
    public void removeGraphSelectionListener(GraphSelectionListener l);

    Vertex[] getSelectedVertices();

}
