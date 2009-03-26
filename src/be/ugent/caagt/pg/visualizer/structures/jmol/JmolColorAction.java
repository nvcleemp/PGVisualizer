/* JmolColorAction.java
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

package be.ugent.caagt.pg.visualizer.structures.jmol;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.ButtonModel;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import org.jmol.api.JmolViewer;

/**
 *
 * @author nvcleemp
 */
public class JmolColorAction extends AbstractAction{

    private ButtonModel previewModel;
    private ColorSelectionModel colorModel;
    private JColorChooser chooser;
    private Component parentComponent;

    public JmolColorAction(String name, ButtonModel previewModel, String component, Color startColor, JmolViewer viewer) {
        super(name);
        this.previewModel = previewModel;
        colorModel = new DefaultColorSelectionModel(startColor);
        colorModel.addChangeListener(new JmolColorChangeListener(viewer, component, colorModel));
        chooser = new JColorChooser(colorModel);
    }

    public void actionPerformed(ActionEvent e) {
        if(previewModel.isSelected()){
            //if preview is enabled we use the chooser connected to the colormodel
            //we just need to reset the color when cancel is pressed.
            final Color oldColor = chooser.getColor();
            JDialog d = JColorChooser.createDialog(parentComponent,
                getValue(AbstractAction.NAME).toString(), true , chooser,
                null, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        colorModel.setSelectedColor(oldColor);
                    }
                });
            d.setVisible(true);
        } else {
            //if preview is disabled we open a general chooser and set the color
            //afterwards.
            Color c = JColorChooser.showDialog(parentComponent, getValue(AbstractAction.NAME).toString(), colorModel.getSelectedColor());
            if(c!=null){
                colorModel.setSelectedColor(c);
            }
        }
    }

}
