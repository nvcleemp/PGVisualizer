/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.embedder.gui;

import be.ugent.caagt.pg.embedder.CombinedEmbedder;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author nvcleemp
 */
public class EmbedderTreeModel implements TreeModel{
    
    private final CombinedEmbedder root = new CombinedEmbedder();

    public Object getRoot() {
        return root;
    }

    public Object getChild(Object parent, int index) {
        if(isLeaf(parent))
            return null;
        else
            return ((CombinedEmbedder)parent).getEmbedderAt(index);
    }

    public int getChildCount(Object parent) {
        if(isLeaf(parent))
            return 0;
        else
            return ((CombinedEmbedder)parent).getSize();
    }

    public boolean isLeaf(Object node) {
        return !(node instanceof CombinedEmbedder);
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        //
    }

    public int getIndexOfChild(Object parent, Object child) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addTreeModelListener(TreeModelListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeTreeModelListener(TreeModelListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
