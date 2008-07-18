/* ClipViewToggler.java
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

package be.ugent.caagt.pg.visualizer.gui.toggler;

import be.ugent.caagt.pg.visualizer.gui.TorusView;
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
