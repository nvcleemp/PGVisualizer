/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui.toggler;

import azul.toroidalembedder.gui.TorusView;
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
public class FillFacesToggler extends AbstractAction implements PropertyChangeListener, Toggler{
    
    private List<AbstractButton> list = new ArrayList<AbstractButton>();
    private TorusView torusView;

    public FillFacesToggler(TorusView torusView) {
        super("Fill faces");
        this.torusView = torusView;
        torusView.addPropertyChangeListener(TorusView.FILL_FACES, this);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof AbstractButton){
            torusView.setPaintFaces(((AbstractButton)e.getSource()).isSelected());
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        for (AbstractButton abstractButton : list) {
            abstractButton.setSelected(torusView.isPaintFaces());
        }
    }
    
    public JCheckBox getJCheckBox(){
        JCheckBox box = new JCheckBox(this);
        box.setSelected(torusView.isPaintFaces());
        list.add(box);
        return box;
    }

    public JCheckBoxMenuItem getJCheckBoxMenuItem(){
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(this);
        menuItem.setSelected(torusView.isPaintFaces());
        list.add(menuItem);
        return menuItem;
    }
}
