/* AbstractEmbedder.java
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

package be.ugent.caagt.pg.embedder;

import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.visualizer.gui.GraphListModel;
import javax.swing.event.ListDataEvent;

/**
 *
 * @author nvcleemp
 */
public abstract class AbstractEmbedder implements Embedder{
    
    protected Graph graph;
    protected GraphListModel graphModel = null;
    
    public AbstractEmbedder(Graph graph){
        this.graph = graph;
    }

    public AbstractEmbedder(GraphListModel graphModel){
        this.graphModel = graphModel;
        this.graph = graphModel.getSelectedGraph();
        graphModel.addGraphModelListener(this);
        resetEmbedder();
    }

    public void selectedGraphChanged() {
        if(graphModel!=null){
            graph = graphModel.getSelectedGraph();
            resetEmbedder();
        }
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

    protected abstract void resetEmbedder();

}
