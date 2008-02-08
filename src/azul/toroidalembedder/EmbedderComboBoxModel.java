/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author nvcleemp
 */
public class EmbedderComboBoxModel implements ComboBoxModel{
    
    private String selected = null;
    private List<String> keys = new ArrayList<String>();
    private List<ListDataListener> listeners = new ArrayList<ListDataListener>();
    private Map<String, Embedder> embedders = new HashMap<String, Embedder>();

    public void setSelectedItem(Object anItem) {
        if(anItem instanceof String){
            selected = (String)anItem;
        }
    }

    public Object getSelectedItem() {
        return selected;
    }

    public int getSize() {
        return keys.size();
    }

    public Object getElementAt(int index) {
        return keys.get(index);
    }

    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
    
    public void fireListDataChanged(){
        ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, keys.size());
        for (ListDataListener l : listeners) {
            l.contentsChanged(e);
        }

    }

    public Embedder getSelectedEmbedder() {
        return embedders.get(getSelectedItem());
    }
    
    public void addEmbedder(String key, Embedder embedder) {
        keys.add(key);
        embedders.put(key, embedder);
        fireListDataChanged();
    }
}
