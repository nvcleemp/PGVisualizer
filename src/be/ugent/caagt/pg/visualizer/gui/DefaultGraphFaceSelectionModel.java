/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.visualizer.gui;

import be.ugent.caagt.pg.graph.Face;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author nvcleemp
 */
public class DefaultGraphFaceSelectionModel implements GraphFaceSelectionModel {
    
    private Set<Face> selection = new HashSet<Face>();
    private List<GraphFaceSelectionListener> listeners = new ArrayList<GraphFaceSelectionListener>();

    public void clearSelection() {
        selection.clear();
        fireSelectionChanged();
    }

    public void addFace(Face f) {
        selection.add(f);
        fireSelectionChanged();
    }

    public void removeFace(Face f) {
        selection.remove(f);
        fireSelectionChanged();
    }

    public void toggleFace(Face f) {
        if(selection.contains(f))
            removeFace(f);
        else
            addFace(f);
    }

    public boolean isSelected(Face f) {
        return selection.contains(f);
    }
    
    public Face[] getSelectedFaces() {
        Face[] arr = new Face[selection.size()];
        selection.toArray(arr);
        return arr;
    }
    
    public void addGraphFaceSelectionListener(GraphFaceSelectionListener l){
        listeners.add(l);
    }

    public void removeGraphFaceSelectionListener(GraphFaceSelectionListener l){
        listeners.remove(l);
    }
    
    private void fireSelectionChanged(){
        for (GraphFaceSelectionListener l : listeners)
            l.graphFaceSelectionChanged();
    }
}
