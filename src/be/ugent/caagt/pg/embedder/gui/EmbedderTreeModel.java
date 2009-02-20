/* EmbedderTreeModel.java
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
