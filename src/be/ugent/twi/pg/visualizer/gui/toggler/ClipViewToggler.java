/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.visualizer.gui.toggler;

import be.ugent.twi.pg.visualizer.gui.TorusView;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;

/**
 *
 * @author nvcleemp
 */
public class ClipViewToggler extends AbstractAction implements PropertyChangeListener, Toggler{
    
    private List<AbstractButton> list = new ArrayList<AbstractButton>();
    private TorusView torusView;

    public ClipViewToggler(TorusView torusView) {
        super("Clip view");
        this.torusView = torusView;
        torusView.addPropertyChangeListener(TorusView.CLIP_VIEW, this);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof AbstractButton){
            torusView.setViewClipped(((AbstractButton)e.getSource()).isSelected());
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        for (AbstractButton abstractButton : list) {
            abstractButton.setSelected(torusView.isViewClipped());
        }
    }
    
    public JCheckBox getJCheckBox(){
        JCheckBox box = new JCheckBox(this);
        box.setSelected(torusView.isViewClipped());
        list.add(box);
        return box;
    }

    public JCheckBoxMenuItem getJCheckBoxMenuItem(){
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(this);
        menuItem.setSelected(torusView.isViewClipped());
        list.add(menuItem);
        return menuItem;
    }
}
