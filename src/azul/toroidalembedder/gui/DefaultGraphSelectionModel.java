/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.Vertex;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author nvcleemp
 */
public class DefaultGraphSelectionModel implements GraphSelectionModel {
    
    private Set<Vertex> selection = new HashSet<Vertex>();
    private List<GraphSelectionListener> listeners = new ArrayList<GraphSelectionListener>();

    public void clearSelection() {
        selection.clear();
        fireSelectionChanged();
    }

    public void addVertex(Vertex v) {
        selection.add(v);
        fireSelectionChanged();
    }

    public void removeVertex(Vertex v) {
        selection.remove(v);
        fireSelectionChanged();
    }

    public void toggleVertex(Vertex v) {
        if(selection.contains(v))
            removeVertex(v);
        else
            addVertex(v);
    }

    public boolean isSelected(Vertex v) {
        return selection.contains(v);
    }
    
    public void addGraphSelectionListener(GraphSelectionListener l){
        listeners.add(l);
    }

    public void removeGraphSelectionListener(GraphSelectionListener l){
        listeners.remove(l);
    }
    
    private void fireSelectionChanged(){
        for (GraphSelectionListener l : listeners)
            l.graphSelectionChanged();
    }
}
