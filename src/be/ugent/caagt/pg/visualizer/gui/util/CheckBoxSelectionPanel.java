/* CheckBoxSelectionPanel.java
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

package be.ugent.caagt.pg.visualizer.gui.util;

import be.ugent.caagt.pg.visualizer.gui.models.TableColumnEnum;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class CheckBoxSelectionPanel<E> extends JPanel{

    private boolean closed = false;
    private GridBagConstraints gbc = new GridBagConstraints();
    private Map<E,JCheckBox> map = new HashMap<E, JCheckBox>();

    public CheckBoxSelectionPanel() {
        super(new GridBagLayout());
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
    }

    public void close(){
        closed = true;
    }

    public void newLine(){
        if(closed)
            throw new IllegalStateException("This panel is already closed");
        gbc.gridx = 0;
        gbc.gridy++;
    }

    public void addElement(E element){
        if(closed)
            throw new IllegalStateException("This panel is already closed");
        if(map.containsKey(element))
            throw new IllegalArgumentException("This panel already contains that element");
        map.put(element, new JCheckBox(element.toString()));
        add(map.get(element),gbc);
        gbc.gridx++;
    }

    public Set<E> getSelectedElements(){
        Set<E> set = new HashSet<E>();
        for (E element : map.keySet()) {
            if(map.get(element).isSelected())
                set.add(element);
        }
        return set;
    }

    public void setAllSelected(boolean state){
        for (JCheckBox box : map.values()) {
            box.setSelected(state);
        }
    }

    public static <T> CheckBoxSelectionPanel<T> buildPanelFromArray(T[] array){
        CheckBoxSelectionPanel<T> panel = new CheckBoxSelectionPanel<T>();
        for (int i = 0; i < array.length; i++) {
            panel.addElement(array[i]);
            panel.newLine();
        }
        return panel;
    }
}
