/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.visualizer.gui.util;

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
}
