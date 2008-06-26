/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.io.IOManager;
import azul.toroidalembedder.graph.general.Graph;
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
