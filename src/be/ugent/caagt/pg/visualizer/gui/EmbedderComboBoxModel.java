/* EmbedderComboBoxModel.java
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

import be.ugent.caagt.pg.embedder.Embedder;
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
