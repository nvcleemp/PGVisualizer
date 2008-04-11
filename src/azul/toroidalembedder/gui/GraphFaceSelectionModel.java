/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.Face;

/**
 *
 * @author nvcleemp
 */
public interface GraphFaceSelectionModel {
    
    public void clearSelection();
    public void addFace(Face v);
    public void removeFace(Face v);
    public void toggleFace(Face v);
    public boolean isSelected(Face v);
    public void addGraphFaceSelectionListener(GraphFaceSelectionListener l);
    public void removeGraphFaceSelectionListener(GraphFaceSelectionListener l);

    public Face[] getSelectedFaces();

}
