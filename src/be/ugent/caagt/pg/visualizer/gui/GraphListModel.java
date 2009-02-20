/* GraphListModel.java
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

import be.ugent.caagt.pg.graph.Graph;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nvcleemp
 */
public interface GraphListModel extends ListModel {
    
    public Graph getGraph(int index);

    public GraphGUIData getGraphGUIData(int index);
    
    public int getCatalogueNumber(int index);

    public int getSelectedCatalogueNumber();

    public String exportUpdatedGraphs();
    
    public Graph getSelectedGraph();
    
    public GraphGUIData getSelectedGraphGUIData();
    
    public void addGraphModelListener(GraphListModelListener l);
    
    public void removeGraphModelListener(GraphListModelListener l);

    public ListSelectionModel getSelectionModel();

    public void addListSelectionListener(ListSelectionListener l);

    public void removeListSelectionListener(ListSelectionListener l);
    
    public void commitGraph(int index);
    
    public void commitSelectedGraph();
    
    public void revertGraph(int index);
    
    public void revertSelectedGraph();
    
    public String getString(int index);
    
    public void putGraph(Graph g, int index);

}
