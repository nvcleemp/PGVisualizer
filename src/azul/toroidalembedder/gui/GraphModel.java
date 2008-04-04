package azul.toroidalembedder.gui;

import azul.io.FileFormatException;
import azul.io.IOManager;
import azul.toroidalembedder.graph.Graph;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GraphModel extends AbstractListModel implements ListDataListener, ListSelectionListener {

    private DefaultListModel list = new DefaultListModel();
    private Map<String, Graph> map = new HashMap<String, Graph>();
    private ListSelectionModel selectionModel;

    public GraphModel(File file) {
        super();
        selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(this);
        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.startsWith("#") && line.length() != 0) {
                    //ignore comments
                    list.addElement(line);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IOManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        list.addListDataListener(this);
    }

    public Graph getGraph(int index) {
        String string = (String)list.get(index);
        try {
            if (map.get(string) == null) {
                map.put(string, IOManager.readPG(string));
            }
        } catch (FileFormatException ex) {
            Logger.getLogger(PGVisualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map.get(string);
    }

    public int getSize() {
        return list.size();
    }

    public Object getElementAt(int index) {
        return getGraph(index);
    }

    public void intervalAdded(ListDataEvent e) {
        fireIntervalAdded(this, e.getIndex0(), e.getIndex1());
    }

    public void intervalRemoved(ListDataEvent e) {
        fireIntervalRemoved(this, e.getIndex0(), e.getIndex1());
    }

    public void contentsChanged(ListDataEvent e) {
        fireContentsChanged(this, e.getIndex0(), e.getIndex1());
    }
    
    public String exportUpdatedGraphs(){
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < list.getSize(); i++) {
            String string = (String)list.get(i);
            if (map.get(string) == null)
                buf.append(string);
            else
                buf.append(IOManager.writePG(map.get(string)));
            buf.append('\n');
        }
        return buf.toString();
    }
    
    public Graph getSelectedGraph(){
        int selectedIndex = selectionModel.getMinSelectionIndex();
        if(selectedIndex < 0 || selectedIndex>= list.size())
            return null;
        else
            return getGraph(selectedIndex);
    }
    
    public void addGraphModelListener(GraphModelListener l){
        listenerList.add(GraphModelListener.class, l);
    }
    
    public void removeGraphModelListener(GraphModelListener l){
        listenerList.remove(GraphModelListener.class, l);
    }
    
    protected void fireSelectedGraphChanged(){
	Object[] listeners = listenerList.getListenerList();

	for (int i = listeners.length - 2; i >= 0; i -= 2) {
	    if (listeners[i] == GraphModelListener.class) {
		((GraphModelListener)listeners[i+1]).selectedGraphChanged();
	    }	       
	}
    }

    @Override
    protected void fireContentsChanged(Object source, int index0, int index1) {
	Object[] listeners = listenerList.getListenerList();
	ListDataEvent e = null;

	for (int i = listeners.length - 2; i >= 0; i -= 2) {
	    if (listeners[i] == ListDataListener.class) {
		if (e == null) {
		    e = new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, index0, index1);
		}
		((ListDataListener)listeners[i+1]).contentsChanged(e);
	    } else if (listeners[i] == GraphModelListener.class) {
		if (e == null) {
		    e = new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, index0, index1);
		}
		((GraphModelListener)listeners[i+1]).contentsChanged(e);
	    }       
	}
    }


    @Override
    protected void fireIntervalAdded(Object source, int index0, int index1) {
	Object[] listeners = listenerList.getListenerList();
	ListDataEvent e = null;

	for (int i = listeners.length - 2; i >= 0; i -= 2) {
	    if (listeners[i] == ListDataListener.class) {
		if (e == null) {
		    e = new ListDataEvent(source, ListDataEvent.INTERVAL_ADDED, index0, index1);
		}
		((ListDataListener)listeners[i+1]).intervalAdded(e);
	    } else if (listeners[i] == GraphModelListener.class) {
		if (e == null) {
		    e = new ListDataEvent(source, ListDataEvent.INTERVAL_ADDED, index0, index1);
		}
		((GraphModelListener)listeners[i+1]).intervalAdded(e);
	    }
	}
    }

    @Override
    protected void fireIntervalRemoved(Object source, int index0, int index1) {
	Object[] listeners = listenerList.getListenerList();
	ListDataEvent e = null;

	for (int i = listeners.length - 2; i >= 0; i -= 2) {
	    if (listeners[i] == ListDataListener.class) {
		if (e == null) {
		    e = new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, index0, index1);
		}
		((ListDataListener)listeners[i+1]).intervalRemoved(e);
	    } else if (listeners[i] == GraphModelListener.class) {
		if (e == null) {
		    e = new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, index0, index1);
		}
		((GraphModelListener)listeners[i+1]).intervalRemoved(e);
	    }
	}
    }

    public void valueChanged(ListSelectionEvent e) {
        fireSelectedGraphChanged();
    }
    
    public void addListSelectionListener(ListSelectionListener l){
        selectionModel.addListSelectionListener(l);
    }

    public void removeListSelectionListener(ListSelectionListener l){
        selectionModel.removeListSelectionListener(l);
    }
    
    public ListSelectionModel getSelectionModel(){
        return selectionModel;
    }

    public void commitGraph(int index){
        list.set(index, IOManager.writePG(map.get(list.get(index))));
    }
    
    public void commitSelectedGraph(){
        commitGraph(selectionModel.getMinSelectionIndex());
    }
    
    public void revertGraph(int index){
        String string = (String)list.get(index);
        map.remove(string);
        try {
            map.put(string, IOManager.readPG(string));
        } catch (FileFormatException ex) {
            Logger.getLogger(PGVisualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(index == selectionModel.getMinSelectionIndex())
            fireSelectedGraphChanged();
    }
    
    public void revertSelectedGraph(){
        revertGraph(selectionModel.getMinSelectionIndex());
    }
}
