/* DefaultGraphFaceSelectionModel.java
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
