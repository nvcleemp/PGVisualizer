/* DelegateGraphListModel.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008 Universiteit Gent
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

import be.ugent.caagt.pg.io.IOManager;
import be.ugent.caagt.pg.graph.Graph;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nvcleemp
 */
public class DelegateGraphListModel implements GraphListModel, ListSelectionListener, ListDataListener{
    
    private GraphListModel delegate;
    private List<Integer> list;
    private ListSelectionModel selectionModel;
    private List<GraphListModelListener> listeners = new ArrayList<GraphListModelListener>();
    private List<ListDataListener> dataListeners = new ArrayList<ListDataListener>();

    public DelegateGraphListModel(GraphListModel delegate, List<Integer> list) {
        this.delegate = delegate;
        this.list = new ArrayList<Integer>(list);
        delegate.addListDataListener(this);
        selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(this);
    }

    public Graph getGraph(int index) {
        return delegate.getGraph(list.get(index));
    }

    public GraphGUIData getGraphGUIData(int index) {
        return delegate.getGraphGUIData(list.get(index));
    }

    public int getCatalogueNumber(int index) {
        return delegate.getCatalogueNumber(list.get(index));
    }

    public int getSelectedCatalogueNumber() {
        return getCatalogueNumber(selectionModel.getMinSelectionIndex());
    }

    public String exportUpdatedGraphs() {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            buf.append(IOManager.writePG(getGraph(i)));
            buf.append(" # " + getGraphGUIData(i).export());
            buf.append('\n');
        }
        return buf.toString();
    }

    public Graph getSelectedGraph() {
        return getGraph(selectionModel.getMinSelectionIndex());
    }

    public GraphGUIData getSelectedGraphGUIData() {
        return getGraphGUIData(selectionModel.getMinSelectionIndex());
    }

    public void addGraphModelListener(GraphListModelListener l) {
        listeners.add(l);
    }

    public void removeGraphModelListener(GraphListModelListener l) {
        listeners.remove(l);
    }

    public ListSelectionModel getSelectionModel() {
        return selectionModel;
    }

    public void addListSelectionListener(ListSelectionListener l) {
        selectionModel.addListSelectionListener(l);
    }

    public void removeListSelectionListener(ListSelectionListener l) {
        selectionModel.removeListSelectionListener(l);
    }

    public void commitGraph(int index) {
        delegate.commitGraph(list.get(index));
    }

    public void commitSelectedGraph() {
        commitGraph(selectionModel.getMinSelectionIndex());
    }

    public void revertGraph(int index) {
        delegate.revertGraph(list.get(index));
    }

    public void revertSelectedGraph() {
        revertGraph(selectionModel.getMinSelectionIndex());
    }

    public String getString(int index) {
        return delegate.getString(list.get(index));
    }

    public void putGraph(Graph g, int index) {
        delegate.putGraph(g, list.get(index));
    }

    public int getSize() {
        return list.size();
    }

    public Object getElementAt(int index) {
        return delegate.getGraph(list.get(index));
    }

    public void addListDataListener(ListDataListener l) {
        dataListeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        dataListeners.remove(l);
    }

    public void valueChanged(ListSelectionEvent e) {
        fireSelectedGraphChanged();
    }

    protected void fireSelectedGraphChanged(){
        for (GraphListModelListener l : listeners) {
            l.selectedGraphChanged();
        }
    }

    //TODO: better boundaries for events
    public void intervalAdded(ListDataEvent e) {
        fireIntervalAdded(new ListDataEvent(this, e.getType(), 0, list.size()-1));
    }

    public void intervalRemoved(ListDataEvent e) {
        fireIntervalRemoved(new ListDataEvent(this, e.getType(), 0, list.size()-1));
    }

    public void contentsChanged(ListDataEvent e) {
        fireContentsChanged(new ListDataEvent(this, e.getType(), 0, list.size()-1));
    }
    
    public void fireIntervalAdded(ListDataEvent e) {
        for (ListDataListener l : dataListeners) {
            l.intervalAdded(e);
        }
    }

    public void fireIntervalRemoved(ListDataEvent e) {
        for (ListDataListener l : dataListeners) {
            l.intervalRemoved(e);
        }
    }

    public void fireContentsChanged(ListDataEvent e) {
        for (ListDataListener l : dataListeners) {
            l.contentsChanged(e);
        }
    }

}
