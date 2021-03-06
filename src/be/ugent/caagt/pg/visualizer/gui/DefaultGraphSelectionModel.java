/* DefaultGraphSelectionModel.java
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

import be.ugent.caagt.pg.graph.Vertex;
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
    
    public Vertex[] getSelectedVertices() {
        Vertex[] arr = new Vertex[selection.size()];
        selection.toArray(arr);
        return arr;
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
