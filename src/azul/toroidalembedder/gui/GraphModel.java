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
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class GraphModel extends AbstractListModel implements ListDataListener {

    private DefaultListModel list = new DefaultListModel();
    private Map<String, Graph> map = new HashMap<String, Graph>();

    public GraphModel(File file) {
        super();
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
}
